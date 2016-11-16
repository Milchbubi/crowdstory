package eWorld.datatypes.identifiers;

import java.io.Serializable;

@SuppressWarnings("serial")
public class MediumIdentifier extends EntryClassIdentifier implements HasMediumShortIdentifier, Serializable {

	// attributes
	
	private MediumShortIdentifier mediumShortIdent;
	
	
	// constructors
	
	/** default constructor for remote procedure call (RPC) */
	public MediumIdentifier() {
	}
	
	public MediumIdentifier(final long entryId, final long mediumId) {
		super(entryId);
		
		this.mediumShortIdent = new MediumShortIdentifier(mediumId);
	}
	
	public MediumIdentifier(final EntryClassIdentifier entryIdent, final MediumShortIdentifier mediumShortIdent) {
		super(entryIdent.getEntryClassId());
		
		assert null != mediumShortIdent;
		
		this.mediumShortIdent = mediumShortIdent;
	}
	
	
	// methods
	
	public long getMediumId() {
		return mediumShortIdent.getMediumId();
	}

	@Override
	public MediumShortIdentifier getShortIdentifier() {
		return mediumShortIdent;
	}

}
