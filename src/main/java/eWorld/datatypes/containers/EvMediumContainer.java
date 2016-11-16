package eWorld.datatypes.containers;

import java.io.Serializable;
import java.util.ArrayList;

import eWorld.datatypes.data.EvMedium;
import eWorld.datatypes.evs.Ev;
import eWorld.datatypes.identifiers.EntryClassIdentifier;
import eWorld.datatypes.identifiers.MediumShortIdentifier;

@SuppressWarnings("serial")
public class EvMediumContainer extends EvDataTypeContainer<
		EntryClassIdentifier,
		MediumShortIdentifier,
		Long,
		EvMedium<Ev, MediumShortIdentifier>
	> implements Serializable  {
	
	// attributes
	
	
	// constructors
	
	/** default constructor for remote procedure call (RPC) */
	public EvMediumContainer() {
	}
	
	public EvMediumContainer(
			EntryClassIdentifier containerIdentifier, 
			ArrayList<EvMedium<Ev, MediumShortIdentifier>> data) {
		super(containerIdentifier, data);
	}
	
	
	// methods

}
