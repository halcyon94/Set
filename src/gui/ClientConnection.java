package gui;

import communication.SetClient;

/**
 * Implements client to server protocol as defined in Protocol.txt
 * todo: make everything static
 * 
 * @author dolenle
 */
public class ClientConnection {
	
	private ClientConnection() {} //prevent instantiation

	public static void userLogin(String username, String password) {
		SetClient.sendMessage("S"+username+"`"+password);
	}
	
	public static void userRegister(String username, String password) {
		SetClient.sendMessage("R"+username+"`"+password);
	}

	public static void refreshUsers(int uid) {
		SetClient.sendMessage("E"+uid);
	}

	public static void refreshGames(int uid) {
		SetClient.sendMessage("G"+uid);
	}
	
	public static void logout(int uid) {
		SetClient.sendMessage("K"+uid);
	}
	
	public static void createGame(int uid, String name) {
		SetClient.sendMessage("C"+uid+"`"+name);
	}
	
	public static void leaveGame(int gid, int uid) {
		SetClient.sendMessage("D"+gid+"`"+uid);
	}
	
	public static void joinGame(int gid, int uid) {
		SetClient.sendMessage("J"+gid+"`"+uid);
	}
	
	public static void sendChat(int uid, String msg) {
		SetClient.sendMessage("M"+uid+"`"+msg);
	}
	
	public static void sendChat(int uid, String msg, int gid) {		
		SetClient.sendMessage("M`"+gid+"`"+uid+"`"+msg);
	}
	
	public static void beginSet(int uid, int gid) {
		SetClient.sendMessage("B"+gid+"`"+uid);
	}
	
	public static void submitSet(int uid, int gid, String cards) {
		SetClient.sendMessage("P"+gid+"`"+uid+"`"+cards);
	}
	
	public static void setFail(int uid, int gid) {
		SetClient.sendMessage("F"+gid+"`"+uid);
	}
	
	public static void requestScoreBoard(int uid, int gid) {
		//System.out.println("A"+gid+"`"+uid);
		SetClient.sendMessage("A"+gid+"`"+uid);
	}
	
	public static void updateSelection(int uid, int gid, String cards) {
		//System.out.println("T"+gid+"`"+uid+"`"+cards);
		SetClient.sendMessage("T"+gid+"`"+uid+"`"+cards);
	}

}
