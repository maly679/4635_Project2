

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
		startGame, quit, help, getGame, getClient, endGame, getNumWords, getFailedAttempts, getPhrase;
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
					numWords = Integer.parseInt(tokenizer.nextToken());
				} catch (NumberFormatException e) {
					System.out.println("Illegal amount");
					return null;
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
		return new Command(commandName, userName, numWords, failedAttempts);
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
		default:
			System.out.println("Illegal command");
		}

	}

	private class Command {
		private String userName;
		private int numWords;
		private int failedAttempts;
		private CommandName commandName;


		public Command(Client.CommandName commandName, String userName, int numWords, int failedAttempts) {
			this.commandName = commandName;
			this.userName = userName;
			this.numWords = numWords;
			this.failedAttempts = failedAttempts;
		}

		public CommandName getCommandName() {
			return this.commandName;
		}

		public String getUserName() {
			return this.userName;
		}

		public int getNumWords() {
			return this.numWords;
		}

		public int getFailedAttempts() {
			return this.failedAttempts;
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
