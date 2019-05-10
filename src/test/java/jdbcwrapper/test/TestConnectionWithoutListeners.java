package jdbcwrapper.test;

import java.sql.Connection;

import jdbcwrapper.WrappedConnection;

public class TestConnectionWithoutListeners extends WrappedConnection<Void> {

	public TestConnectionWithoutListeners(final Connection wrappedConnection) {
		super(wrappedConnection);
	}

}
