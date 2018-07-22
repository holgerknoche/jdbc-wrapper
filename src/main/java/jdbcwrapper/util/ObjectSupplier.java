package jdbcwrapper.util;

import java.sql.SQLException;

public interface ObjectSupplier<T> {

	public T get() throws SQLException;
	
}
