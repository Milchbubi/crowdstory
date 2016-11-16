package eWorld.datatypes.evs;

import java.io.Serializable;

import eWorld.datatypes.elementars.UpDownVote;
import eWorld.datatypes.elementars.UpDownVotes;

@SuppressWarnings("serial")
public class Ev extends EvAbstract implements Serializable {

	// attributes

	private int rank;
	
	private float rating;
	
	/** vote of the user, null for not (loaded|specified) */
	private UpDownVote vote = null;
	
	/** votes of all users, null for not (loaded|specified) */
	private UpDownVotes summarizedVotes = null;
	
	
	// constructors

	/** default constructor for remote procedure call (RPC) */
	public Ev() {
	}
	
	public Ev(int rank, float rating) {
		this.rank = rank;
		this.rating = rating;
	}
	
	public Ev(int rank, float rating, UpDownVote vote, UpDownVotes summarizedVotes) {
		assert null != summarizedVotes;
		assert null != vote;
		
		this.rank = rank;
		this.rating = rating;
		this.vote = vote;
		this.summarizedVotes = summarizedVotes;
	}
	
	
	// methods

	public int getRank() {
		return rank;
	}
	public String getRankString() {
		return "#" + String.valueOf(getRank());
	}
	
	public float getRating() {
		return rating;
	}
	public String getRatingString() {
		if (0 < getRating()) {
			return "+" + String.valueOf(getRating());
		} else {
			return String.valueOf(getRating());
		}
	}
	
	public void setRating(float rating) {
		this.rating = rating;
	}
	
	/**
	 * @param vote vote of the user, null for not (loaded|specified)
	 */
	public void setVote(UpDownVote vote) {
		assert null != vote;
		
		this.vote = vote;
	}
	/**
	 * @return vote of the user, null for not (loaded|specified)
	 */
	public UpDownVote getVote() {
		return vote;
	}
	
	public void setSummarizedVotes(UpDownVotes summarizedVotes) {
		assert null != summarizedVotes;
		
		this.summarizedVotes = summarizedVotes;
	}
	
	public UpDownVotes getSummarizedVotes() {
		return summarizedVotes;
	}
	
	public long getDownVotes() {
		return summarizedVotes.getDownVotes();
	}
	
	public long getUpVotes() {
		return summarizedVotes.getUpVotes();
	}
	
}
