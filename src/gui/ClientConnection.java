package gui;


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

	public int userLogin(String username, String password) {
		System.out.println("S"+username+"`"+password);
		return 0;
	}

	//return 2D Object array in format {Integer(id), String(name), Integer(rating)}
	public Object[][] refreshUsers(int uid) {
		System.out.println("E"+uid);
		Object[][] userData = {
				{new Integer(1), "Skorpion 2", new Integer(5)},
				{new Integer(2), "Eugene 2", new Integer(3)},
				{new Integer(6), "Sheryan 2", new Integer(8)},
				{new Integer(9), "Dolen 2", new Integer(9000)}
		};
		return userData;
	}

	//return 2D Object array in format {Integer(id), String(name), String(playerCount)}
	public Object[][] refreshGames(int uid) {
		System.out.println("G"+uid);
		Object[][] gameData = {
		    	{new Integer(23), "CU-NERDS 2", "2/2"},
		    	{new Integer(5), "FF Crew Bubbies 2", "5/5"},
		    	{new Integer(2), "Dolen was hier 2", "5/5"}
		};
		return gameData;
	}

}
