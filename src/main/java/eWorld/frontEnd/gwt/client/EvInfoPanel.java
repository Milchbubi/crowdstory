package eWorld.frontEnd.gwt.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import eWorld.frontEnd.gwt.client.util.EvButton;

public class EvInfoPanel extends SimplePanel {
	
	// components
	
	private HorizontalPanel panel = new HorizontalPanel();
	
	private VerticalPanel infoPanel = new VerticalPanel();
	
	private EvButton clearButton = new EvButton("clear");
	
	
	// constructors
	
	public EvInfoPanel() {
		panel.add(infoPanel);
		panel.add(clearButton);
		
		addStyleName(EvStyle.eInfoPanel);
		infoPanel.addStyleName(EvStyle.eInfoPanelInfoPanel);
		clearButton.addStyleName(EvStyle.eInfoPanelClearButton);
		setVisible(false);
		
		setWidget(panel);
		
		
		clearButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				infoPanel.clear();
				setVisible(false);
			}
		});
	}
	
	
	// methods
	
	public void addSuccess(String m) {
		Label label = new Label("SUCCESS: " + m);
		label.addStyleName(EvStyle.eInfoPanelSuccess);
		addLabel(label);
	}
	
	public void addInfo(String m) {
		Label label = new Label("INFO: " + m);
		label.addStyleName(EvStyle.eInfoPanelInfo);
		addLabel(label);
	}
	
	public void addProblem(String m) {
		Label label = new Label("PROBLEM: " + m);
		label.addStyleName(EvStyle.eInfoPanelProblem);
		addLabel(label);
	}
	
	public void addFailure(String m) {
		Label label = new Label("FAILURE: " + m);
		label.addStyleName(EvStyle.eInfoPanelFailure);
		addLabel(label);
	}
	
	private void addLabel(Label label) {
		label.addStyleName(EvStyle.eInfoPanelLabel);
		infoPanel.add(label);
		setVisible(true);
	}
}
