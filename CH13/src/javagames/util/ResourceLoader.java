package javagames.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class ResourceLoader {
	
	public static InputStream load(Class<?> clazz, String filePath,
			String resPath) {
		// try the resource first
		InputStream in = null;
		if (!(resPath == null || resPath.isEmpty())) {
			in = clazz.getResourceAsStream(resPath);
		}
		if (in == null) {
			// try the file path
			try {
				in = new FileInputStream(filePath);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		return in;
	}
	
}