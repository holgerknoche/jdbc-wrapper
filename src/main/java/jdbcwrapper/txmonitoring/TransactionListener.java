package jdbcwrapper.txmonitoring;

public interface TransactionListener {
	
	public void onTransactionStart(TxMonitoringConnection connection);
	
	public void onTransactionEnd(TxMonitoringConnection connection, boolean success);

}
