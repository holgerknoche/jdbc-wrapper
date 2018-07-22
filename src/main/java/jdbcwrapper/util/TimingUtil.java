package jdbcwrapper.util;

import java.sql.SQLException;
import java.sql.Statement;

import jdbcwrapper.measurement.MeasuringConnection;

public class TimingUtil {
	
	public static int time(final IntSupplier method, final Statement statement, final MeasuringConnection connection) throws SQLException {
		connection.onStatementStart(statement);
		try {
			return method.get();
		} finally {
			connection.onStatementEnd(statement);
		}		
	}
	
	public static boolean time(final BooleanSupplier method, final Statement statement, final MeasuringConnection connection) throws SQLException {
		connection.onStatementStart(statement);
		try {
			return method.get();
		} finally {
			connection.onStatementEnd(statement);
		}
	}
	
	public static <T> T time(final ObjectSupplier<T> method, final Statement statement, final MeasuringConnection connection) throws SQLException {
		connection.onStatementStart(statement);
		try {
			return method.get();
		} finally {
			connection.onStatementEnd(statement);
		}
	}

}
