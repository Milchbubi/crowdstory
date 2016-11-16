package eWorld.frontEnd.gwt.client.util;

import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.ToggleButton;

import eWorld.frontEnd.gwt.client.EvStyle;

public class EvToggleButton extends ToggleButton {

	public EvToggleButton(String upText) {
		super(upText);
		addStyleName(EvStyle.eToggleButton);
	}
	
}
