package eWorld.datatypes.data;

import java.io.Serializable;

import eWorld.datatypes.elementars.WoString;
import eWorld.datatypes.evs.EvAbstract;
import eWorld.datatypes.exceptions.EvIllegalDataException;
import eWorld.datatypes.identifiers.EvIdentifier;
import eWorld.datatypes.identifiers.MediumIdentifier;
import eWorld.datatypes.identifiers.MediumShortIdentifier;
import eWorld.datatypes.identifiers.UserIdentifier;

@SuppressWarnings("serial")
public class EvMedium <EV extends EvAbstract, IDENT extends EvIdentifier> extends EvDataType<EV, IDENT> implements Serializable {

	public static final int MAX_LINK_LENGTH = 250;
	public static final int MAX_DESCRIPTION_LENGTH = 300;
	
	// attributes

	private String link;
	
	private WoString description = null;
	
	
	// constructors
	
	/** default constructor for remote procedure call (RPC) */
	public EvMedium() {
	}
	
	public EvMedium(EV ev, IDENT identifier, String link, WoString description, UserIdentifier author) {
		super(ev, identifier, author, null);	// TODO add authorTime to arguments
		
		assert null != link;
		
		this.link = link;
		this.description = description;
	}
	
	
	// methods
	
	public String getLink() {
		return link;
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
	 * @return {@code getDescription().getString()} or "" if description(string) is null
	 */
	public String getDescriptionStringOrEmpty() {
		if (null != description) {
			return description.getString();
		} else {
			return "";
		}
	}
	
	public static <EV extends EvAbstract> EvMedium<EV, MediumShortIdentifier> shortCast(EvMedium<EV, MediumIdentifier> medium) {
		return new EvMedium<EV, MediumShortIdentifier>(
				medium.getEv(), 
				medium.getIdentifier().getShortIdentifier(), 
				medium.getLink(), 
				medium.getDescription(),
				medium.getAuthor()
				);
	}
	
	/**
	 * checks if the attributes are valid
	 * @throws EvIllegalDataException if attributes are not valid
	 */
	public void check() throws EvIllegalDataException {
		if (MAX_LINK_LENGTH < link.length()) {
			throw new EvIllegalDataException("the maximum length of argument 'link' is " + MAX_LINK_LENGTH);
		}
		if (null != getDescription() && MAX_DESCRIPTION_LENGTH < getDescription().getString().length()) {
			throw new EvIllegalDataException("the maximum length of argument 'description' is " + MAX_DESCRIPTION_LENGTH);
		}
	}
}
