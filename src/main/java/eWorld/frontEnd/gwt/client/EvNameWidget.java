package eWorld.frontEnd.gwt.client;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;

import eWorld.datatypes.elementars.WoString;

public class EvNameWidget extends FlowPanel {

	/**
	 * 
	 * @param name null is not allowed
	 * @param description null is allowed
	 */
	public EvNameWidget(WoString name, WoString description) {
		assert null != name;
		
		Label nameLabel = new Label(name.getString());
		nameLabel.addStyleName(EvStyle.eNameWidgetName);
		add(nameLabel);
		
		if (null != description) {
			Label descriptionLabel = new Label(description.getString());
			descriptionLabel.addStyleName(EvStyle.eNameWidgetDescription);
			add(descriptionLabel);
		}
	}
}
