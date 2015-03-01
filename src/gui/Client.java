package gui;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

import javax.swing.UIManager.*;

import com.alee.laf.WebLookAndFeel;
import com.alee.managers.notification.*;

import communication.SetClient;


/**
 *	Set Client main class
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
	private Clip wongsound;
	
	/**
	 * Constructor for Client
	 */
	public Client() {
		setDefaultCloseOperation(HIDE_ON_CLOSE);
		getContentPane().setLayout(new CardLayout());
		setVisible(true);
		NotificationManager.setMargin(10,10,50,10);
		WindowListener exitListener = new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
            	if(myID != 0) {
            		if(gameActive) {
            			if(game.isSetRunning())
            				ClientConnection.setFail(myID, gameID);
            			ClientConnection.leaveGame(gameID, myID);
            		}
            		ClientConnection.logout(myID);
            	}
            	System.exit(NORMAL);
            }
        };
        addWindowListener(exitListener);
        //open wrong login sound
        try {
        	AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(getClass().getResource("wrong.wav"));
        	wongsound = AudioSystem.getClip();
        	wongsound.open(audioInputStream);
        } catch (Exception e) {
        	System.err.println("Could not open WONGSOUND");
        }
	}

	/**
	 * Main method for clientside Set program
	 * @param args Command line arguments
	 */
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

	/**
	 * Initializes and displays the login screen. Call this upon logout.
	 */
	public void createLoginFrame() {
		myID = 0;
		gameID = 0;
		gameActive = false;
		NotificationManager.hideAllNotifications();
		if(login!=null)
			login.detachListeners(); //prevent memory leaks here
		login = new LoginPanel();
		SetClient.Connect(this);
		addLoginListeners(login);
		setTitle("Login to Set");
		setSize(280, 150);
		setResizable(false);
		setLocationRelativeTo(null);
		gameActive = false;
		getRootPane().setDefaultButton(login.getButt());
		JPanel c = (JPanel) getContentPane();
		c.add(login, "LOGIN");
		((CardLayout) c.getLayout()).show(c, "LOGIN");
	}

	/**
	 * Initializes and displays the game lobby.
	 */
	public void createLobbyFrame() {
		setTitle("Set Lobby");
		setSize(800, 600);
		setResizable(true);
		setLocationRelativeTo(null);
		ActionListener joinListener = new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				ClientConnection.joinGame(lobby.visibleGame, myID);
			}
		};
		getRootPane().setDefaultButton(null);
		gameActive = false;
		lobby = new LobbyPanel(myID);
		lobby.joinButton.addActionListener(joinListener);
		JPanel c = (JPanel) getContentPane();
		c.add(lobby, "LOBBY");
		((CardLayout) c.getLayout()).show(c, "LOBBY");
	}

	//Load and display the game
	private void createGameFrame() {
		if(myID != 0) {
			game = new GamePanel(myID, 3);
			gameActive = true;
			game.quitButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent event) {
					game.clearGrid();
					if(game.isSetRunning())
        				ClientConnection.setFail(myID, gameID);
					ClientConnection.leaveGame(gameID, myID);
					gameActive = false;
					lobby.resetInfoPanel();
					JPanel c = (JPanel) getContentPane();
					((CardLayout) c.getLayout()).show(c, "LOBBY");
				}
			});
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
					ClientConnection.userLogin(user, pass);
				}
			}
		};
		//Listener for register button
		ActionListener a2 = new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				String user = login.getUser();
				String pass = login.getPassword();
				if(user.isEmpty()) {
					login.showUserPopup("Please enter username.");
				} else if(pass.isEmpty()) {
					login.showPassPopup("Enter a password, you lazy basstord");
				} else {
					ClientConnection.userRegister(user, pass);
				}
			}
		};
		p.addListeners(a1, a2);
	}
	
	/**
	 * Set the current player ID
	 * @param id User id
	 */
	public void setUser(int id) {
		myID = id;
	}
	
	/**
	 * Displays incorrect credentials warning.
	 */
	public void badLogin() {
		SetClient.Connect(this); //reset connection
		login.showPassPopup("NO! WRONG!");
		wongsound.setFramePosition(0);
		wongsound.start();
	}
	
	/**
	 * Displays existing user warning.
	 */
	public void userExists() {
		SetClient.Connect(this); //reset connection
		login.showUserPopup("This username is taken.");
	}
	
	/**
	 * Displays already online user warning.
	 */
	public void userOnline() {
		SetClient.Connect(this); //reset connection
		login.showUserPopup("This user is already online.");
	}
	
	/**
	 * Checks if the user is currently in-game.
	 * @return if the game is running.
	 */
	public boolean isGameActive() { 
		return gameActive;
	}
	
	/**
	 * Creates and loads a game, or reloads the cards from the given array
	 * @param cardIDs array containing card IDs
	 */
	public void enterGame(int[] cardIDs) {
		if(!gameActive) {
			createGameFrame();
			for(int id : cardIDs) {
				game.addCard(id);
			}
		} else {
			game.clearGrid();
			for(int id : cardIDs) {
				game.addCard(id);
			}
			game.repaint();
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
	
	public int getPlayerID() {
		return myID;
	}
	
	public boolean isPlayerValid(int id) {
		return myID == id;
	}
	
	public boolean isGameValid(int id) {
		return gameActive && gameID == id;
	}
}
