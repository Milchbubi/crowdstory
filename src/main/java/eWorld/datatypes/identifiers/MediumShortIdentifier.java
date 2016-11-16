package eWorld.datatypes.identifiers;

import java.io.Serializable;

@SuppressWarnings("serial")
public class MediumShortIdentifier extends EvShortIdentifier<Long> implements HasMediumShortIdentifier, Serializable {

	// attributes
	
	private long mediumId;
	
	
	// constructors
	
	/** default constructor for remote procedure call (RPC) */
	public MediumShortIdentifier() {
	}
	
	public MediumShortIdentifier(long mediumId) {
		assert 0 < mediumId;
		
		this.mediumId = mediumId;
	}
	
	
	// methods

	public long getMediumId() {
		return mediumId;
	}
	
	@Override
	public Long getShortId() {
		return mediumId;
	}

	@Override
	public MediumShortIdentifier getShortIdentifier() {
		return this;
	}

}
