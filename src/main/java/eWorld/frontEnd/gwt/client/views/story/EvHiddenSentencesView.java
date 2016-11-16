package eWorld.frontEnd.gwt.client.views.story;

import java.util.ArrayList;

import com.google.gwt.user.client.ui.InlineLabel;

import eWorld.datatypes.data.EvSentence;
import eWorld.datatypes.evs.Ev;
import eWorld.datatypes.identifiers.SentenceShortIdentifier;

public class EvHiddenSentencesView extends InlineLabel {

	private EvInsertSentenceButton insertButtonBefore;
	
	private EvInsertSentenceButton insertButtonAfter;
	
	private ArrayList<EvSentence<Ev, SentenceShortIdentifier>> hiddenSentences = new ArrayList<EvSentence<Ev, SentenceShortIdentifier>>();
	
	
	public EvHiddenSentencesView() {
		super("[...] ");
	}
	
	
	/**
	 * call this method when all sentences are added
	 */
	public void updateLabel() {
		this.setText("[..." + "-" + hiddenSentences.size() + "] ");
	}
	
	public void setInsertButtonBefore(EvInsertSentenceButton insertButtonBefore) {
		this.insertButtonBefore = insertButtonBefore;
	}
	
	public EvInsertSentenceButton getInsertButtonBefore() {
		return insertButtonBefore;
	}
	
	public void setInsertButtonAfter(EvInsertSentenceButton insertButtonAfter) {
		this.insertButtonAfter = insertButtonAfter;
	}
	
	public EvInsertSentenceButton getInsertButtonAfter() {
		return insertButtonAfter;
	}
	
	public void addSentence(EvSentence<Ev, SentenceShortIdentifier> sentence) {
		this.hiddenSentences.add(sentence);
	}
	
	public ArrayList<EvSentence<Ev, SentenceShortIdentifier>> getHiddenSentences() {
		return hiddenSentences;
	}
	
}
