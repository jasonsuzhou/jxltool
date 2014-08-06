package com.mh.jxltool.util;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Attribute;
import org.dom4j.CDATA;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.Text;
import org.dom4j.io.SAXReader;

/**
 * 
 * @author Jason
 * @date 2011-09-29
 * 
 */
public class XMLUtil {

	public static Document xmlFileToDom(File xmlFile) throws Exception {
		return new SAXReader().read(xmlFile);
	}

	public static Document xmlFileToDom(String strXmlFilePath) throws Exception {
		if (StringUtils.isBlank(strXmlFilePath)) {
			throw new Exception("The xml file path is null or ''");
		}
		Document docImpl = null;
		SAXReader reader = new SAXReader();
		try {
			docImpl = reader.read(new File(strXmlFilePath));
		} catch (DocumentException e) {
			throw new Exception(e);
		}
		return docImpl;
	}

	public static Document xmlStrToDom(final String strXmlContent) throws Exception {
		if (StringUtils.isBlank(strXmlContent)) {
			throw new Exception("The xml content is null or ''");
		}
		String s = strXmlContent;
		s = s.trim();
		if (s.length() > 0) {
			if (s.charAt(0) != '<')
				throw new Exception("The String format is not a xml format");
		}
		Document docImpl = null;
		try {
			docImpl = DocumentHelper.parseText(s);
		} catch (Exception e) {
			throw new Exception(e);
		}
		return docImpl;
	}

	public static Element getRootElement(Document doc) throws Exception {
		if (doc == null) {
			throw new Exception("The document is null, connot get it's root element");
		}
		return doc.getRootElement();
	}

	public static String getNodeAttribute(Node nNode, String strNodeAttributeName) {
		if (nNode == null) {
			return null;
		}
		if (nNode.getNodeType() == Element.ELEMENT_NODE) {
			Element e = (Element) nNode;
			return e.attributeValue(strNodeAttributeName);
		}
		return null;
	}

	public static String getNodeValue(Node sNode, boolean isTrim) {
		if (sNode == null) {
			return null;
		}
		try {
			String sv = null;
			if (sNode.getNodeType() == Element.ELEMENT_NODE || sNode.getNodeType() == Element.CDATA_SECTION_NODE) {
				sv = sNode.getText();
				if (sv != null && isTrim) {
					sv = sv.trim();
				}
			}
			return sv;
		} catch (Exception e) {
			return null;
		}
	}

	public static Element findChildNode(String strChildNodeName, Node nParent) {
		if (nParent == null) {
			return null;
		}
		Element e = (Element) nParent;
		return e.element(strChildNodeName);
	}

	public static Node findNode(Node nFromNode, String strNodeName) {
		if (nFromNode == null) {
			return null;
		}
		if (nFromNode.getName().equals(strNodeName)) {
			return nFromNode;
		}
		Element e = (Element) nFromNode;
		Iterator it = e.elementIterator();
		Node n = null;
		int nodeType = 1;
		String nodeName = null;
		Node returnNode = null;
		while (returnNode == null) {
			while (it.hasNext()) {
				n = (Element) it.next();
				nodeType = n.getNodeType();
				nodeName = n.getName();
				if (nodeType == Element.ELEMENT_NODE && nodeName.equals(strNodeName)) {
					returnNode = n;
					break;
				} else {
					returnNode = findNode(n, strNodeName);
					if (returnNode != null) {
						break;
					}
				}
			}
		}
		return returnNode;
	}

	public static List findChildNodes(String strNodeName, Node nParent) {
		if (nParent == null) {
			return null;
		}
		Element e = (Element) nParent;
		return e.elements(strNodeName);
	}

	public static String getChildNodeValue(Node nParent, String strChildNodeName, boolean isTrim) {
		if (nParent == null) {
			return null;
		}
		if (StringUtils.isNotBlank(strChildNodeName)) {
			Node cNode = findChildNode(strChildNodeName, nParent);
			return getNodeValue(cNode, true);
		} else {
			return null;
		}
	}

	public static String getNodeValue(Document dmDom, String strNodeName, boolean isTrim) {
		if (dmDom == null) {
			return null;
		}
		Node sNode = findNode(dmDom.getRootElement(), strNodeName);
		return getNodeValue(sNode, true);
	}

	public static String convertToString(Document dmDom) throws Exception {
		if (dmDom == null) {
			throw new Exception("The document is null, cannot convert to String");
		}
		return dmDom.asXML();
	}

	public static String convertDomToString(Document dmDom) throws Exception {
		return convertToString(dmDom);
	}

	public static void setNodeAttribute(Node node, String attriName, String value) {
		Element e = (Element) node;
		Attribute attr = e.attribute(attriName);
		if (attr != null) {
			e.remove(attr);
		}
		e.addAttribute(attriName, value);
	}

	public static void removeChildNode(Node parentNode, String childName) {
		if (parentNode == null || childName == null) {
			return;
		}
		List childNodes = findChildNodes(childName, parentNode);
		Element pe = (Element) parentNode;
		if (childNodes != null && childNodes.size() > 0) {
			Node ce = null;
			for (int i = 0, len = childNodes.size(); i < len; i++) {
				ce = (Node) childNodes.get(i);
				pe.remove(ce);
			}
		}
		childNodes = null;
	}

	public static Element populateElement(Document dom, Node parentNode, String eleName, String eleValue,
			boolean isCData) {
		return populateElement(parentNode, eleName, eleValue, isCData);
	}

	public static Element populateElement(Node parentNode, String eleName, String eleValue, boolean isCData) {
		Element childElement = DocumentHelper.createElement(eleName);
		Element eParentNode = (Element) parentNode;
		eParentNode.add(childElement);
		if (StringUtils.isNotBlank(eleValue)) {
			if (isCData) {
				CDATA cdata = DocumentHelper.createCDATA(eleValue);
				childElement.add(cdata);
			} else {
				Text text = DocumentHelper.createText(eleValue);
				childElement.add(text);
			}
		}
		return childElement;
	}

	public static Element populateCDATAElement(Document dom, Node parentNode, String eleName, String eleValue) {
		return populateCDATAElement(parentNode, eleName, eleValue);
	}

	public static Element populateCDATAElement(Node parentNode, String eleName, String eleValue) {
		return populateElement(parentNode, eleName, eleValue, true);
	}

	public static Element populateTextElement(Document dom, Node parentNode, String eleName, String eleValue) {
		return populateTextElement(parentNode, eleName, eleValue);
	}

	public static Element populateTextElement(Node parentNode, String eleName, String eleValue) {
		return populateElement(parentNode, eleName, eleValue, false);
	}

	public static Node find(List<Node> nodes, String nodeName) {
		String ndName = null;
		for (Node nd : nodes) {
			ndName = nd.getName();
			if (ndName.equals(nodeName)) {
				return nd;
			}
		}
		return null;
	}

	public static void renameElementName(Node objectEle, String newName) {
		Element e = (Element) objectEle;
		e.setName(newName);
	}

	public static void renameElementName(Document doc, Node objectEle, String newName) {
		renameElementName(objectEle, newName);
	}

	public static void appendUnderElement(Node nAppendTo, Node nAppendNode) {
		Element e = (Element) nAppendTo;
		String ndName = nAppendNode.getName();
		Node nd = findChildNode(ndName, nAppendTo);
		if (nd != null) {
			e.remove(nd);
		}
		e.add(nAppendNode);
	}

	public static void appendUnderRoot(Document dmDom, Node nAppendNode) {
		appendUnderElement(dmDom.getRootElement(), nAppendNode);
	}

	public static Document createDocument(String rootName) {
		Element eRoot = DocumentHelper.createElement(rootName);
		return DocumentHelper.createDocument(eRoot);
	}
}
