package eWorld.frontEnd.gwt.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import eWorld.datatypes.EvPath;
import eWorld.datatypes.data.EvEntry;
import eWorld.datatypes.evs.Ev;
import eWorld.datatypes.evs.EvVoid;
import eWorld.datatypes.identifiers.EntryClassIdentifier;
import eWorld.datatypes.identifiers.EntryIdentifier;
import eWorld.datatypes.identifiers.EntryShortIdentifier;
import eWorld.frontEnd.gwt.client.util.EvButton;
import eWorld.frontEnd.gwt.client.util.EvHistoryToken;
import eWorld.frontEnd.gwt.client.util.EvImageButton;
import eWorld.frontEnd.gwt.client.views.EvElementClassView;
import eWorld.frontEnd.gwt.client.views.EvElementView;
import eWorld.frontEnd.gwt.client.views.EvEntryPackageView;
import eWorld.frontEnd.gwt.client.views.EvNaviPackageView;

public abstract class EvWindow extends VerticalPanel {

	// static finals
	
	/** to recognize when the top of the hierarchy is achieved, null when not yet loaded */
	private EvEntry<Ev, EntryIdentifier> rootEntry = null;
	
	
	// attributes
	
	
	// components
	
	private HorizontalPanel topPanel = new HorizontalPanel();
	
	/** ascend in hierarchy */
//	private EvButton superButton = new EvButton("< super");
	private EvImageButton superButton = new EvImageButton(new Image(Images.supack));
	
	/** navigation in hierarchy */
	private EvPathView ePath;
	
	
	// handlers
	
	private ValueChangeHandler<String> haHistory = new ValueChangeHandler<String>() {
		@Override
		public void onValueChange(ValueChangeEvent<String> event) {
			loadEntryAndPath();
		}
	};
	
	private ClickHandler haSuper = new ClickHandler() {
		@Override
		public void onClick(ClickEvent event) {
			ascendInHierarchy();
		}
	};
	
	
	// observers
	
	
	// callbacks
	
	
	// constructors
	
	public EvWindow() {
		
//		ePath = new EvPathView(obsEntrySelected);
		ePath = new EvPathView();
		
		// load rootEntry
		EvApp.REQ.getRootEntry(new EvAsyncCallback<EvEntry<Ev, EntryIdentifier>>() {
			@Override
			public void onSuccess(EvEntry<Ev, EntryIdentifier> result) {
				assert null != result;
				
				rootEntry = result;
			}
		});
		
		// load entry and path
		loadEntryAndPath();
		
		// compose
		topPanel.add(superButton);
		topPanel.add(ePath);
		add(topPanel);
		
		// style
		superButton.addStyleName(EvStyle.eWindowSuperButton);
		ePath.addStyleName(EvStyle.eWindowPath);
		topPanel.addStyleName(EvStyle.eWindowTopPanel);
		
		// handlers
		History.addValueChangeHandler(haHistory);
		superButton.addClickHandler(haSuper);
	}
	
	
	// methods
	
	protected void loadEntryAndPath() {
		final EvHistoryToken eHistoryToken = new EvHistoryToken(History.getToken());
		
		if (null != eHistoryToken.getClassId() && null != eHistoryToken.getId()) {
			// load entry given in adressBar
			final EntryIdentifier entryIdentifier = new EntryIdentifier(eHistoryToken.getClassId(), eHistoryToken.getId());	// FIXME classId and id are equal for EntryIdentifier, this might be bad
			final boolean metaView = eHistoryToken.getClassId().equals(eHistoryToken.getId());
			new Timer() {
				@Override
				public void run() {
					// TODO FIXME this is stupid, avoid timer
					load(entryIdentifier, metaView || !eHistoryToken.getDir());
				}
			}.schedule(10);
			EvApp.REQ.getPath(entryIdentifier.getShortIdentifier(), new EvAsyncCallback<EvPath>() {
				@Override
				public void onSuccess(EvPath result) {
					if (true == metaView) {
						// metaView is displayed => duplicate last item
						result.addBack(result.getPath().get(result.getPath().size()-1));
					}
					ePath.setPath(result);
				}
			});
			
		} else {
			// no entry is given in adressBar
			EvApp.REQ.getStartEntry(new EvAsyncCallback<EvEntry<Ev, EntryIdentifier>>() {
				@Override
				public void onSuccess(EvEntry<Ev, EntryIdentifier> result) {
					load(result.getIdentifier(), result.isElement());
					EvApp.REQ.getPath(result.getIdentifier().getShortIdentifier(), new EvAsyncCallback<EvPath>() {
						@Override
						public void onSuccess(EvPath result) {
							ePath.setPath(result);
						}
					});
				}
			});
			
		}
	}
	
	protected EvEntry<Ev, EntryIdentifier> getRootEntry() {
		return rootEntry;
	}
	
	/**
	 * @param enabled true for enabled
	 */
	protected void setSuperButtonEnabled(boolean enabled) {
		superButton.setEnabled(enabled);
	}
	
	protected EvPathView getEvPathView() {
		return ePath;
	}
	
	/**
	 * called when an entry should be loaded
	 * @param entryIdentifier specifies the entry that should be loaded
	 * @param elementView true if elementView should be loaded, false when directoryView should be loaded
	 */
	protected abstract void load(EntryIdentifier entryIdentifier, boolean elementView);
	
	protected abstract void ascendInHierarchy();
	
	public abstract void update();
}
