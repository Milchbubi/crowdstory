package eWorld.frontEnd.gwt.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * unifies the happenings of AsyncCallbacks,
 * f.e. when a callback fails this class encapsulates what to do
 * @author michael
 *
 * @param <TYPE> the type of the AsyncCallback
 */
public abstract class EvAsyncCallback<TYPE> implements AsyncCallback<TYPE> {

	@Override
//	public final void onFailure(Throwable caught) {
	public void onFailure(Throwable caught) {
		EvApp.INFO.addFailure(caught.getMessage());
	}

}

