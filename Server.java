

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class Server {
    private static final String USAGE = "java Server <game_rmi_url>";
    private static final String GAME = "RBC";
    private static final String HOST = "localhost";
    public Server(String player) {
        try {
            // Register the newly created object at rmiregistry.
            try {
                LocateRegistry.getRegistry(1099).list();
            } catch (RemoteException e) {
                LocateRegistry.createRegistry(1099);
            }
            
            PhraseGuessingGameServer gameobj = new PhraseGuessingGameServerImpl(player);
            
            //Create the string URL holding the object's name
    		String rmiObjectName = "rmi://" + HOST + "/" + player;
            Naming.rebind(player, gameobj);
            System.out.println(gameobj + " is ready.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        if (args.length > 1 || (args.length > 0 && args[0].equalsIgnoreCase("-h"))) {
            System.out.println(USAGE);
            System.exit(1);
        }

        String player;
        if (args.length > 0) {
            player = args[0];
        } else {
            player = GAME;
        }

        new Server(player);
    }
}
