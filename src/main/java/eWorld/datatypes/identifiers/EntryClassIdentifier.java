package eWorld.datatypes.identifiers;

import java.io.Serializable;

// TODO rename to DirectoryIdentifier?
@SuppressWarnings("serial")
public class EntryClassIdentifier extends EvCompleteIdentifier implements Serializable {

	// attributes
	
	private long entryClassId;
	
	
	// constructors
	
	/** default constructor for remote procedure call (RPC) */
	public EntryClassIdentifier() {
		
	}
	
	public EntryClassIdentifier(final long entryClassId) {
		assert 0 < entryClassId;
		
		this.entryClassId = entryClassId;
	}
	
	
	// methods
	
	public long getEntryClassId() {
		return entryClassId;
	}
}
