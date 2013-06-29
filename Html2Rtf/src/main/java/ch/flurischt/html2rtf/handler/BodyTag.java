package ch.flurischt.html2rtf.handler;

import static com.tutego.jrtf.Rtf.rtf;
import ch.flurischt.html2rtf.ElementContainer;
import ch.flurischt.html2rtf.NodeHandler;

public class BodyTag implements NodeHandler<ElementContainer, Object> {
	
	public Object handle(ElementContainer input) {
		return rtf().section(input.asParagraph());
	}
	
}
