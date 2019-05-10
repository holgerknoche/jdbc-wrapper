package jdbcwrapper;

import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

public class WrappedConnection<L> implements Connection {

	private final Connection wrappedConnection;
	
	private final List<L> listeners;
	
	public WrappedConnection(final Connection wrappedConnection) {
		this(wrappedConnection, null);
	}
	
	public WrappedConnection(final Connection wrappedConnection, final List<L> listeners) {
		this.wrappedConnection = wrappedConnection;
		this.listeners = (listeners == null) ? Collections.emptyList() : listeners;
	}

	@Override
	public <T> T unwrap(final Class<T> iface) throws SQLException {
		return this.wrappedConnection.unwrap(iface);
	}

	@Override
	public boolean isWrapperFor(final Class<?> iface) throws SQLException {
		return this.wrappedConnection.isWrapperFor(iface);
	}

	@Override
	public Statement createStatement() throws SQLException {
		return this.wrappedConnection.createStatement();
	}

	@Override
	public PreparedStatement prepareStatement(final String sql) throws SQLException {
		return this.wrappedConnection.prepareStatement(sql);
	}

	@Override
	public CallableStatement prepareCall(final String sql) throws SQLException {
		return this.wrappedConnection.prepareCall(sql);
	}

	@Override
	public String nativeSQL(final String sql) throws SQLException {
		return this.wrappedConnection.nativeSQL(sql);
	}

	@Override
	public void setAutoCommit(final boolean autoCommit) throws SQLException {
		this.wrappedConnection.setAutoCommit(autoCommit);
	}

	@Override
	public boolean getAutoCommit() throws SQLException {
		return this.wrappedConnection.getAutoCommit();
	}

	@Override
	public void commit() throws SQLException {
		this.wrappedConnection.commit();
	}

	@Override
	public void rollback() throws SQLException {
		this.wrappedConnection.rollback();
	}

	@Override
	public void close() throws SQLException {
		this.wrappedConnection.close();
	}

	@Override
	public boolean isClosed() throws SQLException {
		return this.wrappedConnection.isClosed();
	}

	@Override
	public DatabaseMetaData getMetaData() throws SQLException {
		return this.wrappedConnection.getMetaData();
	}

	@Override
	public void setReadOnly(final boolean readOnly) throws SQLException {
		this.wrappedConnection.setReadOnly(readOnly);
	}

	@Override
	public boolean isReadOnly() throws SQLException {
		return this.wrappedConnection.isReadOnly();
	}

	@Override
	public void setCatalog(final String catalog) throws SQLException {
		this.wrappedConnection.setCatalog(catalog);
	}

	@Override
	public String getCatalog() throws SQLException {
		return this.wrappedConnection.getCatalog();
	}

	@Override
	public void setTransactionIsolation(final int level) throws SQLException {
		this.wrappedConnection.setTransactionIsolation(level);
	}

	@Override
	public int getTransactionIsolation() throws SQLException {
		return this.wrappedConnection.getTransactionIsolation();
	}

	@Override
	public SQLWarning getWarnings() throws SQLException {
		return this.wrappedConnection.getWarnings();
	}

	@Override
	public void clearWarnings() throws SQLException {
		this.wrappedConnection.clearWarnings();
	}

	@Override
	public Statement createStatement(final int resultSetType, final int resultSetConcurrency) throws SQLException {
		return this.wrappedConnection.createStatement(resultSetType, resultSetConcurrency);
	}

	@Override
	public PreparedStatement prepareStatement(final String sql, final int resultSetType, final int resultSetConcurrency)
			throws SQLException {
		return this.wrappedConnection.prepareStatement(sql, resultSetType, resultSetConcurrency);
	}

	@Override
	public CallableStatement prepareCall(final String sql, final int resultSetType, final int resultSetConcurrency) throws SQLException {
		return this.wrappedConnection.prepareCall(sql, resultSetType, resultSetConcurrency);
	}

	@Override
	public Map<String, Class<?>> getTypeMap() throws SQLException {
		return this.wrappedConnection.getTypeMap();
	}

	@Override
	public void setTypeMap(final Map<String, Class<?>> map) throws SQLException {
		this.wrappedConnection.setTypeMap(map);
	}

	@Override
	public void setHoldability(final int holdability) throws SQLException {
		this.wrappedConnection.setHoldability(holdability);
	}

	@Override
	public int getHoldability() throws SQLException {
		return this.wrappedConnection.getHoldability();
	}

	@Override
	public Savepoint setSavepoint() throws SQLException {
		return this.wrappedConnection.setSavepoint();
	}

	@Override
	public Savepoint setSavepoint(final String name) throws SQLException {
		return this.wrappedConnection.setSavepoint(name);
	}

	@Override
	public void rollback(final Savepoint savepoint) throws SQLException {
		this.wrappedConnection.rollback(savepoint);
	}

	@Override
	public void releaseSavepoint(final Savepoint savepoint) throws SQLException {
		this.wrappedConnection.releaseSavepoint(savepoint);
	}

	@Override
	public Statement createStatement(final int resultSetType, final int resultSetConcurrency, final int resultSetHoldability)
			throws SQLException {
		return this.wrappedConnection.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);
	}

	@Override
	public PreparedStatement prepareStatement(final String sql, final int resultSetType, final int resultSetConcurrency,
			final int resultSetHoldability) throws SQLException {
		return this.wrappedConnection.prepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
	}

	@Override
	public CallableStatement prepareCall(final String sql, final int resultSetType, final int resultSetConcurrency,
			final int resultSetHoldability) throws SQLException {
		return this.wrappedConnection.prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
	}

	@Override
	public PreparedStatement prepareStatement(final String sql, final int autoGeneratedKeys) throws SQLException {
		return this.wrappedConnection.prepareStatement(sql, autoGeneratedKeys);
	}

	@Override
	public PreparedStatement prepareStatement(final String sql, final int[] columnIndexes) throws SQLException {
		return this.wrappedConnection.prepareStatement(sql, columnIndexes);
	}

	@Override
	public PreparedStatement prepareStatement(final String sql, final String[] columnNames) throws SQLException {
		return this.wrappedConnection.prepareStatement(sql, columnNames);
	}

	@Override
	public Clob createClob() throws SQLException {
		return this.wrappedConnection.createClob();
	}

	@Override
	public Blob createBlob() throws SQLException {
		return this.wrappedConnection.createBlob();
	}

	@Override
	public NClob createNClob() throws SQLException {
		return this.wrappedConnection.createNClob();
	}

	@Override
	public SQLXML createSQLXML() throws SQLException {
		return this.wrappedConnection.createSQLXML();
	}

	@Override
	public boolean isValid(final int timeout) throws SQLException {
		return this.wrappedConnection.isValid(timeout);
	}

	@Override
	public void setClientInfo(final String name, final String value) throws SQLClientInfoException {
		this.wrappedConnection.setClientInfo(name, value);
	}

	@Override
	public void setClientInfo(final Properties properties) throws SQLClientInfoException {
		this.wrappedConnection.setClientInfo(properties);
	}

	@Override
	public String getClientInfo(final String name) throws SQLException {
		return this.wrappedConnection.getClientInfo(name);
	}

	@Override
	public Properties getClientInfo() throws SQLException {
		return this.wrappedConnection.getClientInfo();
	}

	@Override
	public Array createArrayOf(final String typeName, final Object[] elements) throws SQLException {
		return this.wrappedConnection.createArrayOf(typeName, elements);
	}

	@Override
	public Struct createStruct(final String typeName, final Object[] attributes) throws SQLException {
		return this.wrappedConnection.createStruct(typeName, attributes);
	}

	@Override
	public void setSchema(final String schema) throws SQLException {
		this.wrappedConnection.setSchema(schema);
	}

	@Override
	public String getSchema() throws SQLException {
		return this.wrappedConnection.getSchema();
	}

	@Override
	public void abort(final Executor executor) throws SQLException {
		this.wrappedConnection.abort(executor);
	}

	@Override
	public void setNetworkTimeout(final Executor executor, final int milliseconds) throws SQLException {
		this.wrappedConnection.setNetworkTimeout(executor, milliseconds);
	}

	@Override
	public int getNetworkTimeout() throws SQLException {
		return this.wrappedConnection.getNetworkTimeout();
	}
	
	protected void notifyConnectionListeners(final Consumer<? super L> event) {
		this.listeners.forEach(event);
	}
	
}
