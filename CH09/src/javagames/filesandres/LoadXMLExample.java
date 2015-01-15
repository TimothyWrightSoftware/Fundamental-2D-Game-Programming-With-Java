package javagames.filesandres;

import java.io.*;
import java.util.List;
import org.w3c.dom.*;
import org.xml.sax.*;
import javagames.util.*;
import javax.xml.parsers.ParserConfigurationException;

public class LoadXMLExample {
	
	public LoadXMLExample() {
	}

	public void loadFile() {
		InputStream in = null;
		try {
			in = ResourceLoader.load(LoadXMLExample.class, "sample.xml", "/sample/xml");
			Document document = XMLUtility.parseDocument(in);
			System.out.println("*********");
			System.out.println("* INSPECT");
			inspectXML(document);
			System.out.println("*********");
			System.out.println("* PARSE");
			parseXML(document);
		} catch (ParserConfigurationException pex) {
			System.out.println("ParserConfigurationException");
			pex.printStackTrace();
		} catch (SAXException ex) {
			System.out.println("SAXException");
			ex.printStackTrace();
		} catch (IOException ex) {
			System.out.println("IOException");
			ex.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (IOException ex) {
			}
		}
	}

	private void inspectXML(Node node) {
		printNode(node);
		if (node.hasAttributes()) {
			NamedNodeMap nodeMap = node.getAttributes();
			for (int i = 0; i < nodeMap.getLength(); ++i) {
				inspectXML(nodeMap.item(i));
			}
		}
		if (node.hasChildNodes()) {
			NodeList nodeList = node.getChildNodes();
			for (int i = 0; i < nodeList.getLength(); ++i) {
				inspectXML(nodeList.item(i));
			}
		}
	}

	private void printNode(Node node) {
		System.out.print("Type: " + getNodeType(node));
		System.out.print(", Name: " + node.getNodeName());
		String value = node.getNodeValue() == null ? "" : node.getNodeValue()
				.trim();
		System.out.println(", Value: '" + value + "'");
	}

	private String getNodeType(Node node) {
		switch (node.getNodeType()) {
		case Node.ATTRIBUTE_NODE:
			return "ATTRIBUTE_NODE";
		case Node.CDATA_SECTION_NODE:
			return "CDATA_SECTION_NODE";
		case Node.COMMENT_NODE:
			return "COMMENT_NODE";
		case Node.DOCUMENT_NODE:
			return "DOCUMENT_NODE";
		case Node.ELEMENT_NODE:
			return "ELEMENT_NODE";
		case Node.ENTITY_NODE:
			return "ENTITY_NODE";
		case Node.TEXT_NODE:
			return "TEXT_NODE";
		default:
			return "Unknown";
		}
	}

	private void parseXML(Document document) {
		Element element = document.getDocumentElement();
		List<Element> elements = XMLUtility.getAllElements(element, "empty");
		System.out.println("Element: " + elements.get(0).getNodeName());
		List<Element> shorthand = XMLUtility.getAllElements(element,
				"shorthand");
		System.out.println("Element: " + shorthand.get(0).getNodeName());
		List<Element> text = XMLUtility.getAllElements(element, "text");
		System.out.println("Text: " + text.get(0).getTextContent());
		List<Element> nested = XMLUtility.getAllElements(element, "nested");
		System.out.println("Element: " + nested.get(0).getNodeName());
		List<Element> elementChildren = XMLUtility
				.getElements(element, "child");
		System.out.println("Get-Child-Count: " + elementChildren.size());
		List<Element> child = XMLUtility.getElements(nested.get(0), "child");
		System.out.println("Get-Child-Count: " + child.size());
		List<Element> allChildren = XMLUtility.getAllElements(element, "child");
		System.out.println("Get-AllChild-Count: " + allChildren.size());
		List<Element> nestedText = XMLUtility.getElements(element,
				"nested-text");
		System.out
				.println("nested-text: " + nestedText.get(0).getTextContent());
		List<Element> nestedChild = XMLUtility.getElements(nestedText.get(0),
				"child");
		System.out.println("nested-text: " + nestedChild.get(0).getNodeName());
		List<Element> attributes = XMLUtility
				.getElements(element, "attributes");
		Element attrElement = attributes.get(0);
		System.out.println("attr1: " + attrElement.getAttribute("attr1"));
		System.out.println("attr2: " + attrElement.getAttribute("attr2"));
		List<Element> specialChars = XMLUtility.getElements(element,
				"special-chars");
		System.out.println("special-chars: "
				+ specialChars.get(0).getTextContent());
		List<Element> cdata = XMLUtility.getElements(element, "cdata");
		System.out.println("cdata: " + cdata.get(0).getTextContent());
	}

	public static void main(String[] args) {
		new LoadXMLExample().loadFile();
	}
}