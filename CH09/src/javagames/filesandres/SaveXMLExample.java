package javagames.filesandres;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class SaveXMLExample {
	
	public SaveXMLExample() {
		
	}

	public void createXMLFile() {
		File file = new File("sample.xml");
		PrintWriter out = null;
		try {
			out = new PrintWriter(file);
			writeXML(out);
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

	private void writeXML(PrintWriter out) {
		out.println("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
		// all XML must have a single root tag
		out.println("<root>");
		// a comment
		out.println(" <!-- This is a comment -->");
		// and empty tag
		out.println(" <empty></empty>");
		// shorthand for a tag with no children
		out.println(" <shorthand/>");
		// text element
		out.println(" <text>I'm some text</text>");
		// nested elements
		out.println(" <nested><child/></nested>");
		// nested text with child
		out.println(" <nested-text>I'm some text <child/></nested-text>");
		// attributes
		out.println(" <attributes attr1=\"value1\" attr2=\"value2\" />");
		// special characters
		out.println(" <special-chars> &lt; &gt; &amp; &apos;"
				+ " &quot; </special-chars>");
		// unparsed character data CDATA
		out.println(" <cdata>");
		// Starting CDATA tag
		out.println(" <![CDATA[");
		out.println("<xml><attr=\"xml inside xml\"/></xml>");
		// Ending CDATA tag
		out.println(" ]]>");
		out.println(" </cdata>");
		// ending root tag
		out.println("</root>");
	}

	public static void main(String[] args) {
		new SaveXMLExample().createXMLFile();
	}
}