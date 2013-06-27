package ch.flurischt.rtf2html;

import java.awt.Color;

import javax.swing.text.AttributeSet;
import javax.swing.text.Element;

/**
 * 
 * @author chris
 * @todo Make some generic methods?
 *
 */
public class RtfUtils {

	/**
	 * Checks whether an Element has a boolean property
	 * 
	 * @todo Check if constant is of type boolean
	 * @param element
	 *            RTF-Element
	 * @param constant
	 *            Constant (e.g StyleConstants.Bold)
	 * @return true if it applies, false otherwise
	 * 
	 */
	public static Boolean getBooleanValue(Element element, Object constant) {
		AttributeSet attributes = element.getAttributes();

		return (attributes.getAttribute(constant) != null && ((Boolean) attributes
				.getAttribute(constant)).booleanValue());

	}

	/**
	 * Gets a value of an attribute (e.g. StyleConstants.FontFamily)
	 * 
	 * @param element
	 *            RTF-Element
	 * @param constant
	 *            Constant (e.g StyleConstants.Bold)
	 * 
	 * @return Value, null otherwise
	 */
	public static String getStringValue(Element element, Object constant) {
		AttributeSet attributes = element.getAttributes();

		Object obj = attributes.getAttribute(constant);
		if (obj == null)
			return null;

		return (String) obj;
	}
	
	/**
	 * Gets a integer value of an attribute (e.g. StyleConstants.FontFamily)
	 * 
	 * @param element
	 *            RTF-Element
	 * @param constant
	 *            Constant (e.g StyleConstants.Foreground)
	 * 
	 * @return Value, null otherwise
	 */
	public static Color getColorValue(Element element, Object constant) {
		AttributeSet attributes = element.getAttributes();

		Object obj = attributes.getAttribute(constant);
		if (obj == null)
			return null;

		return (Color) obj;
	}

	/**
	 * Gets a integer value of an attribute (e.g. StyleConstants.FontFamily)
	 * 
	 * @param element
	 *            RTF-Element
	 * @param constant
	 *            Constant (e.g StyleConstants.Foreground)
	 * 
	 * @return Value, null otherwise
	 */
	public static Integer getIntegerValue(Element element, Object constant) {
		AttributeSet attributes = element.getAttributes();

		Object obj = attributes.getAttribute(constant);
		if (obj == null)
			return null;

		return (Integer) obj;
	}
}
