package ch.flurischt.html2rtf;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static com.tutego.jrtf.RtfPara.p;

import com.tutego.jrtf.RtfPara;
import com.tutego.jrtf.RtfText;

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

	/**
	 * puts the whole content of this container into a paragraph
	 * @return
	 */
	public RtfPara asParagraph() {
		//TODO remove all Collections and switch to arrays
		List<RtfText> texts = asTextList();
		RtfText[] textArray = new RtfText[texts.size()];

		for(int i=0;i<textArray.length;i++)
			textArray[i] = texts.get(i);
		
		return p(textArray);
	}

}
