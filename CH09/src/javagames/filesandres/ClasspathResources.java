package javagames.filesandres;

import java.io.*;

public class ClasspathResources {
	
	public ClasspathResources() {
		
	}

	public void runTest() {
		
		// added reminder
		System.out.println( "Do not forget to add the ./res/asserts/lib folder to the classpath..." ); 
		
		/*
		 * ClassLoader uses absolute path. There is NO '/' at the beginning of
		 * the path!
		 */
		System.out.println();
		System.out.println("***************************");
		System.out.println("ClassLoader - Absolute Path");
		System.out.println();
		InputStream in = ClassLoader
				.getSystemResourceAsStream("javagames/filesandres/Test1.txt");
		printResource(in);
		/*
		 * getClass() instead of class loader. Can be relative...
		 */
		System.out.println();
		System.out.println("***************************");
		System.out.println("getClass() - Relative path");
		System.out.println();
		in = getClass().getResourceAsStream("Test2.txt");
		printResource(in);
		/*
		 * getClass() can also use the absolute path, but it needs a '/' at the
		 * start of the path.
		 */
		System.out.println();
		System.out.println("***************************");
		System.out.println("getClass() - Absolute path");
		System.out.println();
		in = getClass().getResourceAsStream("/javagames/filesandres/Test3.txt");
		printResource(in);
		/*
		 * Because getClass() always returns the subclass, if a subclass is
		 * created in another package, the relative path may not be correct.
		 * However, using an absolute path doesn't allow packages to be moved
		 * around. Use the static class, which also works in static methods.
		 */
		System.out.println();
		System.out.println("***************************");
		System.out.println("getClass() - Absolute path");
		System.out.println();
		in = ClasspathResources.class.getResourceAsStream("Test3.txt");
		printResource(in);
		/*
		 * Either ClassLoader or Class will return null for unknown resources
		 */
		in = getClass().getResourceAsStream("fat/finger/mistake");
		if (in == null) {
			System.out.println();
			System.out.println("***************************");
			System.out.println("Got a null back!!!");
		}
		in = ClassLoader.getSystemResourceAsStream("fat/finger/mistake");
		if (in == null) {
			System.out.println("Got another null back!!!");
		}
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
		new ClasspathResources().runTest();
	}
}