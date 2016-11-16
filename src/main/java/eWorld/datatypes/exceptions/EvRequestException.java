package eWorld.datatypes.exceptions;

import java.io.Serializable;

@SuppressWarnings("serial")
public class EvRequestException extends Exception implements Serializable {

	/** default constructor for remote procedure call (RPC) */
	public EvRequestException() {
	}
	
	public EvRequestException(String m) {
		super(m);
	}
}
