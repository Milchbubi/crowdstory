package eWorld.datatypes.packages;

import java.io.Serializable;

import eWorld.datatypes.data.EvEntry;
import eWorld.datatypes.elementars.WoString;
import eWorld.datatypes.evs.Ev;
import eWorld.datatypes.identifiers.EntryClassIdentifier;
import eWorld.datatypes.identifiers.EntryIdentifier;

@SuppressWarnings("serial")
public abstract class EvPackage implements Serializable {

	// attributes

	EvEntry<Ev, EntryIdentifier> header;
	
	
	// constructors

	/** default constructor for remote procedure call (RPC) */
	public EvPackage() {
		
	}
	
	public EvPackage(EvEntry<Ev, EntryIdentifier> header) {
		
		assert null != header;
		
		this.header = header;
	}
	
	
	// methods

	public EvEntry<Ev, EntryIdentifier> getHeader() {
		return header;
	}
}
