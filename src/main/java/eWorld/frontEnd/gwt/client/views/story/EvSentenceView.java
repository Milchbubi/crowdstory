package eWorld.frontEnd.gwt.client.views.story;

import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.InlineLabel;

public class EvSentenceView extends InlineHTML implements HasEvSentenceHandler {
	
	private EvSentenceHandler sentenceHandler;
	
	
	public EvSentenceView(String sentence, EvSentenceHandler sentenceHandler) {
		super(formatToSafeHtml(sentence));
		
		this.sentenceHandler = sentenceHandler;
	}
	
	
	@Override
	public EvSentenceHandler getSentenceHandler() {
		return sentenceHandler;
	}
	
	private static String formatToSafeHtml(String sentence) {
		String safeHtml = SafeHtmlUtils.htmlEscape(sentence);
		safeHtml = safeHtml.replaceAll("\n", "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>");
		return safeHtml;
	}
	
}
