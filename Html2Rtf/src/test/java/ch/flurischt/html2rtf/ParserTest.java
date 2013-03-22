package ch.flurischt.html2rtf;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

import org.junit.Test;

import com.tutego.jrtf.Rtf;

public class ParserTest {

	private static final String SIMPLE = "mach mal e <b>test</b> fertig";
	private static final String COMPLEX = "<i>dies ist ein <b>fetter</b> test</i>";

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
