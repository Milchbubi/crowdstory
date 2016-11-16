package eWorld.frontEnd.gwt.client.tables;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import eWorld.frontEnd.gwt.client.EvStyle;
import eWorld.frontEnd.gwt.client.Images;
import eWorld.frontEnd.gwt.client.util.EvImageButton;

public class EvCell extends FlowPanel {

	private EvImageButton eButton;
	
	/**
	 * @param widget the widget that should be displayed
	 */
	public EvCell(Widget widget) {
		assert null != widget;
		
		Image e = new Image(Images.e);
		e.addStyleName(EvStyle.eCellEvImage);
		eButton = new EvImageButton(e);
		eButton.addStyleName(EvStyle.eCellEvButton);
		add(eButton);
		add(widget);
	}
	
	/**
	 * @param text the text that should be displayed
	 */
	public EvCell(String text) {
		this(new Label(text));
	}
	
	public EvImageButton getEvButton() {
		return eButton;
	}
}
