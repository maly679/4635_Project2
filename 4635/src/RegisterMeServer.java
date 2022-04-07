import java.rmi.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class RegisterMeServer {
private static final String HOST = "localhost";
private static final int TIMELIMIT_SECONDS = 10;

	public static void main(String[] args) throws Exception {
		//Create a reference to an implementation object...
		RegisterMeImpl registerMeServer = new RegisterMeImpl();
		
		//Create the string URL holding the object's name...
		String rmiObjectName = "rmi://" + HOST + "/RegisterMe";
		
		//'Bind' the object reference to the name...
		Naming.rebind(rmiObjectName, registerMeServer);
		System.out.println("Binding complete...\n");
		
		// In an infinite loop, checks if clients are still alive
		// Server expects a message from the client at least every TIMELIMIT_SECONDS
		// Sleeps for TIMELIMIT_SECONDS, checks if the client has sent a heart-beat
		// Resets the heart bit flag for those still alive
		// Removes the record for clients who failed to send a heart-beat
		while (true) {
			TimeUnit.SECONDS.sleep(TIMELIMIT_SECONDS);
			System.out.print("Prunning the HashMap...");
		    Iterator<Map.Entry<String, ClientStateRecord>> it = registerMeServer.getEntrySet();
		    if (it == null || it.hasNext() == false) {
		    	System.out.println(" nothing to do yet!");
		    	continue;
		    }
	    	int cntAlive = 0, cntDead = 0;
	    	ArrayList<String> removeList = new ArrayList<>();
		    while (it.hasNext()) {
		        Map.Entry<String, ClientStateRecord> pair = it.next();
		        String name = pair.getKey();
		        ClientStateRecord r = pair.getValue();
		        if (r.getIsActive()) {
		        	r.setIsActive(false);
		        	cntAlive++;
		        }		        
		        else {
		        	removeList.add(name);
		        	cntDead++;
		        }
		    }
		    for (String name:removeList)
		    	registerMeServer.removeClientRecord(name);
	        System.out.println("Removed " + cntDead + ", " + cntAlive + " still alive!");
		}
	}
}