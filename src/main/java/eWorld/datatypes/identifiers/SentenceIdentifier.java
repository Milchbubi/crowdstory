package eWorld.datatypes.identifiers;

import java.io.Serializable;

@SuppressWarnings("serial")
public class SentenceIdentifier extends EntryClassIdentifier implements HasSentenceShortIdentifier, Serializable {

	// attributes
	
	private SentenceShortIdentifier shortIdentifier;
	
	
	// constructors
	
	/** default constructor for remote procedure call (RPC) */
	public SentenceIdentifier() {
	}
	
	public SentenceIdentifier(long entryClassId, long sentenceId) {
		super(entryClassId);
		
		shortIdentifier = new SentenceShortIdentifier(sentenceId);
	}
	
	public SentenceIdentifier(EntryClassIdentifier entryClassIdent, SentenceShortIdentifier sentenceShortIdent) {
		super(entryClassIdent.getEntryClassId());
		
		assert null != sentenceShortIdent;
		
		this.shortIdentifier = sentenceShortIdent;
	}
	
	
	// methods
	
	public long getSentenceId() {
		return shortIdentifier.getShortId();
	}
	
	@Override
	public SentenceShortIdentifier getShortIdentifier() {
		return shortIdentifier;
	}
	
}
