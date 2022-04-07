/****
 * COMP 4635 Project 2
 * @author Mohamed A, Erik S, Chad K
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.StringTokenizer;
import java.util.concurrent.TimeUnit;

public class Client implements Runnable{
	private static final String USAGE = "java Client <game_url>";
	private static final String DEFAULT_CLIENT_NAME = "generic";
	private static final int TIMELIMIT_SECONDS = 5;
	static PhraseGuessingGameServer pggs;
	game_state gs;
	WordRepositoryServer wrs;
	public String userName;

	//Possible commands for game
	static enum CommandName {
		startGame, quit, help, endGame, restartGame, guessLetter, guessPhrase, addWord, removeWord, checkWord;
	};

	//Instantiate Client
	public Client(String userName) {
		this.userName = userName;
		
		
		try {
			
			pggs = (PhraseGuessingGameServer) Naming.lookup(userName);
			pggs.keepMyNameWhileAlive(userName);

	
		} catch (Exception e) {
			System.out.println("The runtime failed: " + e.getMessage());
			System.exit(0);
		}
		System.out.println("Connected to game: " + userName);
	}

	public Client() {
		this(DEFAULT_CLIENT_NAME);
	}

	public void run() {

		while (true) {
			try {
//				String userInput = consoleIn.readLine();
//				execute(parse(userInput));
//				
		
					try {
						TimeUnit.SECONDS.sleep(TIMELIMIT_SECONDS);
						pggs.heartBeat(userName);
					} catch (Exception e) {
						e.printStackTrace();
						break;
					}
				
				
			} catch (Exception re) {
				System.out.println(re);
			}
		}
	}

	private Command parse(String userInput) {
		if (userInput == null) {
			return null;
		}

		StringTokenizer tokenizer = new StringTokenizer(userInput);
		if (tokenizer.countTokens() == 0) {
			return null;
		}

		//Base variables for client program logic
		CommandName commandName = null;
		String userName = null;
		int numWords = 0;
		int failedAttempts = 0;
		int userInputTokenNo = 1;
		String token = null;
		String entry = null;
		String word = null;


		while (tokenizer.hasMoreTokens()) {

			switch (userInputTokenNo) {

			case 1:
				try {
					String commandNameString = tokenizer.nextToken();
					commandName = CommandName.valueOf(CommandName.class, commandNameString);
				} catch (IllegalArgumentException commandDoesNotExist) {
					System.out.println("Illegal command");

					return null;

				}
				break;
			case 2:
				//Process userName..
				userName = tokenizer.nextToken();
				break;
			case 3:
				try {
					token = tokenizer.nextToken();
					numWords = Integer.parseInt(token);

				} catch (NumberFormatException e) {
					//If the entry is in fact a guessed letter, or a word operation, process it as such. 
					//This is determined through encountering an exception for parsing the input as integer.
					if(commandName.toString().contains("Word"))
					{
						word = token;
					}
					else 
					{
						entry = token;

					}
					if (!tokenizer.hasMoreTokens()) {
						break;
					}

				}

			//Manage other possibilities of 4 tokens through some logic.
			case 4:
				try {
					token = tokenizer.nextToken();

					if (commandName.toString().contains("Phrase")) {

						entry = entry + " " + token;

						while(tokenizer.hasMoreTokens()) {
							entry = entry + " " + tokenizer.nextToken();
						}
						System.out.println(entry);
						break;
					} else {
						failedAttempts = Integer.parseInt(token);
					}

				} catch (NumberFormatException e) {
					System.out.println("Illegal amount");
					return null;
				}
				break;
			default:
				System.out.println("Illegal command");
				return null;
			}
			userInputTokenNo++;
		}
		
		return new Command(commandName, userName, numWords, failedAttempts, entry, word);
	}

	void execute(Command command) throws RemoteException {
		if (command == null) {
			return;
		}
		
		// all further commands require a userName to be specified
		String userName = command.getUserName();

		if (userName == null) {
			System.out.println("name is not specified");
			return;
		}

		//Main case for game flow
		switch (command.getCommandName()) {

		case startGame:
			System.out.println(pggs.startGame(userName, command.getNumWords(), command.getFailedAttempts()));
			break;

		case addWord: 
			if (pggs.getName(userName) != null) {

				pggs.addWord(userName,command.getWord());
			} else {
				System.out.println("Invalid username!");
			}
			break;
		case removeWord:
			if (pggs.getName(userName) != null) {

				pggs.removeWord(userName,command.getWord());

			} else {
				System.out.println("Invalid username!");
			}
			break;
		case checkWord:
			if (pggs.getName(userName) != null) {

				System.out.println(pggs.checkWord(userName, command.getWord()));
			} else {
				System.out.println("Invalid username!");
			}
			break;
		case guessLetter:

			if (pggs.getName(userName) != null) {
				System.out.println(pggs.guessLetter(userName,command.getEntry().charAt(0)));
			} else {
				System.out.println("Invalid username!");
			}
			break;
		case guessPhrase:
			if (pggs.getName(userName) != null) {
				System.out.println(pggs.guessPhrase(userName,command.getEntry()));
			} 
			else {
				System.out.println("Invalid username!");
			}
			break;
		case restartGame:
			if (pggs.getName(userName) != null) {

				System.out.println(pggs.restartGame(userName));
			} else {
				System.out.println("Invalid username!");
			}
			break;
		case endGame:
			if (pggs.getName(userName) != null) {
				System.out.println(pggs.endGame(userName));
			} else {
				System.out.println("Invalid username!");
			}
			break;
		default:
			System.out.println("Illegal command");

		}
	}

	//Command parsing
	private class Command {
		private String userName;
		private int numWords;
		private int failedAttempts;
		private String entry;
		private String word;
		private CommandName commandName;


		public Command(Client.CommandName commandName, String userName, int numWords, int failedAttempts, String entry, String word) {
			this.commandName = commandName;
			this.userName = userName;
			this.numWords = numWords;
			this.failedAttempts = failedAttempts;
			this.entry = entry;
			this.word = word;
		}

		public CommandName getCommandName() {
			return this.commandName;
		}

		public String getUserName() {
			return this.userName;
		}

		public String getEntry() {
			return this.entry;
		}

		public int getNumWords() {
			return this.numWords;
		}

		public int getFailedAttempts() {
			return this.failedAttempts;
		}

		public String getWord()
		{
			return this.word;
		}

	}

	public static void main(String[] args) throws RemoteException {
		if ((args.length > 1) || (args.length > 0 && args[0].equals("-h"))) {
			System.out.println(USAGE);
			System.exit(1);
		}

		String userName;
		Client thisClient;

		if (args.length > 0) {
			userName = args[0];
			
			// Create a thread to send heart-beats to the server
			
		 thisClient = new Client(userName);
		
			(new Thread(thisClient)).start();
//			new Client(userName).run();
		} else {
			 thisClient = new Client();
			(new Thread(thisClient)).start();

		}
		while (true) {

			System.out.print(thisClient.userName + "@" + thisClient.userName + ">");
			try {
				BufferedReader consoleIn = new BufferedReader(new InputStreamReader(System.in));

				String userInput = consoleIn.readLine();
				thisClient.execute(thisClient.parse(userInput));
				
		
////					try {
//						TimeUnit.SECONDS.sleep(TIMELIMIT_SECONDS);
//						pggs.heartBeat(userName);
////					} catch (Exception e) {
//						e.printStackTrace();
//						break;
//					}
//				
				
			} catch (Exception re) {
				System.out.println(re);
			}
		}
	}
	
	
}
