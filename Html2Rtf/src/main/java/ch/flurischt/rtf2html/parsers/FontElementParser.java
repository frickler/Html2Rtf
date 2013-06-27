package ch.flurischt.rtf2html.parsers;

import java.awt.Color;

import javax.swing.text.Element;
import javax.swing.text.StyleConstants;

import org.jsoup.parser.Tag;

import ch.flurischt.rtf2html.RtfUtils;

public class FontElementParser extends RtfElementParser {

	public FontElementParser(String tagName) {
		super(tagName);
	}

	@Override
	protected Boolean checkEntry(Element element) {
		// Check if font element exists
		return (RtfUtils.getIntegerValue(element, StyleConstants.FontSize) != null)
				|| (RtfUtils
						.getIntegerValue(element, StyleConstants.FontFamily) != null)
				|| (RtfUtils.getColorValue(element, StyleConstants.Foreground) != null);
	}

	@Override
	protected org.jsoup.nodes.Element createNewElement(Element rtfElement,
			Tag tag) {
		org.jsoup.nodes.Element e = new org.jsoup.nodes.Element(tag, "");

		addStringAttribute(rtfElement, e, "face", StyleConstants.FontFamily);
		addIntegerAttribute(rtfElement, e, "size", StyleConstants.FontSize);
		addColorAttribute(rtfElement, e, "color", StyleConstants.Foreground);

		return e;
	}

	private void addIntegerAttribute(Element rtfElement,
			org.jsoup.nodes.Element e, String attributeName, Object constant) {
		Integer style = RtfUtils.getIntegerValue(rtfElement, constant);
		if (style != null) {
			e.attr(attributeName, String.valueOf(style));
		}
	}

	private void addStringAttribute(Element rtfElement,
			org.jsoup.nodes.Element e, String attributeName, Object constant) {
		String style = RtfUtils.getStringValue(rtfElement, constant);
		if (style != null) {
			e.attr(attributeName, style);
		}
	}

	private void addColorAttribute(Element rtfElement,
			org.jsoup.nodes.Element e, String attributeName, Object constant) {
		Color color = RtfUtils.getColorValue(rtfElement, constant);
		if (color != null) {
			e.attr(attributeName, "#" + Integer.toHexString(color.getRGB()));
		}
	}
}
