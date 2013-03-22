package ch.flurischt.html2rtf;

import static com.tutego.jrtf.Rtf.rtf;
import static com.tutego.jrtf.RtfPara.p;
import static com.tutego.jrtf.RtfText.bold;
import static com.tutego.jrtf.RtfText.italic;
import static com.tutego.jrtf.RtfText.text;

import java.io.FileReader;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
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
	private static Object traverseTree(Node curr) {
		final int numOfChilds = curr.childNodeSize();
		RtfPara childs[] = new RtfPara[numOfChilds];

		for (int i = 0; i < numOfChilds; i++)
			childs[i] = (RtfPara) traverseTree(curr.childNode(i));

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
	private static Object getRtfTNode(Node node, RtfPara childs[]) {
		final String name = node.nodeName().toLowerCase();

		Object ret = null;

		System.out.println("===============");
		/*System.out.println(e.nodeName());
		System.out.println(e.ownText());
		System.out.println(e.toString());
		System.out.println("Mixed: " + e.is);*/
		System.out.println("===============");

		if(node instanceof TextNode) {
			ret = p(text(((TextNode) node).text()));
		} else if(node instanceof Element) {
			
			Element e = (Element) node;
			
			if ("body".equals(name)) {
				ret = rtf().section(childs);
			} else if ("i".equals(name)) {
				ret = p(italic(childs));
			} else if ("b".equals(name)) {
				ret = p(bold(e.ownText()));
			}	
		}

		return ret;
	}

}
