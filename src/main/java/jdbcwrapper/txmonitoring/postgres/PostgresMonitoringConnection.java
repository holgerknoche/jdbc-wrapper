package jdbcwrapper.txmonitoring.postgres;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import jdbcwrapper.measurement.MeasuringConnection;
import jdbcwrapper.txmonitoring.AbstractTxMonitoringConnection;

public class PostgresMonitoringConnection extends AbstractTxMonitoringConnection implements MeasuringConnection {

	public PostgresMonitoringConnection(final Connection wrappedConnection) {
		super(wrappedConnection);
	}

	@Override
	protected int getTransactionId() {
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
