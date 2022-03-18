/****
 * COMP 4635 Project 2
 * @author Mohamed A, Erik S, Chad K
 */

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class WordRepositoryServerImpl extends UnicastRemoteObject implements WordRepositoryServer  {
	

	// default constructor for initalizing the WordRepo
	protected WordRepositoryServerImpl() throws RemoteException {
		super();
		// TODO Auto-generated constructor stub
	}

	ArrayList<String> words = new ArrayList<>(); // declaring global arrayList
	
	/* This method generates a random word from the words arrayList
		For the person playing the game to guess
	*/
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
		return generatedWord;
	}
	
	/* This method goes through and populates the words arrayList from the text file
	*/
	public void populateWords() throws RemoteException {
		BufferedReader bufReader = null;
		try {
			bufReader = new BufferedReader(new FileReader("../words.txt"));
		} catch (FileNotFoundException e1) {
			
			e1.printStackTrace();
		} 
		String line = null;
		try {
			line = bufReader.readLine();
		} catch (IOException e) {
			
			e.printStackTrace();
		} 
		while (line != null) { 
			words.add(line); 
		try {
			line = bufReader.readLine();
		} catch (IOException e) {
		
			e.printStackTrace();
		}
		}
		try {
			bufReader.close();
		} catch (IOException e) {
		
			e.printStackTrace();
		}
	}

	// This method adds a word to the arrayList that holds all words in our wordRepo
	public void addWord(String word) throws RemoteException {
		words.add(word);
		
		
	}

	// This method removes a word from the arrayList that holds all words in our wordRepo
	public void removeWord(String word) throws RemoteException {
		words.remove(word);
		
		
	}

	// This method checks if a word is in the arrayList that holds all words in our wordRepo
	public boolean checkWord(String word) throws RemoteException {
		return words.contains(word);
	}

}
