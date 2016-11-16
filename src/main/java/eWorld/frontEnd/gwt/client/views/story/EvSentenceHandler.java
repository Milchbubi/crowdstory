package eWorld.frontEnd.gwt.client.views.story;

import com.google.gwt.user.client.ui.InlineHTML;

import eWorld.datatypes.data.EvSentence;
import eWorld.datatypes.evs.Ev;
import eWorld.datatypes.identifiers.SentenceShortIdentifier;

public class EvSentenceHandler {

	// attributes
	
	private EvSentence<Ev, SentenceShortIdentifier> sentence;
	
	private EvInsertSentenceButton insertButtonBefore;
	
	private EvInsertSentenceButton insertButtonAfter;
	
	private EvSentenceStatView sentenceStatView;
	
	/** "display: inline-block" is used to set margin, but trims normal space-character and has no line wrapping */
	private InlineHTML sentenceHeader;
	
	/** "display: inline" is used for correct line wrapping, but margin can't be set*/
	private EvSentenceView sentenceView;
	
	
	// constructors
	
	/**
	 * use init(..) to initialize
	 */
	public EvSentenceHandler(EvSentence<Ev, SentenceShortIdentifier> sentence) {
		assert null != sentence;
		
		this.sentence = sentence;
	}
	
	public void init(
			EvInsertSentenceButton insertButtonBefore,
			EvInsertSentenceButton insertButtonAfter,
			EvSentenceStatView sentenceStatView,
			InlineHTML sentenceHeader,
			EvSentenceView sentenceView) {
		assert null != insertButtonBefore;
		assert null != insertButtonAfter;
		assert null != sentenceStatView;
		assert null != sentenceHeader;
		assert null != sentenceView;
		
		this.insertButtonBefore = insertButtonBefore;
		this.insertButtonAfter = insertButtonAfter;
		this.sentenceStatView = sentenceStatView;
		this.sentenceHeader = sentenceHeader;
		this.sentenceView = sentenceView;
	}
	
	
	// methods
	
	public EvSentence<Ev, SentenceShortIdentifier> getSentence() {
		return sentence;
	}
	
	public EvInsertSentenceButton getInsertButtonBefore() {
		return insertButtonBefore;
	}
	
	public EvInsertSentenceButton getInsertButtonAfter() {
		return insertButtonAfter;
	}
	
	public EvSentenceStatView getSentenceStatView() {
		return sentenceStatView;
	}
	
	public InlineHTML getSentenceHeader() {
		return sentenceHeader;
	}
	
	public EvSentenceView getSentenceView() {
		return sentenceView;
	}
	
}
