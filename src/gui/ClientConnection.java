package gui;

import java.io.IOException;

import communication.SetClient;


//Placeholder for connection class
public class ClientConnection {

	public ClientConnection() {

	}

	public String getPlayerName(int id) {
		return null;
	}

	public int getPlayerRating(int id) {
		return 0;
	}

	public void userLogin(String username, String password) {
		SetClient.sendMessage("S"+username+"`"+password);
		System.out.println("S"+username+"`"+password);
	}

	//return 2D Object array in format {Integer(id), String(name), Integer(rating)}
	public void refreshUsers(int uid) {
		SetClient.sendMessage("E"+uid);
	}

	//return 2D Object array in format {Integer(id), String(name), String(playerCount)}
	public void refreshGames(int uid) {
		System.out.println("G"+uid);
		Object[][] gameData = {
		    	{new Integer(23), "CU-NERDS 2", "2/2"},
		    	{new Integer(5), "FF Crew Bubbies 2", "5/5"},
		    	{new Integer(2), "Dolen was hier 2", "5/5"}
		};
	}
	
	public void logout(int uid) {
		SetClient.sendMessage("K"+uid);
	}
	
	public void createGame(int uid) {
		SetClient.sendMessage("C"+uid);
	}

}
