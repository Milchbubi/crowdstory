package eWorld.frontEnd.gwt.client.views;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import eWorld.datatypes.identifiers.EntryClassIdentifier;
import eWorld.datatypes.identifiers.EntryIdentifier;
import eWorld.datatypes.identifiers.EntryShortIdentifier;
import eWorld.datatypes.packages.EvElement;
import eWorld.datatypes.packages.EvPackage;
import eWorld.frontEnd.gwt.client.EvApp;
import eWorld.frontEnd.gwt.client.EvAsyncCallback;
import eWorld.frontEnd.gwt.client.EvStyle;

public abstract class EvOrdinaryElementView extends EvNaviPackageView {

	// attributes
	
	/** specifies the element that is loaded and displayed */
	private EntryIdentifier identifier;
	
	/** contains the attributes that are viewed by eAttributeTable */
	private EvElement eElement = null;
	
	
	// handlers
	
	
	// callbacks
	
	EvAsyncCallback<EvElement> cbLoad = new EvAsyncCallback<EvElement>() {
		@Override
		public void onSuccess(EvElement result) {
			assert null != result;
			
			setEvElement(result);
		}
	};
	
	
	// components
	
	private VerticalPanel panel = new VerticalPanel();
	
	private HorizontalPanel topPanel = new HorizontalPanel();
	
	private EvInfoView eInfoView = new EvInfoView();
	
	private FlowPanel viewsPanel = new FlowPanel();
	
//	private HorizontalPanel containerViewsPanel1 = new HorizontalPanel();
	
	private EvMediumView mediumView = new EvMediumView();
	
	private EvStoryView storyView = new EvStoryView();
	
//	private HorizontalPanel containerViewsPanel2 = new HorizontalPanel();
	
	private EvCommentView eCommentView = new EvCommentView();
	
	
	// constructors
	
	public EvOrdinaryElementView(EntryIdentifier identifier, boolean classOptionsVisible) {
		assert null != identifier;
		
		this.identifier = identifier;
		
		// load element
		EvApp.REQ.getElement(
				getCurrentSuperClassIdent(), 
				new EntryShortIdentifier(identifier.getEntryId()), 
				cbLoad);
		
		// compose
		topPanel.add(eInfoView);
		
//		viewsPanel.add(mediumView);
		viewsPanel.add(storyView);
		viewsPanel.add(eCommentView);
		
		panel.add(topPanel);
		panel.add(viewsPanel);
		
		setWidget(panel);
		
		// style
		topPanel.addStyleName(EvStyle.eNaviPackageViewTopPanel);
		mediumView.addStyleName(EvStyle.eElementViewCompactView);
//		eCommentView.addStyleName(EvStyle.eElementViewCompactView);
		viewsPanel.addStyleName(EvStyle.eElementViewViewsPanel);
		panel.addStyleName(EvStyle.eNaviPackageViewPanel);
		
		// handlers
		
	}
	
	
	// methods
	
	protected EntryIdentifier getIdentifier() {
		return identifier;
	}
	
	private void setEvElement(EvElement eElement) {
		assert null != eElement;
		
		this.eElement = eElement;
		
		eInfoView.set(eElement.getHeader());
		
		mediumView.setContainer(eElement.getMediumContainer());
		storyView.setContainer(eElement.getStory(), eElement.getHeader().getName().getString());
		eCommentView.setContainer(eElement.getCommentContainer(), eElement.getHeader().getName().getString());
		
	}
	
	
	// overridden methods
	
	@Override
	public EntryClassIdentifier getCurrentClassIdent() {
		if (null != eElement) {
			return new EntryClassIdentifier(eElement.getHeader().getIdentifier().getEntryId());
		} else {
			return null;
		}
	}
	
	@Override
	public EvPackage getDataPackage() {
		return eElement;
	}

	@Override
	public void update() {
		assert null != eElement;
		
		if (null != eElement) {
			EvApp.REQ.getElement(
					getCurrentSuperClassIdent(), 
					new EntryShortIdentifier(eElement.getHeader().getIdentifier().getEntryId()), 
					cbLoad);
		}
	}

}
