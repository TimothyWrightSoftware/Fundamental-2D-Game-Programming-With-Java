package javagames.filesandres;

import java.util.Properties;

public class PrintSystemProperties {
	
	public static void main(String[] args) {
		
		Properties system = System.getProperties();
		for (Object key : system.keySet()) {
			System.out.println(key + "=" + system.getProperty(key.toString()));
		}
		System.out.println();
		System.out.println("––––––– Some Cool Props –––––––––––––");
		System.out
				.println("java.version=" + System.getProperty("java.version"));
		System.out.println("os.name=" + System.getProperty("os.name"));
		System.out
				.println("user.country=" + System.getProperty("user.country"));
		System.out.println("user.language="
				+ System.getProperty("user.language"));
		System.out.println("user.home=" + System.getProperty("user.home"));
		System.out.println("user.dir=" + System.getProperty("user.dir"));
		System.out.println("user.name=" + System.getProperty("user.name"));
	}
	
}