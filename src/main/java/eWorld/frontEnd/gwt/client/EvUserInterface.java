package eWorld.frontEnd.gwt.client;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class EvUserInterface extends VerticalPanel{

	// observers
	private EvObserver<Void> obsSignInOut = new EvObserver<Void>() {
		@Override
		public void call(Void value) {
			eViewWindow.update();
		}
	};
	
	
	// components
	
	private HorizontalPanel topPanel = new HorizontalPanel();
	
	private HorizontalPanel topPanelRight = new HorizontalPanel();
	
	private EvUserPanel userPanel = new EvUserPanel(obsSignInOut, obsSignInOut);
	
//	private EvWindow eViewWindow = new SimpleWindow();
	private EvWindow eViewWindow = new DoubleWindow();
	
	private FlowPanel bottomPanel = new FlowPanel();
	
	
	// constructors
	
	public EvUserInterface() {
		
		// style
		addStyleName(EvStyle.eUserInterface);
		topPanel.addStyleName(EvStyle.eUserInterfaceTopPanel);
		topPanelRight.addStyleName(EvStyle.eUserInterfaceTopPanelRight);
		eViewWindow.addStyleName(EvStyle.eUserInterfaceWindow);
		bottomPanel.addStyleName(EvStyle.eUserInterfaceBottomPanel);
		
		// compose
		topPanelRight.add(EvApp.INFO);
		topPanelRight.add(userPanel);
		topPanel.add(topPanelRight);
		add(topPanel);
		add(eViewWindow);
		add(bottomPanel);
	}
	
}
