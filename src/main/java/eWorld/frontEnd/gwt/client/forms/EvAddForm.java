package eWorld.frontEnd.gwt.client.forms;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

import eWorld.frontEnd.gwt.client.EvObserver;
import eWorld.frontEnd.gwt.client.EvStyle;
import eWorld.frontEnd.gwt.client.util.EvButton;
import eWorld.frontEnd.gwt.client.util.EvSubmitButton;

/**
 * 
 * @author michael
 *
 * @param <ITEM> type of item that can be added by the form
 */
public abstract class EvAddForm<ITEM> extends EvForm {

	// attributes
	
	private final EvObserver<ITEM> obsClose;
	
	
	// components
	
	private EvButton cancelButton = new EvButton("cancel");
	private EvSubmitButton addButton = new EvSubmitButton("add");
	
	
	// handlers
	
	private ClickHandler haCancel = new ClickHandler() {
		@Override
		public void onClick(ClickEvent event) {
			obsClose.call(null);
		}
	};
	
	private ClickHandler haConfirm = new ClickHandler() {
		@Override
		public void onClick(ClickEvent event) {
			confirm();
		}
	};
	
	
	// constructors
	
	/**
	 * @param obsClose the added item or null when the form is just canceled
	 */
	public EvAddForm(EvObserver<ITEM> obsClose) {
		assert null != obsClose;
		
		this.obsClose = obsClose;
		
		// compose
		super.addToFinalizePanel(cancelButton);
		super.addToFinalizePanel(addButton);
		
		// style
		cancelButton.addStyleName(EvStyle.eAddFormCancelButton);
		addButton.addStyleName(EvStyle.eAddFormAddButton);
		
		// handlers
		cancelButton.addClickHandler(haCancel);
		addButton.addClickHandler(haConfirm);
	}
	
	
	// methods
	
	protected EvObserver<ITEM> getCloseObserver() {
		return obsClose;
	}
	
	// abstract methods
	
	/**
	 * called when confirm-button is pressed
	 */
	protected abstract void confirm();
}
