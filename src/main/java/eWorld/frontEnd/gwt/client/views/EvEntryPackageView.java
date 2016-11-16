package eWorld.frontEnd.gwt.client.views;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

import eWorld.datatypes.data.EvEntry;
import eWorld.datatypes.evs.Ev;
import eWorld.datatypes.evs.EvVoid;
import eWorld.datatypes.identifiers.EntryClassIdentifier;
import eWorld.datatypes.identifiers.EntryIdentifier;
import eWorld.datatypes.identifiers.EntryShortIdentifier;
import eWorld.datatypes.packages.EvEntryPackage;
import eWorld.datatypes.packages.EvPackage;
import eWorld.frontEnd.gwt.client.EvApp;
import eWorld.frontEnd.gwt.client.EvAsyncCallback;
import eWorld.frontEnd.gwt.client.EvDialog;
import eWorld.frontEnd.gwt.client.EvObserver;
import eWorld.frontEnd.gwt.client.EvStyle;
import eWorld.frontEnd.gwt.client.Images;
import eWorld.frontEnd.gwt.client.forms.EvAddEntryForm;
import eWorld.frontEnd.gwt.client.tables.EvEntryTable;
import eWorld.frontEnd.gwt.client.util.EvButton;
import eWorld.frontEnd.gwt.client.util.EvImageButton;

public class EvEntryPackageView extends EvNaviPackageView {

	// attributes
	
	/** contains the entries that are viewed by eEntryTable */
	private EvEntryPackage eEntryPackage = null;
	
	/** called when a row becomes selected */
	private EvObserver<EvEntry<EvVoid, EntryShortIdentifier>> obsEntrySelected;
	
	/** called when eButton is pressed */
	private EvObserver<EntryIdentifier> obsEvButtonPressed;
	
	
	// handlers
	
	private ClickHandler haEvButton = new ClickHandler() {
		@Override
		public void onClick(ClickEvent event) {
//			obsEvButtonPressed.call(new EntryIdentifier(
//					eEntryPackage.getHeader().getIdentifier().getEntryId(),	// identifies i.a. the attributes that should be loaded
//					eEntryPackage.getHeader().getIdentifier().getEntryId()	// identifies i.a. the attributeValues that should be loaded
//					));	// in this case both arguments are equal TODO ? is this better, uncomment these lines?
			obsEvButtonPressed.call(eEntryPackage.getHeader().getIdentifier());	// TODO ? delete this line?
			deselectSelectedRow();
		}
	};
	
	private ClickHandler haAddEntry = new ClickHandler() {
		@Override
		public void onClick(ClickEvent event) {
			
			// dialog that pops up
			final EvDialog dialog = new EvDialog("Add Entry");
			
			// the content of the pop up, observer is called when pop up becomes closed (cancel(false) and add(true))
			EvAddEntryForm addForm = new EvAddEntryForm(
					new EntryClassIdentifier(eEntryPackage.getHeader().getIdentifier().getEntryId()), 
					new EvObserver<EvEntry<Ev, EntryIdentifier>>() {
				@Override
				public void call(EvEntry<Ev, EntryIdentifier> value) {
					dialog.hide();
					
					if (null != value) {
						eEntryTable.addItem(EvEntry.shortCast(value));
						
						// auto-select added entry
						eEntryTable.setSelectedRow(EvEntry.shortCast(value));
						obsEntrySelected.call(EvEntry.voidCast(EvEntry.shortCast(value)));
					}
				}
			});
			
			// initialize dialog
			dialog.setWidget(addForm);
		}
	};
	
	
	// callbacks
	
	private EvAsyncCallback<EvEntryPackage> cbLoadEntries = new EvAsyncCallback<EvEntryPackage>() {
		@Override
		public void onSuccess(EvEntryPackage result) {
			assert null != result;
			
			set(result);
			if (null != obsEntriesLoadedAndDisplayed) {
				obsEntriesLoadedAndDisplayed.call(null);
			}
		}
	};
	
	// observers
	
	/** null when not specified */
	private EvObserver<Void> obsEntriesLoadedAndDisplayed = null;
	
	
	// components
	
	private VerticalPanel panel = new VerticalPanel();
	
	private HorizontalPanel topPanel = new HorizontalPanel();
	
	private EvInfoView eInfoView = new EvInfoView();
	
//	private FlowPanel buttonPanel = new FlowPanel();
	private HorizontalPanel buttonPanel = new HorizontalPanel();
	
	/** button to inter alia display and add attributes */
//	private EvImageButton eButton = new EvImageButton(new Image(Images.e));
	private EvButton eButton = new EvButton("meta");
	
	/** button to add entries */
//	private EvImageButton addEntryButton = new EvImageButton(new Image(Images.add));
	private EvButton addEntryButton = new EvButton("add story here");
	
	/** views the entries that are contained by eEntryContainer */
	private EvEntryTable eEntryTable;
	
	
	// constructors
	
	public EvEntryPackageView(EntryClassIdentifier identifier, EvObserver<EvEntry<EvVoid, EntryShortIdentifier>> obsEntrySelected, EvObserver<EntryIdentifier> obsEvButtonPressed) {
		this(obsEntrySelected, obsEvButtonPressed);
		
		assert null != identifier;
		
		// load entries
		EvApp.REQ.getEntryPackage(identifier, cbLoadEntries);
	}
	
	public EvEntryPackageView(EntryClassIdentifier identifier, 
			EvObserver<EvEntry<EvVoid, EntryShortIdentifier>> obsEntrySelected, 
			EvObserver<EntryIdentifier> obsEvButtonPressed,
			EvObserver<Void> obsEntriesLoadedAndDisplayed) {
		this(obsEntrySelected, obsEvButtonPressed);
		
		assert null != identifier;
		
		this.obsEntriesLoadedAndDisplayed = obsEntriesLoadedAndDisplayed;
		
		// load entries
		EvApp.REQ.getEntryPackage(identifier, cbLoadEntries);
	}
	
	public EvEntryPackageView(EvEntryPackage entryPackage, EvObserver<EvEntry<EvVoid, EntryShortIdentifier>> obsEntrySelected, EvObserver<EntryIdentifier> obsEvButtonPressed) {
		this(obsEntrySelected, obsEvButtonPressed);
		
		assert null != entryPackage;
		
		// set entries
		set(entryPackage);
	}
	
	private EvEntryPackageView(EvObserver<EvEntry<EvVoid, EntryShortIdentifier>> obsEntrySelected, EvObserver<EntryIdentifier> obsEvButtonPressed) {
		assert null != obsEntrySelected;
		assert null != obsEvButtonPressed;
		
		this.obsEntrySelected = obsEntrySelected;
		this.obsEvButtonPressed = obsEvButtonPressed;
		eEntryTable = new EvEntryTable(obsEntrySelected);
		
		// compose
		buttonPanel.add(addEntryButton);
		buttonPanel.add(eButton);
		
		topPanel.add(eInfoView);
		topPanel.add(buttonPanel);
		
		panel.add(topPanel);
		panel.add(eEntryTable);
		setWidget(panel);
		
		// style
		
		eButton.addStyleName(EvStyle.eNaviPackageViewTopPanelButtonPanelButton);
		addEntryButton.addStyleName(EvStyle.eNaviPackageViewTopPanelButtonPanelButton);
		buttonPanel.addStyleName(EvStyle.eNaviPackageViewTopPanelButtonPanel);
		topPanel.addStyleName(EvStyle.eNaviPackageViewTopPanel);
		eEntryTable.addStyleName(EvStyle.eViewTable);
		panel.addStyleName(EvStyle.eNaviPackageViewPanel);
		
		// handlers
		eButton.addClickHandler(haEvButton);
		addEntryButton.addClickHandler(haAddEntry);
	}
	
	
	// methods
	
	public void setObservers(EvObserver<EvEntry<EvVoid, EntryShortIdentifier>> obsEntrySelected, EvObserver<EntryIdentifier> obsEvButtonPressed) {
		assert null != obsEntrySelected;
		assert null != obsEvButtonPressed;
		
		this.obsEntrySelected = obsEntrySelected;
		this.obsEvButtonPressed = obsEvButtonPressed;
		
		eEntryTable.setObservers(obsEntrySelected);
	}
	
	public EvEntryPackage get() {
		return eEntryPackage;
	}
	
	private void set(EvEntryPackage eEntryPackage) {
		assert null != eEntryPackage;
		
		this.eEntryPackage = eEntryPackage;
		
		eInfoView.set(eEntryPackage.getHeader());
		
		eEntryTable.setItemContainer(eEntryPackage.getEntryContainer(), true);
	}
	
	public void setSelectedRow(EvEntry<Ev, EntryShortIdentifier> entry) {
		eEntryTable.setSelectedRow(entry);
	}
	
	public void deselectSelectedRow() {
		eEntryTable.deselectSelectedRow();
	}

	
	// overridden methods

	@Override
	public EntryClassIdentifier getCurrentClassIdent() {
		if (null != eEntryPackage) {
			return new EntryClassIdentifier(eEntryPackage.getHeader().getIdentifier().getEntryId());
		} else {
			return null;
		}
	}

	@Override
	public EntryClassIdentifier getCurrentSuperClassIdent() {
		if (null != eEntryPackage) {
			return eEntryPackage.getHeader().getIdentifier();
		} else {
			return null;
		}
	}
	
	@Override
	public EvEntryPackage getDataPackage() {
		return eEntryPackage;
	}
	

	@Override
	public void update() {
//		assert null != eEntryPackage;	FIXME? should never be null
		
		if (null != eEntryPackage) {
			EvApp.REQ.getEntryPackage(new EntryClassIdentifier(eEntryPackage.getHeader().getIdentifier().getEntryId()), cbLoadEntries);
		}
	}
	
}
