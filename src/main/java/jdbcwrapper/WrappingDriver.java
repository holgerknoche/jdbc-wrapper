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
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WrappingDriver implements java.sql.Driver {

	private static final String DRIVER_MAP_NAME = "jdbc-wrapper.properties";
	
	private static final String URL_PREFIX = "jdbc:wrapped:";
	
	private static final String URL_PREFIX_REGEX = "^jdbc:(?<type>.*?):";
	
	private static final Pattern URL_PREFIX_PATTERN = Pattern.compile(URL_PREFIX_REGEX);
	
	private static final Logger PARENT_LOGGER = Logger.getLogger("org.hkn.jdbc.wrapper");
	
	private static final Logger LOGGER = Logger.getLogger(WrappingDriver.class.getName());
	
	private static WrappingDriver registeredInstance;
	
	private final Map<String, Constructor<?>> typeToWrapperMap;
	
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
		this.typeToWrapperMap = this.loadWrapperMap();
	}
	
	private Map<String, Constructor<?>> loadWrapperMap() {			
		// Load the wrapper map from the properties file
		Properties properties = new Properties();
		ClassLoader classLoader = this.getClass().getClassLoader();
		
		try (InputStream inputStream = classLoader.getResourceAsStream(DRIVER_MAP_NAME)) {
			if (inputStream != null) {				
				properties.load(inputStream);
			}
		} catch (IOException e) {
			LOGGER.log(Level.WARNING, "Error reading the wrapper map.", e);
		}
		
		// Build the wrapper map from the properties
		Map<String, Constructor<?>> wrapperMap = new HashMap<>(properties.size());
		for (Entry<Object, Object> entry : properties.entrySet()) {
			String name = (String) entry.getKey();
			String wrapperClassName = (String) entry.getValue();
			
			try {
				Class<?> wrapperClass = Class.forName(wrapperClassName);
				Constructor<?> constructor = wrapperClass.getConstructor(Connection.class);
				
				wrapperMap.put(name, constructor);
			} catch (ClassNotFoundException e) {
				LOGGER.log(Level.WARNING, e, () -> "Wrapper class " + wrapperClassName + " not found.");
			} catch (NoSuchMethodException e) {
				LOGGER.log(Level.WARNING, () -> "Wrapper class " + wrapperClassName + " does not have a one-argument constructor.");
			} catch (SecurityException e) {
				LOGGER.log(Level.WARNING, "Error preparing the wrapper map.", e);
			}
		}
		
		return wrapperMap;
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
			return this.createDefaultWrapper(connection);
		}
		
		Constructor<?> wrapperConstructor = this.typeToWrapperMap.get(type);
		if (wrapperConstructor == null) {
			return this.createDefaultWrapper(connection);
		}
		
		try {
			return (Connection) wrapperConstructor.newInstance(connection);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException	| InvocationTargetException e) {
			LOGGER.log(Level.WARNING, e, () -> "Error instantiating the wrapper for type '" + type + "'.");
			return this.createDefaultWrapper(connection);
		}
	}
	
	protected Connection createDefaultWrapper(final Connection connection) {
		return new WrappedConnection(connection);
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
