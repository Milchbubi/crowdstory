package eWorld.frontEnd.gwt.client.util;

import eWorld.datatypes.identifiers.AbstractIdentifier;

/**
 * A Button that can be identified through an identifier,
 * this can be useful for event-handling and makes it possible 
 * to handle many different events without the need of many anonym classes
 * @author michael
 *
 * @param <IDENT> identifier that can be used to identify a Button when it becomes clicked
 */
public class IdButton<IDENT extends AbstractIdentifier> extends EvButton implements Identifiable<IDENT> {

	private IDENT identifier;
	
	
	public IdButton(IDENT identifier) {
		assert null != identifier;
		
		this.identifier = identifier;
	}
	
	
	@Override
	public IDENT getIdentifier() {
		return identifier;
	}
}
