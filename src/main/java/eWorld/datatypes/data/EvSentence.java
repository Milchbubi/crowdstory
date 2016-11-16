package eWorld.datatypes.data;

import java.io.Serializable;
import java.util.Date;

import eWorld.datatypes.elementars.WoString;
import eWorld.datatypes.evs.EvAbstract;
import eWorld.datatypes.exceptions.EvIllegalDataException;
import eWorld.datatypes.identifiers.EvIdentifier;
import eWorld.datatypes.identifiers.UserIdentifier;

@SuppressWarnings("serial")
public class EvSentence <EV extends EvAbstract, IDENT extends EvIdentifier> extends EvDataType<EV, IDENT> implements Serializable {

	// static finals
	
	public static final int MAX_SENTENCE_LENGTH = 200;
	
	public static final int MIN_SENTENCE_LENGTH = 20;
	
	
	// attributes
	
	/** the position of the sentence within a story, null for not specified */
	private Long position = null;
	
	private WoString sentence;
	
	
	// constructors
	
	/** default constructor for remote procedure call (RPC) */
	public EvSentence() {
	}
	
	/**
	 * primarily used by client
	 * @param ev
	 * @param identifier
	 */
	public EvSentence(EV ev, IDENT identifier, WoString sentence) {
		this(ev, identifier, null, null, null, sentence);
	}
	
	/**
	 * primarily used by server
	 * @param ev
	 * @param identifier
	 * @param position the position of the sentence within a story, can be null
	 * @param author can be null
	 * @param authorTime
	 * @param sentence
	 */
	public EvSentence(EV ev, IDENT identifier, Long position, UserIdentifier author, Date authorTime, WoString sentence) {
		super(ev, identifier, author, authorTime);
		
		assert null != sentence;
		
		this.position = position;
		this.sentence = sentence;
	}
	
	
	// methods
	
	public Long getPosition() {
		return position;
	}
	
	public void setPosition(Long position) {
		this.position = position;
	}
	
	public WoString getSentence() {
		return sentence;
	}
	
	/**
	 * checks the given String if it matches all requirements to be a valid sentence
	 * @param sentence the String to check
	 * @return a String that describes why the given String is not a valid sentence or null if it is a valid sentence
	 */
	public static String getErrorString(String sentence) {
		if (MAX_SENTENCE_LENGTH < sentence.length()) {
			return "the maximum length of a sentence is " + MAX_SENTENCE_LENGTH + " characters";
		} else if (MIN_SENTENCE_LENGTH > sentence.length()) {
			return "a sentence should have at least " + MIN_SENTENCE_LENGTH + " characters";
		} else {
			return null;
		}
	}
	
	/**
	 * checks if the attributes are valid
	 * @throws EvIllegalDataException if attributes are not valid
	 */
	public void check() throws EvIllegalDataException {
		String errorString = getErrorString(sentence.getString());
		if (null != errorString) {
			throw new EvIllegalDataException(errorString);
		}
	}
	
}
