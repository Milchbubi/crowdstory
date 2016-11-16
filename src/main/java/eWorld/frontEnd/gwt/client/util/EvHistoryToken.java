package eWorld.frontEnd.gwt.client.util;

/**
 * used for object-oriented access to the browsers history stack
 * @author michael
 *
 */
public class EvHistoryToken {

	private static final String CLASS_ID = "classId";
	private static final String ID = "id";
	private static final String DIR = "dir";
	
	private static final String ASSIGNMENT = "=";
	private static final String DELIMITER = ";";
	
	private Long classId = null;
	private Long id = null;
	private Boolean dir = null;
	
	public EvHistoryToken(String historyToken) {
		
		try {
			this.classId = Long.valueOf(getArgumentValueFromHistoryToken(historyToken, CLASS_ID));
		} catch (NumberFormatException e) {
			this.classId = null;
		}
		try {
			this.id = Long.valueOf(getArgumentValueFromHistoryToken(historyToken, ID));
		} catch (NumberFormatException e) {
			this.id = null;
		}
		try {
			this.dir = Boolean.valueOf(getArgumentValueFromHistoryToken(historyToken, DIR));
		} catch (NumberFormatException e) {
			this.dir = null;
		}
	}
	
	public EvHistoryToken(Long classId, Long id) {
		this.classId = classId;
		this.id = id;
	}
	
	private String getArgumentValueFromHistoryToken(String historyToken, String argument) {
		
		int argumentStart = historyToken.indexOf(argument);
		if (0 > argumentStart) {
			// historyToken does not contain given argument
			return null;
		}
		int argumentValueStart = argumentStart + argument.length() + ASSIGNMENT.length();
		
		int argumentValueLength = historyToken.substring(argumentValueStart).indexOf(DELIMITER);
		if (0 > argumentValueLength) {
			// delimiter does not exist
			return historyToken.substring(argumentValueStart);
		} else {
			// delimiter does exist
			return historyToken.substring(argumentValueStart, argumentValueStart + argumentValueLength);
		}
	}
	
	public Long getClassId() {
		return classId;
	}
	
	public Long getId() {
		return id;
	}
	
	/**
	 * dir for directory
	 * @return
	 */
	public Boolean getDir() {
		return dir;
	}
	
	public String toHistoryToken() {
		String token = new String();
		
		if (null != classId) {
			token += CLASS_ID + ASSIGNMENT + classId + DELIMITER;
		}
		if (null != id) {
			token += ID + ASSIGNMENT + id + DELIMITER;
		}
		if (null != dir) {
			token += DIR + ASSIGNMENT + dir + DELIMITER;
		}
		
		return token;
	}
	
}
