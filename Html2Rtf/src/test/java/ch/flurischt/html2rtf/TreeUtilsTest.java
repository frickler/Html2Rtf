package ch.flurischt.html2rtf;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.junit.Test;

import ch.flurischt.rtf2html.DomUtils;

public class TreeUtilsTest {

	@Test
	public void testPath() {
		org.jsoup.nodes.Document doc = Jsoup.parse("<html> " + "<head></head> "
				+ "<body> " + " <div> " + "  <b><u><i>test</i></u></b> "
				+ " </div> " + "</body> " + "</html>");

		Element iTag = doc.select("i").first();

		List<Node> l = DomUtils.path(iTag);

		assertEquals("html", l.get(1).nodeName());
		assertEquals("body", l.get(2).nodeName());
		assertEquals("div", l.get(3).nodeName());
		assertEquals("b", l.get(4).nodeName());
		assertEquals("u", l.get(5).nodeName());
		assertEquals("i", l.get(6).nodeName());

	}

	@Test
	public void testCommonAncestor() {
		org.jsoup.nodes.Document doc = Jsoup.parse("<html> " + "<head></head> "
				+ "<body> " + " <div> "
				+ "  <b><u><i>test1</i></u><i>test2</i></b> " + " </div> "
				+ "</body> " + "</html>");

		Element iTag1 = doc.select("i").first();
		Element iTag2 = doc.select("i").last();

		Node ancestor = DomUtils.findCommonAncestor(iTag1, iTag2);

		assertEquals("b", ancestor.nodeName());

	}

	@Test
	public void testCommonAncestorList() {
		org.jsoup.nodes.Document doc = Jsoup.parse("<html> " + "<head></head> "
				+ "<body> " + " <div> "
				+ "  <b><div><u><i>test1</i></u><i>test2</i></div></b> "
				+ " </div> " + "</body> " + "</html>");

		List<Node> nodes = new ArrayList<Node>();
		nodes.add(doc.select("i").first());
		nodes.add(doc.select("i").last());
		nodes.add(doc.select("u").first());

		Node ancestor = DomUtils.findCommonAncestor(nodes);

		assertEquals("div", ancestor.nodeName());

	}

	@Test
	public void testSubParent() {
		org.jsoup.nodes.Document doc = Jsoup.parse("<html> " + "<head></head> "
				+ "<body> " + " <div> "
				+ "  <b><div><u><i>test1</i></u><i>test2</i></div></b> "
				+ " </div><i>end</i> " + "</body> " + "</html>");

		Node ancestor = DomUtils.findSubNode(doc.select("div").first(), doc
				.select("i").first());

		assertEquals("b", ancestor.nodeName());

	}

	@Test
	public void testCommonAncestorListBody() {
		org.jsoup.nodes.Document doc = Jsoup.parse("<html> " + "<head></head> "
				+ "<body> " + " <div> "
				+ "  <b><div><u><i>test1</i></u><i>test2</i></div></b> "
				+ " </div><i>end</i> " + "</body> " + "</html>");

		List<Node> nodes = new ArrayList<Node>();
		nodes.add(doc.select("i").first());
		nodes.add(doc.select("i").last());
		nodes.add(doc.select("u").first());

		Node ancestor = DomUtils.findCommonAncestor(nodes);

		assertEquals("body", ancestor.nodeName());

	}

}
