package eWorld.frontEnd.gwt.client.tables;

import com.google.gwt.event.dom.client.ClickEvent;

import eWorld.datatypes.containers.EvStory;
import eWorld.datatypes.data.EvSentence;
import eWorld.datatypes.evs.Ev;
import eWorld.datatypes.identifiers.EntryClassIdentifier;
import eWorld.datatypes.identifiers.SentenceIdentifier;
import eWorld.datatypes.identifiers.SentenceShortIdentifier;
import eWorld.frontEnd.gwt.client.EvApp;
import eWorld.frontEnd.gwt.client.EvAsyncCallback;
import eWorld.frontEnd.gwt.client.EvObserver;
import eWorld.frontEnd.gwt.client.EvStyle;

public class EvSentenceTable extends EvTable<
		EntryClassIdentifier,
		SentenceShortIdentifier,
		Long,
		EvSentence<Ev, SentenceShortIdentifier>,
		EvStory
	> {

	// attributes
	
	private final EvObserver<EvSentence<Ev, SentenceShortIdentifier>> obsRowSelected;
	
	
	// constructors
	
	/**
	 * 
	 * @param obsRowSelected called when an item becomes selected, parameter is the selected item
	 */
	public EvSentenceTable(EvObserver<EvSentence<Ev, SentenceShortIdentifier>> obsRowSelected) {
		assert null != obsRowSelected;
		
		this.obsRowSelected = obsRowSelected;
		
	}
	
	
	// overridden methods
	
	@Override
	protected String getAdditionalRowStyle() {
		return EvStyle.eMediumTableRow;	// TODO replace with individual style
	}

	@Override
	protected int extendCaptionRow(int row, int startCol) {
		
		int col = startCol;
		
		setText(row, col++, "Position");
		setText(row, col++, "Sentence");
		
		return col;
	}

	@Override
	protected int extendDataRow(int row, int startCol,
			EvSentence<Ev, SentenceShortIdentifier> item) {
		
		int col = startCol;
		
		setText(row, col++, String.valueOf(item.getPosition()));
		setText(row, col++, item.getSentence().getString());
		
		return col;
	}

	@Override
	protected void vote(SentenceShortIdentifier shortIdentifier, final boolean vote,
			final Cell cell) {
		assert null != shortIdentifier;
		assert null != cell;
		
		SentenceIdentifier completeIdentifier = new SentenceIdentifier(getItemContainer().getClassIdentifier(), shortIdentifier);
		
		EvApp.REQ.voteSentence(completeIdentifier, vote, new EvAsyncCallback<Void>() {
			@Override
			public void onSuccess(Void result) {
				setRowVoteUpdate(vote, cell);
			}
		});
		
		setRowVoteRequested(vote, cell);
	}

	@Override
	protected void rowSelected(EvSentence<Ev, SentenceShortIdentifier> item,
			Cell cell, ClickEvent event) {
		obsRowSelected.call(item);
	}

}
