package ch.flurischt.html2rtf;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static com.tutego.jrtf.RtfPara.p;

import com.tutego.jrtf.RtfPara;
import com.tutego.jrtf.RtfTemplate;
import com.tutego.jrtf.RtfText;
import com.tutego.jrtf.RtfTextPara;

import static com.tutego.jrtf.RtfText.text;

public class ElementContainer {

	private List<Object> content = new LinkedList<Object>();

	public void add(Object o) {
		if (o instanceof ElementContainer)
			content.addAll(((ElementContainer) o).content);
		else if (o != null)
			content.add(o);
	}

	public List<RtfText> asTextList() {
		List<RtfText> list = new ArrayList<RtfText>();
		for (Object o : content) {
			if (o instanceof RtfText)
				list.add((RtfText) o);
			else if (o instanceof String) {
				list.add(text((String) o));
			} else
				throw new RuntimeException("WTF?");
		}
		return list;
	}

	public List<RtfPara> asParagraph() {
		List<RtfPara> paras = new ArrayList<RtfPara>();
		// TODO this makes a paragraph out of every text. stupid...
		for (Object o : content) {
			paras.add(p(o));
		}

		return paras;
	}

}
