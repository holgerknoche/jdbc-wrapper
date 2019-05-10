package jdbcwrapper.util;

import java.lang.reflect.Constructor;
import java.util.Collections;
import java.util.List;

public class DriverMetadata {	 

	public final Constructor<?> connectionWrapperConstructor;
	
	public final boolean acceptsConnectionListeners;
	
	public final List<Constructor<?>> connectionListenerConstructors;

	public DriverMetadata(final Constructor<?> connectionWrapperConstructor, final boolean acceptsConnectionListeners, final List<Constructor<?>> connectionListenerConstructors) {
		this.connectionWrapperConstructor = connectionWrapperConstructor;
		this.acceptsConnectionListeners = acceptsConnectionListeners;
		this.connectionListenerConstructors = Collections.unmodifiableList(connectionListenerConstructors);
	}
	
}
