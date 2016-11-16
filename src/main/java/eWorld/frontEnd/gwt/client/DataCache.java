package eWorld.frontEnd.gwt.client;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;

import eWorld.datatypes.EvPath;
import eWorld.datatypes.data.EvComment;
import eWorld.datatypes.data.EvEntry;
import eWorld.datatypes.data.EvMedium;
import eWorld.datatypes.data.EvSentence;
import eWorld.datatypes.evs.Ev;
import eWorld.datatypes.evs.EvVoid;
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
import eWorld.datatypes.user.RegisterUser;
import eWorld.datatypes.user.SignInUser;
import eWorld.datatypes.user.User;

public class DataCache {
	
	/** Create a remote service proxy to talk to the server-side GeneralService. */
	private final GeneralServiceAsync generalService = GWT.create(GeneralService.class);
	
	/** stores the user that is signed in, null if not signed in */
	private User<UserIdentifier> signedInUser = null;
	
	public void register(RegisterUser user, EvAsyncCallback<Void> callback) {
		generalService.register(user, callback);
	}
	
	public void signIn(SignInUser user, final EvAsyncCallback<User<UserIdentifier>> callback) {
		generalService.signIn(user, new EvAsyncCallback<User<UserIdentifier>>() {
			@Override
			public void onSuccess(User<UserIdentifier> result) {
				signedInUser = result;
				callback.onSuccess(result);
			}
		});
	}
	
	public void getSignedInUser(final EvAsyncCallback<User<UserIdentifier>> callback) {
		generalService.getSignedInUser(new EvAsyncCallback<User<UserIdentifier>>() {
			@Override
			public void onSuccess(User<UserIdentifier> result) {
				signedInUser = result;
				callback.onSuccess(result);
			}
		});
	}
	
	public void signOut(final EvAsyncCallback<Void> callback) {
		generalService.signOut(new EvAsyncCallback<Void>() {
			@Override
			public void onSuccess(Void result) {
				signedInUser = null;
				callback.onSuccess(result);
			}
		});
	}
	
	public void getRootEntry(EvAsyncCallback<EvEntry<Ev, EntryIdentifier>> callback) {
		generalService.getRootEntry(callback);
	}
	
	public void getStartEntry(EvAsyncCallback<EvEntry<Ev, EntryIdentifier>> callback) {
		generalService.getStartEntry(callback);
	}
	
	public void getEntry(EntryShortIdentifier identifier, EvAsyncCallback<EvEntry<Ev, EntryIdentifier>> callback) {
		generalService.getEntry(identifier, callback);
	}
	
	public void getRootPackage(EvAsyncCallback<RootPackage> callback) {
		generalService.getRootPackage(callback);
	}
	
	@Deprecated
	public void getStartEntryPackage(EvAsyncCallback<EvEntryPackage> callback) {
		generalService.getStartEntryPackage(callback);
	}
	
	public void getEntryPackage(EntryClassIdentifier identifier, EvAsyncCallback<EvEntryPackage> callback) {
		getEntryPackage(identifier, false, callback);
	}
	public void getEntryPackage(final EntryClassIdentifier identifier, boolean force, final EvAsyncCallback<EvEntryPackage> callback) {
		// TODO use the force not anytime
		generalService.getEntryPackage(identifier, new EvAsyncCallback<EvEntryPackage>() {
			// on cold start it happens sometimes that getEntryPackage(..) doesn't work
			// try it again then FIXME dirty workaround (doesn't even work properly)
			@Override
			public void onSuccess(EvEntryPackage result) {
				callback.onSuccess(result);
			}
			@Override
			public void onFailure(Throwable caught) {
				EvApp.INFO.addProblem(caught.getMessage());
				EvApp.INFO.addProblem("failed to getEntryPackage with id " + identifier.getEntryClassId() + ", workaround...");
				new Timer() {
					@Override
					public void run() {
						generalService.getEntryPackage(identifier, callback);
					}
				}.schedule(3000);
			}
		});
	}

	public void addEntry(EvEntry<EvVoid, EntryClassIdentifier> entry, EvAsyncCallback<EvEntry<Ev, EntryIdentifier>> callback) {
		generalService.addEntry(entry, callback);
	}
	
	public void linkEntry(EntryIdentifier source, EntryClassIdentifier destination, EvAsyncCallback<EvEntry<Ev, EntryIdentifier>> callback) {
		generalService.linkEntry(source, destination, callback);
	}
	
	public void voteEntry(EntryIdentifier entryIdent, boolean vote, EvAsyncCallback<Void> callback) {
		// TODO cache them if often voted and send a list to the server
		generalService.voteEntry(entryIdent, vote, callback);
	}
	
	public void getPath(EntryShortIdentifier identifier, EvAsyncCallback<EvPath> callback) {
		generalService.getPath(identifier, callback);
	}
	
	public void getElement(EntryClassIdentifier classIdentifier, EntryShortIdentifier valueIdentifier, EvAsyncCallback<EvElement> callback) {
		getElement(classIdentifier, valueIdentifier, false, callback);
	}
	public void getElement(EntryClassIdentifier classIdentifier, EntryShortIdentifier valueIdentifier, boolean force, EvAsyncCallback<EvElement> callback) {
		// TODO use the force not anytime
		generalService.getElement(classIdentifier, valueIdentifier, callback);
	}
	
	public void addMedium(EvMedium<EvVoid, EntryClassIdentifier> medium, EvAsyncCallback<EvMedium<Ev, MediumIdentifier>> callback){
		generalService.addMedium(medium, callback);
	}
	
	public void voteMedium(MediumIdentifier mediumIdent, boolean vote, EvAsyncCallback<Void> callback){
		// TODO cache them if often voted and send a list to the server
		generalService.voteMedium(mediumIdent, vote, callback);
	}
	
	public void addComment(EvComment<EvVoid, EntryClassIdentifier> comment, EvAsyncCallback<EvComment<Ev, CommentIdentifier>> callback){
		generalService.addComment(comment, callback);
	}
	
	public void voteComment(CommentIdentifier commentIdent, boolean vote, EvAsyncCallback<Void> callback){
		// TODO cache them if often voted and send a list to the server
		generalService.voteComment(commentIdent, vote, callback);
	}
	
	public void addSentences(ArrayList<EvSentence<EvVoid, EntryClassIdentifier>> sentences, Long afterPosition, Long beforePosition, EvAsyncCallback<ArrayList<EvSentence<Ev, SentenceShortIdentifier>>> callback) {
		generalService.addSentences(sentences, afterPosition, beforePosition, callback);
	}
	
	public void addSentenceAlternate(EvSentence<EvVoid, EntryClassIdentifier> sentence, EvAsyncCallback<EvSentence<Ev, SentenceIdentifier>> callback) {
		generalService.addSentenceAlternate(sentence, callback);
	}
	
	public void voteSentence(SentenceIdentifier sentenceIdent, boolean vote, EvAsyncCallback<Void> callback) {
		// TODO cache them if often voted and send a list to the server
		generalService.voteSentence(sentenceIdent, vote, callback);
	}
}
