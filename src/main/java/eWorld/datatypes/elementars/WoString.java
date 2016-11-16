package eWorld.datatypes.elementars;

import java.io.Serializable;

@SuppressWarnings("serial")
public class WoString implements Serializable {

	// attributes

	private String string;
	
	
	// constructors

	/** default constructor for remote procedure call (RPC) */
	public WoString() {
		
	}
	
	public WoString(String string) {
		this.string = string;
	}
	
	
	// methods

	public String getString() {
		return string;
	}
	
}
