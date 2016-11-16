package eWorld.frontEnd.gwt.client.views;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.TextArea;

import eWorld.datatypes.containers.EvCommentContainer;
import eWorld.datatypes.data.EvComment;
import eWorld.datatypes.elementars.WoString;
import eWorld.datatypes.evs.Ev;
import eWorld.datatypes.evs.EvVoid;
import eWorld.datatypes.identifiers.CommentIdentifier;
import eWorld.datatypes.identifiers.EntryClassIdentifier;
import eWorld.frontEnd.gwt.client.EvApp;
import eWorld.frontEnd.gwt.client.EvAsyncCallback;
import eWorld.frontEnd.gwt.client.EvStyle;
import eWorld.frontEnd.gwt.client.Images;
import eWorld.frontEnd.gwt.client.tables.EvCommentTable;
import eWorld.frontEnd.gwt.client.util.EvButton;

public class EvCommentView extends EvView {
	
	// components
	
	private FlowPanel panel = new FlowPanel();
	
	private EvViewCaption eCaption = new EvViewCaption(new Image(Images.network_grey), "Comments");	// TODO change image
	
	/** displayes the comments */
	private EvCommentTable eCommentTable = new EvCommentTable();
	
	/** to write a comment */
	private TextArea commentArea = new TextArea();
	private EvButton commentButton = new EvButton("Answer");
	private InlineLabel commentError = new InlineLabel();
	
	// constructor
	
	public EvCommentView() {
		
		// compose
		panel.add(eCaption);
		panel.add(eCommentTable);
		panel.add(commentArea);
		panel.add(commentButton);
		panel.add(commentError);
		setWidget(panel);
		
		
		// style
		commentArea.addStyleName(EvStyle.eCommentViewCommentArea);
		commentButton.addStyleName(EvStyle.eCommentViewCommentButton);
		
		// handlers
		commentButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				commentButtonPressed();
			}
		});
		
	}
	
	
	// methods
	
	public void setContainer(EvCommentContainer eCommentContainer, String entryName) {
		assert null != eCommentTable;
		assert null != entryName;
		
		eCaption.setAbout(entryName);
		
		eCommentTable.setCommentContainer(eCommentContainer, false);
	}
	
	private void commentButtonPressed() {
		
		commentError.removeStyleName(EvStyle.eCommentViewCommentError);
		commentError.setText("sending...");
		
		EvComment<EvVoid, EntryClassIdentifier> comment = new EvComment<EvVoid, EntryClassIdentifier>(
				EvVoid.INST,
				eCommentTable.getCommentContainer().getClassIdentifier(),
				null, null, new WoString(commentArea.getValue())
				);
		
		EvApp.REQ.addComment(comment, new EvAsyncCallback<EvComment<Ev, CommentIdentifier>>() {
			@Override
			public void onFailure(Throwable caught) {
				super.onFailure(caught);
				commentError.addStyleName(EvStyle.eCommentViewCommentError);
				commentError.setText("Error: " + caught.getMessage());
			}
			@Override
			public void onSuccess(EvComment<Ev, CommentIdentifier> result) {
				commentError.setText("");
				eCommentTable.addItem(EvComment.shortCast(result));
				commentArea.setValue("");
			}
			
		});
		
	}
	
}
