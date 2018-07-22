package jdbcwrapper;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;

public class WrappedPreparedStatement extends WrappedStatement implements PreparedStatement {

	private final PreparedStatement wrappedStatement;
	
	public WrappedPreparedStatement(final PreparedStatement statement) {
		super(statement);
		
		this.wrappedStatement = statement;
	}

	@Override
	public ResultSet executeQuery() throws SQLException {
		return this.wrappedStatement.executeQuery();
	}

	@Override
	public int executeUpdate() throws SQLException {
		return this.wrappedStatement.executeUpdate();
	}

	@Override
	public void setNull(final int parameterIndex, final int sqlType) throws SQLException {
		this.wrappedStatement.setNull(parameterIndex, sqlType);
	}

	@Override
	public void setBoolean(final int parameterIndex, final boolean x) throws SQLException {
		this.wrappedStatement.setBoolean(parameterIndex, x);
	}

	@Override
	public void setByte(final int parameterIndex, final byte x) throws SQLException {
		this.wrappedStatement.setByte(parameterIndex, x);
	}

	@Override
	public void setShort(final int parameterIndex, final short x) throws SQLException {
		this.wrappedStatement.setShort(parameterIndex, x);
	}

	@Override
	public void setInt(final int parameterIndex, final int x) throws SQLException {
		this.wrappedStatement.setInt(parameterIndex, x);
	}

	@Override
	public void setLong(final int parameterIndex, final long x) throws SQLException {
		this.wrappedStatement.setLong(parameterIndex, x);
	}

	@Override
	public void setFloat(final int parameterIndex, final float x) throws SQLException {
		this.wrappedStatement.setFloat(parameterIndex, x);
	}

	@Override
	public void setDouble(final int parameterIndex, final double x) throws SQLException {
		this.wrappedStatement.setDouble(parameterIndex, x);
	}

	@Override
	public void setBigDecimal(final int parameterIndex, final BigDecimal x) throws SQLException {
		this.wrappedStatement.setBigDecimal(parameterIndex, x);
	}

	@Override
	public void setString(final int parameterIndex, final String x) throws SQLException {
		this.wrappedStatement.setString(parameterIndex, x);
	}

	@Override
	public void setBytes(final int parameterIndex, final byte[] x) throws SQLException {
		this.wrappedStatement.setBytes(parameterIndex, x);
	}

	@Override
	public void setDate(final int parameterIndex, final Date x) throws SQLException {
		this.wrappedStatement.setDate(parameterIndex, x);
	}

	@Override
	public void setTime(final int parameterIndex, final Time x) throws SQLException {
		this.wrappedStatement.setTime(parameterIndex, x);
	}

	@Override
	public void setTimestamp(final int parameterIndex, final Timestamp x) throws SQLException {
		this.wrappedStatement.setTimestamp(parameterIndex, x);
	}

	@Override
	public void setAsciiStream(final int parameterIndex, final InputStream x, final int length) throws SQLException {
		this.wrappedStatement.setAsciiStream(parameterIndex, x, length);
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public void setUnicodeStream(final int parameterIndex, final InputStream x, final int length) throws SQLException {
		this.wrappedStatement.setUnicodeStream(parameterIndex, x, length);
	}

	@Override
	public void setBinaryStream(final int parameterIndex, final InputStream x, final int length) throws SQLException {
		this.wrappedStatement.setBinaryStream(parameterIndex, x, length);
	}

	@Override
	public void clearParameters() throws SQLException {
		this.wrappedStatement.clearParameters();
	}

	@Override
	public void setObject(final int parameterIndex, final Object x, final int targetSqlType) throws SQLException {
		this.wrappedStatement.setObject(parameterIndex, x, targetSqlType);
	}

	@Override
	public void setObject(final int parameterIndex, final Object x) throws SQLException {
		this.wrappedStatement.setObject(parameterIndex, x);
	}

	@Override
	public boolean execute() throws SQLException {
		return this.wrappedStatement.execute();
	}

	@Override
	public void addBatch() throws SQLException {
		this.wrappedStatement.addBatch();
	}

	@Override
	public void setCharacterStream(final int parameterIndex, final Reader reader, final int length) throws SQLException {
		this.wrappedStatement.setCharacterStream(parameterIndex, reader, length);
	}

	@Override
	public void setRef(final int parameterIndex, final Ref x) throws SQLException {
		this.wrappedStatement.setRef(parameterIndex, x);
	}

	@Override
	public void setBlob(final int parameterIndex, final Blob x) throws SQLException {
		this.wrappedStatement.setBlob(parameterIndex, x);
	}

	@Override
	public void setClob(final int parameterIndex, final Clob x) throws SQLException {
		this.wrappedStatement.setClob(parameterIndex, x);
	}

	@Override
	public void setArray(final int parameterIndex, final Array x) throws SQLException {
		this.wrappedStatement.setArray(parameterIndex, x);
	}

	@Override
	public ResultSetMetaData getMetaData() throws SQLException {
		return this.wrappedStatement.getMetaData();
	}

	@Override
	public void setDate(final int parameterIndex, final Date x, final Calendar cal) throws SQLException {
		this.wrappedStatement.setDate(parameterIndex, x, cal);
	}

	@Override
	public void setTime(final int parameterIndex, final Time x, final Calendar cal) throws SQLException {
		this.wrappedStatement.setTime(parameterIndex, x, cal);
	}

	@Override
	public void setTimestamp(final int parameterIndex, final Timestamp x, final Calendar cal) throws SQLException {
		this.wrappedStatement.setTimestamp(parameterIndex, x, cal);
	}

	@Override
	public void setNull(final int parameterIndex, final int sqlType, final String typeName) throws SQLException {
		this.wrappedStatement.setNull(parameterIndex, sqlType, typeName);
	}

	@Override
	public void setURL(final int parameterIndex, final URL x) throws SQLException {
		this.wrappedStatement.setURL(parameterIndex, x);
	}

	@Override
	public ParameterMetaData getParameterMetaData() throws SQLException {
		return this.wrappedStatement.getParameterMetaData();
	}

	@Override
	public void setRowId(final int parameterIndex, final RowId x) throws SQLException {
		this.wrappedStatement.setRowId(parameterIndex, x);
	}

	@Override
	public void setNString(final int parameterIndex, final String value) throws SQLException {
		this.wrappedStatement.setNString(parameterIndex, value);
	}

	@Override
	public void setNCharacterStream(final int parameterIndex, final Reader value, final long length) throws SQLException {
		this.wrappedStatement.setNCharacterStream(parameterIndex, value, length);
	}

	@Override
	public void setNClob(final int parameterIndex, final NClob value) throws SQLException {
		this.wrappedStatement.setNClob(parameterIndex, value);
	}

	@Override
	public void setClob(final int parameterIndex, final Reader reader, final long length) throws SQLException {
		this.wrappedStatement.setClob(parameterIndex, reader, length);
	}

	@Override
	public void setBlob(final int parameterIndex, final InputStream inputStream, final long length) throws SQLException {
		this.wrappedStatement.setBlob(parameterIndex, inputStream, length);
	}

	@Override
	public void setNClob(final int parameterIndex, final Reader reader, final long length) throws SQLException {
		this.wrappedStatement.setNClob(parameterIndex, reader, length);
	}

	@Override
	public void setSQLXML(final int parameterIndex, final SQLXML xmlObject) throws SQLException {
		this.wrappedStatement.setSQLXML(parameterIndex, xmlObject);
	}

	@Override
	public void setObject(final int parameterIndex, final Object x, final int targetSqlType, final int scaleOrLength) throws SQLException {
		this.wrappedStatement.setObject(parameterIndex, x, targetSqlType, scaleOrLength);
	}

	@Override
	public void setAsciiStream(final int parameterIndex, final InputStream x, final long length) throws SQLException {
		this.wrappedStatement.setAsciiStream(parameterIndex, x, length);
	}

	@Override
	public void setBinaryStream(final int parameterIndex, final InputStream x, final long length) throws SQLException {
		this.wrappedStatement.setBinaryStream(parameterIndex, x, length);
	}

	@Override
	public void setCharacterStream(final int parameterIndex, final Reader reader, final long length) throws SQLException {
		this.wrappedStatement.setCharacterStream(parameterIndex, reader, length);
	}

	@Override
	public void setAsciiStream(final int parameterIndex, final InputStream x) throws SQLException {
		this.wrappedStatement.setAsciiStream(parameterIndex, x);
	}

	@Override
	public void setBinaryStream(final int parameterIndex, final InputStream x) throws SQLException {
		this.wrappedStatement.setBinaryStream(parameterIndex, x);
	}

	@Override
	public void setCharacterStream(final int parameterIndex, final Reader reader) throws SQLException {
		this.wrappedStatement.setCharacterStream(parameterIndex, reader);
	}

	@Override
	public void setNCharacterStream(final int parameterIndex, final Reader value) throws SQLException {
		this.wrappedStatement.setNCharacterStream(parameterIndex, value);
	}

	@Override
	public void setClob(final int parameterIndex, final Reader reader) throws SQLException {
		this.wrappedStatement.setClob(parameterIndex, reader);
	}

	@Override
	public void setBlob(final int parameterIndex, final InputStream inputStream) throws SQLException {
		this.wrappedStatement.setBlob(parameterIndex, inputStream);
	}

	@Override
	public void setNClob(final int parameterIndex, final Reader reader) throws SQLException {
		this.wrappedStatement.setNClob(parameterIndex, reader);
	}
	
}
