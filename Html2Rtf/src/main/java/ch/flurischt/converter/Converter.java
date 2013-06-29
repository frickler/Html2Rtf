package ch.flurischt.converter;

import java.io.StringWriter;

import ch.flurischt.html2rtf.Html2RtfParser;
import ch.flurischt.rtf2html.Rtf2Html;

import com.tutego.jrtf.Rtf;

/**
 * Converts a html-fragment to rtf-text and vice versa
 * 
 * @author chris
 * 
 */
public class Converter {

	/**
	 * Converts a HTML-Tree to RTF
	 * 
	 * @param html
	 * 
	 * @return
	 */
	public static String toRtf(String html) {
		Rtf rtf = new Html2RtfParser().parse(html);
		StringWriter s = new StringWriter();
		rtf.out(s);
		return s.toString();
	}

	/**
	 * Converts a String in RTF-Format to HTML
	 * 
	 * @param rtf
	 * @return
	 */
	public static String toHtml(String rtf) {

		return new Rtf2Html().toHtml(rtf);
	}

}
