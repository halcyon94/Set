package gui;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

import javax.swing.UIManager.*;

import com.alee.laf.WebLookAndFeel;
import com.alee.managers.notification.*;
import communication.SetClient;


/**
 *	Set GUI tester
 *	@author Dolen Le
 *	@version 1.0
 */
public class Client extends JFrame {

	private int setSize = 3;
	private final int ROWS = setSize;
	private int myID = 0;
	private int gameID;
	private boolean gameActive = false;

	private LoginPanel login;
	private LobbyPanel lobby;
	private GamePanel game;
	
	private ClientConnection connection = new ClientConnection();
	
	public Client() {
		setDefaultCloseOperation(HIDE_ON_CLOSE);
		getContentPane().setLayout(new CardLayout());
		setVisible(true);
		NotificationManager.setMargin(10,10,50,10);
		WindowListener exitListener = new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
            	if(myID != 0) {
            		if(gameActive)
            			connection.leaveGame(gameID, myID);
            		connection.logout(myID);
            		System.exit(NORMAL);
            	}
            }
        };
        addWindowListener(exitListener);
	}

	//Main Method
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				WebLookAndFeel.initializeManagers();
				try {
					for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
						if ("Nimbus".equals(info.getName())) {
							UIManager.setLookAndFeel(info.getClassName());
							break;
						}
					}
				} catch (Exception e) {
					try {UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());}
					catch (Exception e2) {System.out.println("Could not set UI LaF!");}
				}
				Client window = new Client();
				window.createLoginFrame();
			}
		});
	}

	//Load and display the login dialog
	public void createLoginFrame() {
		login = new LoginPanel(connection);
		SetClient.Connect(this);
		addLoginListeners(login);
		setTitle("Login to Set");
		setSize(280, 150);
		setResizable(false);
		setLocationRelativeTo(null);
		getRootPane().setDefaultButton(login.getButt());
		JPanel c = (JPanel) getContentPane();
		c.add(login, "LOGIN");
		((CardLayout) c.getLayout()).show(c, "LOGIN");
	}

	//Load and display the game lobby
	public void createLobbyFrame() {
		setTitle("Set Lobby");
		setSize(800, 600);
		setResizable(true);
		setLocationRelativeTo(null);
		ActionListener joinListener = new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				connection.joinGame(lobby.visibleGame, myID);
			}
		};
		getRootPane().setDefaultButton(null);
		lobby = new LobbyPanel(connection, myID);
		lobby.joinButton.addActionListener(joinListener);
		JPanel c = (JPanel) getContentPane();
		c.add(lobby, "LOBBY");
		((CardLayout) c.getLayout()).show(c, "LOBBY");
	}

	//Load and display the game
	private void createGameFrame() {
		if(myID != 0) {
			game = new GamePanel(myID, 3, connection);
			gameActive = true;
			setTitle("Set");
			setSize(960, 768);
			setResizable(true);
			setLocationRelativeTo(null);
			JPanel c = (JPanel) getContentPane();
			c.setBorder(BorderFactory.createEmptyBorder(3,3,3,3));
			c.add(game, "GAME");
			((CardLayout) c.getLayout()).show(c, "GAME");
		} else {
			System.err.println("Error: Attempted to start game without logging in");
		}
	}

	private void addLoginListeners(LoginPanel p) {
		//Listener for login button
		ActionListener a1 = new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				String user = login.getUser();
				String pass = login.getPassword();
				if(user.isEmpty()) {
					login.showUserPopup("this is garbage");
				} else if(pass.isEmpty()) {
					login.showPassPopup("geez, you cant leave this empty");
				} else {
					connection.userLogin(user, pass);
				}
			}
		};
		//Listener for register button
		ActionListener a2 = new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				createLobbyFrame();
			}
		};
		p.addListeners(a1, a2);
	}
	
	public void setUser(int id) {
		myID = id;
	}
	
	public void badLogin() {
		SetClient.Connect(this);
		login.showPassPopup("NO! WRONG!");
	}

	public void enterGame(int[] cardIDs) {
		if(!gameActive) {
			createGameFrame();
			for(int id : cardIDs) {
				game.addCard(id);
			}
		} else {
			System.err.println("ERROR - attempted to join new game when already in game");
		}
	}
	
	public LobbyPanel getLobbyPanel() {
		return lobby;
	}
	
	public GamePanel getGamePanel() {
		return game;
	}
	
	public void setGameID(int gid) {
		gameID = gid;
	}
}