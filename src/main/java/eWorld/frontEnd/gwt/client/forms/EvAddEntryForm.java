package eWorld.frontEnd.gwt.client.forms;

import java.util.ArrayList;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.LongBox;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;

import eWorld.datatypes.data.EvEntry;
import eWorld.datatypes.data.EvSentence;
import eWorld.datatypes.elementars.WoString;
import eWorld.datatypes.evs.Ev;
import eWorld.datatypes.evs.EvVoid;
import eWorld.datatypes.exceptions.EvIllegalDataException;
import eWorld.datatypes.identifiers.EntryClassIdentifier;
import eWorld.datatypes.identifiers.EntryIdentifier;
import eWorld.datatypes.identifiers.EntryShortIdentifier;
import eWorld.datatypes.identifiers.SentenceShortIdentifier;
import eWorld.frontEnd.gwt.client.EvApp;
import eWorld.frontEnd.gwt.client.EvAsyncCallback;
import eWorld.frontEnd.gwt.client.EvObserver;
import eWorld.frontEnd.gwt.client.EvStyle;
import eWorld.frontEnd.gwt.client.Images;
import eWorld.frontEnd.gwt.client.views.story.EvWriteStoryForm;

public class EvAddEntryForm extends EvAddForm<EvEntry<Ev, EntryIdentifier>> {

	// attributes
	
	private final EntryClassIdentifier classIdentifier;
	
	/** used to link an entry, null when atm not specified (set by loadEntryFromIdInput(..) */
	private EntryIdentifier sourceIdent = null;
	
	/** caches the value from idInput to know if it has changed */
	private Long lastIdInput = null;
	
	
	// components
	
	private CheckBox linkOnlyCheckBox = new CheckBox("Tick if only a link to an existing entry should be created and paste its id below.");
	
	/** activated when linkOnlyCheckBox is checked */
	private LongBox idInput = new LongBox();
	
	/** readOnly when linkOnlyCheckBox is checked */
	private TextBox nameInput = new TextBox();
	private TextArea descriptionInput = new TextArea();
	private Grid entryTypeGrid = new Grid(2, 2);
	private RadioButton directoryInput = new RadioButton("isElement", "Directory");
	private Image directoryImage = new Image(Images.directory);
	private RadioButton elementInput = new RadioButton("isElement", "Element");
	private Image elementImage = new Image(Images.element);
	private EvWriteStoryForm storyInput = new EvWriteStoryForm(EvStyle.eAddFormWriteStoryTextInput);
	
	
	// handlers
	
	private ValueChangeHandler<Boolean> haLinkOnlyCheckBox = new ValueChangeHandler<Boolean>() {
		@Override
		public void onValueChange(ValueChangeEvent<Boolean> event) {
			if (true == event.getValue()) {
				idInput.setReadOnly(false);
				idInput.setFocus(true);
				
				clearEntryInputs();
				setEntryInputReadOnly(true);
				
			} else {
				idInput.setValue(null);
				idInput.setReadOnly(true);
				
				setEntryInputReadOnly(false);
				nameInput.setFocus(true);
				
			}
			
			clearErrorPanel();
		}
	};
	
	private ValueChangeHandler<Long> haIdInput = new ValueChangeHandler<Long>() {
		@Override
		public void onValueChange(ValueChangeEvent<Long> event) {
			checkIdInput();
		}
	};
	
	
	// constructors
	
	/**
	 * 
	 * @param classIdentifier
	 * @param obsClose the added item or null when the form is just canceled
	 */
	public EvAddEntryForm(EntryClassIdentifier classIdentifier, EvObserver<EvEntry<Ev, EntryIdentifier>> obsClose) {
		super(obsClose);
		
		assert null != classIdentifier;
		
		this.classIdentifier = classIdentifier;
		
		// configure widgets
		linkOnlyCheckBox.setValue(false);
		idInput.setReadOnly(true);
		
		// compose
		super.addToDialogTable("Link", linkOnlyCheckBox);
		super.addToDialogTable("Id", idInput);
		
		super.addToDialogTable("Name", nameInput);
//		super.addToDialogTable("Description", descriptionInput);
		
		entryTypeGrid.setWidget(0, 0, directoryInput);
		entryTypeGrid.setWidget(0, 1, directoryImage);
		entryTypeGrid.setWidget(1, 0, elementInput);
		entryTypeGrid.setWidget(1, 1, elementImage);
		super.addToDialogTable("Type", entryTypeGrid);
		
		super.addToDialogTable("Story", storyInput);
		
		// style
		nameInput.addStyleName(EvStyle.eAddFormNameInput);
		descriptionInput.addStyleName(EvStyle.eAddFormDescriptionInput);
		
		// handlers
		linkOnlyCheckBox.addValueChangeHandler(haLinkOnlyCheckBox);
		idInput.addValueChangeHandler(haIdInput);
		
		// timer that listens if idInput has changed
		new Timer() {
			@Override
			public void run() {
				checkIdInput();
			}
		}.scheduleRepeating(200);
	}
	
	
	// methods
	
	private void clearEntryInputs() {
		nameInput.setValue(null);
		descriptionInput.setValue(null);
		directoryInput.setValue(null);
		elementInput.setValue(null);
		storyInput.deleteTextInput();
	}
	
	private void setEntryInputReadOnly(boolean readOnly) {
		nameInput.setReadOnly(readOnly);
		descriptionInput.setReadOnly(readOnly);
		directoryInput.setEnabled(!readOnly);
		elementInput.setEnabled(!readOnly);
		storyInput.setReadOnly(readOnly);
	}
	
	@Override
	protected void confirm() {
		
		super.clearErrorPanel();
		
		if (!linkOnlyCheckBox.getValue()) {
			add();
		} else {
			link();
		}
	}
	
	private void add() {
		String name = nameInput.getValue();
		if (name.isEmpty()) {
			super.addToErrorPanel("Please input a name.");
			return;
		}
		
		String description = descriptionInput.getValue();
		
		boolean isElement;
		if (true == directoryInput.getValue()) {
			isElement = false;
		} else if (true == elementInput.getValue()) {
			isElement = true;
		} else {
			super.addToErrorPanel("Please choose a type.");
			return;
		}
		
		EvEntry<EvVoid, EntryClassIdentifier> entry = new EvEntry<EvVoid, EntryClassIdentifier>(
				new EvVoid(),
				classIdentifier,
				new WoString(name),
				new WoString(description),
				isElement,
				null	// author is set by server
				);
		
		// get sentences from textInput
		final String[] story = storyInput.getInput();
		if (0 == story.length) {
			// if no sentences where inputted
			super.addToErrorPanel("Please input at least one sentence.");
			return;
		}
		for (String sentence : story) {
			String errorString = EvSentence.getErrorString(sentence);
			if (null != errorString) {
				// if a sentence is not valid
				super.addToErrorPanel(errorString);
				return;
			}
		}
		
		// add entry
		EvApp.REQ.addEntry(entry, new EvAsyncCallback<EvEntry<Ev, EntryIdentifier>>() {
			@Override
			public void onSuccess(final EvEntry<Ev, EntryIdentifier> addedEntry) {
				
				// add sentences
				EntryClassIdentifier identifier = new EntryClassIdentifier(addedEntry.getIdentifier().getEntryId());
				ArrayList<EvSentence<EvVoid, EntryClassIdentifier>> sentences = new ArrayList<EvSentence<EvVoid, EntryClassIdentifier>>();
				for (String sentence : story) {
					EvSentence<EvVoid, EntryClassIdentifier> eSentence = new EvSentence<EvVoid, EntryClassIdentifier>(
							EvVoid.INST,
							identifier,
							new WoString(sentence)
							);
					sentences.add(eSentence);
				}
				EvApp.REQ.addSentences(sentences, null, null, new EvAsyncCallback<ArrayList<EvSentence<Ev, SentenceShortIdentifier>>>() {
					@Override
					public void onSuccess(
							ArrayList<EvSentence<Ev, SentenceShortIdentifier>> result) {
						getCloseObserver().call(addedEntry);
						
					}
					@Override
					public void onFailure(Throwable caught) {
						addToErrorPanel(caught.getMessage());
					}
				});
				
			}
			@Override
			public void onFailure(Throwable caught) {
				addToErrorPanel(caught.getMessage());
			}
		});
		
	}
	
	private void link() {
		if (null == sourceIdent) {
			// load sourceIdent first and call afterwards this method again
			loadEntryFromIdInput(true);
		} else {
			// link entry
			EvApp.REQ.linkEntry(sourceIdent, classIdentifier, new EvAsyncCallback<EvEntry<Ev, EntryIdentifier>>() {
				@Override
				public void onSuccess(EvEntry<Ev, EntryIdentifier> result) {
					getCloseObserver().call(result);
				}
				@Override
				public void onFailure(Throwable caught) {
					addToErrorPanel(caught.getMessage());
				}
			});
		}
	}
	
	/**
	 * checks if idInput has changed and loads entry if so
	 */
	private void checkIdInput() {
		if (null == idInput.getValue() || !idInput.getValue().equals(lastIdInput)) {
			lastIdInput = idInput.getValue();
			if (!idInput.isReadOnly()) {
				loadEntryFromIdInput(false);
			}
		}
	}
	
	/**
	 * loads the entry which id is given in idInput and sets sourceIdent
	 * @param submit true when afterwards method link() should be called otherwise false
	 */
	private void loadEntryFromIdInput(final boolean submit) {
		assert !idInput.isReadOnly();
		
		clearErrorPanel();
		
		sourceIdent = null;
		
		clearEntryInputs();
		
		if (null == idInput.getValue()) {
			addToErrorPanel("Please input an Id of an entry.");
		} else {
			EvApp.REQ.getEntry(new EntryShortIdentifier(idInput.getValue()), new EvAsyncCallback<EvEntry<Ev, EntryIdentifier>>() {
				@Override
				public void onSuccess(EvEntry<Ev, EntryIdentifier> result) {
					sourceIdent = result.getIdentifier();
					
//					idInput.setValue(result.getIdentifier().getEntryId());
					nameInput.setValue(result.getName().getString());
					descriptionInput.setValue(result.getDescriptionString());
					directoryInput.setValue(!result.isElement());
					elementInput.setValue(result.isElement());
					
					if (true == submit) {
						link();
					}
				}
				@Override
				public void onFailure(Throwable caught) {
					addToErrorPanel(caught.getMessage());
				}
			});
		}
	}
}
