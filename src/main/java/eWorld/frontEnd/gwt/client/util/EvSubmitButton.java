package eWorld.frontEnd.gwt.client.util;

import com.google.gwt.user.client.ui.SubmitButton;

import eWorld.frontEnd.gwt.client.EvStyle;

public class EvSubmitButton extends SubmitButton {

	public EvSubmitButton() {
		super();
	}
	
	public EvSubmitButton(String html) {
		super(html);
		
		setStyleName(EvStyle.eUtilButton);
	}
}
