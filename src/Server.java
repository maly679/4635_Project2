/****
 * COMP 4635 Project 2
 * @author Mohamed A, Erik S, Chad K
 */
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

public class Server {
    private static final String USAGE = "java Server <game_rmi_url>";
    private static final String GAME = "generic";
    private static final String HOST = "localhost";
    public Server(String clientname) {
        try {
            // Register the newly created object at rmiregistry.
            try {
                LocateRegistry.getRegistry(1099).list();
            } catch (RemoteException e) {
                LocateRegistry.createRegistry(1099);
            }
            PhraseGuessingGameServer pggs = new PhraseGuessingGameServerImpl(clientname);
            
            //Create the string URL holding the object's name
    		String rmiObjectName = "rmi://" + HOST + "/" + clientname;
            Naming.rebind(clientname, pggs);
            System.out.println(pggs + " is ready.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        if (args.length > 1 || (args.length > 0 && args[0].equalsIgnoreCase("-h"))) {
            System.out.println(USAGE);
            System.exit(1);
        }

        String clientname;
        if (args.length > 0) {
            clientname = args[0];
        } else {
            clientname = GAME;
        }

        new Server(clientname);
    }
}
