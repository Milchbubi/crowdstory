package eWorld.frontEnd.gwt.client.views.story;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.TextArea;

import eWorld.datatypes.data.EvSentence;
import eWorld.frontEnd.gwt.client.EvStyle;

public class EvWriteStoryForm extends FlowPanel {

	// static finals
	
	private static final String SENTENCE_DELIMITER = "<_>"/*"<._.>"*/;
	
	private static final String FULLSTOP = ". ";
	
	/** defines how often the input becomes examined */
	private static final int EXAMINE_INPUT_TIMER = 50;
	
	
	// attributes
	
	/** used by examineInput(), prevents SENTENCE_DELIMITER from resetting when it was already deleted from user before */
	private int examineInputLastDelimiterPos = 0;
	
	private Timer examineInputTimer = new Timer() {
		@Override
		public void run() {
			examineInput();
		}
	};
	
	
	// components
	
	private TextArea textInput = new TextArea();
	
	private InlineLabel textStatusLabel = new InlineLabel();
	
	
	// constructors
	
	public EvWriteStoryForm(String textInputStyleName) {
		
		// style
		textInput.addStyleName(EvStyle.eWriteStoryFormTextInput);
		textInput.addStyleName(textInputStyleName);
		
		// assemble
		add(textInput);
		add(textStatusLabel);
		
		// timers
		/*textInput.addValueChangeHandler(new ValueChangeHandler<String>() { fires only when textArea becomes deselected
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				examineInput();
			}
		});*/
		examineInputTimer.scheduleRepeating(EXAMINE_INPUT_TIMER);
	}
	
	
	// methods
	
	public void setExamineInputTimerActive(boolean active) {
		examineInputTimer.cancel();
		if (true == active) {
			examineInputTimer.scheduleRepeating(EXAMINE_INPUT_TIMER);
		}
	}
	
	public void focus() {
		textInput.setFocus(true);
	}
	
	/**
	 * 
	 * TODO not perfect, race conditions might happen if input changes while this method is running
	 */
	private void examineInput() {
		
		// get last sentence of input
		String text = textInput.getValue();
		
		int lastSentenceStart = text.lastIndexOf(SENTENCE_DELIMITER);
		if (0 > lastSentenceStart) {
			// there is only one sentence
			lastSentenceStart = 0;
		} else {
			// skip delimiter
			lastSentenceStart += SENTENCE_DELIMITER.length();
		}
		
		String lastSentence = text.substring(lastSentenceStart);
		
		// insert SENTENCE_DELIMITER if appropriate
		int delimiterPos = 0;
		while (true) {
			
			delimiterPos = lastSentence.indexOf(FULLSTOP, delimiterPos);
			if (delimiterPos < 0) {
				break;
			}
			delimiterPos += FULLSTOP.length();
			
			if (delimiterPos >= EvSentence.MAX_SENTENCE_LENGTH/4 && lastSentenceStart + delimiterPos > examineInputLastDelimiterPos) {
				// insert SENTENCE_DELIMITER
				String before = text.substring(0, lastSentenceStart + delimiterPos);
				String after = text.substring(lastSentenceStart + delimiterPos);
				textInput.setValue(before + SENTENCE_DELIMITER + after);
				examineInputLastDelimiterPos = lastSentenceStart + delimiterPos;
				break;
			}
			
		}
		
		// update textStatusLabel
		textStatusLabel.setText("current sentence: " + lastSentence.length() + "/" + EvSentence.MAX_SENTENCE_LENGTH);
		
	}
	
	public String[] getInput() {
		
		String text = textInput.getValue();
		
		// append space if text is not ending with a space
		if (!text.endsWith(" ")) {
			text += " ";
		}
		
		return text.split(SENTENCE_DELIMITER);
	}
	
	public void deleteTextInput() {
		textInput.setValue("");
	}
	
	public void setReadOnly(boolean readOnly) {
		textInput.setReadOnly(readOnly);
	}
	
}
