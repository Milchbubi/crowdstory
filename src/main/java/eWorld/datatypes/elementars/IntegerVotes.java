package eWorld.datatypes.elementars;

import java.io.Serializable;

/**
 * 
 * @author michael
 * TODO this class is not needed by front-end, move to another package?
 */
@SuppressWarnings("serial")
public class IntegerVotes extends AbstractVotes implements Serializable {

	private long sum;
	
	private long votes;
	
	
	/** default constructor for remote procedure call (RPC) */
	public IntegerVotes() {
	}
	
	public IntegerVotes(long sum, long votes) {
		this.sum = sum;
		this.votes = votes;
	}
	
	
	public long getSum() {
		return sum;
	}
	
	public long getVotes() {
		return votes;
	}
}
