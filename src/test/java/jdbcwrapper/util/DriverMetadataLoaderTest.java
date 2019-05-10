package jdbcwrapper.util;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.junit.jupiter.api.Test;

public class DriverMetadataLoaderTest {

	@Test
	public void testDriverWithListeners() {
		String metadata = "postgresql.connection.wrapper=jdbcwrapper.test.TestConnectionWithListeners\n" + 
				"postgresql.connection.listeners=jdbcwrapper.test.TestConnectionListener1, jdbcwrapper.test.TestConnectionListener2";
		InputStream inputStream = new ByteArrayInputStream(metadata.getBytes());
		
		new DriverMetadataLoader().loadMetadata(inputStream);
	}
	
}
