package eWorld.frontEnd.gwt.client;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * displays a specified widget with a label below it
 * @author michael
 *
 * @param <INPUT_BOX> type of the widget that should be displayed
 */
public class EvInputBox<INPUT_BOX extends Widget> extends VerticalPanel {

	private INPUT_BOX inputBox;
	
	private Label label = new Label();
	
	public EvInputBox(INPUT_BOX inputBox) {
		assert null != inputBox;
		
		this.inputBox = inputBox;
		
		add(inputBox);
		add(label);
	}
	
	public INPUT_BOX getInputBox() {
		return inputBox;
	}
	
	public void setLabel(String text) {
		label.setText(text);
	}
	
	public void clearLabel() {
		label.setText("");
	}
	
	public void focusInputBox() {
		inputBox.getElement().focus();
	}
}
