package ch.flurischt.html2rtf.handler;

import com.tutego.jrtf.RtfText;

import ch.flurischt.html2rtf.ElementContainer;
import ch.flurischt.html2rtf.NodeHandler;

public abstract class TextWrapper implements
		NodeHandler<ElementContainer, Object> {
	
	public Object handle(ElementContainer input) {
		ElementContainer container = new ElementContainer();
		for (RtfText t : input.asTextList())
			container.add(wrapText(t));
		return container;
	}

	protected abstract RtfText wrapText(Object textToWrap);
}
