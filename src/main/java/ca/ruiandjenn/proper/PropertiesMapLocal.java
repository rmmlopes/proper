package ca.ruiandjenn.proper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

public class PropertiesMapLocal extends PropertiesMap {
	private String fileName = null;

	public PropertiesMapLocal(Map map) {
		super(map);
	}

	public PropertiesMapLocal() {
	}

	public void load(String fileName) throws IOException {
		try {
			load(new FileInputStream(fileName));
			this.fileName = fileName;
		}
		catch (FileNotFoundException fnfe) {
			load(ClassLoader.getSystemResourceAsStream(fileName));
		}
	}
	
	public void store() throws IOException {
		if (fileName != null) {
			FileOutputStream fos = new FileOutputStream(new File(fileName));
			store(fos, true);
		}
	}
}
