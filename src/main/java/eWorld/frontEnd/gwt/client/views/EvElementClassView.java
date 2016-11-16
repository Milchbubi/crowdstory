package eWorld.frontEnd.gwt.client.views;

import eWorld.datatypes.identifiers.EntryClassIdentifier;
import eWorld.datatypes.identifiers.EntryIdentifier;

public class EvElementClassView extends EvOrdinaryElementView {

	public EvElementClassView(EntryIdentifier identifier) {
		super(identifier, true);
	}
	
	@Override
	public EntryClassIdentifier getCurrentSuperClassIdent() {
		return new EntryClassIdentifier(getIdentifier().getEntryId());
	}

}
