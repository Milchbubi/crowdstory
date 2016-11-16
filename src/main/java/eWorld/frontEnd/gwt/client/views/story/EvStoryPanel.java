package eWorld.frontEnd.gwt.client.views.story;

import java.util.ArrayList;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.WidgetCollection;

import eWorld.datatypes.containers.EvStory;
import eWorld.datatypes.data.EvSentence;
import eWorld.datatypes.elementars.UpDownVote;
import eWorld.datatypes.evs.Ev;
import eWorld.datatypes.identifiers.EntryClassIdentifier;
import eWorld.datatypes.identifiers.SentenceIdentifier;
import eWorld.datatypes.identifiers.SentenceShortIdentifier;
import eWorld.datatypes.settings.EvStoryPanelSettings;
import eWorld.frontEnd.gwt.client.EvApp;
import eWorld.frontEnd.gwt.client.EvAsyncCallback;
import eWorld.frontEnd.gwt.client.EvObserver;
import eWorld.frontEnd.gwt.client.EvStyle;
import eWorld.frontEnd.gwt.client.util.EvDownWidget;
import eWorld.frontEnd.gwt.client.util.EvUpWidget;
import eWorld.frontEnd.gwt.client.util.EvVoteWidget;

public class EvStoryPanel extends FlowPanel {
	
	private EvStoryPanelSettings settings;
	
	/** set by setStory(..) */
	private EvStory story = null;
	
	/** references all widgets of all sentences that are currently displayed */
	private ArrayList<EvSentenceHandler> sentenceHandlers = new ArrayList<EvSentenceHandler>();
	
	/** references the currently selected sentence */
	private EvSentenceHandler currentlySelectedSentence = null;
	
	/** the insertSentenceButton that belongs to insertPanel, null if insertPanel is not visible */
	private EvInsertSentenceButton currentlyClickedInsertSentenceButton = null;
	
	/** set by setStory(..) */
	private EvInsertSentencePanel insertPanel = null;
	
	/** set by setStory(..) */
	private Float averageRating = null;
	private Float minimumRating = Float.MAX_VALUE;
	private Float maximumRating = Float.MIN_VALUE;
	
	
	// handlers
	
	private MouseOverHandler haMouseOver = new MouseOverHandler() {
		@Override
		public void onMouseOver(MouseOverEvent event) {
			Widget widget = (Widget)event.getSource();
			if (widget instanceof HasEvSentenceHandler) {
				EvSentenceHandler sentenceHandler = ((HasEvSentenceHandler)widget).getSentenceHandler();
				selectSentence(sentenceHandler);
			}
			
		}
	};
	
	private ClickHandler haClick = new ClickHandler() {
		@Override
		public void onClick(ClickEvent event) {
			Widget widget = (Widget)event.getSource();
			if (widget instanceof HasEvSentenceHandler) {
				EvSentenceHandler sentenceHandler = ((HasEvSentenceHandler)widget).getSentenceHandler();
				selectSentence(sentenceHandler);
			}
			
		}
	};
	
	private ClickHandler haClickHiddenSentencesView = new ClickHandler() {
		@Override
		public void onClick(ClickEvent event) {
			expandHiddenSentencesView((EvHiddenSentencesView)event.getSource());
		}
	};
	
	
	// observers
	
	/** called when insertPanel becomes closed */
	private EvObserver<ArrayList<EvSentence<Ev, SentenceShortIdentifier>>> obsInsertPanelClosed = new EvObserver<ArrayList<EvSentence<Ev, SentenceShortIdentifier>>>() {
		@Override
		public void call(ArrayList<EvSentence<Ev, SentenceShortIdentifier>> value) {
			
			// if sentences where added
			if (value != null) {
				story.addAll(value);
				setStory(story);
			}
			
			// update button that belongs to the insertPanel
			assert null != currentlyClickedInsertSentenceButton;
			if (null != currentlyClickedInsertSentenceButton) {
				
				// remove z-index
				currentlyClickedInsertSentenceButton.removeStyleName(EvStyle.eStoryPanelInsertSentenceButtonClicked);
				
				// hide button if no sentence around is selected
				if (null == currentlySelectedSentence ||
						(currentlySelectedSentence.getInsertButtonBefore() != currentlyClickedInsertSentenceButton &&
						currentlySelectedSentence.getInsertButtonAfter() != currentlyClickedInsertSentenceButton)
						) {
					currentlyClickedInsertSentenceButton.setVisible(settings.isInsertsVisible());
				}
				
				// no insertSentenceButton counts as clicked now
				currentlyClickedInsertSentenceButton = null;
			}
			
		}
	};
	
	
	// constructors
	
	/**
	 * 
	 * @param insertButtonsVisible true when insertButtons are visible
	 * @param statViewsVisible true when sentenceStatViews are visible
	 * @param settings specifies how things should be displayed
	 */
	public EvStoryPanel(EvStoryPanelSettings settings) {
		
		this.settings = settings;
		
		// style
		addStyleName(EvStyle.eStoryPanel);
		
		// handler to make widgets in the panel clickable TODO not efficient implemented
		addDomHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				
				// get element of clicked widgets
				Element element = Element.as(event.getNativeEvent().getEventTarget());
				
				// get all widgets that can be clicked
				ArrayList<WidgetCollection> widgetCollectionCollection = new ArrayList<WidgetCollection>();
				widgetCollectionCollection.add(getChildren());
				for (Widget widget : widgetCollectionCollection.get(0)) {
					if (widget instanceof EvSentenceStatView) {
						widgetCollectionCollection.add(((EvSentenceStatView)widget).getWidgets());
					}
				}
				
				// find widget that matches clicked element
				for (WidgetCollection widgets : widgetCollectionCollection) for (Widget widget : widgets) {
					if (widget.getElement().equals(element)) {
						
						if (widget instanceof EvInsertSentenceButton) {
							
							// cast to EvInsertSentenceButton
							EvInsertSentenceButton button = ((EvInsertSentenceButton)widget);
							
							// set insertPanel
							insertPanel.setPositions(button.getAfterPosition(), button.getBeforePosition());
							insert(insertPanel, getWidgetIndex(widget));
							insertPanel.setVisible(true);
							insertPanel.focus();
							
							// set z-index of button
							button.addStyleName(EvStyle.eStoryPanelInsertSentenceButtonClicked);
							currentlyClickedInsertSentenceButton = button;
							
						} else if (widget instanceof EvVoteWidget) {
							
							// get sentenceStatView of clicked voteWidget
							final EvSentenceStatView statView = (EvSentenceStatView)widget.getParent();
							
							// get arguments for vote request
							SentenceIdentifier completeIdentifier = new SentenceIdentifier(story.getClassIdentifier(), statView.getSentenceHandler().getSentence().getIdentifier());
							final boolean vote = widget instanceof EvDownWidget ? false : true;
							
							// vote
							EvApp.REQ.voteSentence(completeIdentifier, vote, new EvAsyncCallback<Void>() {
								@Override
								public void onSuccess(Void result) {
									statView.setVoteUpdate(vote);
									int votes = EvApp.getUser().getVotes();
									statView.incrementRating(vote ? votes : -votes);
								}
							});
							
							statView.setVoteRequested(vote);
						}
						
						break;
					}
				}
				
			}
			
		}, ClickEvent.getType());
		
	}
	
	
	// methods
	
	/**
	 * can be used to access the settings, after settings have changed setAll() should be called
	 * @return
	 */
	public EvStoryPanelSettings getSettings() {
		return settings;
	}
	
	/**
	 * sets the given story and calls setAll()
	 * @param story the story to render
	 */
	public void setStory(EvStory story) {
		assert null != story;
		
		this.story = story;
		
		setAll();
	}
	
	/**
	 * renders the story set by setStory(..) in consideration of the current setting
	 * this method should be called after settings have changed
	 */
	public void setAll() {
		
		// clear old
		clear();
		sentenceHandlers.clear();
		
		// set new
		if (null == insertPanel || insertPanel.getEntry().getEntryClassId() != story.getClassIdentifier().getEntryClassId()) {
			// create new insertPanel if there is none or old insertPanel is from another story
			insertPanel = new EvInsertSentencePanel(story.getClassIdentifier(), obsInsertPanelClosed);
		}
		
		// calculate averageRating, minimumRating, maximumRating
		minimumRating = Float.MAX_VALUE;
		maximumRating = Float.MIN_VALUE;
		float ratingSum = 0;
		for (EvSentence<Ev, SentenceShortIdentifier> sentence : story.getData()) {
			float rating = sentence.getEv().getRating();
			if (rating < minimumRating) {
				minimumRating = rating;
			}
			if (rating > maximumRating) {
				maximumRating = rating;
			}
			ratingSum += rating;
		}
		if (!story.getData().isEmpty()) {
			averageRating = ratingSum / story.getData().size();
		} else {
			averageRating = null;
		}
		
		// variables for caching
		EvInsertSentenceButton insertButtonBefore = null;
		EvInsertSentenceButton insertButtonAfter = null;
		EvHiddenSentencesView currentHiddenSentencesView = null;
		
		// iterate over all sentences
		for (int i = 0; i < story.getData().size(); i++) {
			EvSentence<Ev, SentenceShortIdentifier> sentence = story.getData().get(i);
			insertButtonBefore = insertButtonAfter;
			
			if (isSentenceFiltered(sentence)) {
				// sentence should  be hidden
				if (null == currentHiddenSentencesView) {
					currentHiddenSentencesView = constructHiddenSentencesView();
					if (null == insertButtonBefore) {
						insertButtonBefore = constructInsertSentenceButton(story.getData(), i-1);
					}
					currentHiddenSentencesView.setInsertButtonBefore(insertButtonBefore);
					add(insertButtonBefore);
					insertButtonAfter = null;
					add(currentHiddenSentencesView);
				}
				currentHiddenSentencesView.addSentence(sentence);
				
			} else {
				// sentence should be visible
				
				EvSentenceHandler sentenceHandler = new EvSentenceHandler(sentence);
				sentenceHandlers.add(sentenceHandler);
				
				// insertButton
				if (null == insertButtonBefore) {
					insertButtonBefore = constructInsertSentenceButton(story.getData(), i-1);
				}
				if (null != currentHiddenSentencesView) {
					// finish currentHiddenSentenceView if there is one
					currentHiddenSentencesView.setInsertButtonAfter(insertButtonBefore);
					currentHiddenSentencesView.updateLabel();
					currentHiddenSentencesView = null;
				}
				add(insertButtonBefore);
				insertButtonAfter = constructInsertSentenceButton(story.getData(), i);
				
				// statView
				EvSentenceStatView sentenceStatView = constructSentenceStatView(sentenceHandler);
				add(sentenceStatView);
				
				// sentenceHeader, display: inline-block allows to set margin, but trims normal space-character
				InlineHTML sentenceHeader = constructSentenceHeader();
				add(sentenceHeader);
				
				// sentence itself
				EvSentenceView sentenceView = constructSentenceView(sentence, sentenceHandler);
				add(sentenceView);
				
				// initialize sentenceHandler
				sentenceHandler.init(insertButtonBefore, insertButtonAfter, sentenceStatView, sentenceHeader, sentenceView);
				
			}
		}
		
		// add insertButton on the end of the story
		if (null == insertButtonAfter) {
			insertButtonAfter = constructInsertSentenceButton(story.getData(), story.getData().size()-1);
		}
		add(insertButtonAfter);
		
		// finish currentHiddenSentenceView if there is one
		if (null != currentHiddenSentencesView) {
			currentHiddenSentencesView.setInsertButtonAfter(insertButtonAfter);
			currentHiddenSentencesView.updateLabel();
			currentHiddenSentencesView = null;
		}
		
		// set visibility of buttons
		setTextSizeVarying();
		setInsertButtonsVisible();
		setStatViewsVisible();
		
	}
	
	private void expandHiddenSentencesView(EvHiddenSentencesView hiddenSentencesView) {
		
		EvInsertSentenceButton insertButtonBefore = null;
		EvInsertSentenceButton insertButtonAfter = hiddenSentencesView.getInsertButtonBefore();
		int insertIndex = this.getWidgetIndex(hiddenSentencesView);
		
		// iterate over all hidden sentences and insert them in to the panel
		for (int i = 0; i < hiddenSentencesView.getHiddenSentences().size(); i++) {
			EvSentence<Ev, SentenceShortIdentifier> sentence = hiddenSentencesView.getHiddenSentences().get(i);
			
			EvSentenceHandler sentenceHandler = new EvSentenceHandler(sentence);
			sentenceHandlers.add(sentenceHandler);
			
			// insertButton
			insertButtonBefore = insertButtonAfter;
			if (i > 0) {
				insert(insertButtonBefore, insertIndex++);
			}
			if (i == hiddenSentencesView.getHiddenSentences().size()-1) {
				insertButtonAfter = hiddenSentencesView.getInsertButtonAfter();
			} else {
				insertButtonAfter = constructInsertSentenceButton(hiddenSentencesView.getHiddenSentences(), i);
			}
			
			// statView
			EvSentenceStatView sentenceStatView = constructSentenceStatView(sentenceHandler);
			insert(sentenceStatView, insertIndex++);
			
			// sentenceHeader, display: inline-block allows to set margin, but trims normal space-character
			InlineHTML sentenceHeader = constructSentenceHeader();
			insert(sentenceHeader, insertIndex++);
			
			// sentence itself
			EvSentenceView sentenceView = constructSentenceView(sentence, sentenceHandler);
			sentenceView.addStyleName(EvStyle.eStoryPanelSentenceViewFiltered);
			insert(sentenceView, insertIndex++);
			
			// initialize sentenceHandler
			sentenceHandler.init(insertButtonBefore, insertButtonAfter, sentenceStatView, sentenceHeader, sentenceView);
		}
		
		// remove hiddenSentencesView (should be now on incremented index)
		remove(insertIndex);
		
		// set visibility of buttons
		setTextSizeVarying();
		setInsertButtonsVisible();
		setStatViewsVisible();
	}
	
	private float ratingToTextSize(float rating) {
		
		float minimumTextSize = 12;
		float averageTextSize = 18;
		float maximumTextSize = 24;
		
		if (rating < averageRating) {
			// worse than average
			float relativeRating = (rating-minimumRating) / (averageRating-minimumRating+1);	// 0.0 minimum, 1.0 average
			return minimumTextSize + relativeRating*(averageTextSize-minimumTextSize);
		} else {
			// better or equal average
			float relativeRating = (rating-averageRating) / (maximumRating-averageRating+1);	// 0.0 average, 1.0 maximum
			return averageTextSize + relativeRating*(maximumTextSize-averageTextSize);
		}
		
		/*if (rating > 0) {
			return 12 + (float)( Math.signum(rating) * Math.sqrt(Math.abs(rating)) );
//			return 30 - 15 / (rating/10);
		} else if (rating < 0) {
			return 12 + (float)( Math.signum(rating) * Math.sqrt(Math.sqrt(Math.abs(rating))) );
//			return 5 + 5 / Math.abs(rating/10);
		} else {
			return 12;
		}*/
		
	}
	
	/**
	 * constructs widget and sets styles and handlers
	 * @param story
	 * @param indexBefore the index of the sentence (in argument story), indexAfter is calculated automatically (indexAfter = indexBefore+1)
	 * @return
	 */
	private EvInsertSentenceButton constructInsertSentenceButton(
			ArrayList<EvSentence<Ev, SentenceShortIdentifier>> sentences, 
			int indexBefore) {
		EvInsertSentenceButton insertButton = new EvInsertSentenceButton(
				getPositionFromSentenceOrNull(sentences, indexBefore), 
				getPositionFromSentenceOrNull(sentences, indexBefore+1)
				);
		insertButton.addStyleName(EvStyle.eStoryPanelInsertSentenceButton);
		return insertButton;
	}
	/**
	 * helperMethod for constructInsertSentenceButton(..)
	 * @param story
	 * @param index
	 * @return null if index out of bounds
	 */
	private Long getPositionFromSentenceOrNull(ArrayList<EvSentence<Ev, SentenceShortIdentifier>> sentences, int index) {
		if (sentences.size() <= index || 0 > index) {
			return null;
		} else {
			return sentences.get(index).getPosition();
		}
	}
	
	/**
	 * constructs widget and sets styles and handlers
	 * @param sentenceHandler
	 * @return
	 */
	private EvSentenceStatView constructSentenceStatView(EvSentenceHandler sentenceHandler) {
		EvSentenceStatView sentenceStatView = new EvSentenceStatView(sentenceHandler);
		String hightlightStyle = getHighlightStyle(sentenceHandler.getSentence());
		if (null != hightlightStyle) {
			sentenceStatView.addStyleName(hightlightStyle);
		}
		sentenceStatView.addStyleName(EvStyle.eStoryPanelSentenceStatView);
		sentenceStatView.addDomHandler(haMouseOver, MouseOverEvent.getType());
		sentenceStatView.addDomHandler(haClick, ClickEvent.getType());
		return sentenceStatView;
	}
	
	/**
	 * constructs widget and sets styles and handlers
	 * @return
	 */
	private InlineHTML constructSentenceHeader() {
		// display: inline-block allows to set margin, but trims normal space-character
//		InlineHTML sentenceHeader = new InlineHTML("&thinsp;");
		InlineHTML sentenceHeader = new InlineHTML("&zwj;");
		sentenceHeader.addStyleName(EvStyle.eStoryPanelSentenceView);
		sentenceHeader.addStyleName(EvStyle.eStoryPanelSentenceHeader);
		return sentenceHeader;
	}
	
	/**
	 * constructs widget and sets styles and handlers
	 * @param sentence
	 * @param sentenceHandler
	 * @return
	 */
	private EvSentenceView constructSentenceView(EvSentence<Ev, SentenceShortIdentifier> sentence, EvSentenceHandler sentenceHandler) {
		EvSentenceView sentenceView = new EvSentenceView(sentence.getSentence().getString(), sentenceHandler);
		String hightlightStyle = getHighlightStyle(sentence);
		if (null != hightlightStyle) {
			sentenceView.addStyleName(hightlightStyle);
		}
		sentenceView.addStyleName(EvStyle.eStoryPanelSentenceView);
		sentenceView.addDomHandler(haMouseOver, MouseOverEvent.getType());
		sentenceView.addDomHandler(haClick, ClickEvent.getType());
		return sentenceView;
	}
	
	/**
	 * constructs widget and sets styles and handlers
	 * @return
	 */
	private EvHiddenSentencesView constructHiddenSentencesView() {
		EvHiddenSentencesView hiddenSentencesView = new EvHiddenSentencesView();
		hiddenSentencesView.addStyleName(EvStyle.eStoryPanelSentenceView);
		hiddenSentencesView.addStyleName(EvStyle.eStoryPanelHiddenSentencesView);
		hiddenSentencesView.addDomHandler(haClickHiddenSentencesView, ClickEvent.getType());
		return hiddenSentencesView;
	}
	
	/**
	 * checks in consideration of the current filter settings if the given sentence should be filtered
	 * @param sentence
	 * @return true if sentence should be filtered, false if it should be displayed
	 */
	public boolean isSentenceFiltered(EvSentence<Ev, SentenceShortIdentifier> sentence) {
		if (sentence.getEv().getRating() < settings.getMinRating()) {
			return true;
		} else if (!settings.isHighlightMyUpvoted() && settings.isOnlyMyUpvoted() && sentence.getEv().getVote() != UpDownVote.UP) {
			return true;
		} else if (!settings.isHighlightMyDownvoted() && settings.isNotMyDownvoted() && sentence.getEv().getVote() == UpDownVote.DOWN) {
			return true;
		} else if (!settings.isHighlightAuthor() && null != settings.getOnlyAuthor() && sentence.getAuthor().getUserId() != settings.getOnlyAuthor().getUserId()) {
			return true;
		} else if (!settings.isHighlightNew() && null != settings.getNewSince() && sentence.getAuthorTime().getTime() < settings.getNewSince()) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * check if the given sentence should be highlighted and if so returns the styleName
	 * @param sentence
	 * @return the styleName for the given sentence or null if the sentence should not be highlighted
	 */
	public String getHighlightStyle(EvSentence<Ev, SentenceShortIdentifier> sentence) {
		
		if (settings.isHighlightAuthor() && null != settings.getOnlyAuthor() && sentence.getAuthor().getUserId() == settings.getOnlyAuthor().getUserId()) {
			// own
			return EvStyle.eTableHighlightOwn;
			
		} else if (settings.isHighlightMyUpvoted() && sentence.getEv().getVote() == UpDownVote.UP) {
			// upvoted
			return EvStyle.eTableHighlightUpvoted;
			
		} else if (settings.isHighlightMyDownvoted() && sentence.getEv().getVote() == UpDownVote.DOWN) {
			// downvoted
			return EvStyle.eTableHighlightDownvoted;
			
		} else if (settings.isHighlightNew() && null != settings.getNewSince() && sentence.getAuthorTime().getTime() >= settings.getNewSince()) {
			// new
			return EvStyle.eTableHighlightNew;
			
		} else {
			// not highlighted
			return null;
		}
	}
	
	/**
	 * sets the textSize like specified by settings
	 */
	public void setTextSizeVarying() {
		if (true == settings.isTextSizeVarying()) {
			for (EvSentenceHandler sentenceHandler : sentenceHandlers) {
				float size = ratingToTextSize(sentenceHandler.getSentence().getEv().getRating());
				sentenceHandler.getSentenceHeader().getElement().getStyle().setFontSize(size, Style.Unit.PX);
				sentenceHandler.getSentenceView().getElement().getStyle().setFontSize(size, Style.Unit.PX);
			}
		} else {
			for (EvSentenceHandler sentenceHandler : sentenceHandlers) {
				sentenceHandler.getSentenceHeader().getElement().getStyle().setFontSize(14, Style.Unit.PX);
				sentenceHandler.getSentenceView().getElement().getStyle().setFontSize(14, Style.Unit.PX);
			}
		}
	}
	
	/**
	 * sets the visibility of the insertButtons like specified by settings
	 */
	public void setInsertButtonsVisible() {
		for (EvSentenceHandler sentenceHandler : sentenceHandlers) {
			sentenceHandler.getInsertButtonBefore().setVisible(settings.isInsertsVisible());
			sentenceHandler.getInsertButtonAfter().setVisible(settings.isInsertsVisible());
		}
	}
	
	/**
	 * sets the visibility of the statViews like specified by settings
	 */
	public void setStatViewsVisible() {
		for (EvSentenceHandler sentenceHandler : sentenceHandlers) {
			sentenceHandler.getSentenceStatView().setVisible(settings.isStatsVisible());
		}
	}
	
	/**
	 * 
	 * @param sentenceHandler specifies the sentence to select
	 */
	private void selectSentence(EvSentenceHandler sentenceHandler) {
		
		// deselect old currentlySelectedSentence
		if (null != currentlySelectedSentence && sentenceHandler != currentlySelectedSentence) {
			
			// sentence itself
			currentlySelectedSentence.getSentenceView().removeStyleName(EvStyle.eStoryPanelSentenceHover);
			
			// statView
			currentlySelectedSentence.getSentenceStatView().removeStyleName(EvStyle.eStoryPanelSentenceHover);
			currentlySelectedSentence.getSentenceStatView().setVisible(settings.isStatsVisible());
			
			// insertButtons
			if (currentlySelectedSentence.getInsertButtonBefore() != currentlyClickedInsertSentenceButton) {
				currentlySelectedSentence.getInsertButtonBefore().setVisible(settings.isInsertsVisible());
			}
			if (currentlySelectedSentence.getInsertButtonAfter() != currentlyClickedInsertSentenceButton) {
				currentlySelectedSentence.getInsertButtonAfter().setVisible(settings.isInsertsVisible());
			}
		}
		
		// select new currentlySelectedSentence (or reselect in case sentences was deselected in other way)
		currentlySelectedSentence = sentenceHandler;
		
		// sentence itself
		currentlySelectedSentence.getSentenceView().addStyleName(EvStyle.eStoryPanelSentenceHover);
		
		// statView
		sentenceHandler.getSentenceStatView().addStyleName(EvStyle.eStoryPanelSentenceHover);
		sentenceHandler.getSentenceStatView().setVisible(true);
		
		// insertButtons
		sentenceHandler.getInsertButtonBefore().setVisible(true);
		sentenceHandler.getInsertButtonAfter().setVisible(true);
		
	}
	
}
