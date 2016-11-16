package eWorld.datatypes.containers;

import java.io.Serializable;
import java.util.ArrayList;

import eWorld.datatypes.data.EvEntry;
import eWorld.datatypes.evs.Ev;
import eWorld.datatypes.identifiers.EntryClassIdentifier;
import eWorld.datatypes.identifiers.EntryShortIdentifier;

@SuppressWarnings("serial")
public class EvEntryContainer extends EvObjectContainer<
		EntryClassIdentifier,
		EntryShortIdentifier,
		Long,
		EvEntry<Ev, EntryShortIdentifier>
		>
		implements Serializable {

	// attributes
	
	
	// constructors

	/** default constructor for remote procedure call (RPC) */
	public EvEntryContainer() {
		
	}
	
	public EvEntryContainer(
			EntryClassIdentifier classIdentifier,
			ArrayList<EvEntry<Ev, EntryShortIdentifier>> data) {
		super(classIdentifier, data);
	}
	
	
	// methods
	
	public String toString() {
		return toString("\n");
	}
	@Deprecated
	public String toString(String endl) {
		String string = getClassIdentifier().getEntryClassId() + ":" + endl;
		
		string += toStringFillUp("id")
				+ toStringFillUp("placing")
				+ toStringFillUp("rating")
				+ toStringFillUp("downVotes")
				+ toStringFillUp("upVotes")
				+ toStringFillUp("voted")
				+ toStringFillUp("name")
				+ toStringFillUp("description")
				+ endl;	// String.format(...) is not supported by gwt
		
		for (EvEntry<Ev, EntryShortIdentifier> entry : getData()) {
			
			String vote;
			if (null == entry.getEv().getVote()) {
				vote = "null";
			} else {
				vote = String.valueOf(entry.getEv().getVote());
			}
			
			string += toStringFillUp(String.valueOf(entry.getIdentifier().getShortIdentifier().getEntryId()))
					+ toStringFillUp(String.valueOf(entry.getEv().getRank()))
					+ toStringFillUp(String.valueOf(entry.getEv().getRating()))
					+ toStringFillUp(String.valueOf(entry.getEv().getDownVotes()))
					+ toStringFillUp(String.valueOf(entry.getEv().getUpVotes()))
					+ toStringFillUp(vote)
					+ toStringFillUp(entry.getName().getString())
					+ toStringFillUp(entry.getDescription().getString())
					+ endl;
		}
		
		return string;
	}
	private String toStringFillUp(String str) {
		
		for (int i = str.length(); i <= 15; i++) {
			str += "_";
		}
		
		return str;
	}

}
