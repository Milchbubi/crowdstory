package eWorld.datatypes.identifiers;

import java.io.Serializable;

@SuppressWarnings("serial")
public class SentenceShortIdentifier extends EvShortIdentifier<Long> implements HasSentenceShortIdentifier, Serializable {

	// attributes
	
	private long sentenceId;
	
	
	// constructors
	
	/** default constructor for remote procedure call (RPC) */
	public SentenceShortIdentifier() {
	}
	
	public SentenceShortIdentifier(long sentenceId) {
		this.sentenceId = sentenceId;
	}
	
	
	// methods
	
	@Override
	public Long getShortId() {
		return sentenceId;
	}

	@Override
	public SentenceShortIdentifier getShortIdentifier() {
		return this;
	}

}
