package ch.flurischt.rtf2html.parsers;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.text.Element;

import org.jsoup.nodes.Node;
import org.jsoup.parser.Tag;

import ch.flurischt.rtf2html.RtfUtils;

public abstract class RtfElementParser {

	private Tag tag;

	public RtfElementParser(String tagName) {
		this.tag = Tag.valueOf(tagName);
	}

	/**
	 * Checks whether an RTF-Element has the desired constant
	 * 
	 * @return
	 */
	protected abstract Boolean checkEntry(Element element);

	protected abstract org.jsoup.nodes.Element createNewElement(
			Element rtfElement, Tag tag);

	public void parseDocElements(Iterator<Entry<Node, Element>> it) {
		while (it.hasNext()) {
			Map.Entry<Node, Element> entry = it.next();
			if (checkEntry(entry.getValue())) {

				changeParent(entry.getKey(),
						createNewElement(entry.getValue(), tag));

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
			}
			// it.remove(); // avoids a ConcurrentModificationException
		}
	}

	/**
	 * Changes the position of two nodes
	 * 
	 * @param oldParent
	 * @param newParent
	 */
	private void changeParent(org.jsoup.nodes.Node node,
			org.jsoup.nodes.Element newParent) {

		if (newParent == null)
			return;

		node.replaceWith(newParent);
		newParent.appendChild(node);

	}

}
