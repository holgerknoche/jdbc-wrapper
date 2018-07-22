package jdbcwrapper.util;

import java.sql.SQLException;

public interface BooleanSupplier {
	
	public boolean get() throws SQLException;

}
