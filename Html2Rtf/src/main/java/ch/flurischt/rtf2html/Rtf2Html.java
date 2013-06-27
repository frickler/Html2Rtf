package ch.flurischt.rtf2html;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.StyleConstants;
import javax.swing.text.rtf.RTFEditorKit;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Node;

import ch.flurischt.rtf2html.parsers.BooleanElementParser;
import ch.flurischt.rtf2html.parsers.FontElementParser;
import ch.flurischt.rtf2html.parsers.RtfElementParser;

public class Rtf2Html {

	private List<RtfElementParser> parserItems;
	private org.jsoup.nodes.Element body;
	private Map<Node, Element> entries;

	public Rtf2Html() {
		super();
		entries = new LinkedHashMap<Node, Element>();
		parserItems = new ArrayList<RtfElementParser>();

		org.jsoup.nodes.Document document = Jsoup.parse("<body></body>");
		body = document.body();

		// setup the handlers
		parserItems.add(new FontElementParser("font"));
		parserItems.add(new BooleanElementParser("b", StyleConstants.Bold));
		parserItems.add(new BooleanElementParser("i", StyleConstants.Italic));
		parserItems
				.add(new BooleanElementParser("u", StyleConstants.Underline));

	}

	public String toHtml(String s2) {
		RTFEditorKit rtfeditorkit = new RTFEditorKit();
		DefaultStyledDocument defaultstyleddocument = new DefaultStyledDocument();
		readString(s2, defaultstyleddocument, rtfeditorkit);
		scanDocument(defaultstyleddocument);

		buildDom();

		return removeEmptyNodes(body).toString();
	}

	private void buildDom() {

		for (RtfElementParser r : parserItems) {
			r.parseDocElements(entries.entrySet().iterator());
		}

	}

	// private void checkForTag(String tag, Object constant) {
	// Iterator<Entry<Node, Element>> it = entries.entrySet().iterator();
	//
	// while (it.hasNext()) {
	// Map.Entry<Node, Element> entry = it.next();
	// if (RtfUtils.getBooleanValue(entry.getValue(), constant)
	// && !checkDomNodeForProperty(entry.getKey(), tag)) {
	// org.jsoup.nodes.Element boldNode = new org.jsoup.nodes.Element(
	// Tag.valueOf(tag), "");
	//
	// changeParent(entry.getKey(), boldNode);
	//
	// Node n = getNextSibling(boldNode);
	// while (checkNodeForProperty(n, constant)
	// && !checkDomNodeForProperty(n, tag)) {
	//
	// changeParent(n, boldNode);
	//
	// if (!it.hasNext())
	// break;
	//
	// // n = it.next().getKey();
	// n = getNextSibling(n);
	// }
	// }
	// // it.remove(); // avoids a ConcurrentModificationException
	// }
	// }

	private Node getNextSibling(org.jsoup.nodes.Node node) {

		if (node == null)
			return null;

		// �berpr�fung ob es sich um den letzten Nopde handelt
		int index = node.siblingIndex();
		if (index + 1 > node.siblingNodes().size() - 1)
			return null;

		return node.siblingNodes().get(index + 1);
	}

	private Boolean checkDomNodeForProperty(org.jsoup.nodes.Node node,
			String tagName) {

		while (node.parent() != null) {
			if (node instanceof org.jsoup.nodes.Element) {
				org.jsoup.nodes.Element ele = (org.jsoup.nodes.Element) node;
				if (ele.tagName().equals(tagName))
					return true;

			}
			node = node.parent();
		}

		return false;
	}

	private Boolean checkNodeForProperty(org.jsoup.nodes.Node node,
			Object constant) {

		Node sibling = getNextSibling(node);

		if (sibling == null)
			return false;

		// Wenn keine Kinder vorhanden sind, es ein ein Leaf
		if (sibling.childNodes().size() == 0) {
			if (entries.get(sibling) != null) {
				if (!RtfUtils.getBooleanValue(entries.get(sibling), constant)) {
					return false;
				}
			}
		} else {
			return checkAllDomChildrenHaveProperty(sibling, constant);
		}
		return false;
	}

	private Boolean checkAllDomChildrenHaveProperty(org.jsoup.nodes.Node node,
			Object constant) {

		for (Node child : node.childNodes()) {
			if (entries.get(child) != null)
				if (!RtfUtils.getBooleanValue(entries.get(child), constant))
					return false;

			return checkAllDomChildrenHaveProperty(child, constant);
		}
		return true;

	}

	protected org.jsoup.nodes.Element removeEmptyNodes(org.jsoup.nodes.Element e) {
		for (org.jsoup.nodes.Element element : e.select("*")) {

			if (!element.hasText()) {
				element.remove();
			}
		}
		return e;
	}

	private void readString(String s, Document document,
			RTFEditorKit rtfeditorkit) {
		try {
			ByteArrayInputStream bytearrayinputstream = new ByteArrayInputStream(
					s.getBytes());
			rtfeditorkit.read(bytearrayinputstream, document, 0);
		} catch (Exception exception) {
			return;
			// exception.printStackTrace();
		}
	}

	private void scanDocument(Document document) {

		Element element = document.getDefaultRootElement();
		parseElements(element, document);

	}

	private void parseElements(Element element, Document document) {
		for (int i = 0; i < element.getElementCount(); i++) {
			Element element1 = element.getElement(i);
			if (element1.isLeaf())
				addToMap(element1, document);
			parseElements(element1, document);
		}
	}

	private void addToMap(Element element, Document document) {
		int i = element.getStartOffset();
		int j = element.getEndOffset();
		String s;
		try {
			s = document.getText(i, j - i);
			org.jsoup.nodes.TextNode n = new org.jsoup.nodes.TextNode(s, "");
			body.appendChild(n);

			entries.put(n, element);
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
