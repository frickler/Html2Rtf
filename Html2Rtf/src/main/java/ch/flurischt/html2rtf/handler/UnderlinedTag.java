package ch.flurischt.html2rtf.handler;

import static com.tutego.jrtf.RtfText.underline;

import com.tutego.jrtf.RtfText;

public class UnderlinedTag extends TextWrapper {
	@Override
	protected RtfText wrapText(Object textToWrap) {
		return underline(textToWrap);
	}
}
