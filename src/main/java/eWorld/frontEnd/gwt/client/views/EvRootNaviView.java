package eWorld.frontEnd.gwt.client.views;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;

import eWorld.datatypes.identifiers.EntryClassIdentifier;
import eWorld.datatypes.packages.EvPackage;
import eWorld.datatypes.packages.RootPackage;
import eWorld.frontEnd.gwt.client.EvApp;
import eWorld.frontEnd.gwt.client.EvAsyncCallback;
import eWorld.frontEnd.gwt.client.EvStyle;

public class EvRootNaviView extends EvNaviPackageView {
	
	private static final String PROJECT_NAME = "UnnamedProject";
	
	
	// attributes
	
	private final EntryClassIdentifier rootIdent;
	
	/** loaded by update(), set by set() */
	private RootPackage rootPackage = null;
	
	
	// components
	
	private FlowPanel panel = new FlowPanel();
	
	private Label welcomeLabel = new Label("Welcome to " + PROJECT_NAME);
	
	private Grid grid = new Grid(2, 2);
	
	private EvStoryView eStoryView = new EvStoryView();
	
	private EvCommentView eCommentView = new EvCommentView();
	
	
	// constructors
	
	public EvRootNaviView(EntryClassIdentifier rootIdent) {
		assert null != rootIdent;
		
		this.rootIdent = rootIdent;
		
		// load RootPackage
		update();
	}
	
	
	// methods
	
	private void set(RootPackage rootPackage) {
		this.rootPackage = rootPackage;
		
		panel.clear();
		
		// welcomeLabel
		welcomeLabel.addStyleName(EvStyle.eRootNaviViewWelcomeLabel);
		panel.add(welcomeLabel);
		
		// grid
		grid.addStyleName(EvStyle.eRootNaviViewGrid);
		grid.setText(0, 0, "registered users:");
		grid.setText(0, 1, String.valueOf(rootPackage.getRegisteredUsers()));
		grid.setText(1, 0, "number of stories:");
		grid.setText(1, 1, String.valueOf(rootPackage.getEntries()));
		panel.add(grid);
		
		// story
		eStoryView.setContainer(rootPackage.getStory(), PROJECT_NAME);
		panel.add(eStoryView);
		
		// comments
		eCommentView.setContainer(rootPackage.getCommentContainer(), PROJECT_NAME);
		panel.add(eCommentView);
		
		setWidget(panel);
	}
	
	@Override
	public EntryClassIdentifier getCurrentClassIdent() {
		return rootIdent;
	}

	@Override
	public EntryClassIdentifier getCurrentSuperClassIdent() {
		return rootIdent;
	}

	@Override
	public EvPackage getDataPackage() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void update() {
		EvApp.REQ.getRootPackage(new EvAsyncCallback<RootPackage>() {
			@Override
			public void onSuccess(RootPackage result) {
				set(result);
			}
		});
	}

}
