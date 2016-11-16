package eWorld.frontEnd.gwt.client.forms;

import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;

import eWorld.datatypes.data.EvMedium;
import eWorld.datatypes.elementars.WoString;
import eWorld.datatypes.evs.Ev;
import eWorld.datatypes.evs.EvVoid;
import eWorld.datatypes.identifiers.EntryClassIdentifier;
import eWorld.datatypes.identifiers.MediumIdentifier;
import eWorld.frontEnd.gwt.client.EvApp;
import eWorld.frontEnd.gwt.client.EvAsyncCallback;
import eWorld.frontEnd.gwt.client.EvObserver;

public class EvAddMediumForm extends EvAddForm<EvMedium<Ev, MediumIdentifier>> {

	// attributes
	
	private final EntryClassIdentifier entryIdentifier;
	
	
	// components
	
	private TextBox linkInput = new TextBox();
	private TextArea descriptionInput = new TextArea();
	
	
	// constructors
	
	/**
	 * 
	 * @param entryIdentifier identifies the entry where medium should be added
	 * @param obsClose the added item or null when the form is just canceled
	 */
	public EvAddMediumForm(EntryClassIdentifier entryIdentifier, EvObserver<EvMedium<Ev, MediumIdentifier>> obsClose) {
		super(obsClose);
		
		assert null != entryIdentifier;
		
		this.entryIdentifier = entryIdentifier;
		
		// compose
		super.addToDialogTable("Link", linkInput);
		super.addToDialogTable("Description", descriptionInput);
	}

	
	// methods

	@Override
	protected void confirm() {
		
		super.clearErrorPanel();
		
		String link = linkInput.getValue();
		String description = descriptionInput.getValue();
		
		if (link.isEmpty()) {
			super.addToErrorPanel("Please input a link.");
			return;
		}
		
		EvMedium<EvVoid, EntryClassIdentifier> medium = new EvMedium<EvVoid, EntryClassIdentifier>(
				new EvVoid(),
				entryIdentifier,
				link,
				new WoString(description),
				null	// author is set by server
				);
		
		EvApp.REQ.addMedium(medium, new EvAsyncCallback<EvMedium<Ev, MediumIdentifier>>() {
			@Override
			public void onSuccess(EvMedium<Ev, MediumIdentifier> result) {
				getCloseObserver().call(result);
			}
		});
	}
}
