

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
    String clientname;

    static enum CommandName {
//        newAccount, getAccount, deleteAccount, deposit, withdraw, balance, quit, help, list, getNumberAccounts, getName, 
    	startGame, quit, help, getGame, getClient;
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
//        float amount = 0;
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
                    break;
//                case 3:
//                    try {
//                        amount = Float.parseFloat(tokenizer.nextToken());
//                    } catch (NumberFormatException e) {
//                        System.out.println("Illegal amount");
//                        return null;
//                    }
//                    break;
                default:
                    System.out.println("Illegal command");
                    return null;
            }
            userInputTokenNo++;
        }
        return new Command(commandName, userName);
    }

    void execute(Command command) throws RemoteException {
        if (command == null) {
            return;
        }

        switch (command.getCommandName()) {
//            case list:
//                try {
//                    for (String accountHolder : bankobj.listAccounts()) {
//                        System.out.println(accountHolder);
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    return;
//                }
//                return;
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
                pggs.startGame(userName, 4, 4);
                return;
//            case deleteAccount:
//                clientname = userName;
//                bankobj.deleteAccount(userName);
//                return;
        }

        // all further commands require a Account reference
//        game_state gs2 = pggs.getPlayer(userName);
//        if (gs2 == null) {
//            System.out.println("No account for " + userName);
//            return;
//        } else {
//            gs = gs2;
//            clientname = userName;
//        }

        switch (command.getCommandName()) {
            case getGame:
                System.out.println(pggs);
                break;
            case getClient:
                System.out.println(pggs.getClient(this.clientname));
                break;
//            case withdraw:
//                account.withdraw(command.getAmount());
//                break;
//            case balance:
//                System.out.println("balance: $" + account.getBalance());
//                break;   
//            case getName:
//                System.out.println("Name: " + account.getName());
//                break;
//            case getNumberAccounts:
//                System.out.println("# Accounts: " + bankobj.getNumberAccounts());
//                break;
            default:
                System.out.println("Illegal command");
        }
    }

    private class Command {
        private String userName;
//        private float amount;
        private CommandName commandName;

        private String getUserName() {
            return userName;
        }

//        private float getAmount() {
//            return amount;
//        }

        private CommandName getCommandName() {
            return commandName;
        }

        private Command(Client.CommandName commandName, String userName) {
            this.commandName = commandName;
            this.userName = userName;
//            this.amount = amount;
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
