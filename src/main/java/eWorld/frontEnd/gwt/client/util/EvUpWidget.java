package eWorld.frontEnd.gwt.client.util;

import eWorld.datatypes.elementars.UpDownVote;
import eWorld.frontEnd.gwt.client.EvStyle;
import eWorld.frontEnd.gwt.client.Images;

public class EvUpWidget extends EvVoteWidget {

	public EvUpWidget(UpDownVote vote) {
		super(vote);
	}

	@Override
	public boolean isVoted(UpDownVote vote) {
		if (null == vote || UpDownVote.UP != vote) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	protected String getDefaultStyleName() {
		return EvStyle.eVoteWidgetUp;
	}
	
	@Override
	public String getDefaultImage() {
		return Images.up;
	}

	@Override
	public String getRequestedImage() {
		return Images.upRequested;
	}

	@Override
	public String getVotedImage() {
		return Images.upVoted;
	}

}
