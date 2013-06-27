package ch.flurischt.rtf2html.parsers;

import javax.swing.text.Element;

import org.jsoup.parser.Tag;

import ch.flurischt.rtf2html.RtfUtils;

public class BooleanElementParser extends RtfElementParser {

	private Object constant;

	public BooleanElementParser(String tagName, Object constant) {
		super(tagName);
		this.constant = constant;
	}

	@Override
	protected Boolean checkEntry(Element element) {

		return RtfUtils.getBooleanValue(element, constant);
	}

	@Override
	protected org.jsoup.nodes.Element createNewElement(Element rtfElement,
			Tag tag) {
		return new org.jsoup.nodes.Element(tag, "");
	}

}
