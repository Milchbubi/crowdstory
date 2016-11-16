package eWorld;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import eWorld.database.Gate;
import eWorld.datatypes.data.EvEntry;
import eWorld.datatypes.elementars.WoString;
import eWorld.datatypes.evs.EvVoid;
import eWorld.datatypes.exceptions.EvRequestException;
import eWorld.datatypes.identifiers.EntryClassIdentifier;
import eWorld.datatypes.identifiers.EntryIdentifier;
import eWorld.datatypes.identifiers.UserIdentifier;
import eWorld.datatypes.user.RegisterUser;

public class Shell {
	
	private final Gate gate;
	
	public Shell(Gate gate) {
		assert null != gate;
		
		this.gate = gate;
	}
	
	/**
	 * asks for commands till exitCommand is entered
	 */
	public void doCommands() {
		while (!doCommand());
	}
	
	/**
	 * asks for input and executes it
	 * @return true if exit was entered
	 */
	public boolean doCommand() {
		
		System.out.print("enter input: ");
		
		String[] command = getCommand();
//		printCommand(command);
		
		switch (command[0]) {
		
		case "addUser":
			addUser(command);
			return false;
			
		case "addEntry":
			addEntry(command);
			return false;
		
		case "voteEntry":
			voteEntry(command);
			return false;
		
		case "listEntries":
			listEntries(command);
			return false;
			
		case "exit":
			return true;
			
		default:
			System.out.println("bad input");
			return false;
		}
	}
	
	private String[] getCommand() {
		
//		Scanner scanner = new Scanner(System.in);
//		String input = scanner.nextLine();
//		scanner.close();
		
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
		String input = null;
		try {
			input = bufferedReader.readLine();
		} catch (IOException e) {
			System.out.println("failed to read command");
		}
		
		return input.split(" ");
	}
	
	private void printCommand(String[] command) {
		assert null != command;
		
		for (String s : command) {
			System.out.println("'" + s + "'");
		}
	}
	
	private void addUser(String[] cmd) {
		if (3 != cmd.length) {
			System.out.println("arguments should be: pseudonym, password");
			return;
		}
		
		RegisterUser user = new RegisterUser(cmd[1], cmd[2]);
		
		try {
			gate.registerUser(user);
		} catch (EvRequestException e) {
			System.out.println(e.getMessage());
		}
	}
	
	private void addEntry(String[] cmd) {
		if (7 != cmd.length) {
			System.out.println("arguments should be: userId, classId, name, description, isElement, votes");
			return;
		}
		
		EvEntry<EvVoid, EntryClassIdentifier> entry = new EvEntry<EvVoid, EntryClassIdentifier>(
				new EvVoid(),
				new EntryClassIdentifier(Long.valueOf(cmd[2])), 
				new WoString(cmd[3]), 
				new WoString(cmd[4]),
				Boolean.valueOf(cmd[5]),
				new UserIdentifier(42)
				);
		
		try {
			gate.addEntry(entry, new UserIdentifier(Long.valueOf(cmd[1])), Integer.valueOf(cmd[6]));
		} catch (EvRequestException e) {
			System.out.println(e.getMessage());
		}
	}
	
	private void voteEntry(String[] cmd) {
		if (6 != cmd.length) {
			System.out.println("arguments should be: userId, entryClassId, entryId, vote(true|false), votes");
			return;
		}
		
		try {
			gate.voteEntry(
					new UserIdentifier(Long.valueOf(cmd[1])), 
					new EntryIdentifier(Long.valueOf(cmd[2]),Long.valueOf(cmd[3])), 
					Boolean.valueOf(cmd[4]), 
					Integer.valueOf(cmd[5]));
		} catch (EvRequestException e) {
			System.out.println(e.getMessage());
		}
	}
	
	private void listEntries(String cmd[]) {
		if (3 != cmd.length) {
			System.out.println("arguments should be: userId, entryClassId");
			return;
		}
		
//		EvEntryContainer entryContainer = gate.listEntriesClassic(
//				new UserIdentifier(Long.valueOf(cmd[1])), 
//				new EntryClassIdentifier(Long.valueOf(cmd[2])));
//		
//		System.out.println(entryContainer.toString());
		
		System.out.println("atm not supported");
	}
}
