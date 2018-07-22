package jdbcwrapper.util;

import java.sql.SQLException;

public interface IntSupplier {
	
	public int get() throws SQLException;

}
