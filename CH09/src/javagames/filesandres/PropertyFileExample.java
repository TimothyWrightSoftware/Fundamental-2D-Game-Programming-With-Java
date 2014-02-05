package javagames.filesandres;

import java.io.*;
import java.util.Properties;
import javagames.util.ResourceLoader;

public class PropertyFileExample {
	
	private Properties props;

	public PropertyFileExample() {
		props = new Properties();
	}

	private void loadAndPrintProperties() {
		boolean loadPropsFirst = getLoadFirstProperty();
		if (loadPropsFirst) {
			props.putAll(System.getProperties());
		}
		loadProperties();
		if (!loadPropsFirst) {
			props.putAll(System.getProperties());
		}
		printProperties();
	}

	private void loadProperties() {
		InputStream in = null;
		try {
			in = ResourceLoader.load(PropertyFileExample.class,
					"./testing.properties", "/testing.properties");
			props.load(in);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (Exception e) {
			}
		}
	}

	private boolean getLoadFirstProperty() {
		try {
			String value = System.getProperty("load.props.first", "false");
			return Boolean.parseBoolean(value);
		} catch (Exception e) {
			return false;
		}
	}

	private void printProperties() {
		System.out.println("load.props.first=" + getLoadFirstProperty());
		System.out.println("user.home=" + props.getProperty("user.home", "."));
		System.out.println("prop1=" + props.getProperty("prop1", "default1"));
		System.out.println("prop2=" + props.getProperty("prop2", "default2"));
		System.out.println("prop3=" + props.getProperty("prop3", "default3"));
		System.out.println("override="
				+ props.getProperty("override", "defaultOverride"));
	}

	public static void main(String[] args) {
		PropertyFileExample example = new PropertyFileExample();
		example.loadAndPrintProperties();
	}
}