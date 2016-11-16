package eWorld.frontEnd.gwt.client.tables;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;

import eWorld.frontEnd.gwt.client.EvObserver;
import eWorld.frontEnd.gwt.client.EvStyle;
import eWorld.frontEnd.gwt.client.Images;
import eWorld.frontEnd.gwt.client.util.EvImageButton;

public class EvNestedTableView extends FlowPanel {

	private final EvObserver<Void> obsClose;
	
	private ClickHandler haClose = new ClickHandler() {
		@Override
		public void onClick(ClickEvent event) {
			obsClose.call(null);
		}
	};
	
	private EvImageButton closeButton = new EvImageButton(new Image(Images.close));
	
	public EvNestedTableView(Widget content, EvObserver<Void> obsClose) {
		assert null != content;
		assert null != obsClose;
		
		this.obsClose = obsClose;
		
		add(closeButton);
		add(content);
		
		closeButton.addStyleName(EvStyle.eNestedTableViewCloseButton);
		
		closeButton.addClickHandler(haClose);
	}
}
