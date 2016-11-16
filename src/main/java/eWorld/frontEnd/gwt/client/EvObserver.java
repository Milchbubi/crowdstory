package eWorld.frontEnd.gwt.client;

/**
 * used to describe what is to do when a certain event occurs
 * @author michael
 *
 * @param <TYPE>
 */
public abstract class EvObserver<TYPE> {

	/**
	 * called when a certain event occurs
	 * @param value describes the event more specific
	 */
	public abstract void call(TYPE value);
}
