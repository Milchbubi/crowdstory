package eWorld.frontEnd.gwt.server;

import java.util.ArrayList;

import javax.servlet.http.HttpSession;

import eWorld.database.ApacheCassandraGate;
import eWorld.database.Gate;
import eWorld.datatypes.EvPath;
import eWorld.datatypes.data.EvComment;
import eWorld.datatypes.data.EvEntry;
import eWorld.datatypes.data.EvMedium;
import eWorld.datatypes.data.EvSentence;
import eWorld.datatypes.evs.Ev;
import eWorld.datatypes.evs.EvVoid;
import eWorld.datatypes.exceptions.EvIllegalDataException;
import eWorld.datatypes.exceptions.EvRequestException;
import eWorld.datatypes.identifiers.CommentIdentifier;
import eWorld.datatypes.identifiers.EntryClassIdentifier;
import eWorld.datatypes.identifiers.EntryIdentifier;
import eWorld.datatypes.identifiers.EntryShortIdentifier;
import eWorld.datatypes.identifiers.MediumIdentifier;
import eWorld.datatypes.identifiers.SentenceIdentifier;
import eWorld.datatypes.identifiers.SentenceShortIdentifier;
import eWorld.datatypes.identifiers.UserIdentifier;
import eWorld.datatypes.packages.EvElement;
import eWorld.datatypes.packages.EvEntryPackage;
import eWorld.datatypes.packages.RootPackage;
import eWorld.datatypes.user.AdvancedUser;
import eWorld.datatypes.user.RegisterUser;
import eWorld.datatypes.user.SignInUser;
import eWorld.datatypes.user.User;
import eWorld.frontEnd.gwt.client.GeneralService;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server-side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class GeneralServiceImpl extends RemoteServiceServlet implements
		GeneralService {
	
	// attributes
	
	private static final String DATABASE_NODE = "127.0.0.1";
	private static final Gate DB_GATE = new ApacheCassandraGate(DATABASE_NODE);
	
	private static final String SESSION_ATTRIBUTE_ADVANCED_USER = "advancedUser";
	
	
	// methods
	
	/**
	 * prefer {@code getAdvancedUserOrThrow()}
	 * @return the user associated with the request or null if user is not signed in
	 */
	private AdvancedUser getAdvancedUserOrNull() {
		HttpSession session = getThreadLocalRequest().getSession();
		
		return (AdvancedUser)session.getAttribute(SESSION_ATTRIBUTE_ADVANCED_USER);
	}
	
	/**
	 * @return the user associated with the request or throws NullPointerException if user is not signed in
	 * @throws EvRequestException when user is not signed in
	 */
	private AdvancedUser getAdvancedUserOrThrow() throws EvRequestException {
		AdvancedUser user = getAdvancedUserOrNull();
		
		if (null == user) {
			throw new EvRequestException("user is not signed in");
		}
		
		return user;
	}
	
	
	@Override
	public void register(RegisterUser user) throws EvRequestException {
		if (null == user) throw new NullPointerException("argument 'user' is null");
		
		DB_GATE.registerUser(user);
	}

	@Override
	public User<UserIdentifier> signIn(SignInUser user) throws EvRequestException {
		if (null == user) throw new NullPointerException("argument 'user' is null");
		
		AdvancedUser advancedUser = DB_GATE.signInUser(user);
		
		getThreadLocalRequest().getSession().setAttribute(SESSION_ATTRIBUTE_ADVANCED_USER, advancedUser);
		
		return advancedUser.constructUser();
	}
	
	@Override
	public User<UserIdentifier> getSignedInUser() {
		AdvancedUser user = getAdvancedUserOrNull();
		if (null == user) {
			return null;
		} else {
			return user.constructUser();
		}
	}

	@Override
	public void signOut() {
		getThreadLocalRequest().getSession().invalidate();
	}
	
	@Override
	public EvEntry<Ev, EntryIdentifier> getRootEntry() {
		return DB_GATE.getRootEntry();
	}
	
	@Override
	public EvEntry<Ev, EntryIdentifier> getStartEntry() {
		/*EvEntry<Ev, EntryIdentifier> rootEntry = DB_GATE.getRootEntry();
		assert null != rootEntry;
		EvEntry<Ev, EntryIdentifier> startEntry = DB_GATE.getTopRatedEntry(new EntryClassIdentifier(rootEntry.getIdentifier().getEntryId()));
		
		if (null != startEntry) {
			return startEntry;
		} else {
			return rootEntry;
		}*/
		return DB_GATE.getRootEntry();
	}
	
	@Override
	public EvEntry<Ev, EntryIdentifier> getEntry(EntryShortIdentifier identifier)
			throws EvRequestException {
		if (null == identifier) throw new NullPointerException("argument 'identifier' is null");
		
		return DB_GATE.getEntry(identifier);
	}
	
	@Override
	public RootPackage getRootPackage() {
		
		AdvancedUser user = getAdvancedUserOrNull();
		UserIdentifier userIdentifier;
		if (null == user) {
			userIdentifier = null;
		} else {
			userIdentifier = user.getIdentifier();
		}
		
		return DB_GATE.getRootPackage(userIdentifier);
	}
	
	@Override
	@Deprecated
	public EvEntryPackage getStartEntryPackage() {
		
		AdvancedUser user = getAdvancedUserOrNull();
		UserIdentifier userIdentifier;
		if (null == user) {
			userIdentifier = null;
		} else {
			userIdentifier = user.getIdentifier();
		}
		
		return DB_GATE.getStartEntryPackage(userIdentifier);
	}
	
	@Override
	public EvEntryPackage getEntryPackage(EntryClassIdentifier identifier) {
		if (null == identifier) throw new NullPointerException("argument 'identifier' is null");
		
		AdvancedUser user = getAdvancedUserOrNull();
		UserIdentifier userIdentifier;
		if (null == user) {
			userIdentifier = null;
		} else {
			userIdentifier = user.getIdentifier();
		}
		
		return DB_GATE.getEntryPackage(userIdentifier, identifier);
	}

	@Override
	public EvEntry<Ev, EntryIdentifier> addEntry(EvEntry<EvVoid, EntryClassIdentifier> entry) throws EvRequestException, EvIllegalDataException {
		if (null == entry) throw new NullPointerException("argument 'entry' is null");
		
		entry.check();
		
		AdvancedUser user = getAdvancedUserOrThrow();
		
		entry.setAuthor(user.getIdentifier());
		return DB_GATE.addEntry(entry, user.getIdentifier(), user.getVotes());
	}

	@Override
	public EvEntry<Ev, EntryIdentifier> linkEntry(EntryIdentifier source,
			EntryClassIdentifier destination) throws EvRequestException {
		if (null == source) throw new NullPointerException("argument 'source' is null");
		if (null == destination) throw new NullPointerException("argument 'destination' is null");
		
		AdvancedUser user = getAdvancedUserOrThrow();
		
		return DB_GATE.copyEntry(source, destination, user.getIdentifier(), user.getVotes());
	}

	@Override
	public void voteEntry(EntryIdentifier entryIdent, boolean vote) throws EvRequestException {
		if (null == entryIdent) throw new NullPointerException("argument 'entryIdent' is null");
		
		AdvancedUser user = getAdvancedUserOrThrow();
		
		DB_GATE.voteEntry(user.getIdentifier(), entryIdent, vote, user.getVotes());
	}

	@Override
	public EvPath getPath(EntryShortIdentifier identifier) {
		if (null == identifier) throw new NullPointerException("argument 'identifier' is null");
		
		return DB_GATE.getPath(identifier);
	}
	
	@Override
	public EvElement getElement(EntryClassIdentifier classIdentifier, EntryShortIdentifier valueIdentifier) {
		if (null == classIdentifier) throw new NullPointerException("argument 'classIdentifier' is null");
		if (null == valueIdentifier) throw new NullPointerException("argument 'valueIdentifier' is null");
		
		AdvancedUser user = getAdvancedUserOrNull();
		UserIdentifier userIdentifier;
		if (null == user) {
			userIdentifier = null;
		} else {
			userIdentifier = user.getIdentifier();
		}
		
		return DB_GATE.getElement(userIdentifier, classIdentifier, valueIdentifier);
	}

	@Override
	public EvMedium<Ev, MediumIdentifier> addMedium(EvMedium<EvVoid, EntryClassIdentifier> medium)
			throws EvRequestException, EvIllegalDataException {
		if (null == medium) throw new NullPointerException("argument 'medium' is null");
		
		medium.check();
		
		AdvancedUser user = getAdvancedUserOrThrow();
		
		medium.setAuthor(user.getIdentifier());
		return DB_GATE.addMedium(medium, user.getIdentifier(), user.getVotes());
	}

	@Override
	public void voteMedium(MediumIdentifier mediumIdent, boolean vote)
			throws EvRequestException {
		if (null == mediumIdent) throw new NullPointerException("argument 'mediumIdent' is null");
		
		AdvancedUser user = getAdvancedUserOrThrow();
		
		DB_GATE.voteMedium(user.getIdentifier(), mediumIdent, vote, user.getVotes());
	}

	@Override
	public EvComment<Ev, CommentIdentifier> addComment(
			EvComment<EvVoid, EntryClassIdentifier> comment)
			throws EvRequestException, EvIllegalDataException {
		if (null == comment) throw new NullPointerException("argument 'comment' is null");
		
		comment.check();
		
		AdvancedUser user = getAdvancedUserOrThrow();
		
		comment.setAuthor(user.getIdentifier());
		comment.setAuthorPseudonym(user.getPseudonym());
		return DB_GATE.addComment(comment, user.getIdentifier(), user.getVotes());
	}

	@Override
	public void voteComment(CommentIdentifier commentIdent, boolean vote)
			throws EvRequestException {
		if (null == commentIdent) throw new NullPointerException("argument 'commentIdent' is null");
		
		AdvancedUser user = getAdvancedUserOrThrow();
		
		DB_GATE.voteComment(user.getIdentifier(), commentIdent, vote, user.getVotes());
	}

	@Override
	public ArrayList<EvSentence<Ev, SentenceShortIdentifier>> addSentences(
			ArrayList<EvSentence<EvVoid, EntryClassIdentifier>> sentences,
			Long afterPosition, Long beforePosition) throws EvRequestException,
			EvIllegalDataException {
		if (null == sentences) throw new NullPointerException("argument 'sentences' is null");
		
		for (EvSentence<EvVoid, EntryClassIdentifier> sentence : sentences) {
			sentence.check();
		}
		
		AdvancedUser user = getAdvancedUserOrThrow();
		for (EvSentence<EvVoid, EntryClassIdentifier> sentence : sentences) {
			sentence.setAuthor(user.getIdentifier());
		}
		return DB_GATE.addSentences(sentences, afterPosition, beforePosition, user.getIdentifier(), user.getVotes());
	}

	@Override
	public EvSentence<Ev, SentenceIdentifier> addSentenceAlternate(
			EvSentence<EvVoid, EntryClassIdentifier> sentence)
			throws EvRequestException, EvIllegalDataException {
		if (null == sentence) throw new NullPointerException("argument 'sentence' is null");
		
		sentence.check();
		
		AdvancedUser user = getAdvancedUserOrThrow();
		
		sentence.setAuthor(user.getIdentifier());
		return DB_GATE.addSentenceAlternate(sentence, user.getIdentifier(), user.getVotes());
	}

	@Override
	public void voteSentence(SentenceIdentifier sentenceIdent, boolean vote)
			throws EvRequestException {
		if (null == sentenceIdent) throw new NullPointerException("argument 'sentenceIdent' is null");
		
		AdvancedUser user = getAdvancedUserOrThrow();
		
		DB_GATE.voteSentence(user.getIdentifier(), sentenceIdent, vote, user.getVotes());
	}

}