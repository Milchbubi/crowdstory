package eWorld.datatypes.identifiers;

import java.io.Serializable;

@SuppressWarnings("serial")
public abstract class EvShortIdentifier<TYPE> implements EvIdentifier, Serializable {

	public abstract TYPE getShortId();
	
	public boolean equals(Object obj) {
		if (null != obj && obj instanceof EvShortIdentifier) {
			return getShortId().equals(((EvShortIdentifier)obj).getShortId());	// TODO FIXME? parameterize and surround with try-catch-block?
		} else {
			return false;
		}
	}
}
