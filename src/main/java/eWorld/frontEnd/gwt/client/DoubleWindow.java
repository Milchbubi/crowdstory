package eWorld.frontEnd.gwt.client;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;

import eWorld.datatypes.data.EvEntry;
import eWorld.datatypes.elementars.WoString;
import eWorld.datatypes.evs.Ev;
import eWorld.datatypes.evs.EvVoid;
import eWorld.datatypes.identifiers.EntryClassIdentifier;
import eWorld.datatypes.identifiers.EntryIdentifier;
import eWorld.datatypes.identifiers.EntryShortIdentifier;
import eWorld.datatypes.identifiers.UserIdentifier;
import eWorld.datatypes.packages.EvEntryPackage;
import eWorld.datatypes.packages.EvPackage;
import eWorld.frontEnd.gwt.client.views.EvElementClassView;
import eWorld.frontEnd.gwt.client.views.EvElementView;
import eWorld.frontEnd.gwt.client.views.EvEntryPackageView;
import eWorld.frontEnd.gwt.client.views.EvNaviPackageView;
import eWorld.frontEnd.gwt.client.views.EvRootNaviView;

@Deprecated	// TODO implement new class
public class DoubleWindow extends EvWindow {
	
	// components
	
	private SimplePanel viewPanelWrapper = new SimplePanel();
	
	private HorizontalPanel viewPanel = new HorizontalPanel();
//	private FlowPanel viewPanel = new FlowPanel();
	
	private EvSlider leftSlider = new EvSlider();
	
	private EvSlider rightSlider = new EvSlider();
	
	/** view that became slid out (becomes deleted before next slide-action) */
	private EvSlider formerSlider = null;
	
	
	// observers
	
	private EvObserver<EvEntry<EvVoid, EntryShortIdentifier>> obsLeftEntrySelected = new EvObserver<EvEntry<EvVoid, EntryShortIdentifier>>() {
		@Override
		public void call(EvEntry<EvVoid, EntryShortIdentifier> value) {
			assert null != value;
			
			if (!value.isElement()) {
				setRightView(new EvEntryPackageView(new EntryClassIdentifier(value.getIdentifier().getEntryId()), obsRightEntrySelected, obsRightEvButtonPressed));
			} else {
				setRightView(new EvElementView(new EntryIdentifier(leftSlider.getView().getCurrentClassIdent(), value.getIdentifier())));
			}
			
			getEvPathView().removeBack();
			getEvPathView().addBack(value);
		}
	};
	
	private EvObserver<EvEntry<EvVoid, EntryShortIdentifier>> obsRightEntrySelected = new EvObserver<EvEntry<EvVoid, EntryShortIdentifier>>() {
		@Override
		public void call(EvEntry<EvVoid, EntryShortIdentifier> value) {
			assert null != value;
			
			if (!value.isElement()) {
				slideInViewFromRight(new EvEntryPackageView(new EntryClassIdentifier(value.getIdentifier().getEntryId()), obsRightEntrySelected, obsRightEvButtonPressed));
			} else {
				slideInViewFromRight(new EvElementView(new EntryIdentifier(rightSlider.getView().getCurrentClassIdent(), value.getIdentifier())));
			}
			
			getEvPathView().addBack(value);
			setSuperButtonEnabled(true);
		}
	};
	
	private EvObserver<EntryIdentifier> obsLeftEvButtonPressed = new EvObserver<EntryIdentifier>() {
		@Override
		public void call(EntryIdentifier value) {
			assert null != value;
			
			setRightView(new EvElementClassView(value));
			
			getEvPathView().removeBack();
			getEvPathView().addBack(EvEntry.voidCast(EvEntry.shortCast(leftSlider.getView().getDataPackage().getHeader())));
		}
	};
	
	private EvObserver<EntryIdentifier> obsRightEvButtonPressed = new EvObserver<EntryIdentifier>() {
		@Override
		public void call(EntryIdentifier value) {
			assert null != value;
			
			slideInViewFromRight(new EvElementClassView(value));
			
			getEvPathView().addBack(EvEntry.voidCast(EvEntry.shortCast(leftSlider.getView().getDataPackage().getHeader())));
			setSuperButtonEnabled(true);
		}
	};
	
	/** TODO change this */
	private EvObserver<Void> obsLeftEntriesLoadedAndDisplayed = new EvObserver<Void>() {
		@Override
		public void call(Void value) {
			EvEntryPackageView leftView = (EvEntryPackageView)leftSlider.getView();
			EvNaviPackageView rightView = rightSlider.getView();
			
			assert null != leftView;
			
			if (null != leftView && null != rightView) {
				EvPackage rightPackage = rightView.getDataPackage();
				if (null != rightPackage) {
					// when rightView has already loaded
					leftView.setSelectedRow(EvEntry.shortCast(rightPackage.getHeader()));
				} else {
					// rightView has not yet loaded	TODO select correct row anyway
					
				}
			}
		}
	};
	
	
	// constructors
	
	public DoubleWindow() {
		
		// load
//		EvApp.REQ.getStartEntryPackage(new EvAsyncCallback<EvEntryPackage>(){
//			@Override
//			public void onSuccess(EvEntryPackage result) {
//				EvEntryPackageView view = new EvEntryPackageView(result, obsLeftEntrySelected, obsLeftEvButtonPressed);
//				setLeftView(view);
//				// get top rated entry of package and load it
//				EvEntry<Ev, EntryShortIdentifier> topEntry = result.getEntryContainer().getData().get(0);
//				if (null != topEntry) {
//					view.setSelectedRow(topEntry);
//					obsLeftEntrySelected.call(EvEntry.voidCast(topEntry));
//				}
//			}
//		});
		
		// compose
		viewPanel.add(leftSlider);
		viewPanel.add(rightSlider);
		viewPanelWrapper.setWidget(viewPanel);
		add(viewPanelWrapper);
		
		// style
		viewPanel.addStyleName(EvStyle.eWindowViewPanel);
		viewPanelWrapper.addStyleName(EvStyle.eDoubleWindowViewPanelWrapper);
		
		// update style when windowSize changes
		Window.addResizeHandler(new ResizeHandler() {
			@Override
			public void onResize(ResizeEvent event) {
				updateViewStyles();
			}
		});
		
	}

	
	// methods

	private void setLeftView(EvNaviPackageView view) {
		assert null != view;
		
		setViewHelpMethode(view);
		leftSlider.setView(view);
		updateViewStyles();
	}
	
	private void setRightView(EvNaviPackageView view) {
		assert null != view;
		
		setViewHelpMethode(view);
		rightSlider.setView(view);
		updateViewStyles();
	}
	
	private void slideInViewFromLeft(final EvNaviPackageView view) {
		assert null != view;

		setViewHelpMethode(view);
		
		// slide rightView out
		formerSlider = rightSlider;
		if (null != formerSlider) {
			formerSlider.getElement().getStyle().setMarginRight(-formerSlider.getOffsetWidth(), Style.Unit.PX);
//			formerSlider.getElement().getStyle().setWidth(0, Style.Unit.PX);
		}
		
		// do the real work
		rightSlider = leftSlider;
		leftSlider = new EvSlider(view);
		viewPanel.insert(leftSlider, 0);
		updateViewStyles();
		
		// slide leftView in
		leftSlider.removeStyleName(EvStyle.eDoubleWindowNaviViewSlide);
		leftSlider.getElement().getStyle().setMarginLeft(-leftSlider.getOffsetWidth(), Style.Unit.PX);
		new Timer() {
			// wait a short time that it is realized that style changes
			// AttachHandler does not work
			// TODO implement in better way, thats stupid
			@Override
			public void run() {
				leftSlider.addStyleName(EvStyle.eDoubleWindowNaviViewSlide);
				leftSlider.getElement().getStyle().clearMarginLeft();
			}
		}.schedule(10);
		
		// update observers if necessary
		if (rightSlider.getView() instanceof EvEntryPackageView) {	// TODO find better solution for this
			((EvEntryPackageView) rightSlider.getView()).setObservers(obsRightEntrySelected, obsRightEvButtonPressed);
		}
	}
	
	private void slideInViewFromRight(final EvNaviPackageView view) {
		assert null != view;
		
		setViewHelpMethode(view);
		
		// slide leftView out
		formerSlider = leftSlider;
		if (null != formerSlider) {
			formerSlider.getElement().getStyle().setMarginLeft(-formerSlider.getOffsetWidth(), Style.Unit.PX);
		}
		
		// do the real work
		leftSlider = rightSlider;
		rightSlider = new EvSlider(view);
		viewPanel.add(rightSlider);
		
		// slide rightView in, not needed in this implementation
//		rightSlider.removeStyleName(EvStyle.eDoubleWindowNaviViewSlide);
//		rightSlider.getElement().getStyle().setWidth(0, Style.Unit.PX);
//		rightSlider.getElement().getStyle().setMarginLeft(rightSlider.getOffsetWidth(), Style.Unit.PX);
//		new Timer() {
//			// wait a short time that it is realized that style changes
//			// AttachHandler does not work
//			// TODO implement in better way, thats stupid
//			@Override
//			public void run() {
//				rightSlider.addStyleName(EvStyle.eDoubleWindowNaviViewSlide);
//				rightSlider.getElement().getStyle().clearWidth();
//				rightSlider.getElement().getStyle().clearMarginLeft();
//				updateViewStyles();
//			}
//		}.schedule(10);
		
		updateViewStyles();
		
		// update observers if necessary
		if (leftSlider.getView() instanceof EvEntryPackageView) {	// TODO find better solution for this
			((EvEntryPackageView) leftSlider.getView()).setObservers(obsLeftEntrySelected, obsLeftEvButtonPressed);
		}
	}
	
	private void setViewHelpMethode(EvNaviPackageView view) {
		assert null != view;
		
		// remove view that was slid out at the last slide-action
		if (null != formerSlider) {
			viewPanel.remove(formerSlider);
		}
		
	}
	
	/**
	 * important: all views have to be already attached (they must have a parent)
	 * TODO this is maybe not the best implementation
	 */
	private void updateViewStyles() {
		
//		final int windowWidth = this.getOffsetWidth() - 50;
		final int windowWidth = Window.getClientWidth() - 50;
//		final int leftSliderWidth = (int)(windowWidth * 0.3);
//		final int rightSliderWidth = (int)(windowWidth * 0.7);
		int leftSliderWidth = Math.max(370, (int)(windowWidth * 0.3));
		int rightSliderWidth = Math.max(500, windowWidth - leftSliderWidth);
		
		if (windowWidth < leftSliderWidth + rightSliderWidth) {
			leftSliderWidth = 0;
			rightSliderWidth = windowWidth;
		}
		
		viewPanelWrapper.getElement().getStyle().setWidth(windowWidth, Style.Unit.PX);
		
		leftSlider.removeStyleName(EvStyle.eDoubleWindowRightNaviView);
		leftSlider.addStyleName(EvStyle.eDoubleWindowLeftNaviView);
		leftSlider.getElement().getStyle().setWidth(leftSliderWidth, Style.Unit.PX);
	
		rightSlider.removeStyleName(EvStyle.eDoubleWindowLeftNaviView);
		rightSlider.addStyleName(EvStyle.eDoubleWindowRightNaviView);
		rightSlider.getElement().getStyle().setWidth(rightSliderWidth, Style.Unit.PX);
		
		if (null != formerSlider) {
//			formerSlider.getElement().getStyle().setWidth(0, Style.Unit.PX);
		}
		
//		new Timer() {
//			// wait a short time till the inner elements tried to fit in the given widths
//			// TODO implement in better way, thats stupid
//			@Override
//			public void run() {
//				
//				int leftWidth = leftSliderWidth;
//				int rightWidth = rightSliderWidth;
////				int leftInnerWidth = leftSlider.getView().getOffsetWidth();
//				int leftInnerWidth = leftSlider.getView().getElement().getClientWidth();
////				int rightInnerWidth = rightSlider.getView().getOffsetWidth();
//				int rightInnerWidth = rightSlider.getView().getElement().getClientWidth();
//				
//				if (leftWidth < leftInnerWidth) {
//					leftSlider.getElement().getStyle().setWidth(leftInnerWidth, Style.Unit.PX);
//					leftWidth = leftInnerWidth;
//				}
//				if (rightWidth < rightInnerWidth) {
//					rightSlider.getElement().getStyle().setWidth(rightInnerWidth, Style.Unit.PX);
//					rightWidth = rightInnerWidth;
//				}
//				
//				if (windowWidth < leftWidth + rightWidth) {
//					leftSlider.getElement().getStyle().setWidth(0, Style.Unit.PX);
//					rightSlider.getElement().getStyle().setWidth(windowWidth, Style.Unit.PX);
//				}
//			}
//		}.schedule(10);
		
	}
	
	@Override
	protected void load(final EntryIdentifier entryIdentifier, boolean elementView) {
		assert null != entryIdentifier;
		
		// setLeftView
		if (null != getRootEntry() && getRootEntry().getIdentifier().getEntryClassId() == entryIdentifier.getEntryClassId()) {
			// top|root is achieved, display RootView
			setLeftView(new EvRootNaviView(getRootEntry().getIdentifier()));
			setSuperButtonEnabled(false);
			
		} else {
			// setLeftView to class of entryIdentifier
	//		setLeftView(new EvEntryPackageView(entryIdentifier, obsLeftEntrySelected, obsLeftEvButtonPressed, obsLeftEntriesLoadedAndDisplayed));
			setLeftView(new EvEntryPackageView(entryIdentifier, obsLeftEntrySelected, obsLeftEvButtonPressed, new EvObserver<Void>() {	// TODO Observer is redundant, use line above when possible
				@Override
				public void call(Void value) {
					EvEntryPackageView leftView = (EvEntryPackageView)leftSlider.getView();
					EvNaviPackageView rightView = rightSlider.getView();
					
					assert null != leftView;
					
					if (null != leftView && null != rightView) {
						EvPackage rightPackage = rightView.getDataPackage();
						if (null != rightPackage) {
							// when rightView has already loaded
							leftView.setSelectedRow(EvEntry.shortCast(rightPackage.getHeader()));
						} else {
							// rightView has not yet loaded -> fake something FIXME thats stupid 
							leftView.setSelectedRow(new EvEntry<Ev, EntryShortIdentifier>(
									new Ev(),
									entryIdentifier.getShortIdentifier(),
									new WoString(), new WoString(), false, new UserIdentifier()
									));
						}
					}
				}
			}));
			setSuperButtonEnabled(true);
			
		}
		
		// setRightView
		if (true == elementView) {
			setRightView(new EvElementView(entryIdentifier));
		} else {
			setRightView(new EvEntryPackageView(new EntryClassIdentifier(entryIdentifier.getEntryId()), obsRightEntrySelected, obsRightEvButtonPressed));
		}
		
	}
	
	@Override
	protected void ascendInHierarchy() {
		
		if (null == leftSlider.getView()) {
			EvApp.INFO.addProblem("leftSlider is null");
		}
		
		if (leftSlider.getView().getCurrentClassIdent().getEntryClassId() == getRootEntry().getIdentifier().getEntryId()) {
			// top|root is achieved
			slideInViewFromLeft(new EvRootNaviView(getRootEntry().getIdentifier()));
			setSuperButtonEnabled(false);
			getEvPathView().removeBack();
			
		} else if (leftSlider.getView().getCurrentClassIdent().getEntryClassId() == getRootEntry().getIdentifier().getEntryClassId()) {
			// top|root was already achieved (should not occur because superButton was disabled)
			setLeftView(new EvRootNaviView(getRootEntry().getIdentifier()));
			
		} else {
			// ascend
			EvEntryPackageView view = new EvEntryPackageView(
					leftSlider.getView().getCurrentSuperClassIdent(), 
					obsLeftEntrySelected, 
					obsLeftEvButtonPressed,
					obsLeftEntriesLoadedAndDisplayed);
			slideInViewFromLeft(view);
			getEvPathView().removeBack();
		}
		
	}

	@Override
	public void update() {
		if (null != leftSlider.getView()) {
			leftSlider.getView().update();
		}
		if (null != rightSlider.getView()) {
			rightSlider.getView().update();
		}
	}
	
	private class EvSlider extends SimplePanel {
		
		/** Widget that is displayed, or null when empty */
		private EvNaviPackageView view = null;
		
		public EvSlider(){
			this(null);
		}
		
		public EvSlider(EvNaviPackageView view) {
			this.view = view;
			
			addStyleName(EvStyle.eDoubleWindowNaviView);
			addStyleName(EvStyle.eDoubleWindowNaviViewSlide);
			
			setView(view);
		}
		
		public EvNaviPackageView getView() {
			return view;
		}
		
		public void setView(EvNaviPackageView view) {
			this.view = view;
			
			if (null != view) {
				setWidget(view);
			} else {
				clear();
			}
		}
	}
	
}
