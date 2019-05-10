package jdbcwrapper.txmonitoring.postgres;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import jdbcwrapper.measurement.MeasuringConnection;
import jdbcwrapper.txmonitoring.AbstractTxMonitoringConnection;
import jdbcwrapper.txmonitoring.TransactionListener;

public class PostgresMonitoringConnection extends AbstractTxMonitoringConnection<TransactionListener> implements MeasuringConnection {

	public PostgresMonitoringConnection(final Connection wrappedConnection, final List<TransactionListener> listeners) {
		super(wrappedConnection, listeners);
	}

	@Override
	public int getTransactionId() {
		try (Statement statement = this.createStatement()) {
			ResultSet resultSet = statement.executeQuery("select txid_current()");

			if (resultSet.next()) {
				return resultSet.getInt(1);
			} else {
				return 0;
			}
		} catch (SQLException e) {
			return 0;
		}
	}
		
}
