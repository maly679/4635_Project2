import java.rmi.*;
import java.util.concurrent.TimeUnit;

public class RegisterMeClient implements Runnable{
	private static final String USAGE =
		"java RegisterMeClient <client name>";
	private static final String HOST = "localhost";
	static String name = "Maryam";
	private static final int TIMELIMIT_SECONDS = 5;
	static RegisterMe registerMe;
	
	public void run () {
		while(true) {
			try {
				TimeUnit.SECONDS.sleep(TIMELIMIT_SECONDS);
				registerMe.heartBeat(name);
			} catch (Exception e) {
				e.printStackTrace();
				break;
			}
		}
	}

	public static void main(String[] args) {
        if (args.length != 1) {
			System.out.println(USAGE);
			System.exit(1);
		}		
		try {
			name = args[0];
			//Obtain a reference to the object from the registry
			registerMe = (RegisterMe)Naming.lookup("rmi://" + HOST + "/RegisterMe");
			
			registerMe.keepMyNameWhileAlive(name);
			
			// Create a thread to send heart-beats to the server
			(new Thread(new RegisterMeClient())).start();
			
			// Do the rest of the client's work...
			System.out.println("Started the heart-beat thread, getting on with the client work!");
			
		} catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
}