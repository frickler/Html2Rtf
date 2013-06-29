package ch.flurischt.html2rtf.handler;

import static com.tutego.jrtf.RtfText.italic;

import com.tutego.jrtf.RtfText;

public class ItalicTag extends TextWrapper {

	@Override
	protected RtfText wrapText(Object textToWrap) {
		return italic(textToWrap);
	}
}
