package eWorld.frontEnd.gwt.client.views.story;

import java.util.ArrayList;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Label;
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
import eWorld.frontEnd.gwt.client.EvStyle;
import eWorld.frontEnd.gwt.client.Images;
import eWorld.frontEnd.gwt.client.util.EvButton;
import eWorld.frontEnd.gwt.client.util.EvImageButton;

public class EvInsertSentencePanel extends FlowPanel {
	
	// static finals
	
	
	// attributes
	
	private final EntryClassIdentifier entry;
	
	private final EvObserver<ArrayList<EvSentence<Ev, SentenceShortIdentifier>>> obsClosed;
	
	/** set by setPositions(Long afterPosition, Long beforePosition) */
	private Long afterPosition = null;
	private Long beforePosition = null;
	
	
	// components
	
	private EvWriteStoryForm writeStoryForm = new EvWriteStoryForm(EvStyle.eInsertSentencePanelTextInput);
	
	/** contains closeButton and sendButton */
	private FlowPanel buttonPanel = new FlowPanel();
	
	/** button to close or hide widget */
	private EvButton cancelButton = new EvButton("cancel");
	
	/** button to add sentences */
	private EvButton sendButton = new EvButton("send");
	
	
	// constructors
	
	/**
	 * 
	 * @param entry specifies the entry where the sentences should be added
	 * @param obsClosed called when widget becomes closed, provides the added sentences or null if inserting was canceled
	 * IMPORTANT call method setPositions(Long afterPosition, Long beforePosition) to set positions
	 */
	public EvInsertSentencePanel(EntryClassIdentifier entry, EvObserver<ArrayList<EvSentence<Ev, SentenceShortIdentifier>>> obsClosed) {
		assert null != entry;
		assert null != obsClosed;
		
		this.entry = entry;
		this.obsClosed = obsClosed;
		
		// style
		addStyleName(EvStyle.eInsertSentencePanel);
		buttonPanel.addStyleName(EvStyle.eInsertSentencePanelButtonPanel);
		cancelButton.addStyleName(EvStyle.eInsertSentencePanelCloseButton);
		sendButton.addStyleName(EvStyle.eInsertSentencePanelSendButton);
		
		// compose
		buttonPanel.add(cancelButton);
		buttonPanel.add(sendButton);
		
		add(writeStoryForm);
		add(buttonPanel);
		
		// handlers
		cancelButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				cancel();
			}
		});
		sendButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				confirm();
			}
		});
		
	}
	
	
	// methods
	
	/**
	 * 
	 * @param afterPosition null if there is no sentence after
	 * @param beforePosition null if there is no sentence before
	 */
	public void setPositions(Long afterPosition, Long beforePosition) {
		this.afterPosition = afterPosition;
		this.beforePosition = beforePosition;
	}
	
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		
		// activate timer if visible, disable if not
		writeStoryForm.setExamineInputTimerActive(visible);
		
		// adapt position if widget would be out of window
		this.getElement().getStyle().clearMarginLeft();	// use margin from css first (if there is an old adaption)
		
		int clientWidth = Window.getClientWidth();
		int width = this.getOffsetWidth();
		
		int left = this.getAbsoluteLeft();
		int right = left + width;
		
		if (0 > left) {
			this.getElement().getStyle().setMarginLeft(-(width/2) - left, Unit.PX);
		}
		if (clientWidth < right) {
			this.getElement().getStyle().setMarginLeft(clientWidth - right - (width/2), Unit.PX);
		}
		
	}
	
	public void focus() {
		writeStoryForm.focus();
	}
	
	public EntryClassIdentifier getEntry() {
		return entry;
	}
	
	public Long getAfterPosition() {
		return afterPosition;
	}
	
	public Long getBeforePosition() {
		return beforePosition;
	}
	
	private void confirm() {
		
		ArrayList<EvSentence<EvVoid, EntryClassIdentifier>> sentences = new ArrayList<EvSentence<EvVoid, EntryClassIdentifier>>();
		
		// get sentences from textInput
		for (String sentence : writeStoryForm.getInput()) {
			sentences.add(new EvSentence<EvVoid, EntryClassIdentifier>(
					EvVoid.INST,
					entry,
					new WoString(sentence)
					));
		}
		
		// send sentences to server
		EvApp.REQ.addSentences(sentences, afterPosition, beforePosition, new EvAsyncCallback<ArrayList<EvSentence<Ev, SentenceShortIdentifier>>>() {
			@Override
			public void onSuccess(
					ArrayList<EvSentence<Ev, SentenceShortIdentifier>> result) {
				writeStoryForm.deleteTextInput();
				setVisible(false);
				obsClosed.call(result);
			}
		});
		
	}
	
	private void cancel() {
		setVisible(false);
		obsClosed.call(null);
	}
	
}
