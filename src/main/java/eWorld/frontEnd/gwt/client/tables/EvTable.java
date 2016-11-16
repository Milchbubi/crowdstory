package eWorld.frontEnd.gwt.client.tables;

import java.util.ArrayList;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLTable;
import com.google.gwt.user.client.ui.Label;

import eWorld.datatypes.containers.EvDataTypeContainer;
import eWorld.datatypes.data.EvDataType;
import eWorld.datatypes.evs.Ev;
import eWorld.datatypes.identifiers.EvCompleteIdentifier;
import eWorld.datatypes.identifiers.EvShortIdentifier;
import eWorld.frontEnd.gwt.client.EvApp;
import eWorld.frontEnd.gwt.client.EvStyle;
import eWorld.frontEnd.gwt.client.util.EvDownWidget;
import eWorld.frontEnd.gwt.client.util.EvRatingWidget;
import eWorld.frontEnd.gwt.client.util.EvUpWidget;

public abstract class EvTable<
		CLASS_IDENT extends EvCompleteIdentifier,
		ITEM_IDENT extends EvShortIdentifier<ITEM_IDENT_TYPE>,
		ITEM_IDENT_TYPE,
		ITEM extends EvDataType<Ev, ITEM_IDENT>,
		ITEM_CONTAINER extends EvDataTypeContainer<CLASS_IDENT, ITEM_IDENT, ITEM_IDENT_TYPE, ITEM>
	> extends FlexTable {

	// attributes
	
	private static final String RANK_TITLE = "todo";
	private static final String RATING_TITLE = "upVotes - downVotes";
	private static final String DOWN_VOTE_TITLE = "belongs not to this list";
	private static final String UP_VOTE_TITLE = "belongs to this list";
	
	/** the value of a colIndex when it should be hidden */
	private static final int COL_HIDDEN = -1;
	
	private int colIndexRank;
	private int colIndexRating;
	private int colIndexDownVote;
	private int colIndexUpVote;
	private int colIndexExtensionStart;
	
	/** contains the items that are viewed */
	private ITEM_CONTAINER eItemContainer = null;
	
	
	// handlers
	
	private ClickHandler haCellSelected = new ClickHandler() {
		@Override
		public void onClick(ClickEvent event) {
			assert null != event;
			
			HTMLTable.Cell cell = getCellForEvent(event);
			
			assert null != cell;
			if (null == cell) {
				return;
			}
			
			int rowIndex = cell.getRowIndex() -1;
			int colIndex = cell.getCellIndex();
			
			ITEM_IDENT shortIdentifier = eItemContainer.getData().get(rowIndex).getIdentifier();
			
			if (colIndexDownVote == colIndex) {
				// downVote
				vote(shortIdentifier, false, cell);
				
			} else if (colIndexUpVote == colIndex) {
				// upVote
				vote(shortIdentifier, true, cell);
				
			} else {
				
				// something else specified trough subclass
				ITEM item = eItemContainer.getData().get(rowIndex);
				rowSelected(item, cell, event);
			}
		}
	};
	
	
	// constructors
	
	public EvTable() {
		init(0, 1, 2, 3, 4);
	}
	
	/**
	 * 
	 * @param eColsVisible true when EvColumns should be visible, false when they should be hidden
	 */
	public EvTable(boolean eColsVisible) {
		if (true == eColsVisible) {
			init(0, 1, 2, 3, 4);
		} else {
			init(COL_HIDDEN, COL_HIDDEN, COL_HIDDEN, COL_HIDDEN, 0);
		}
	}
	
	public EvTable(int colIndexRank, int colIndexRating, int colIndexDownVote, int colIndexUpVote, int colIndexExtensionStart) {
		init(colIndexRank, colIndexRating, colIndexDownVote, colIndexUpVote, colIndexExtensionStart);
	}
	
	private void init(int colIndexRank, int colIndexRating, int colIndexDownVote, int colIndexUpVote, int colIndexExtensionStart) {
		this.colIndexRank = colIndexRank;
		this.colIndexRating = colIndexRating;
		this.colIndexDownVote = colIndexDownVote;
		this.colIndexUpVote = colIndexUpVote;
		this.colIndexExtensionStart = colIndexExtensionStart;
		
		// style
		if (COL_HIDDEN != colIndexRank) getColumnFormatter().addStyleName(colIndexRank, EvStyle.eTableSlimColumn);
		if (COL_HIDDEN != colIndexRating) getColumnFormatter().addStyleName(colIndexRating, EvStyle.eTableSlimColumn);
		if (COL_HIDDEN != colIndexDownVote) getColumnFormatter().addStyleName(colIndexDownVote, EvStyle.eTableSlimColumn);
		if (COL_HIDDEN != colIndexUpVote) getColumnFormatter().addStyleName(colIndexUpVote, EvStyle.eTableSlimColumn);
		addStyleName(EvStyle.eTable);
		
		// handlers
		addClickHandler(haCellSelected);
	}
	
	
	// methods
	
	/**
	 * @param eItemContainer dataContainer that should be displayed
	 * @param caption true if a caption should be set otherwise false
	 */
	public void setItemContainer(ITEM_CONTAINER eItemContainer, boolean caption) {
		assert null != eItemContainer;
		
		this.eItemContainer = eItemContainer;
		
		removeAllRows();
		
		ArrayList<ITEM> eItems = eItemContainer.getData();
		
		
		if (true == caption) {
			setCaption();
		}
		
		if (eItems.isEmpty()) {
			// container is empty
			Label noItemsLabel = new Label("there are no items yet");
			noItemsLabel.addStyleName(EvStyle.eTableNoItemsLabel);
			setWidget(1, 0, noItemsLabel);
			
		} else {
			// container is not empty
			for (int i = 0; i < eItems.size(); i++) {
				setItem(eItems.get(i), i +1);
			}
		}
	}
	
	private void setCaption() {
		setEvCaptions(0);
		extendCaptionRow(0, getColIndexExtensionStart());
		
		// style
		this.getRowFormatter().addStyleName(0, EvStyle.eTableHeader);
	}
	
	/**
	 * 
	 * @param item
	 * @param row 0 is reserved for caption
	 */
	private void setItem(ITEM item, int row) {
		Ev ev = item.getEv();
		
		setEv(ev, row);
		extendDataRow(row, getColIndexExtensionStart(), item);
		
		// style
		this.getRowFormatter().addStyleName(row, EvStyle.eTableRow);
		this.getRowFormatter().addStyleName(row, getAdditionalRowStyle());
	}
	
	private void setEvCaptions(int row) {
		if (COL_HIDDEN != colIndexRank) {
			setText(row, colIndexRank, "Rank");
			getCellFormatter().getElement(row, colIndexRank).setTitle(RANK_TITLE);
		}
		if (COL_HIDDEN != colIndexRating) {
			setText(row, colIndexRating, "Rating");
			getCellFormatter().getElement(row, colIndexRating).setTitle(RATING_TITLE);
		}
		if (COL_HIDDEN != colIndexDownVote) {
			setWidget(row, colIndexDownVote, new HTML("Down<br/>Vote"));
			getCellFormatter().getElement(row, colIndexDownVote).setTitle(DOWN_VOTE_TITLE);
		}
		if (COL_HIDDEN != colIndexUpVote) {
			setWidget(row, colIndexUpVote, new HTML("Up<br/>Vote"));
			getCellFormatter().getElement(row, colIndexUpVote).setTitle(UP_VOTE_TITLE);
		}
	}
	
	/**
	 * 
	 * @param ev contains the data that should be filled in the given row
	 * @param row the row of the table that should be filled with the given data
	 * @return the column-index where other data can be filled in (next free column)
	 */
	private void setEv(Ev ev, int row) {
		assert null != ev;
		
		if (COL_HIDDEN != colIndexRank) {
			setText(row, colIndexRank, ev.getRankString());
			getCellFormatter().getElement(row, colIndexRank).setTitle(RANK_TITLE);
		}
		if (COL_HIDDEN != colIndexRating) {
			EvRatingWidget ratingWidget = new EvRatingWidget(ev.getRating());
			setWidget(row, colIndexRating, ratingWidget);
			getCellFormatter().getElement(row, colIndexRating).setTitle(RATING_TITLE);
		}
		if (COL_HIDDEN != colIndexDownVote) {
			EvDownWidget downWidget = new EvDownWidget(ev.getVote());
			setWidget(row, colIndexDownVote, downWidget);
			downWidget.setStyleToCell(getCellFormatter(), row, colIndexDownVote);
			getCellFormatter().getElement(row, colIndexDownVote).setTitle(DOWN_VOTE_TITLE);
		}
		if (COL_HIDDEN != colIndexUpVote) {
			EvUpWidget upWidget = new EvUpWidget(ev.getVote());
			setWidget(row, colIndexUpVote, upWidget);
			upWidget.setStyleToCell(getCellFormatter(), row, colIndexUpVote);
			getCellFormatter().getElement(row, colIndexUpVote).setTitle(UP_VOTE_TITLE);
		}
		
	}
	
	/**
	 * 
	 * @return the start-index of the column where (direct) subclasses put additional data in
	 */
	protected int getColIndexExtensionStart() {
		return colIndexExtensionStart;
	}
	
	/**
	 * @return container that contains the items that are viewed
	 */
	public ITEM_CONTAINER getItemContainer() {
		return eItemContainer;
	}
	
	/**
	 * 
	 * @param vote
	 * @param cell specifies the row
	 */
	protected void setRowVoteUpdate(boolean vote, HTMLTable.Cell cell) {
		assert null != cell;
		
		if (COL_HIDDEN != colIndexDownVote) {
			EvDownWidget downWidget = (EvDownWidget)getWidget(cell.getRowIndex(), colIndexDownVote);
			if (false == vote) {
				// downVoted
				downWidget.setVoted();
				
			} else {
				// upVoted
				downWidget.setDefault();
			}
			downWidget.setStyleToCell(getCellFormatter(), cell.getRowIndex(), colIndexDownVote);
		}
		
		if (COL_HIDDEN != colIndexUpVote) {
			EvUpWidget upWidget = (EvUpWidget)getWidget(cell.getRowIndex(), colIndexUpVote);
			if (false == vote) {
				// downVoted
				upWidget.setDefault();
				
			} else {
				// upVoted
				upWidget.setVoted();
			}
			upWidget.setStyleToCell(getCellFormatter(), cell.getRowIndex(), colIndexUpVote);
		}
		
		if (COL_HIDDEN != colIndexRating) {
			EvRatingWidget ratingWidget = (EvRatingWidget)getWidget(cell.getRowIndex(), colIndexRating);
			int votes = EvApp.getUser().getVotes();
			ratingWidget.incrementRating(vote ? votes : -votes);
		}
		
	}
	
	/**
	 * 
	 * @param vote
	 * @param cell specifies the row
	 */
	protected void setRowVoteRequested(boolean vote, HTMLTable.Cell cell) {
		assert null != cell;
		
		if (false == vote) {
			// downVoting
			if (COL_HIDDEN != colIndexDownVote) ((EvDownWidget)getWidget(cell.getRowIndex(), colIndexDownVote)).setRequested();
			
		} else {
			// upVoting
			if (COL_HIDDEN != colIndexUpVote) ((EvUpWidget)getWidget(cell.getRowIndex(), colIndexUpVote)).setRequested();
		}
	}
	
	/**
	 * adds the given item to the end of the table
	 * @param item
	 */
	public void addItem(ITEM item) {
		if (eItemContainer.getData().isEmpty()) {
			setCaption();
		}
		eItemContainer.addItem(item);
		setItem(item, eItemContainer.getData().size());	// row 0 is reserved for caption
	}
	
	
	// abstract methods
	
	protected abstract String getAdditionalRowStyle();
	
	/**
	 * extends the caption row
	 * @param row the row that is modified
	 * @param startCol the first column where the data should be filled in TODO delete this argument
	 * @return the column-index where other data can be filled in (next free column)
	 */
	protected abstract int extendCaptionRow(int row, int startCol);
	
	/**
	 * extends the given row with additional data
	 * @param row the row that is modified
	 * @param startCol the first column where the data should be filled in TODO delete this argument
	 * @param item contains the data that should be added
	 * @return the column-index where other data can be filled in (next free column)
	 */
	protected abstract int extendDataRow(int row, int startCol, ITEM item);
	
	protected abstract void vote(ITEM_IDENT shortIdentifier, boolean vote, HTMLTable.Cell cell);
	
	/**
	 * called when a row becomes selected and the selected cell is not handled by superclass
	 * @param item the item that is displayed in the selected row
	 * @param cell the cell that is selected, can be used f.e. to get columnIndex|cellIndex
	 * @param event can be used to get f.e. clickPosition
	 */
	protected abstract void rowSelected(ITEM item, HTMLTable.Cell cell, ClickEvent event);
}
