package javagames.filesandres;

import java.io.*;

public class ReadingDataFromFiles {
	
	public void runTest() {
		String path = "./res/assets/text/lorem-ipsum.txt";
		File file = new File(path);
		readInBytes(file);
		readInStrings(file);
	}

	public void readInBytes(File file) {
		System.out.println();
		System.out.println();
		System.out.println("********************");
		System.out.println("Reading in bytes");
		System.out.println();
		InputStream in = null;
		try {
			in = new FileInputStream(file);
			int next = -1;
			while ((next = in.read()) != -1) {
				System.out.print((char) next);
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (IOException ex) {
			}
		}
	}

	public void readInStrings(File file) {
		System.out.println();
		System.out.println();
		System.out.println("********************");
		System.out.println("Reading in strings");
		System.out.println();
		Reader reader = null;
		try {
			reader = new FileReader(file);
			BufferedReader buf = new BufferedReader(reader);
			String line = null;
			while ((line = buf.readLine()) != null) {
				System.out.println(line);
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (IOException ex) {
			}
		}
	}

	public static void main(String[] args) {
		new ReadingDataFromFiles().runTest();
	}
}