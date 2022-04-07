import java.rmi.*;

public interface RegisterMe extends Remote {
	public void keepMyNameWhileAlive(String name) throws RemoteException;
	public boolean heartBeat(String name) throws RemoteException;
}
