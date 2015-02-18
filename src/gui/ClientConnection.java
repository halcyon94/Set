package gui;

import java.io.IOException;

import communication.SetClient;


//Placeholder for connection class
public class ClientConnection {

	public ClientConnection() {

	}

	public void userLogin(String username, String password) {
		SetClient.sendMessage("S"+username+"`"+password);
		//System.out.println("S"+username+"`"+password);
	}

	//return 2D Object array in format {Integer(id), String(name), Integer(rating)}
	public void refreshUsers(int uid) {
		SetClient.sendMessage("E"+uid);
	}

	//return 2D Object array in format {Integer(id), String(name), String(playerCount)}
	public void refreshGames(int uid) {
		SetClient.sendMessage("G"+uid);
//		Object[][] gameData = {
//		    	{new Integer(23), "CU-NERDS 2", "2/2"},
//		    	{new Integer(5), "FF Crew Bubbies 2", "5/5"},
//		    	{new Integer(2), "Dolen was hier 2", "5/5"}
//		};
	}
	
	public void logout(int uid) {
		SetClient.sendMessage("K"+uid);
	}
	
	public void createGame(int uid, String name) {
		SetClient.sendMessage("C"+uid+"`"+name);
	}
	
	public void leaveGame(int gid, int uid) {
		SetClient.sendMessage("D"+gid+"`"+uid);
	}
	
	public void joinGame(int gid, int uid) {
		SetClient.sendMessage("J"+gid+"`"+uid);
	}
	
	public void lobbyChat(int uid, String msg) {
		SetClient.sendMessage("M"+uid+"`"+msg);
	}
	
	public void beginSet(int uid, int gid) {
		SetClient.sendMessage("B"+gid+"`"+uid);
	}

}
