import java.rmi.*;
import java.rmi.server.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class RegisterMeImpl extends UnicastRemoteObject implements RegisterMe {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private HashMap <String, ClientStateRecord> clientRecords = new HashMap<>();
	
	public RegisterMeImpl() throws RemoteException {
		// Nothing for now!
	}
	
	public synchronized void keepMyNameWhileAlive(String name) throws RemoteException {
		// If the record for this client does not exist, create and add record
		if (!clientRecords.containsKey(name)) {
			clientRecords.put(name, new ClientStateRecord(name));
		}
	}
	
	public synchronized boolean heartBeat(String name) throws RemoteException {
		ClientStateRecord r = clientRecords.get(name);
		if (r != null) {
			r.setIsActive(true);
		}
		return false;
	}
	
	public synchronized Iterator<Map.Entry<String, ClientStateRecord>> getEntrySet () {
		return clientRecords.entrySet().iterator();
	}
	
	public synchronized void removeClientRecord(String name) {
		clientRecords.remove(name);
	}
}