package eWorld.datatypes.elementars;

import java.io.Serializable;

import eWorld.datatypes.data.EvDataType;
import eWorld.datatypes.evs.EvAbstract;
import eWorld.datatypes.identifiers.EvIdentifier;
import eWorld.datatypes.identifiers.UserIdentifier;

@SuppressWarnings("serial")
public class EvString <EV extends EvAbstract, IDENT extends EvIdentifier> extends EvDataType<EV, IDENT> implements Serializable {

	// attributes

	private WoString woString;
	
	
	// constructors

	/** default constructor for remote procedure call (RPC) */
	public EvString() {
	}
	
	public EvString(EV ev, IDENT identifier, WoString woString, UserIdentifier author) {
		super(ev, identifier, author, null);	// TODO add authorTime to arguments
		
		assert null != woString;
		
		this.woString = woString;
	}

	
	// methods

	public WoString getWoString() {
		return woString;
	}
}
