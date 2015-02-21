package gui;

import communication.SetClient;

/**
 * Implements client to server protocol as defined in Protocol.txt
 * todo: make everything static
 * 
 * @author dolenle
 */
public class ClientConnection {

	public void userLogin(String username, String password) {
		SetClient.sendMessage("S"+username+"`"+password);
	}
	
	public void userRegister(String username, String password) {
		SetClient.sendMessage("R"+username+"`"+password);
	}

	public void refreshUsers(int uid) {
		SetClient.sendMessage("E"+uid);
	}

	public void refreshGames(int uid) {
		SetClient.sendMessage("G"+uid);
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
	
	public void sendChat(int uid, String msg) {
		SetClient.sendMessage("M"+uid+"`"+msg);
	}
	
	public void sendChat(int uid, String msg, int gid) {
		SetClient.sendMessage("M`"+gid+"`"+uid+"`"+msg);
	}
	
	public void beginSet(int uid, int gid) {
		SetClient.sendMessage("B"+gid+"`"+uid);
	}
	
	public void submitSet(int uid, int gid, String cards) {
		SetClient.sendMessage("P"+gid+"`"+uid+"`"+cards);
	}
	
	public void setFail(int uid, int gid) {
		SetClient.sendMessage("F"+gid+"`"+uid);
	}
	
	public void requestScoreBoard(int uid, int gid) {
		System.out.println("A"+gid+"`"+uid);
		SetClient.sendMessage("A"+gid+"`"+uid);
	}

}
