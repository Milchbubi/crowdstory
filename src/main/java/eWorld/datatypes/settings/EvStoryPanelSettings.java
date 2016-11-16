package eWorld.datatypes.settings;

import java.io.Serializable;
import java.util.Date;

import eWorld.datatypes.identifiers.UserIdentifier;
import eWorld.frontEnd.gwt.client.EvApp;

@SuppressWarnings("serial")
public class EvStoryPanelSettings implements Serializable {

	// attributes
	
	/** true when insertButtons are visible */
	private boolean insertsVisible = false;
	
	/** true when sentenceStatViews are visible */
	private boolean statsVisible = false;
	
	/** true when textSize is dependent on rating */
	private boolean textSizeVarying = true;
	
	/** the minimum rating content must have to be displayed */
	private float minRating = 0;
	
	/** true when only sentences are displayed that are upvoted by the specified user */
	private boolean onlyMyUpvoted = false;
	
	/** true when sentences that are upvoted by the specified user should be highlighted */
	private boolean highlightMyUpvoted = true;
	
	/** true when only sentences are displayed that are not downvoted by the specified user */
	private boolean notMyDownvoted = false;
	
	/** true when sentences that are downvoted by the specified user should be highlighted */
	private boolean highlightMyDownvoted = true;
	
	/** null if not restricted */
	private UserIdentifier onlyAuthor = EvApp.getUserIdentifier();
	
	/** true if contributions of the Author specified by onlyAuthor should be highlighted */
	private boolean highlightAuthor = true;
	
	/** display only content that is newer than this timestamp, null if not restricted */
	private Long newSince = (new Date().getTime() / (1000*60*60*24)) * (1000*60*60*24);	// the beginning of the current day is standard
	
	/** true if content specified by newSince should be highlighted */
	private boolean highlightNew = true;
	
	
	// constructors
	
	/** default constructor for remote procedure call (RPC) */
	public EvStoryPanelSettings() {
	}
	
	public EvStoryPanelSettings(boolean insertsVisible, boolean statsVisible, boolean textSizeVarying) {
		this.insertsVisible = insertsVisible;
		this.statsVisible = statsVisible;
		this.textSizeVarying = textSizeVarying;
	}
	
	
	// methods
	
	public boolean isInsertsVisible() {
		return insertsVisible;
	}
	public void setInsertsVisible(boolean insertsVisible) {
		this.insertsVisible = insertsVisible;
	}
	
	public boolean isStatsVisible() {
		return statsVisible;
	}
	public void setStatsVisible(boolean statsVisible) {
		this.statsVisible = statsVisible;
	}
	
	public boolean isTextSizeVarying() {
		return textSizeVarying;
	}
	public void setTextSizeVarying(boolean textSizeVarying) {
		this.textSizeVarying = textSizeVarying;
	}
	
	public float getMinRating() {
		return minRating;
	}
	public void setMinRating(float minRating) {
		this.minRating = minRating;
	}
	
	public boolean isOnlyMyUpvoted() {
		return onlyMyUpvoted;
	}
	public void setOnlyMyUpvoted(boolean onlyMyUpvoted) {
		this.onlyMyUpvoted = onlyMyUpvoted;
	}
	
	public boolean isHighlightMyUpvoted() {
		return highlightMyUpvoted;
	}
	public void setHighlightMyUpvoted(boolean highlightMyUpvoted) {
		this.highlightMyUpvoted = highlightMyUpvoted;
	}
	
	public boolean isNotMyDownvoted() {
		return notMyDownvoted;
	}
	public void setNotMyDownvoted(boolean notMyDownvoted) {
		this.notMyDownvoted = notMyDownvoted;
	}
	
	public boolean isHiglightMyDownvoted() {
		return highlightMyDownvoted;
	}
	public void setHiglightMyDownvoted(boolean highlightMyDownvoted) {
		this.highlightMyDownvoted = highlightMyDownvoted;
	}
	
	public UserIdentifier getOnlyAuthor() {
		return onlyAuthor;
	}
	/**
	 * @param onlyAuthor null if not restricted
	 */
	public void setOnlyAuthor(UserIdentifier onlyAuthor) {
		this.onlyAuthor = onlyAuthor;
	}
	
	public boolean isHighlightAuthor() {
		return highlightAuthor;
	}
	public void setHighlightAuthor(boolean highlightAuthor) {
		this.highlightAuthor = highlightAuthor;
	}
	
	public boolean isHighlightMyDownvoted() {
		return highlightMyDownvoted;
	}
	public void setHighlightMyDownvoted(boolean highlightMyDownvoted) {
		this.highlightMyDownvoted = highlightMyDownvoted;
	}
	
	public Long getNewSince() {
		return newSince;
	}
	/**
	 * @param newSince null if not restricted
	 */
	public void setNewSince(Long newSince) {
		this.newSince = newSince;
	}
	
	public boolean isHighlightNew() {
		return highlightNew;
	}
	public void setHighlightNew(boolean highlightNew) {
		this.highlightNew = highlightNew;
	}
	
}
