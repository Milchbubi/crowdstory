package eWorld.datatypes.elementars;

import java.io.Serializable;

@SuppressWarnings("serial")
public class UpDownVotes extends AbstractVotes implements Serializable {

	private long downVotes;
	
	private long upVotes;
	
	
	/** default constructor for remote procedure call (RPC) */
	public UpDownVotes() {
	}
	
	public UpDownVotes(long downVotes, long upVotes) {
		this.downVotes = downVotes;
		this.upVotes = upVotes;
	}
	
	
	public long getDownVotes() {
		return downVotes;
	}
	
	public long getUpVotes() {
		return upVotes;
	}
}
