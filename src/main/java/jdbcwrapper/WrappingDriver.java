package jdbcwrapper;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jdbcwrapper.util.DriverMetadata;
import jdbcwrapper.util.DriverMetadataLoader;

public class WrappingDriver implements java.sql.Driver {

	private static final String PROPERTIES_NAME = "jdbc-wrapper.properties";
	
	private static final String URL_PREFIX = "jdbc:wrapped:";
	
	private static final String URL_PREFIX_REGEX = "^jdbc:(?<type>.*?):";
	
	private static final Pattern URL_PREFIX_PATTERN = Pattern.compile(URL_PREFIX_REGEX);
	
	private static final Logger PARENT_LOGGER = Logger.getLogger("org.hkn.jdbc.wrapper");
	
	private static final Logger LOGGER = Logger.getLogger(WrappingDriver.class.getName());
	
	private static WrappingDriver registeredInstance;
	
	private final Map<String, DriverMetadata> typeToMetadataMap;
	
	static {
		try {
			register();
		} catch (SQLException e) {
			throw new ExceptionInInitializerError(e);
		}
	}
	
	public static void register() throws SQLException {
		if (isRegistered()) {
			throw new IllegalStateException("Driver is already registered.");
		}
		
		WrappingDriver instance = new WrappingDriver();
		DriverManager.registerDriver(instance);
		registeredInstance = instance;
	}
	
	public static boolean isRegistered() {
		return (registeredInstance != null);
	}
	
	public static void deregister() throws SQLException {
		if (!isRegistered()) {
			throw new IllegalStateException("Driver is not registered.");
		}
		
		DriverManager.deregisterDriver(registeredInstance);
		registeredInstance = null;
	}
	
	public WrappingDriver() {
		this.typeToMetadataMap = this.loadMetadata();
	}
	
	private Map<String, DriverMetadata> loadMetadata() {			
		// Load the metadata map from the properties file
		ClassLoader classLoader = this.getClass().getClassLoader();
		
		try (InputStream inputStream = classLoader.getResourceAsStream(PROPERTIES_NAME)) {
			return new DriverMetadataLoader().loadMetadata(inputStream);
		} catch (IOException e) {
			LOGGER.log(Level.WARNING, "Error reading the metadata.", e);
			return Collections.emptyMap();
		}				
	}
		
	@Override
	public boolean acceptsURL(final String url) throws SQLException {
		return needsRewriting(url);
	}
	
	private static boolean needsRewriting(final String url) {
		return url.startsWith(URL_PREFIX);
	}

	@Override
	public Connection connect(final String url, final Properties info) throws SQLException {
		if (!this.acceptsURL(url)) {
			return null;
		}
		
		// Rewrite the URL if necessary and create the delegate connection
		String actualUrl = (needsRewriting(url)) ? rewriteUrl(url) : url;
		Driver delegateDriver = DriverManager.getDriver(actualUrl);		
		Connection delegateConnection = delegateDriver.connect(actualUrl, info);
		
		// Determine the type from the URL and instantiate the appropriate wrapper
		String type = this.determineType(actualUrl);		
		return this.createWrappedConnection(delegateConnection, type);
	}
	
	private String determineType(final String url) {
		Matcher matcher = URL_PREFIX_PATTERN.matcher(url);
		
		if (matcher.find()) {
			return matcher.group("type");
		} else {
			return null;
		}
	}
	
	private Connection createWrappedConnection(final Connection connection, final String type) {
		if (type == null) {
			// No type provided, use default wrapper
			return this.createDefaultWrapper(connection);
		}
		
		DriverMetadata metadata = this.typeToMetadataMap.get(type);
		if (metadata == null) {
			// No metadata provided, use default wrapper
			return this.createDefaultWrapper(connection);
		}
		
		Constructor<?> wrapperConstructor = metadata.connectionWrapperConstructor;
		
		try {
			if (metadata.acceptsConnectionListeners) {
				// Create connection listeners, if supported
				List<Object> connectionListeners = this.instantiateConnectionListeners(metadata);
				return (Connection) wrapperConstructor.newInstance(connection, connectionListeners);
			} else {
				// Invoke non-listener constructor
				return (Connection) wrapperConstructor.newInstance(connection);
			}
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			LOGGER.log(Level.WARNING, e, () -> "Error instantiating the wrapper for type '" + type + "'.");
			return this.createDefaultWrapper(connection);
		}
	}
	
	private List<Object> instantiateConnectionListeners(final DriverMetadata metadata) {
		List<Constructor<?>> listenerConstructors = metadata.connectionListenerConstructors;
		List<Object> listeners = new ArrayList<>(listenerConstructors.size());
		
		for (Constructor<?> listenerConstructor : listenerConstructors) {
			Object listener = this.instantiateListener(listenerConstructor);
			
			if (listener != null) {
				listeners.add(listener);
			}
		}
		
		return listeners;
	}
	
	private Object instantiateListener(final Constructor<?> listenerConstructor) {
		try {
			return listenerConstructor.newInstance();
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			LOGGER.log(Level.WARNING, e, () -> "Error instantiating listener type " + listenerConstructor.getName() + ", skipping.");
			return null;
		}
	}
	
	protected Connection createDefaultWrapper(final Connection connection) {
		// By default, don't wrap at all
		return connection;
	}
	
	private static String rewriteUrl(final String url) {
		return url.replaceFirst(URL_PREFIX, "jdbc:");
	}

	@Override
	public int getMajorVersion() {
		return 1;
	}

	@Override
	public int getMinorVersion() {
		return 0;
	}

	@Override
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		return PARENT_LOGGER;
	}

	@Override
	public DriverPropertyInfo[] getPropertyInfo(final String url, final Properties info) throws SQLException {
		return new DriverPropertyInfo[0];
	}

	@Override
	public boolean jdbcCompliant() {
		return false;
	}	

}
