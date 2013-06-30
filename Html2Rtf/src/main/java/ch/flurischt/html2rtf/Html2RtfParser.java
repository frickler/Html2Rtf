package ch.flurischt.html2rtf;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;

import com.tutego.jrtf.Rtf;

public class Html2RtfParser {

	/**
	 * default to our config inside the jar
	 */
	public static final String DEFAULT_CONFIG_FILE = "/html2rtf_config.xml";

	private final Map<String, NodeHandler<ElementContainer, Object>> handlers = new HashMap<String, NodeHandler<ElementContainer, Object>>();

	public Html2RtfParser() throws IOException {
		InputStream configFile = getClass().getResourceAsStream(
				DEFAULT_CONFIG_FILE);
		setUp(configFile);
	}

	public Html2RtfParser(InputStream configFile) throws IOException {
		setUp(configFile);
	}

	/**
	 * parses the config xml file and creates the handlers map. in the given xml
	 * file for each tagname a handler-class can be specified. an instance of this class
	 * is put into the handlers map (as long as this class exists in the classpath)
	 * 
	 * @param handlersConfig
	 * @throws IOException
	 */
	private void setUp(InputStream configFile) throws IOException {
		Document doc = Jsoup.parse(configFile, null, "/");
		Elements tags = doc.select("tags > handler");
		for (Element handlerTag : tags) {
			String classname = null;
			String tagname = null;
			for (Element e : handlerTag.children()) {
				if ("class".equals(e.tagName().toLowerCase())) {
					classname = e.text();
				} else if ("tag".equals(e.tagName().toLowerCase())) {
					tagname = e.text();
				}
			}
			if (classname == null || tagname == null)
				throw new RuntimeException("wrong config file!");

			try {
				Class<?> cls = Class.forName(classname);
				@SuppressWarnings("unchecked")
				NodeHandler<ElementContainer, Object> handler = (NodeHandler<ElementContainer, Object>) cls
						.newInstance();
				handlers.put(tagname, handler);
			} catch (ClassNotFoundException e1) {
				throw new IOException("wrong config file!", e1);
			} catch (InstantiationException e1) {
				throw new IOException("wrong config file!", e1);
			} catch (IllegalAccessException e1) {
				throw new IOException("wrong config file!", e1);
			}

		}
	}

	public Rtf parse(String html) {
		// TODO simpleText() strips the <br> tag. is this a problem?
		html = Jsoup.clean(html, Whitelist.basic());
		// force the given source to be in the body tag
		Document doc = Jsoup.parseBodyFragment(html);

		return (Rtf) traverseTree(doc.body());
	}

	public void parse(FileReader htmlFile) {
		// parse(htmlFile.)
	}

	/**
	 * traverses the dom tree recursively.
	 * 
	 * @param curr
	 *            the starting element
	 * @return a jrtf rtf document
	 */
	private Object traverseTree(Node curr) {
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
	private Object getRtfTNode(Node node, ElementContainer childs) {
		final String name = node.nodeName().toLowerCase();

		Object ret = null;

		if (node instanceof TextNode) {
			ret = ((TextNode) node).text();
		} else if (node instanceof Element) {

			if (name.equals("p"))
				return childs;

			NodeHandler<ElementContainer, Object> handler = handlers.get(name);

			//TODO better use a NoOpHandler that ignores the tag and log it?
			if (handler == null)
				throw new RuntimeException("WTF? Don't know this tag: " + name);

			ret = handler.handle(childs);
		}

		return ret;
	}

}
