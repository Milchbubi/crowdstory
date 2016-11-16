package eWorld.datatypes.identifiers;

import java.io.Serializable;

@SuppressWarnings("serial")
public class EntryShortIdentifier extends EvShortIdentifier<Long> implements HasEntryShortIdentifier, Serializable {

	// attributes
	
	private long entryId;
	
	
	// constructors
	
	/** default constructor for remote procedure call (RPC) */
	public EntryShortIdentifier() {
		
	}
	
	public EntryShortIdentifier(long entryId) {
		assert 0 < entryId;
		
		this.entryId = entryId;
	}
	
	
	// methods

	public long getEntryId() {
		return entryId;
	}

	@Override
	public Long getShortId() {
		return entryId;
	}

	@Override
	public EntryShortIdentifier getShortIdentifier() {
		return this;
	}
}
