package eWorld.datatypes.identifiers;

import java.io.Serializable;

@SuppressWarnings("serial")
public class EntryIdentifier extends EntryClassIdentifier implements HasEntryShortIdentifier, Serializable {

	// attributes
	
	private EntryShortIdentifier entryShortIdentifier;
	
	
	// constructors
	
	/** default constructor for remote procedure call (RPC) */
	public EntryIdentifier() {
	}
	
	public EntryIdentifier(final long entryClassId, final long entryId) {
		super(entryClassId);
		
		this.entryShortIdentifier = new EntryShortIdentifier(entryId);
	}
	
	public EntryIdentifier(final EntryClassIdentifier entryClassIdent, final EntryShortIdentifier entryShortIdent) {
		super(entryClassIdent.getEntryClassId());
		
		assert null != entryShortIdent;
		
		this.entryShortIdentifier = entryShortIdent;
	}
	
	
	// methods
	
	public long getEntryId() {
		return entryShortIdentifier.getEntryId();
	}

	@Override
	public EntryShortIdentifier getShortIdentifier() {
		return entryShortIdentifier;
	}
}
