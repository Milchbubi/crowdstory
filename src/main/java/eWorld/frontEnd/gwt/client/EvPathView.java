package eWorld.frontEnd.gwt.client;

import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Label;

import eWorld.datatypes.EvPath;
import eWorld.datatypes.data.EvEntry;
import eWorld.datatypes.evs.EvVoid;
import eWorld.datatypes.identifiers.EntryShortIdentifier;
import eWorld.frontEnd.gwt.client.util.EvHistoryToken;

public class EvPathView extends Label {

	// TODO call observer when element of the path becomes selected (extend FlowPanel | HorizontalPanel instead of Label)
//	private EvObserver<EvEntry<EvVoid, EntryShortIdentifier>> obsEntrySelected;
	
	private EvPath path = new EvPath();
	
	public EvPathView() {
		refreshView();
	}
	
	/**
	 * TODO use this constructor and delete the one above
	 * @param obsEntrySelected
	 */
//	public EvPathView(EvObserver<EvEntry<EvVoid, EntryShortIdentifier>> obsEntrySelected) {
//		assert null != obsEntrySelected;
//		
//		this.obsEntrySelected = obsEntrySelected;
//		
//		refreshView();
//	}
	
	
	public void setPath(EvPath path) {
		assert null != path;
		
		this.path = path;
		refreshView();
		refreshHistory();
	}
	
	public void addBack(EvEntry<EvVoid, EntryShortIdentifier> element) {
		assert null != element;
		
		path.addBack(element);
		refreshView();
		refreshHistory();
	}
	
	/**
	 * removes the last element from the path or does nothing if there are no elements
	 */
	public void removeBack() {
		path.removeBack();
		refreshView();
		refreshHistory();
	}
	
	private void refreshView() {
		assert null != path;
		
		String text = " / ";
		
		for (EvEntry<EvVoid, EntryShortIdentifier> element : path.getPath()) {
			text += element.getName().getString() + " / ";
		}
		
		setText(text);
	}
	
	private void refreshHistory() {
		if (path.getPath().size() >= 2) {
			// top is not achieved
			EvHistoryToken historyToken = new EvHistoryToken(
					path.getPath().get(path.getPath().size()-2).getIdentifier().getShortId(), 
					path.getPath().get(path.getPath().size()-1).getIdentifier().getShortId());
			History.newItem(historyToken.toHistoryToken(), false);
		} else {
			// top is achieved
			History.newItem("", false);
		}
	}
	
}
