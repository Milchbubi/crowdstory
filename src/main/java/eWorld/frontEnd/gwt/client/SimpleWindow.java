package eWorld.frontEnd.gwt.client;

import com.google.gwt.user.client.ui.SimplePanel;

import eWorld.datatypes.data.EvEntry;
import eWorld.datatypes.evs.EvVoid;
import eWorld.datatypes.identifiers.EntryClassIdentifier;
import eWorld.datatypes.identifiers.EntryIdentifier;
import eWorld.datatypes.identifiers.EntryShortIdentifier;
import eWorld.frontEnd.gwt.client.views.EvElementClassView;
import eWorld.frontEnd.gwt.client.views.EvElementView;
import eWorld.frontEnd.gwt.client.views.EvEntryPackageView;
import eWorld.frontEnd.gwt.client.views.EvNaviPackageView;

public class SimpleWindow extends EvWindow {

	// attributes
	
	/** becomes true when obsEvButtonPressed is called and false when ascendInHierarchy is called */
	private boolean eViewDisplayedFlag = false;
	
	
	// components
	
	private SimplePanel viewContainer = new SimplePanel();
	
	private EvNaviPackageView currentView;
	
	
	// observers
	
	private EvObserver<EvEntry<EvVoid, EntryShortIdentifier>> obsEntrySelected = new EvObserver<EvEntry<EvVoid, EntryShortIdentifier>>() {
		@Override
		public void call(EvEntry<EvVoid, EntryShortIdentifier> value) {
			assert null != value;
			
			if (!value.isElement()) {
				setEntryPackageView(value.getIdentifier());
			} else {
				setElementView(new EntryIdentifier(currentView.getCurrentClassIdent(), value.getIdentifier()));
			}
			getEvPathView().addBack(value);
		}
	};
	
	private EvObserver<EntryIdentifier> obsEvButtonPressed = new EvObserver<EntryIdentifier>() {
		@Override
		public void call(EntryIdentifier value) {
			assert null != value;
			
			currentView = new EvElementClassView(value);
			
			viewContainer.setWidget(currentView);
			
			eViewDisplayedFlag = true;
		}
	};
	
	
	// constructors
	
	public SimpleWindow() {
		
		// compose
		add(viewContainer);
		
		// style
		viewContainer.addStyleName(EvStyle.eWindowViewPanel);
		
	}
	
	
	// methods
	
	private void setEntryPackageView(EntryShortIdentifier identifier) {
		assert null != identifier;
		
		currentView = new EvEntryPackageView(new EntryClassIdentifier(identifier.getEntryId()), obsEntrySelected, obsEvButtonPressed);
		
		viewContainer.setWidget(currentView);
	}
	
	private void setElementView(EntryIdentifier identifier) {
		assert null != identifier;
		
		currentView = new EvElementView(identifier);
		
		viewContainer.setWidget(currentView);
	}
	
	@Override
	protected void load(EntryIdentifier entryIdentifier, boolean elementView) {
		assert null != entryIdentifier;
		
		if (true == elementView) {
			setElementView(entryIdentifier);
		} else {
			setEntryPackageView(entryIdentifier.getShortIdentifier());
		}
	}
	
	@Override
	protected void ascendInHierarchy() {
		
		if (getRootEntry().getIdentifier().getEntryId() != currentView.getCurrentClassIdent().getEntryClassId()) {
			// ascend
			currentView = new EvEntryPackageView(currentView.getCurrentSuperClassIdent(), obsEntrySelected, obsEvButtonPressed);
			
			if (false == eViewDisplayedFlag) { // when eView was displayed ePath didn't become extended
				getEvPathView().removeBack();
			} else {
				eViewDisplayedFlag = false;
			}
			
		} else {
			// top is achieved
			currentView = new EvEntryPackageView(new EntryClassIdentifier(getRootEntry().getIdentifier().getEntryId()), obsEntrySelected, obsEvButtonPressed);
		}
		
		viewContainer.setWidget(currentView);
	}
	
	@Override
	public void update() {
		currentView.update();
	}
	
}
