package eWorld.datatypes.containers;

import java.io.Serializable;
import java.util.ArrayList;

import eWorld.datatypes.data.EvSentence;
import eWorld.datatypes.evs.Ev;
import eWorld.datatypes.identifiers.EntryClassIdentifier;
import eWorld.datatypes.identifiers.SentenceShortIdentifier;
import eWorld.frontEnd.gwt.client.EvApp;

@SuppressWarnings("serial")
public class EvStory extends EvDataTypeContainer<
		EntryClassIdentifier,
		SentenceShortIdentifier,
		Long,
		EvSentence<Ev, SentenceShortIdentifier>
	> implements Serializable {

	// attributes
	
	
	// constructors
	
	/** default constructor for remote procedure call (RPC) */
	public EvStory() {
	}
	
	public EvStory(EntryClassIdentifier containerIdentifier, ArrayList<EvSentence<Ev, SentenceShortIdentifier>> data) {
		super(containerIdentifier, data);
	}
	
	
	// methods
	
	public void add(EvSentence<Ev, SentenceShortIdentifier> sentence) {
		for (int i = 0; i < getData().size(); i++) {
			if (sentence.getPosition() < getData().get(i).getPosition()) {
				getData().add(i, sentence);
				return;
			}
		}
		getData().add(sentence);
	}
	
	public void addAll(ArrayList<EvSentence<Ev, SentenceShortIdentifier>> sentences) {
		for (EvSentence<Ev, SentenceShortIdentifier> sentence : sentences) {
			add(sentence);
		}
	}
	
}
