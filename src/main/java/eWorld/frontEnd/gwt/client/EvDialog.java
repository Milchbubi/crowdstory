package eWorld.frontEnd.gwt.client;

import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Widget;

public class EvDialog extends DialogBox {

	public EvDialog(String caption) {
		setText(caption);
		setGlassEnabled(true);
	}
	
	@Override
	public void setWidget(Widget widget) {
		super.setWidget(widget);
		super.center();
		super.show();
	}
}
