package ch.flurischt.html2rtf;

import static org.junit.Assert.assertNotNull;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

import org.junit.Test;

import sun.org.mozilla.javascript.internal.ast.ForInLoop;

import ch.flurischt.converter.Converter;

public class ParserTest {

	private static final String RTF_SIMPLE = "{\\rtf1\\ansi\\ansicpg1252\\deff0\\deflang2055{\\fonttbl{\\f0\\fnil\\fcharset0 Arial;}}\\viewkind4\\uc1\\pard\\ul\\b\\fs22 Test B\\b0 o\\b ld Italic\\ulnone\\b0\\par}";
	private static final String RTF_COMPLEX = "{\\rtf1\\fbidis\\ansi\\ansicpg1252\\deff0\\deflang2055{\\fonttbl{\\f0\\fswiss\\fprq2\\fcharset0 Calibri;}{\\f1\\fnil\\fcharset0 Arial;}}  \\viewkind4\\uc1\\pard\\ltrpar\\sa160\\sl252\\slmult1\\f0\\fs22 Normal\\par  \\b Fett\\par  \\ul\\b0 Unterstrichen\\par  \\ulnone\\i Kursiv\\par  \\ul\\b\\i0 Fett/Unterstrichen\\par  \\b0 Ein \\i langer\\i0  Unterstrichener, nicht \\b Fe\\b0 tt\\b er\\b0  text\\par  \\pard\\ltrpar\\ulnone\\b\\f1\\par  }";

	private static final String HTML_SIMPLE = "mach mal e <b>test</b> fertig";
	private static final String HTML_COMPLEX = "<i>dies ist ein <b>fe<u>tt</u>er</b><br/> test</i>";
	private static final String HTML_ATTRIBUTES = "<html><body><div align=\"left\"><font color=\"#000000\"> <b><u><font size=\"2\">Test Bold Italic</font></u></b><br /> <br /> <br /> </font></div> </body></html>";

	private static final String FOLDER = "C:\\Users\\chris\\Desktop\\";

	/**
	 * 
	 */
	@Test
	public void testCompexRtf() throws Exception {

		String rtf = RTF_COMPLEX;

		for (int i = 0; i < 10; i++) {

			String html = Converter.toHtml(rtf);

			System.out.println(html);

			assertNotNull(html);

			toFile(html, "complex.html");

			rtf = Converter.toRtf(html);

			toFile(rtf, "complex.rtf");

			assertNotNull(rtf);
		}

	}

	private void toFile(String text, String filename) {
		PrintStream out = null;
		try {
			out = new PrintStream(new FileOutputStream(FOLDER + filename));
			out.print(text);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (out != null)
				out.close();
		}

	}

}
