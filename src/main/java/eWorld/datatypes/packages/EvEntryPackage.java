package eWorld.datatypes.packages;

import java.io.Serializable;

import eWorld.datatypes.containers.EvEntryContainer;
import eWorld.datatypes.data.EvEntry;
import eWorld.datatypes.evs.Ev;
import eWorld.datatypes.identifiers.EntryIdentifier;

@SuppressWarnings("serial")
public class EvEntryPackage extends EvPackage implements Serializable {

	// attributes
	
	private EvEntryContainer eEntryContainer;
	
	
	// constructors

	/** default constructor for remote procedure call (RPC) */
	public EvEntryPackage() {
		
	}
	
	public EvEntryPackage(EvEntry<Ev, EntryIdentifier> header,
			EvEntryContainer eEntryContainer) {
		super(header);
		
		assert null != eEntryContainer;
		
		this.eEntryContainer = eEntryContainer;
	}
	
	
	// methods
	
	public EvEntryContainer getEntryContainer() {
		return eEntryContainer;
	}
	
}
