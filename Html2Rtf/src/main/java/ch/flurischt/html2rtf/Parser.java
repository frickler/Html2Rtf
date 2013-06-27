package ch.flurischt.html2rtf;

import static com.tutego.jrtf.Rtf.rtf;
import static com.tutego.jrtf.RtfText.bold;
import static com.tutego.jrtf.RtfText.italic;
import static com.tutego.jrtf.RtfText.lineBreak;
import static com.tutego.jrtf.RtfText.underline;

import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.safety.Whitelist;

import com.tutego.jrtf.Rtf;
import com.tutego.jrtf.RtfText;


public class Parser {

	private static final Map<String,NodeHandler<ElementContainer, Object>> handlers = new HashMap<String, NodeHandler<ElementContainer, Object> >();
	
	// setup the handlers
	static{
		
		handlers.put("body", new NodeHandler<ElementContainer, Object>() {
			
			public Object handle(ElementContainer input) {
				return rtf().section(input.asParagraph());
			}
			
		});
	
		//TODO same code in three methods. not cool bro!
		handlers.put("i", new NodeHandler<ElementContainer, Object>() {
			
			public Object handle(ElementContainer input) {
				ElementContainer container = new ElementContainer();
				for(RtfText t : input.asTextList())
					container.add(italic(t));
				return container;
			}
		});
		
		handlers.put("b", new NodeHandler<ElementContainer, Object>() {
			
			public Object handle(ElementContainer input) {
				ElementContainer container = new ElementContainer();
				for(RtfText t : input.asTextList())
					container.add(bold(t));
				return container;
			}
		});
		
		handlers.put("u", new NodeHandler<ElementContainer, Object>() {
			
			public Object handle(ElementContainer input) {
				ElementContainer container = new ElementContainer();
				for(RtfText t : input.asTextList())
					container.add(underline(t));
				return container;
			}
		});
		
		//TODO does not work with the current Jsoup Whitelist
		handlers.put("br", new NodeHandler<ElementContainer, Object>() {
			
			public Object handle(ElementContainer input) {
				return lineBreak();
			}
		});
		
	}
	
	public static Rtf parse(String html) {
		//TODO simpleText() strips the <br> tag. is this a problem?
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
		ElementContainer childs = new ElementContainer();

		for (int i = 0; i < numOfChilds; i++)
			childs.add(traverseTree(curr.childNode(i)));

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
	private static Object getRtfTNode(Node node, ElementContainer childs) {
		final String name = node.nodeName().toLowerCase();

		Object ret = null;

		if(node instanceof TextNode) {
			ret = ((TextNode) node).text();
		} else if(node instanceof Element) {
			NodeHandler<ElementContainer, Object> handler = handlers.get(name);
			
			if(handler==null)
				throw new RuntimeException("WTF? Don't know this tag"); //TODO
			
			ret = handler.handle(childs);
		}

		return ret;
	}

}
