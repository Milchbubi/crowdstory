package eWorld.datatypes.data;

import java.io.Serializable;

import eWorld.datatypes.elementars.WoString;
import eWorld.datatypes.evs.EvAbstract;
import eWorld.datatypes.exceptions.EvIllegalDataException;
import eWorld.datatypes.identifiers.EvIdentifier;
import eWorld.datatypes.identifiers.UserIdentifier;

@SuppressWarnings("serial")
public abstract class EvObject <EV extends EvAbstract, IDENT extends EvIdentifier> extends EvDataType<EV, IDENT> implements Serializable {

	// attributes

	private WoString name;
	
	private WoString description = null;
	
	
	// constructors

	/** default constructor for remote procedure call (RPC) */
	public EvObject() {
	}
	
	/**
	 * 
	 * @param ev
	 * @param identifier
	 * @param name
	 * @param description can be null
	 * @param author the id of the user who suggested the item, can be null
	 */
	public EvObject(EV ev, IDENT identifier, WoString name, WoString description, UserIdentifier author) {
		super(ev, identifier, author, null);	// TODO add authorTime to arguments
		
		assert null != name;
		
		this.name = name;
		this.description = description;
	}

	
	// methods

	public WoString getName() {
		return name;
	}
	
	/**
	 * @return may be null
	 */
	public WoString getDescription() {
		return description;
	}
	
	/**
	 * @return {@code getDescription().getString()} or null if description(string) is null
	 */
	public String getDescriptionString() {
		if (null != description) {
			return description.getString();
		} else {
			return null;
		}
	}
	
	/**
	 * checks if the attributes are valid
	 * @throws EvIllegalDataException if attributes are not valid
	 */
	public void check() throws EvIllegalDataException {
		if (getMaxNameLength() < getName().getString().length()) {
			throw new EvIllegalDataException("the maximum length of argument 'name' is " + getMaxNameLength());
		}
		if (null != getDescription() && getMaxDescriptionLength() < getDescription().getString().length()) {
			throw new EvIllegalDataException("the maximum length of argument 'description' is " + getMaxDescriptionLength());
		}
	}
	
	public abstract int getMaxNameLength();
	
	public abstract int getMaxDescriptionLength();
}
