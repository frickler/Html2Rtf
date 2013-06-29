package ch.flurischt.html2rtf.handler;

import static com.tutego.jrtf.RtfText.lineBreak;
import ch.flurischt.html2rtf.ElementContainer;
import ch.flurischt.html2rtf.NodeHandler;

public class BrTag implements NodeHandler<ElementContainer, Object> {
	public Object handle(ElementContainer input) {
		return lineBreak();
	}
}
