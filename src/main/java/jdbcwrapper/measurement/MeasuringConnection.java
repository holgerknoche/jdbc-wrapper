package jdbcwrapper.measurement;

import java.sql.Statement;

public interface MeasuringConnection {
	
	public void onStatementStart(Statement statement);
	
	public void onStatementEnd(Statement statement);

}
