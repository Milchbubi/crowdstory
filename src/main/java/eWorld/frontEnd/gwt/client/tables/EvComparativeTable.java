package eWorld.frontEnd.gwt.client.tables;

import java.util.ArrayList;

import eWorld.datatypes.containers.EvDataTypeContainer;
import eWorld.datatypes.containers.IdDataTypeContainer;
import eWorld.datatypes.data.EvDataType;
import eWorld.datatypes.data.IdDataType;
import eWorld.datatypes.evs.Ev;
import eWorld.datatypes.identifiers.EvCompleteIdentifier;
import eWorld.datatypes.identifiers.EvShortIdentifier;

public abstract class EvComparativeTable<
		CLASS_IDENT extends EvCompleteIdentifier,
		ITEM_IDENT extends EvShortIdentifier<ITEM_IDENT_TYPE>,
		ITEM_IDENT_TYPE,
		ITEM extends EvDataType<Ev, ITEM_IDENT>,
		ITEM_CONTAINER extends EvDataTypeContainer<CLASS_IDENT, ITEM_IDENT, ITEM_IDENT_TYPE, ITEM>,
		COMPARATIVE_ITEM extends IdDataType<ITEM_IDENT>,
		COMPARATIVE_ITEM_CONTAINER extends IdDataTypeContainer<CLASS_IDENT, ITEM_IDENT, ITEM_IDENT_TYPE, COMPARATIVE_ITEM>
	> extends EvTable<CLASS_IDENT, ITEM_IDENT, ITEM_IDENT_TYPE, ITEM, ITEM_CONTAINER> {

	public EvComparativeTable(boolean eColsVisible) {
		super(eColsVisible);
	}
	
	/**
	 * searches in comparativeData for the comparativeItem that matches item
	 * @param item
	 * @param startIndex the index in comparativeData where the search should start (recommendation of startSearchIndex)
	 * @param comparativeData
	 * @return the index of the matching comparativeItem in comparativeData or -1 if comparativeData does not contain a matching comparativeItem
	 */
	protected int findMatching(ITEM item, int startIndex, ArrayList<COMPARATIVE_ITEM> comparativeData) {
		assert null != item;
		assert null != comparativeData;
		
		ITEM_IDENT_TYPE id = item.getIdentifier().getShortId();
		
		// lucky strike
		if (comparativeData.size() > startIndex && id.equals(comparativeData.get(startIndex).getIdentifier().getShortId())) {
			return startIndex;
		}
		
		// search alternating
		for (int d=1; ; d++) {
			
			if (0 > startIndex-d) {
				// first item achieved, search incrementing
				return findMatchingIncrementing(id, startIndex+d, comparativeData);
			}
			if (comparativeData.size() <= startIndex+d) {
				// last item achieved, search decrementing
				return findMatchingDecrementing(id, startIndex-d, comparativeData);
			}
			
			if (id.equals(comparativeData.get(startIndex+d).getIdentifier().getShortId())) {
				return startIndex+d;
			}
			if (id.equals(comparativeData.get(startIndex-d).getIdentifier().getShortId())) {
				return startIndex-d;
			}
		}
	}
	
	private int findMatchingIncrementing(ITEM_IDENT_TYPE id, int startIndex, ArrayList<COMPARATIVE_ITEM> comparativeData) {
		
		for (int i=startIndex; comparativeData.size() > i; i++) {
			if (id.equals(comparativeData.get(i).getIdentifier().getShortId())) {
				return i;
			}
		}
		
		return -1;
	}
	
	private int findMatchingDecrementing(ITEM_IDENT_TYPE id, int startIndex, ArrayList<COMPARATIVE_ITEM> comparativeData) {
		
		for (int i=startIndex; 0 <= i; i--) {
			if (id.equals(comparativeData.get(i).getIdentifier().getShortId())) {
				return i;
			}
		}
		
		return -1;
	}
}
