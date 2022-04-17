package operation;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class ReadObject {
	Properties p = new Properties();

	public Properties getObjectRepository(String filepath) {
		try {
			System.out.println("property file path. " + filepath);
			// Read object repository file
			InputStream stream = new FileInputStream(new File(filepath));
			// load all objects
			p.load(stream);
		} catch (Exception e) {
			System.out.println("Problem in file path. " + filepath);
		}
		return p;
	}

}
