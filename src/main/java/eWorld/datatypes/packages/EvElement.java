package eWorld.datatypes.packages;

import java.io.Serializable;

import eWorld.datatypes.containers.EvCommentContainer;
import eWorld.datatypes.containers.EvMediumContainer;
import eWorld.datatypes.containers.EvStory;
import eWorld.datatypes.data.EvEntry;
import eWorld.datatypes.evs.Ev;
import eWorld.datatypes.identifiers.EntryIdentifier;

@SuppressWarnings("serial")
public class EvElement extends EvPackage implements Serializable {

	// attributes
	
	private EvMediumContainer eMediumContainer;
	
	private EvStory eStory;
	
	private EvCommentContainer eCommentContainer;
	
	
	// constructors

	/** default constructor for remote procedure call (RPC) */
	public EvElement() {
	}
	
	public EvElement(
			EvEntry<Ev, EntryIdentifier> header,
			EvMediumContainer eMediumContainer,
			EvStory eStory,
			EvCommentContainer eCommentContainer) {
		super(header);
		
		assert null != eMediumContainer;
		assert null != eStory;
		assert null != eCommentContainer;
		
		this.eMediumContainer = eMediumContainer;
		this.eStory = eStory;
		this.eCommentContainer = eCommentContainer;
	}
	
	
	// methods
	
	public EvMediumContainer getMediumContainer() {
		return eMediumContainer;
	}
	
	public EvStory getStory() {
		return eStory;
	}
	
	public EvCommentContainer getCommentContainer() {
		return eCommentContainer;
	}
	
}