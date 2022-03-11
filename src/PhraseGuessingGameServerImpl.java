import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;

public class PhraseGuessingGameServerImpl extends UnicastRemoteObject  implements PhraseGuessingGameServer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	HashMap<String, game_state> game_states = new HashMap<>();
	String phrase;
	String clientname;
	public PhraseGuessingGameServerImpl(String clientname) throws RemoteException {
		super();
		this.clientname = clientname;
		// TODO Auto-generated constructor stub
	}

	@Override
	public String startGame(String player, int number_of_words, int failed_attempt_factor) throws RemoteException {
		// TODO Auto-generated method stub
		game_state gs = new game_stateImpl(player);
		gs.setNumWords(number_of_words);
		gs.setFailedAttempts(failed_attempt_factor);
		gs.setPhrase();
		game_states.put(player, gs);
		return player;
	}

	@Override
	public String guessLetter(String player, char letter) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String guessPhrase(String player, String phrase) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String endGame(String player) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String restartGame(String player) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	//	@Override
	//	public String addWord() throws RemoteException {
	//		// TODO Auto-generated method stub
	//		return null;
	//	}
	//
	//	@Override
	//	public String removeWord() throws RemoteException {
	//		// TODO Auto-generated method stub
	//		return null;
	//	}
	//
	//	@Override
	//	public String checkWord() throws RemoteException {
	//		// TODO Auto-generated method stub
	//		return null;
	//	}
	//	
	@Override
	public synchronized String getName(String client) throws RemoteException  {
		return game_states.get(client).getName();
	}

	public synchronized String getPhrase(String client) throws RemoteException  {
		return game_states.get(client).getPhrase();
	}
	public synchronized int getNumWords (String client) throws RemoteException {

		return game_states.get(client).getNumWords();

	}

	@Override
	public int getFailedAttempts(String client) throws RemoteException {
		// TODO Auto-generated method stub
		return game_states.get(client).getFailedAttempts();
	}



}
