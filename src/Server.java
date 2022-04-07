/****
 * COMP 4635 Project 2
 * @author Mohamed A, Erik S, Chad K
 */
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

public class Server {
	private static final String USAGE = "java Server <game_rmi_url>";
	private static final String GAME = "generic";
	private static final String HOST = "localhost";
	private static final int TIMELIMIT_SECONDS = 10;

	private PhraseGuessingGameServerImpl pggs;
	public Server(String clientname) {
		try {
			// Register the newly created object at rmiregistry.
			try {
				LocateRegistry.getRegistry(1099).list();
			} catch (RemoteException e) {
				LocateRegistry.createRegistry(1099);
			}
			pggs = new PhraseGuessingGameServerImpl(clientname);

			//Create the string URL holding the object's name
			String rmiObjectName = "rmi://" + HOST + "/" + clientname;
			Naming.rebind(clientname, pggs);
			System.out.println(pggs + " is ready.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws RemoteException, InterruptedException {
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

		Server thisServer = new Server(clientname);

		while (true) {

			TimeUnit.SECONDS.sleep(TIMELIMIT_SECONDS);
			System.out.print("Prunning the HashMap...");
			@SuppressWarnings("unchecked")
			Iterator<Map.Entry<String, game_state>> it =  thisServer.pggs.getEntrySet();
			if (it == null || it.hasNext() == false) {
				System.out.println(" nothing to do yet!");
				continue;
			}
			int cntAlive = 0, cntDead = 0;
			ArrayList<String> removeList = new ArrayList<>();
			while (it.hasNext()) {
				Map.Entry<String, game_state> pair = it.next();
				String name = pair.getKey();
				game_state r = pair.getValue();
				if (((game_stateImpl) r).getIsActive()) {
					((game_stateImpl) r).setIsActive(false);
					cntAlive++;
				}		        
				else {
					removeList.add(name);
					cntDead++;
				}
			}
			for (String name:removeList)
				thisServer.pggs.removeEntry(name);
			System.out.println("Removed " + cntDead + ", " + cntAlive + " still alive!");
		}
	}
}
