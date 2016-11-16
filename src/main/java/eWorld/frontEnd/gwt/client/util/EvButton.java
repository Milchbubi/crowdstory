package eWorld.frontEnd.gwt.client.util;

import com.google.gwt.user.client.ui.Button;

import eWorld.frontEnd.gwt.client.EvStyle;

public class EvButton extends Button {

	public EvButton() {
		super();
	}
	
	public EvButton(String html) {
		super(html);
		
		setStyleName(EvStyle.eUtilButton);
	}
}
