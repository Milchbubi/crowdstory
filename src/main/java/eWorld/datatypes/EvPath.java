package eWorld.datatypes;

import java.io.Serializable;
import java.util.ArrayList;

import eWorld.datatypes.data.EvEntry;
import eWorld.datatypes.evs.EvVoid;
import eWorld.datatypes.identifiers.EntryShortIdentifier;

/**
 * describes the hierarchical place of f.e. an Entry
 * @author michael
 *
 */
@SuppressWarnings("serial")
public class EvPath implements Serializable {

	/** contains all elements of the path */
	private ArrayList<EvEntry<EvVoid, EntryShortIdentifier>> elements = new ArrayList<EvEntry<EvVoid, EntryShortIdentifier>>();
	
	
	/** default constructor for remote procedure call (RPC) */
	public EvPath() {
		
	}
	
	
	public void addFront(EvEntry<EvVoid, EntryShortIdentifier> element) {
		assert null != element;
		
		elements.add(0, element);
	}
	
	public void addBack(EvEntry<EvVoid, EntryShortIdentifier> element) {
		assert null != element;
		elements.add(element);
	}
	
	/**
	 * removes the last element from the path or does nothing if there are no elements
	 */
	public void removeBack() {
		if (0 < elements.size()) {
			elements.remove(elements.size() -1);
		}
	}
	
	public ArrayList<EvEntry<EvVoid, EntryShortIdentifier>> getPath() {
		return elements;
	}
}
