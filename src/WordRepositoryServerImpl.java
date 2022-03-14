import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class WordRepositoryServerImpl extends UnicastRemoteObject implements WordRepositoryServer  {
	


	protected WordRepositoryServerImpl() throws RemoteException {
		super();
		// TODO Auto-generated constructor stub
	}

	ArrayList<String> words = new ArrayList<>();
	
 

//	@Override
//	public boolean createWord(String word) throws RemoteException {
//		// TODO Auto-generated method stub
//		return false;
//	}
//
//	@Override
//	public boolean removeWord(String word) throws RemoteException {
//		// TODO Auto-generated method stub
//		return false;
//	}
//
//	@Override
//	public boolean checkWord(String word) throws RemoteException {
//		// TODO Auto-generated method stub
//		return false;
//	}

	public String getRandomWord(int length) throws RemoteException {
		// TODO Auto-generated method stub
		if (words.size() == 0) {
		populateWords();
		}
		String generatedWord = "";
		for (int i = 0; i < length; i++) 
		{
			// generating the index using Math.random()
			int index = (int) ((Math.random() * (words.size() - 1)) + 1);

			generatedWord = generatedWord + words.get(index).toLowerCase()  + " " ;
		}
		System.out.println(generatedWord);
		return generatedWord;
	}
	
	public void populateWords() throws RemoteException {
		BufferedReader bufReader = null;
		try {
			bufReader = new BufferedReader(new FileReader("../words.txt"));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 
		String line = null;
		try {
			line = bufReader.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		while (line != null) { 
			words.add(line); 
		try {
			line = bufReader.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
		try {
			bufReader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void addWord(String word) throws RemoteException {
		words.add(word);
		// System.out.println(words.toString());
		
	}

	@Override
	public void removeWord(String word) throws RemoteException {
		words.remove(word);
		System.out.println(words.toString());
		
	}

	@Override
	public boolean checkWord(String word) throws RemoteException {
		return words.contains(word);
	}

}
