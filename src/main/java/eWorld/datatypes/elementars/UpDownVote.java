package eWorld.datatypes.elementars;

import java.io.Serializable;

@SuppressWarnings("serial")
public enum UpDownVote implements Serializable {

	OPEN, DOWN, UP;
	
	
	// static methods
	
	public static String getDownVoteString() {
		return "\\/";
	}
	
	public static String getUpVoteString() {
		return "/\\";
	}
	
	/**
	 * @return downVoting in progress
	 */
	public static String getDownVotingString() {
		return ">\\/<";
	}
	
	/**
	 * @return upVoting in progress
	 */
	public static String getUpVotingString() {
		return ">/\\<";
	}
	
	public static String getDownVotedString() {
		return "[\\/]";
	}
	
	public static String getUpVotedString() {
		return "[/\\]";
	}
}
