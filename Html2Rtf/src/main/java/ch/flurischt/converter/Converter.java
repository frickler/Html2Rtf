package ch.flurischt.converter;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.io.FileUtils;

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

	private static final String CONVERT_MODE_HTML2RTF = "h2r";
	private static final String CONVERT_MODE_RTF2HTML = "r2h";

	/**
	 * Converts a HTML-Tree to RTF
	 * 
	 * @param html
	 * 
	 * @return
	 * @throws IOException
	 */
	public static String toRtf(String html) throws IOException {
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

	public static void main(String[] args) {
		try {
			parse(args);
		} catch (ParseException exp) {
			// TODO ?
			System.err.println("Unexpected exception:" + exp.getMessage());
		} catch (IOException e) {
			System.err.println("Could not open/write File: " + e.getMessage());
		}
	}

	private static void parse(String[] args) throws ParseException, IOException {
		CommandLineParser parser = new GnuParser();
		Options options = new Options();
		options.addOption("i", "in", true, "path to input file");
		options.addOption("o", "out", true, "path to output file");
		options.addOption("m", "mode", true, "Mode: h2r (html to rtf) or r2h");

		CommandLine line = parser.parse(options, args);

		String inFile = null;
		String outFile = null;
		String mode = null;

		if (line.hasOption("in"))
			inFile = line.getOptionValue("in");
		if (line.hasOption("out"))
			outFile = line.getOptionValue("out");
		if (line.hasOption("mode"))
			mode = line.getOptionValue("mode");

		if ((!CONVERT_MODE_HTML2RTF.equals(mode) && !CONVERT_MODE_RTF2HTML
				.equals(mode)) || inFile == null || outFile == null) {
			printHelp(
					options,
					"parse a \"well\" formed html page into a rtf document and vice versa\n",
					"\n", System.out);
			return;
		}

		convert(mode, inFile, outFile);
	}

	private static void printHelp(final Options options, String header,
			final String footer, final OutputStream out) {
		final PrintWriter writer = new PrintWriter(out);
		final HelpFormatter helpFormatter = new HelpFormatter();
		helpFormatter.printHelp(writer, 80, "java -jar xyz.jar", header,
				options, 3, 5, footer, true);
		writer.close();
	}

	private static void convert(String mode, String inFilePath,
			String outFilePath) throws IOException {

		String input = FileUtils.readFileToString(new File(inFilePath));
		String out = null;

		if (CONVERT_MODE_HTML2RTF.equals(mode)) {
			out = Converter.toRtf(input);
		} else if (CONVERT_MODE_RTF2HTML.equals(mode)) {
			out = Converter.toHtml(input);
		}
		FileUtils.write(new File(outFilePath), out);
	}
}
