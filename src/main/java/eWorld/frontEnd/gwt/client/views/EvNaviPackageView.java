package eWorld.frontEnd.gwt.client.views;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Label;

import eWorld.datatypes.data.EvEntry;
import eWorld.datatypes.elementars.WoString;
import eWorld.datatypes.evs.Ev;
import eWorld.datatypes.identifiers.EntryClassIdentifier;
import eWorld.datatypes.identifiers.EntryIdentifier;
import eWorld.datatypes.packages.EvPackage;
import eWorld.frontEnd.gwt.client.EvStyle;
import eWorld.frontEnd.gwt.client.util.EvDownWidget;
import eWorld.frontEnd.gwt.client.util.EvUpWidget;

public abstract class EvNaviPackageView extends EvView {

	/**
	 * 
	 * @return classIdentifier of the container that is currently displayed
	 */
	public abstract EntryClassIdentifier getCurrentClassIdent();
	
	/**
	 * 
	 * @return superClassIdentifier of the container that is currently displayed
	 */
	public abstract EntryClassIdentifier getCurrentSuperClassIdent();
	
	/**
	 * 
	 * @return the EvPackage that is displayed or null if not specified
	 */
	public abstract EvPackage getDataPackage();
	
	/**
	 * updates the widget and reloads all data from server
	 */
	public abstract void update();
	
	/**
	 * displays an Ev object (including things like placing, rating, votes)
	 * and also gives the opportunity to vote?
	 * @author michael
	 *
	 */
	protected class EvInfoView extends FlowPanel {
		
		private HorizontalPanel ePanel = new HorizontalPanel();
		
		private FlowPanel caption = new FlowPanel();
		private InlineLabel captionName;
		private InlineLabel captionId;
		private Label captionDescription;
		
		private FlowPanel rank = new FlowPanel();
		private Label rankCaption = new Label("Rank");
		private Label rankValue;
		
		private FlowPanel rating = new FlowPanel();
		private Label ratingCaption = new Label("Rating");
		private Label ratingValue;
		
		private EvDownWidget downVote;
		
		private FlowPanel downVotes = new FlowPanel();
//		private Label downVotesCaption = new Label("DownVotes");
		private Label downVotesValue;
		
		private EvUpWidget upVote;
		
		private FlowPanel upVotes = new FlowPanel();
//		private Label upVotesCaption = new Label("UpVotes");
		private Label upVotesValue;
		
		public EvInfoView() {
			
			// style
			caption.addStyleName(EvStyle.eNaviPackageViewCaptionPanel);
			rank.addStyleName(EvStyle.eNaviPackageViewInfoViewColumn);
			rating.addStyleName(EvStyle.eNaviPackageViewInfoViewColumn);
			downVotes.addStyleName(EvStyle.eNaviPackageViewInfoViewColumn);
			upVotes.addStyleName(EvStyle.eNaviPackageViewInfoViewColumn);
			addStyleName(EvStyle.eNaviPackageViewInfoView);
			
		}
		
		public void set(EvEntry<Ev, EntryIdentifier> entry) {
			assert null != entry;
			
			clear();
			
			setEv(entry.getEv());
			setCaption(entry.getName(), entry.getDescription(), entry.getIdentifier().getEntryId());
			
			ePanel.add(rank);
			ePanel.add(rating);
			ePanel.add(downVote);
			ePanel.add(downVotes);
			ePanel.add(upVote);
			ePanel.add(upVotes);
			
			add(caption);
			add(ePanel);
			
		}
		
		/**
		 * displays the given Ev
		 * @param e
		 */
		private void setEv(Ev e) {
			assert null != e;
			
			rankValue = new Label(e.getRankString());
			ratingValue = new Label(e.getRatingString());
			downVote = new EvDownWidget(e.getVote());
			downVotesValue = new Label(String.valueOf(e.getDownVotes()));
			downVotesValue.setTitle("DownVotes");
			upVote = new EvUpWidget(e.getVote());
			upVotesValue = new Label(String.valueOf(e.getUpVotes()));
			upVotesValue.setTitle("UpVotes");
			
			// style
			
			
			// compose TODO? do this in constructor?
			rank.clear();
			rating.clear();
			downVotes.clear();
			upVotes.clear();
			
			rank.add(rankCaption);
			rank.add(rankValue);
			
			rating.add(ratingCaption);
			rating.add(ratingValue);
			
//			downVotes.add(downVotesCaption);
			downVotes.add(downVotesValue);
			
//			upVotes.add(upVotesCaption);
			upVotes.add(upVotesValue);
		}
		
		private void setCaption(WoString name, WoString description, long id) {
			assert null != name;
			
			caption.clear();
			
			captionName = new InlineLabel(name.getString());
			captionName.addStyleName(EvStyle.eNaviPackageViewCaptionPanelName);
			caption.add(captionName);
			
			captionId = new InlineLabel(" id: " + id);
			caption.add(captionId);
			
			if (null != description) {
				captionDescription = new Label(description.getString());
				caption.add(captionDescription);
			}
		}
	}
}
