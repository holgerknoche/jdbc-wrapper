package jdbcwrapper.test;

import java.sql.Connection;
import java.util.List;

import jdbcwrapper.WrappedConnection;

public class TestConnectionWithListeners extends WrappedConnection<TestConnectionListener> {

	public TestConnectionWithListeners(final Connection wrappedConnection, final List<TestConnectionListener> listeners) {
		super(wrappedConnection, listeners);
		// TODO Auto-generated constructor stub
	}

}
