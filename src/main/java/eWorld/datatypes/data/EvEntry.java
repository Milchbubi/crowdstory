package eWorld.datatypes.data;

import java.io.Serializable;

import eWorld.datatypes.elementars.WoString;
import eWorld.datatypes.evs.Ev;
import eWorld.datatypes.evs.EvAbstract;
import eWorld.datatypes.evs.EvVoid;
import eWorld.datatypes.identifiers.EntryIdentifier;
import eWorld.datatypes.identifiers.EntryShortIdentifier;
import eWorld.datatypes.identifiers.EvIdentifier;
import eWorld.datatypes.identifiers.UserIdentifier;

@SuppressWarnings("serial")
public class EvEntry <EV extends EvAbstract, IDENT extends EvIdentifier> extends EvObject<EV, IDENT> implements Serializable {
	
	public static final int MAX_NAME_LENGTH = 64;
	public static final int MAX_DESCRIPTION_LENGTH = 300;
	
	/** true if the entry describes an Element (no further subclasses) otherwise false */
	private boolean isElement;
	
	/** default constructor for remote procedure call (RPC) */
	public EvEntry() {
	}
	
	/**
	 * 
	 * @param ev
	 * @param identifier
	 * @param name
	 * @param description
	 * @param isElement true if the entry describes an Element (no further subclasses) otherwise false
	 * @param authorId the id of the user who suggested the entry, can be null
	 */
	public EvEntry(EV ev, IDENT identifier, WoString name, WoString description, boolean isElement, UserIdentifier author) {
		super(ev, identifier, name, description, author);
		
		this.isElement = isElement;
	}
	
	public boolean isElement() {
		return isElement;
	}
	
	/**
	 * TODO FIXME shouldn't be static ?
	 * TODO move to superclass (and override here) ?
	 * @param entry
	 * @return
	 */
	public static <EV extends EvAbstract> EvEntry<EV, EntryShortIdentifier> shortCast(EvEntry<EV, EntryIdentifier> entry) {
		return new EvEntry<EV, EntryShortIdentifier>(
				entry.getEv(), 
				entry.getIdentifier().getShortIdentifier(), 
				entry.getName(), 
				entry.getDescription(), 
				entry.isElement,
				entry.getAuthor()
				);
	}
	
	/**
	 * TODO FIXME shouldn't be static ?
	 * TODO move to superclass (and override here) ?
	 * @param entry
	 * @return
	 */
	public static EvEntry<EvVoid, EntryShortIdentifier> voidCast(EvEntry<Ev, EntryShortIdentifier> entry) {
		return new EvEntry<EvVoid, EntryShortIdentifier>(
				new EvVoid(),
				entry.getIdentifier().getShortIdentifier(), 
				entry.getName(), 
				entry.getDescription(), 
				entry.isElement,
				entry.getAuthor()
				);
	}

	@Override
	public int getMaxNameLength() {
		return MAX_NAME_LENGTH;
	}

	@Override
	public int getMaxDescriptionLength() {
		return MAX_DESCRIPTION_LENGTH;
	}
	
}
