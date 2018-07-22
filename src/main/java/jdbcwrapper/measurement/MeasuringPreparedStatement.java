package jdbcwrapper.measurement;

import static jdbcwrapper.util.TimingUtil.time;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import jdbcwrapper.WrappedPreparedStatement;

public class MeasuringPreparedStatement extends WrappedPreparedStatement {

	private final MeasuringConnection connection;
	
	public MeasuringPreparedStatement(final PreparedStatement wrappedStatement, final MeasuringConnection connection) {
		super(wrappedStatement);
		
		this.connection = connection;
	}
	
	@Override
	public boolean execute(final String sql) throws SQLException {
		return time(() -> super.execute(sql), this, this.connection);
	}
	
	@Override
	public boolean execute(final String sql, final int autoGeneratedKeys) throws SQLException {
		return time(() -> super.execute(sql, autoGeneratedKeys), this, this.connection);
	}
	
	@Override
	public boolean execute(final String sql, final int[] columnIndexes) throws SQLException {
		return time(() -> super.execute(sql, columnIndexes), this, this.connection);
	}
	
	@Override
	public boolean execute(final String sql, final String[] columnNames) throws SQLException {
		return time(() -> super.execute(sql, columnNames), this, this.connection);
	}
	
	@Override
	public int[] executeBatch() throws SQLException {
		return time(() -> super.executeBatch(), this, this.connection);
	}
	
	@Override
	public ResultSet executeQuery(final String sql) throws SQLException {
		return time(() -> super.executeQuery(sql), this, this.connection);
	}
	
	@Override
	public int executeUpdate(final String sql) throws SQLException {
		return time(() -> super.executeUpdate(sql), this, this.connection);
	}
	
	@Override
	public int executeUpdate(final String sql, final int autoGeneratedKeys) throws SQLException {
		return time(() -> super.executeUpdate(sql, autoGeneratedKeys), this, this.connection);
	}
	
	@Override
	public int executeUpdate(final String sql, final int[] columnIndexes) throws SQLException {
		return time(() -> super.executeUpdate(sql, columnIndexes), this, this.connection);
	}
	
	@Override
	public int executeUpdate(final String sql, final String[] columnNames) throws SQLException {
		return time(() -> super.executeUpdate(sql, columnNames), this, this.connection);
	}
	
	@Override
	public boolean execute() throws SQLException {
		return time(() -> super.execute(), this, this.connection);
	}
	
	@Override
	public ResultSet executeQuery() throws SQLException {
		return time(() -> super.executeQuery(), this, this.connection);
	}
	
	@Override
	public int executeUpdate() throws SQLException {
		return time(() -> super.executeUpdate(), this, this.connection);
	}
	
}