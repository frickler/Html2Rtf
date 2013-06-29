package ch.flurischt.html2rtf.handler;

import static com.tutego.jrtf.RtfText.bold;

import com.tutego.jrtf.RtfText;

public class BoldTag extends TextWrapper {
	@Override
	protected RtfText wrapText(Object textToWrap) {
		return bold(textToWrap);
	}
}
