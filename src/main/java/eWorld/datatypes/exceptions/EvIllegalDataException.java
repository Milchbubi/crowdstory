package eWorld.datatypes.exceptions;

import java.io.Serializable;

@SuppressWarnings("serial")
public class EvIllegalDataException extends Exception implements Serializable {

	/** default constructor for remote procedure call (RPC) */
	public EvIllegalDataException() {
	}
	
	public EvIllegalDataException(String m) {
		super(m);
	}
}
