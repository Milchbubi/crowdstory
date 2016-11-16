package eWorld.frontEnd.gwt.client.util;

import eWorld.datatypes.identifiers.AbstractIdentifier;

public interface Identifiable<IDENT extends AbstractIdentifier> {

	/**
	 * 
	 * @return an identifier that identifies the object
	 */
	public IDENT getIdentifier();
}
