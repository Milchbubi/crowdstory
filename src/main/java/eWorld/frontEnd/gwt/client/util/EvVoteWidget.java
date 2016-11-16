package eWorld.frontEnd.gwt.client.util;

import com.google.gwt.user.client.ui.HTMLTable.CellFormatter;
import com.google.gwt.user.client.ui.Image;

import eWorld.datatypes.elementars.UpDownVote;
import eWorld.frontEnd.gwt.client.EvStyle;

/**
 * 
 * @author michael
 * TODO image for the case, that vote is null (stands for vote is not (known|loaded))
 */
public abstract class EvVoteWidget extends Image {
	
	private enum Status {
		DEFAULT, REQUESTED, VOTED;
	}
	
	private Status status;
	
	
	public EvVoteWidget(UpDownVote vote) {
		if (!isVoted(vote)) {
			setDefault();
		} else {
			setVoted();
		}
	}
	
	
	public void setStyle() {
		addStyleName(getDefaultStyleName());
		if(Status.VOTED != status) {
			removeStyleName(EvStyle.eVoteWidgetVoted);
			addStyleName(EvStyle.eVoteWidgetNotVoted);
		} else {
			removeStyleName(EvStyle.eVoteWidgetNotVoted);
			addStyleName(EvStyle.eVoteWidgetVoted);
		}
	}
	
	/**
	 * can be used when widget is attached to table
	 * @param cellFormatter
	 * @param row
	 * @param col
	 */
	public void setStyleToCell(CellFormatter cellFormatter, int row, int col) {
		cellFormatter.addStyleName(row, col, getDefaultStyleName());
		if(Status.VOTED != status) {
			cellFormatter.removeStyleName(row, col, EvStyle.eVoteWidgetVoted);
			cellFormatter.addStyleName(row, col, EvStyle.eVoteWidgetNotVoted);
		} else {
			cellFormatter.removeStyleName(row, col, EvStyle.eVoteWidgetNotVoted);
			cellFormatter.addStyleName(row, col, EvStyle.eVoteWidgetVoted);
		}
	}
	
	public void setDefault() {
		status = Status.DEFAULT;
		setUrl(getDefaultImage());
	}
	
	public void setRequested() {
		status = Status.REQUESTED;
		setUrl(getRequestedImage());
	}
	
	public void setVoted() {
		status = Status.VOTED;
		setUrl(getVotedImage());
	}
	
	/**
	 * 
	 * @param vote
	 * @return true for voted
	 */
	protected abstract boolean isVoted(UpDownVote vote);
	
	protected abstract String getDefaultStyleName();
	
	protected abstract String getDefaultImage();
	
	protected abstract String getRequestedImage();
	
	protected abstract String getVotedImage();
}
