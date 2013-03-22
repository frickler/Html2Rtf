package ch.flurischt.html2rtf;

import static com.tutego.jrtf.Rtf.rtf;
import static com.tutego.jrtf.RtfPara.p;
import static com.tutego.jrtf.RtfText.bold;

import java.io.FileReader;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;

import com.tutego.jrtf.Rtf;
import com.tutego.jrtf.RtfPara;

public class Parser {

	public static Rtf parse(String html) {
		html = Jsoup.clean(html, Whitelist.simpleText());
		// force the given source to be in the body tag
		Document doc = Jsoup.parseBodyFragment(html);

		return (Rtf) traverseTree(doc.body());
	}

	public static void parse(FileReader htmlFile) {
		// parse(htmlFile.)
	}

	/**
	 * traverses the dom tree recursively.
	 * 
	 * @param curr
	 *            the starting element
	 * @return a jrtf rtf document
	 */
	private static Object traverseTree(Element curr) {
		final int numOfChilds = curr.children().size();
		RtfPara childs[] = new RtfPara[numOfChilds];

		for (int i = 0; i < numOfChilds; i++)
			childs[i] = (RtfPara) traverseTree(curr.child(i));

		return getRtfTNode(curr, childs);
	}

	/**
	 * maps the given elemen e to its rtf type. when the rtf element has
	 * subelement they are given in the childs array
	 * 
	 * @param e
	 *            the current html node for which a rtf element should be
	 *            created
	 * @param childs
	 *            the rtf child elements if any
	 * @return an rtf child element
	 */
	private static Object getRtfTNode(Element e, RtfPara childs[]) {
		final String name = e.nodeName().toLowerCase();

		Object ret = null;

		System.out.println("===============");
		System.out.println(e.nodeName());
		System.out.println(e.val());
		System.out.println(e.ownText());
		System.out.println(e.toString());
		System.out.println("===============");

		// TODO
		if ("body".equals(name)) {
			ret = rtf().section(childs);
		} else if ("i".equals(name)) {

		} else if ("b".equals(name)) {
			ret = p(bold(e.ownText()));
		}

		return ret;
	}

}
