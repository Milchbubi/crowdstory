package eWorld.datatypes.data;

import java.io.Serializable;

import eWorld.datatypes.identifiers.EvIdentifier;

@SuppressWarnings("serial")
public class IdDataType <IDENT extends EvIdentifier> implements Serializable {

	// attributes
	
	private IDENT identifier;
	
	
	// constructors

	/** default constructor for remote procedure call (RPC) */
	public IdDataType() {
	}
	
	public IdDataType(IDENT identifier) {
		assert null != identifier;
		
		this.identifier = identifier;
	}
	
	
	// methods
	
	public IDENT getIdentifier() {
		return identifier;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (null != obj && obj instanceof IdDataType) {
			return identifier.equals(((IdDataType)obj).identifier);	// TODO FIXME? parameterize and surround with try-catch-block?
		} else {
			return false;
		}
	}
}
