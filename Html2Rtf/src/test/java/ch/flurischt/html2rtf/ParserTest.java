package ch.flurischt.html2rtf;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

import org.junit.Test;

import com.tutego.jrtf.Rtf;
import static com.tutego.jrtf.Rtf.rtf;
import static com.tutego.jrtf.RtfDocfmt.*;
import static com.tutego.jrtf.RtfHeader.*;
import static com.tutego.jrtf.RtfInfo.*;
import static com.tutego.jrtf.RtfFields.*;
import static com.tutego.jrtf.RtfPara.*;
import static com.tutego.jrtf.RtfSectionFormatAndHeaderFooter.*;
import static com.tutego.jrtf.RtfText.*;
import static com.tutego.jrtf.RtfUnit.*;

public class ParserTest {

	private static final String SIMPLE = "mach mal e <b>test</b> fertig";
	private static final String COMPLEX = "<i>dies ist ein <b>fe<u>tt</u>er</b> test</i>";

	private static final String FILENAME = "/tmp/test.rtf";

	/**
	 * 
	 */
	@Test
	public void testSimpleHtml() {
		Rtf rtf = Parser.parse(SIMPLE);
		assertNotNull(rtf);
		toFile(rtf, FILENAME);
	}

	/**
	 * 
	 */
	@Test
	public void testComplexHtml() {
		Rtf r = Parser.parse(COMPLEX);
		assertNotNull(r);
		toFile(r, FILENAME);
	}

	@Test
	public void testJrtFSyntax() throws IOException{
		rtf().section(
				p(
						text("test"),
						italic("bla")
				), 
				p(
						text("neuer absatz")
				)
				).out(new FileWriter("/tmp/jrtf.rtf"));
	}
	
	private void toFile(Rtf rtf, String filename) {
		try {
			rtf.out(new FileWriter(filename));
		} catch (IOException e) {
			fail("No File written");
		}

		// make sure jrtf wrote this thing
		File f = new File(FILENAME);
		assertTrue(f.exists());
	}

}
