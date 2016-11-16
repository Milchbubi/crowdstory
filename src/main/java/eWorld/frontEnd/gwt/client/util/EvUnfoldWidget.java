package eWorld.frontEnd.gwt.client.util;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import eWorld.frontEnd.gwt.client.EvStyle;

public class EvUnfoldWidget extends FlowPanel {

	private final EvToggleButton headerToggleButton;
	
	private final Widget unfoldWidget;
	
	/**
	 * 
	 * @param headerString
	 * @param widget the widget that should be wrapped
	 */
	public EvUnfoldWidget(String headerString, Widget widget) {
		
		// init headerLabel
		headerToggleButton = new EvToggleButton(headerString);
		headerToggleButton.setValue(false);
		
		// init optionPanel
		this.unfoldWidget = widget;
		unfoldWidget.setVisible(false);
		
		// style
		unfoldWidget.addStyleName(EvStyle.eOptionPanelOptionPanel);
		addStyleName(EvStyle.eOptionPanel);
		
		// assemble
		add(headerToggleButton);
		add(unfoldWidget);
		
		// handlers
		headerToggleButton.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				unfoldWidget.setVisible(event.getValue());
			}
		});
		
		/*addDomHandler(new MouseOverHandler() {
			@Override
			public void onMouseOver(MouseOverEvent event) {
				optionPanel.setVisible(true);
			}
		}, MouseOverEvent.getType());
		
		addDomHandler(new MouseOutHandler() {
			@Override
			public void onMouseOut(MouseOutEvent event) {
				optionPanel.setVisible(false);
			}
		}, MouseOutEvent.getType());
		
		addDomHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				optionPanel.setVisible(true);
			}
		}, ClickEvent.getType());*/
		
	}
	
}
