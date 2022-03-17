

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.StringTokenizer;

public class Client {
	private static final String USAGE = "java Client <game_url> <client_name>";
	private static final String DEFAULT_CLIENT_NAME = "generic";

	PhraseGuessingGameServer pggs;
	game_state gs;
	WordRepositoryServer wrs;
	String clientname;

	static enum CommandName {
		//        newAccount, getAccount, deleteAccount, deposit, withdraw, balance, quit, help, list, getNumberAccounts, getName, 
		startGame, quit, help, getGame, getClient, endGame, getNumWords, getFailedAttempts, getPhrase, restartGame, guessLetter,guessPhrase,addWord,removeWord,checkWord;
	};

	public Client(String clientname) {
		this.clientname = clientname;
		try {
			pggs = (PhraseGuessingGameServer) Naming.lookup(clientname);
		} catch (Exception e) {
			System.out.println("The runtime failed: " + e.getMessage());
			System.exit(0);
		}
		System.out.println("Connected to game: " + clientname);
	}

	public Client() {
		this(DEFAULT_CLIENT_NAME);
	}

	public void run() {
		BufferedReader consoleIn = new BufferedReader(new InputStreamReader(System.in));

		while (true) {
			System.out.print(clientname + "@" + this.clientname + ">");
			try {
				String userInput = consoleIn.readLine();
				execute(parse(userInput));
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
				userName = tokenizer.nextToken();

			case 3:
				try {
					token = tokenizer.nextToken();
					


					numWords = Integer.parseInt(token);

				} catch (NumberFormatException e) {
					//If the entry is in fact a guessed letter, process it as such. This is determined
					//through encountering an exception for parsing the input as integer.
					//System.out.println(commandName.toString());
					if(commandName.toString().contains("Word"))
					{
						System.out.println(commandName.toString());
						
						word = token;
						System.out.println(word);
					}
					else
					{
					entry = token;
					}
					break;
					
				}

			case 4:
				try {
					failedAttempts = Integer.parseInt(tokenizer.nextToken());

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

		switch (command.getCommandName()) {

		case quit:
			System.exit(0);
		case help:
			for (CommandName commandName : CommandName.values()) {
				System.out.println(commandName);
			}
			return;

		}

		// all further commands require a name to be specified
		String userName = command.getUserName();
		if (userName == null) {
			userName = clientname;
		}

		if (userName == null) {
			System.out.println("name is not specified");
			return;
		}

		switch (command.getCommandName()) {
		case startGame:
			clientname = userName;
			pggs.startGame(userName, command.getNumWords(), command.getFailedAttempts());
			break;
		case addWord: 
			System.out.println(command.getWord());
			//pggs.addWord(clientname,command.getWord());
			break;
		case removeWord:
			pggs.removeWord(clientname,command.getWord());
			break;
		case checkWord:
			System.out.println(pggs.checkWord(clientname, command.getWord()));
			break;
		case getGame:
			System.out.println(pggs);
			break;
		case getClient:
			System.out.println(pggs.getName(clientname));
			break;
		case getNumWords:
			System.out.println(pggs.getNumWords(clientname));
			break;
		case getFailedAttempts:
			System.out.println(pggs.getFailedAttempts(clientname));
			break;
		case getPhrase:
			System.out.println(pggs.getPhrase(clientname));
			break;
		case guessLetter:
			System.out.println(pggs.guessLetter(clientname,command.getEntry().charAt(0)));
			break;
		case guessPhrase:
			System.out.println(pggs.guessPhrase(clientname,command.getEntry()));
			break;
		case restartGame:
					String phrase = pggs.getPhrase(clientname);
					//pggs.r(userName);
					System.out.println("Thanks for playing! Your phrase was " + phrase);
					//YOUR SCORE WAS: 
					System.exit(0);
					break;
		case endGame:
			String word = pggs.getPhrase(clientname);
			pggs.endGame(userName);
			System.out.println("Thanks for playing! Your phrase was " + word);
			System.exit(0);
			break;
		default:
			System.out.println("Illegal command");
		}

	}

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

	public static void main(String[] args) {
		if ((args.length > 1) || (args.length > 0 && args[0].equals("-h"))) {
			System.out.println(USAGE);
			System.exit(1);
		}

		String clientname;
		if (args.length > 0) {
			clientname = args[0];
			new Client(clientname).run();
		} else {
			new Client().run();
		}
	}
}
