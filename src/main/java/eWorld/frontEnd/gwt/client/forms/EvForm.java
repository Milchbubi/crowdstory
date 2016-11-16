package eWorld.frontEnd.gwt.client.forms;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import eWorld.frontEnd.gwt.client.EvStyle;

public abstract class EvForm extends FormPanel {
	
	private FlowPanel panel = new FlowPanel();
	
	private FlexTable dialogTable = new FlexTable();
	
	private VerticalPanel errorPanel = new VerticalPanel();
	
	private HorizontalPanel finalizePanel = new HorizontalPanel();
	
	protected EvForm() {
		
		// style
		finalizePanel.addStyleName(EvStyle.eFormFinalizePanel);
		
		// compose
		panel.add(dialogTable);
		panel.add(errorPanel);
		panel.add(finalizePanel);
		setWidget(panel);
		
		// style
		errorPanel.addStyleName(EvStyle.eFormErrorPanel);
		
	}
	
	protected void addToDialogTable(String text, final Widget widget) {
		assert null != text;
		assert null != widget;
		
		int row = dialogTable.getRowCount();
		
		dialogTable.setText(row, 0, text + ":");
		dialogTable.setWidget(row, 1, widget);
		
		// focus widget of first row
		if (0 == row) {
			Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
				@Override
				public void execute() {
					widget.getElement().focus();
				}
			});
		}
	}
	
	protected void addToFinalizePanel(Widget widget) {
		assert null != widget;
		
		finalizePanel.add(widget);
	}
	
	protected void addToErrorPanel(String message) {
		assert null != message;
		
		errorPanel.add(new Label(message));
	}
	
	protected void clearErrorPanel() {
		errorPanel.clear();
	}
}
