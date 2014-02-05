package javagames.filesandres;

import java.io.*;
import javagames.util.ResourceLoader;


public class ResourceLoaderExample {
	
	public ResourceLoaderExample() {
		
	}

	public void runTest() {
		Class<?> clazz = ResourceLoaderExample.class;
		// load absolute resource
		String filePath = "not/used";
		String resPath = "/javagames/filesandres/Test1.txt";
		InputStream in = ResourceLoader.load(clazz, filePath, resPath);
		printResource(in);
		// load relative resource
		filePath = "not/used";
		resPath = "Test2.txt";
		in = ResourceLoader.load(clazz, filePath, resPath);
		printResource(in);
		// load absolute file path
		filePath = "C:/Book/res/assets/lib/javagames/filesandres/Test3.txt";
		resPath = "/not/available";
		in = ResourceLoader.load(clazz, filePath, resPath);
		printResource(in);
		// load relative file path
		filePath = "res/assets/lib/javagames/filesandres/Test3.txt";
		resPath = "/not/available";
		in = ResourceLoader.load(clazz, filePath, resPath);
		printResource(in);
		// error with both is null
		filePath = "fat/finger";
		resPath = "fat/finger/too";
		in = ResourceLoader.load(clazz, filePath, resPath);
		printResource(in);
	}

	private void printResource(InputStream in) {
		try {
			InputStreamReader reader = new InputStreamReader(in);
			BufferedReader buf = new BufferedReader(reader);
			String line = null;
			while ((line = buf.readLine()) != null) {
				System.out.println(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (Exception e) {
			}
		}
	}

	public static void main(String[] args) {
		new ResourceLoaderExample().runTest();
	}
}