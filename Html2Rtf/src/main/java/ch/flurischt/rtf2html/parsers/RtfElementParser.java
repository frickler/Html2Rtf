package ch.flurischt.rtf2html.parsers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.text.Element;

import org.jsoup.nodes.Node;
import org.jsoup.parser.Tag;

import ch.flurischt.rtf2html.DomUtils;

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

				List<Node> siblings = new ArrayList<Node>();
				// org.jsoup.nodes.Element node = createNewElement(
				// entry.getValue(), tag);

				siblings.add(entry.getKey());

				// changeParent(n, node);
				if (!it.hasNext())
					break;

				entry = it.next();
				while (checkEntry(entry.getValue())) {
					siblings.add(entry.getKey());

					if (!it.hasNext())
						break;

					entry = it.next();
				}

				// TODO optimize / cleanup this mess
				org.jsoup.nodes.Element ancestor = (org.jsoup.nodes.Element) DomUtils
						.findCommonAncestor(siblings);

				org.jsoup.nodes.Element ele = createNewElement(
						entry.getValue(), tag);

				// Replace entries with highest child
				for (int i = 0; i < siblings.size(); i++) {

					Node n = DomUtils.findSubNode(ancestor, siblings.get(i));
					siblings.remove(i);
					siblings.add(i, n);

				}

				Node pos = siblings.get(0);
				pos.replaceWith(ele);

				for (Node node : siblings) {
					ele.appendChild(node);
				}

			}

		}
	}

}
