package javagames.filesandres;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FilesAndDirectories {
	
	public FilesAndDirectories() {
		
	}

	public void runTest() {
		// list files and folders
		String dir = "D:\\code examples";
		File file = new File(dir);
		displayInfo(0, file);
	}

	private void displayInfo(int depth, File file) {
		// Name, Date, Size, Attr
		boolean executable = file.canExecute();
		boolean readable = file.canRead();
		boolean writable = file.canWrite();
		boolean hidden = file.isHidden();
		boolean directory = file.isDirectory();
		long lastModified = file.lastModified();
		long length = file.length();
		String name = file.getName();
		// create ASCII file structure
		StringBuilder buf = new StringBuilder();
		for (int i = 0; i < depth; ++i) {
			buf.append("|");
		}
		if (directory) {
			buf.append("+ ");
		}
		if (name.isEmpty()) {
			buf.append(".");
		} else {
			buf.append(name);
		}
		// add modification date
		buf.append("\t\t");
		Date date = new Date(lastModified);
		buf.append(new SimpleDateFormat().format(date));
		buf.append("\t\t");
		// add file size in kilobytes
		long kb = length / 1024;
		DecimalFormat format = new DecimalFormat();
		format.setGroupingUsed(true);
		buf.append(format.format(kb));
		buf.append(" KB");
		// add read, write, execute attribute flags
		buf.append("\t\t");
		if (hidden)
			buf.append(".");
		if (readable)
			buf.append("R");
		if (writable)
			buf.append("W");
		if (executable)
			buf.append("X");
		// print everything to the command line
		System.out.println(buf.toString());
		File[] children = file.listFiles();
		if (children != null) {
			for (File child : children) {
				displayInfo(depth + 1, child);
			}
		}
	}

	public static void main(String[] args) {
		new FilesAndDirectories().runTest();
	}
}