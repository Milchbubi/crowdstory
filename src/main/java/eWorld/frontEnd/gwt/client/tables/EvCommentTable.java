package eWorld.frontEnd.gwt.client.tables;

import java.util.ArrayList;
import java.util.Date;

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;

import eWorld.datatypes.containers.EvCommentContainer;
import eWorld.datatypes.data.EvComment;
import eWorld.datatypes.evs.Ev;
import eWorld.datatypes.identifiers.CommentIdentifier;
import eWorld.datatypes.identifiers.CommentShortIdentifier;
import eWorld.frontEnd.gwt.client.EvApp;
import eWorld.frontEnd.gwt.client.EvAsyncCallback;
import eWorld.frontEnd.gwt.client.EvStyle;
import eWorld.frontEnd.gwt.client.util.EvDownWidget;
import eWorld.frontEnd.gwt.client.util.EvRatingWidget;
import eWorld.frontEnd.gwt.client.util.EvUpWidget;

public class EvCommentTable extends FlexTable {

	// attributes
	
	/** the index of the column where some metaData is displayed (author, possibility to vote, etc.) */
	private final int colIndexMetaData = 0;
	
	/** the index of the column where the comments are displayed */
	private final int colIndexComment = 1;
	
	/** contains the comments that are viewed */
	private EvCommentContainer eCommentContainer = null;
	
	
	// handlers
	
	private ClickHandler voteDownHandler = new ClickHandler() {
		@Override
		public void onClick(ClickEvent event) {
			assert null != event;
			
			CommentWidget commentWidget = (CommentWidget)(((Widget)event.getSource()).getParent());
			
			assert null != commentWidget;
			if (null == commentWidget) {
				return;
			}
			
			vote(commentWidget, false);
		}
	};
	
	private ClickHandler voteUpHandler = new ClickHandler() {
		@Override
		public void onClick(ClickEvent event) {
			assert null != event;
			
			CommentWidget commentWidget = (CommentWidget)(((Widget)event.getSource()).getParent());
			
			assert null != commentWidget;
			if (null == commentWidget) {
				return;
			}
			
			vote(commentWidget, true);
		}
	};
	
	
	// constructors
	
	public EvCommentTable() {
		
		// style
//		addStyleName(EvStyle.eTable);
	}
	
	
	// methods
	
	public EvCommentContainer getCommentContainer() {
		return eCommentContainer;
	}
	
	/**
	 * @param eCommentContainer comments that should be displayed
	 * @param caption true if a caption should be set otherwise false
	 */
	public void setCommentContainer(EvCommentContainer eCommentContainer, boolean caption) {
		assert null != eCommentContainer;
		
		this.eCommentContainer = eCommentContainer;
		
		removeAllRows();
		
		ArrayList<EvComment<Ev, CommentShortIdentifier>> eComments = eCommentContainer.getData();
		
		
		if (true == caption) {
			setCaption();
		}
		
		if (eComments.isEmpty()) {
			// container is empty
			Label noCommentsLabel = new Label("there are no comments yet");
			noCommentsLabel.addStyleName(EvStyle.eTableNoItemsLabel);
			setWidget(1, 0, noCommentsLabel);
			
		} else {
			// container is not empty
//			for (int i = 0; i < eComments.size(); i++) {
			for (int i = eComments.size()-1, row = 1; i >= 0 ; i--, row++) {
				setItem(eComments.get(i), row);
			}
		}
	}
	
	private void setCaption() {
		setText(0, colIndexMetaData, "MetaData");
		setText(0, colIndexComment, "Comments");
		
		// style
		this.getRowFormatter().addStyleName(0, EvStyle.eTableHeader);
	}
	
	/**
	 * 
	 * @param item
	 * @param row 0 is reserved for caption
	 */
	private void setItem(EvComment<Ev, CommentShortIdentifier> item, int row) {
		
		setWidget(row, colIndexComment, new CommentWidget(item));
		
		// style
//		this.getRowFormatter().addStyleName(row, EvStyle.eTableRow);
	}
	
	/**
	 * adds the given item to the end of the table
	 * @param item
	 */
	public void addItem(EvComment<Ev, CommentShortIdentifier> item) {
		assert null != item;
		
		if (eCommentContainer.getData().isEmpty()) {
			this.clearCell(1, 0);	// delete noCommentsLabel
		}
		eCommentContainer.addItem(item);
		setItem(item, eCommentContainer.getData().size());	// row 0 is reserved for caption
	}
	
	private void vote(final CommentWidget commentWidget, final boolean vote) {
		assert null != commentWidget;
		
		CommentIdentifier identifier = new CommentIdentifier(getCommentContainer().getClassIdentifier(), commentWidget.getComment().getIdentifier());
		
		EvApp.REQ.voteComment(identifier, vote, new EvAsyncCallback<Void>() {
			@Override
			public void onSuccess(Void result) {
				commentWidget.setVoteUpdate(vote);
				int votes = EvApp.getUser().getVotes();
				commentWidget.incrementRating(vote ? votes : -votes);
			}
		});
		
		commentWidget.setVoteRequested(vote);
	}

	private class CommentWidget extends FlowPanel {
		
		private EvComment<Ev, CommentShortIdentifier> comment;
		
		private InlineLabel pseudonym;
		private InlineLabel datum;
		private Label text;
		private EvDownWidget downWidget;
		private EvUpWidget upWidget;
		private EvRatingWidget rating;
		
		public CommentWidget(EvComment<Ev, CommentShortIdentifier> comment) {
			assert null != comment;
			
			this.comment = comment;
			
			pseudonym = new InlineLabel(comment.getAuthorPseudonym());
			datum = new InlineLabel((DateTimeFormat.getFormat(("yyyy.MM.dd 'at' HH.mm")).format(new Date(comment.getIdentifier().getShortId()))));
			text = new Label(comment.getComment().getString());
			downWidget = new EvDownWidget(comment.getEv().getVote());
			upWidget = new EvUpWidget(comment.getEv().getVote());
			rating = new EvRatingWidget(comment.getEv().getRating());
			
			// style
			pseudonym.addStyleName(EvStyle.eCommentTablePseudonym);
			datum.addStyleName(EvStyle.eCommentTableDatum);
			text.addStyleName(EvStyle.eCommentTableText);
			downWidget.setStyle();
			downWidget.addStyleName(EvStyle.eCommentTableVoteWidget);
			upWidget.setStyle();
			upWidget.addStyleName(EvStyle.eCommentTableVoteWidget);
			rating.addStyleName(EvStyle.eCommentTableRating);
			
			// compose
			add(pseudonym);
			add(datum);
			add(text);
			add(downWidget);
			add(upWidget);
			add(rating);
			
			// handlers
			downWidget.addClickHandler(voteDownHandler);
			upWidget.addClickHandler(voteUpHandler);			
		}
		
		public void setVoteUpdate(boolean vote) {
			if (false == vote) {
				// downVoted
				downWidget.setVoted();
				upWidget.setDefault();
				
			} else {
				// upVoted
				downWidget.setDefault();
				upWidget.setVoted();
			}
			downWidget.setStyle();
			upWidget.setStyle();
		}
		
		public void setVoteRequested(boolean vote) {
			if (false == vote) {
				// downVoting
				downWidget.setRequested();
				
			} else {
				// upVoting
				upWidget.setRequested();
			}
		}
		
		/**
		 * 
		 * @param increment positive for increment, negative for decrement
		 */
		public void incrementRating(int increment) {
			rating.incrementRating(increment);
		}
		
		public EvComment<Ev, CommentShortIdentifier> getComment() {
			return comment;
		}
		
		public EvDownWidget getDownWidget() {
			return downWidget;
		}
		
		public EvUpWidget getUpWidget() {
			return upWidget;
		}
		
	}
}
