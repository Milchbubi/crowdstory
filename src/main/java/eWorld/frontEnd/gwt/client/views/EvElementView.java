package eWorld.frontEnd.gwt.client.views;

import eWorld.datatypes.identifiers.EntryClassIdentifier;
import eWorld.datatypes.identifiers.EntryIdentifier;

public class EvElementView extends EvOrdinaryElementView {

	public EvElementView(EntryIdentifier identifier) {
		super(identifier, false);
	}
	
	@Override
	public EntryClassIdentifier getCurrentSuperClassIdent() {
		return new EntryClassIdentifier(getIdentifier().getEntryClassId());
	}

}
