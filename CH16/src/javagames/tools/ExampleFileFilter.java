package javagames.tools;

import java.io.File;
import javax.swing.filechooser.FileFilter;

public class ExampleFileFilter extends FileFilter {
	
	private String description;
	private String[] filters;

	public ExampleFileFilter(String description, String[] filters) {
		this.description = description;
		this.filters = filters;
	}

	@Override
	public boolean accept(File f) {
		if (f.isDirectory()) {
			return true;
		}
		String extension = getExtension(f);
		if (extension != null) {
			for (String filter : filters) {
				if (extension.equalsIgnoreCase(filter)) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public String getDescription() {
		return description;
	}

	public String getExtension(File f) {
		String ext = null;
		String s = f.getName();
		int i = s.lastIndexOf('.');
		if (i > 0 && i < s.length() - 1) {
			ext = s.substring(i + 1).toLowerCase();
		}
		return ext;
	}
}