package eWorld.frontEnd.gwt.client.util;

import eWorld.datatypes.elementars.UpDownVote;
import eWorld.frontEnd.gwt.client.EvStyle;
import eWorld.frontEnd.gwt.client.Images;

public class EvDownWidget extends EvVoteWidget {

	public EvDownWidget(UpDownVote vote) {
		super(vote);
	}

	@Override
	public boolean isVoted(UpDownVote vote) {
		if (null == vote || UpDownVote.DOWN != vote) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	protected String getDefaultStyleName() {
		return EvStyle.eVoteWidgetDown;
	}
	
	@Override
	public String getDefaultImage() {
		return Images.down;
	}

	@Override
	public String getRequestedImage() {
		return Images.downRequested;
	}

	@Override
	public String getVotedImage() {
		return Images.downVoted;
	}
	
}
