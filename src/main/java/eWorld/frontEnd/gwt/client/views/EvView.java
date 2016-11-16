package eWorld.frontEnd.gwt.client.views;

import com.google.gwt.user.client.ui.SimplePanel;

import eWorld.frontEnd.gwt.client.EvStyle;

public abstract class EvView extends SimplePanel {

	public EvView() {
		addStyleName(EvStyle.eView);
	}
	
}
