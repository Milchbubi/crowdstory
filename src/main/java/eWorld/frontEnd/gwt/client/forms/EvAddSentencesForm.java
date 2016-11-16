package eWorld.frontEnd.gwt.client.forms;

import java.util.ArrayList;

import com.google.gwt.user.client.ui.LongBox;
import com.google.gwt.user.client.ui.TextArea;

import eWorld.datatypes.data.EvSentence;
import eWorld.datatypes.elementars.WoString;
import eWorld.datatypes.evs.Ev;
import eWorld.datatypes.evs.EvVoid;
import eWorld.datatypes.identifiers.EntryClassIdentifier;
import eWorld.datatypes.identifiers.SentenceShortIdentifier;
import eWorld.frontEnd.gwt.client.EvApp;
import eWorld.frontEnd.gwt.client.EvAsyncCallback;
import eWorld.frontEnd.gwt.client.EvObserver;

@Deprecated
public class EvAddSentencesForm extends EvAddForm<ArrayList<EvSentence<Ev, SentenceShortIdentifier>>> {

	// attributes
	
	private final EntryClassIdentifier entryIdentifier;
	
	
	// components
	
	private LongBox afterInput = new LongBox();
	private LongBox beforeInput = new LongBox();
	private TextArea textInput = new TextArea();
	
	
	// constructors
	
	/**
	 * 
	 * @param entryIdentifier identifies the entry where sentences should be added
	 * @param obsClose the added item or null when the form is just canceled
	 */
	public EvAddSentencesForm(EntryClassIdentifier entryIdentifier, EvObserver<ArrayList<EvSentence<Ev, SentenceShortIdentifier>>> obsClose) {
		super(obsClose);
		
		assert null != entryIdentifier;
		
		this.entryIdentifier = entryIdentifier;
		
		// compose
		super.addToDialogTable("After Position", afterInput);
		super.addToDialogTable("Before Position", beforeInput);
		super.addToDialogTable("Text", textInput);
	}

	
	// methods
	
	@Override
	protected void confirm() {
		
		super.clearErrorPanel();
		
		Long after = afterInput.getValue();
		Long before = beforeInput.getValue();
		String text = textInput.getValue();
		
		ArrayList<EvSentence<EvVoid, EntryClassIdentifier>> sentences = new ArrayList<EvSentence<EvVoid, EntryClassIdentifier>>();
		
		EvSentence<EvVoid, EntryClassIdentifier> sentence = new EvSentence<EvVoid, EntryClassIdentifier>(
				EvVoid.INST,
				entryIdentifier,
				new WoString(text)
				);
		sentences.add(sentence);
		
		EvApp.REQ.addSentences(sentences, after, before, new EvAsyncCallback<ArrayList<EvSentence<Ev, SentenceShortIdentifier>>>() {
			@Override
			public void onSuccess(
					ArrayList<EvSentence<Ev, SentenceShortIdentifier>> result) {
				getCloseObserver().call(result);
			}
		});
		
	}

}
