package eWorld.datatypes.elementars;

import java.io.Serializable;

@SuppressWarnings("serial")
public class IntegerVote implements Serializable {
	
	/** null when specified user has not yet voted */
	private Integer vote;
	
	
	/** default constructor for remote procedure call (RPC) */
	public IntegerVote() {
	}
	
	/**
	 * @param vote null when specified user has not yet voted
	 */
	public IntegerVote(Integer vote) {
		this.vote = vote;
	}
	
	/**
	 * @return null when specified user has not yet voted
	 */
	public Integer getVote() {
		return vote;
	}
}
