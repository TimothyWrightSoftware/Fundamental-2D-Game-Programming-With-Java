package javagames.filesandres;

import java.io.*;

public class SavingPropertyFiles {
	
	public SavingPropertyFiles() {
		
	}

	public void saveFile() {
		File file = new File("testing.properties");
		PrintWriter out = null;
		try {
			out = new PrintWriter(file);
			out.println("# This is a comment");
			out.println("prop1=fileValue1");
			out.println("# This is another comment");
			out.println("prop2=fileValue2");
			out.println("# This can be overriden from the");
			out.println("! command line, or not...");
			out.println("override=fileoverride");
		} catch (FileNotFoundException fex) {
			fex.printStackTrace();
		} finally {
			try {
				out.close();
				System.out.println("Wrote: " + file.getPath());
			} catch (Exception e) {
			}
		}
	}

	public static void main(String[] args) {
		new SavingPropertyFiles().saveFile();
	}
}