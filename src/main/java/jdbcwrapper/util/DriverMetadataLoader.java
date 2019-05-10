package jdbcwrapper.util;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DriverMetadataLoader {	
	
	private static final char IDENTIFIER_SEPARATOR_CHAR = '.';
	
	private static final String LISTENERS_SEPARATOR_REGEX = ", *";
	
	private static final String CONNECTION_WRAPPER_TYPE_PROPERTY = "connection.wrapper";
	
	private static final String CONNECTION_LISTENER_TYPES_PROPERTY = "connection.listeners";
		
	private static final Logger LOGGER = Logger.getLogger(DriverMetadataLoader.class.getName());
	
	public Map<String, DriverMetadata> loadMetadata(final InputStream inputStream) {
		Properties properties = new Properties();
		
		// Load properties from the given input stream
		try {
			if (inputStream != null) {
				properties.load(inputStream);
			}
		} catch (IOException e) {
			LOGGER.log(Level.WARNING, "Error reading driver metadata, defaults assumed.", e);
		}
		
		// Sort the properties according to their prefix (i.e., driver identifier)
		NestedMap<String, String, String> sortedProperties = this.sortProperties(properties);
		
		return this.instantiateMetadata(sortedProperties);
	}
	
	private NestedMap<String, String, String> sortProperties(final Properties properties) {
		NestedMap<String, String, String> sortedProperties = new NestedMap<>();
		
		for (Entry<Object, Object> entry : properties.entrySet()) {
			String key = (String) entry.getKey();
			 
			// Separate the identifier from the remainder of the string
			String identifier;
			String propertyName;
			
			int separatorPosition = key.indexOf(IDENTIFIER_SEPARATOR_CHAR);
			if (separatorPosition < 0) {
				// No separator was found, the entire string is assumed to be the
				// identifier
				identifier = key;
				propertyName = "";
			} else {
				// Separate identifier and property name
				identifier = key.substring(0, separatorPosition);
				
				int propertyStartIndex = separatorPosition + 1;
				if (propertyStartIndex == key.length()) {
					// String ends with a separator
					propertyName = "";
				} else {
					// Property name is the remainder after the separator
					propertyName = key.substring(propertyStartIndex);
				}
			}
			
			// Store the value
			String value = (String) entry.getValue();
			sortedProperties.put(identifier, propertyName, value);
		}
		
		return sortedProperties;
	}
	
	private Map<String, DriverMetadata> instantiateMetadata(final NestedMap<String, String, String> properties) {
		Map<String, DriverMetadata> metadataMap = new HashMap<>();
		
		for (String identifier : properties.getOuterKeys()) {
			DriverMetadata metadata = this.instantiateMetadata(identifier, properties.get(identifier));
			
			metadataMap.put(identifier, metadata);
		}
		
		return metadataMap;
	}
	
	private DriverMetadata instantiateMetadata(final String identifier, final Map<String, String> properties) {
		String wrapperTypeName = properties.get(CONNECTION_WRAPPER_TYPE_PROPERTY);
		if (wrapperTypeName == null) {
			// No wrapper type supplied, do not return any metadata
			LOGGER.log(Level.WARNING, () -> "No wrapper type supplied for " + identifier + ".");
			return null;
		}
		
		// Look for a suitable constructor		
		WrapperConstructorInfo wrapperConstructorInfo = this.findWrapperConstructor(wrapperTypeName);
		if (wrapperConstructorInfo == null) {
			// No suitable constructor was found, error is already logged
			return null;
		}
		
		List<Constructor<?>> listenerConstructors;
		String listenerTypeNames = properties.get(CONNECTION_LISTENER_TYPES_PROPERTY);
		if (listenerTypeNames == null) {
			// No listener types given, assume an empty list
			listenerConstructors = Collections.emptyList();
		} else {
			// Find listener constructors
			listenerConstructors = this.findListenerConstructors(listenerTypeNames);
		}
		
		return new DriverMetadata(wrapperConstructorInfo.constructor, wrapperConstructorInfo.acceptsListeners, listenerConstructors);
	}

	private WrapperConstructorInfo findWrapperConstructor(final String typeName) {
		Class<?> wrapperType;
		
		try {
			wrapperType = Class.forName(typeName);
		} catch (ClassNotFoundException e) {
			LOGGER.log(Level.WARNING, e, () -> "Error instantiating wrapper type " + typeName + ".");
			return null;
		}
		
		Constructor<?> constructorCandidate = null;
		boolean acceptsListeners = false;

		// Look for an appropriate constructor
		for (Constructor<?> constructor : wrapperType.getConstructors()) {
			if (argumentsCompatible(constructor, Connection.class, List.class)) {
				// Constructor accepting listeners was found. These are preferred, so break
				// out of the loop
				constructorCandidate = constructor;
				acceptsListeners = true;
				break;
			}

			if (argumentsCompatible(constructor, Connection.class)) {
				// Constructor without listeners was found. Save this, but continue to
				// look for one accepting listeners
				constructorCandidate = constructor;
				acceptsListeners = false;
			}
		}
		
		if (constructorCandidate == null) {
			LOGGER.log(Level.WARNING, () -> "No appropriate constructor was found for type " + typeName + ".");
			return null;
		}
		
		return new WrapperConstructorInfo(constructorCandidate, acceptsListeners);
	}
	
	private static boolean argumentsCompatible(final Constructor<?> constructor, final Class<?>... requiredArgumentTypes) {
		Class<?>[] actualArgumentTypes = constructor.getParameterTypes();

		// Assert equal number of arguments
		if (actualArgumentTypes.length != requiredArgumentTypes.length) {
			return false;
		}
		
		// Check assignment compatibility of the arguments
		for (int argumentIndex = 0; argumentIndex < actualArgumentTypes.length; argumentIndex++) {
			Class<?> actualType = actualArgumentTypes[argumentIndex];
			Class<?> requiredType = requiredArgumentTypes[argumentIndex];
			
			if (!actualType.isAssignableFrom(requiredType)) {
				return false;
			}
		}
		
		return true;
	}
	
	private List<Constructor<?>> findListenerConstructors(final String listenerTypeNameString) {
		String[] listenerTypeNames = listenerTypeNameString.split(LISTENERS_SEPARATOR_REGEX);
		
		if (listenerTypeNames.length == 0 || listenerTypeNameString.isEmpty()) {
			return Collections.emptyList();
		}
		
		List<Constructor<?>> listenerConstructors = new ArrayList<>(listenerTypeNames.length);
		for (String listenerTypeName : listenerTypeNames) {
			Constructor<?> constructor = this.findListenerConstructor(listenerTypeName);
			
			if (constructor != null) {
				listenerConstructors.add(constructor);
			}
		}
		
		return listenerConstructors;
	}
	
	private Constructor<?> findListenerConstructor(final String listenerTypeName) {
		try {
			// Find a parameterless constructor on the type
			Class<?> listenerType = Class.forName(listenerTypeName);
			return listenerType.getConstructor();
		} catch (ClassNotFoundException e) {
			LOGGER.log(Level.WARNING, e, () -> "Error instantiating listener type " + listenerTypeName + ".");
			return null;
		} catch (NoSuchMethodException e) {
			LOGGER.log(Level.WARNING, e, () -> "No suitable constructor was found on type " + listenerTypeName + ".");
			return null;
		}
	}
	
	private static class WrapperConstructorInfo {
		
		public final Constructor<?> constructor;
		
		public final boolean acceptsListeners;

		public WrapperConstructorInfo(final Constructor<?> constructor, final boolean acceptsListeners) {
			this.constructor = constructor;
			this.acceptsListeners = acceptsListeners;
		}		
	}
	
}
