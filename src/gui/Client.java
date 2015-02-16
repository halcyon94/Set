package gui;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.Random;

import javax.swing.UIManager.*;

import com.alee.laf.WebLookAndFeel;
import com.alee.managers.notification.*;

/**
 *	Set GUI tester
 *	@author Dolen Le
 *	@version 1.0
 */
public class Client extends JFrame {

	private int setSize = 3;
	private final int ROWS = setSize;
	private int myId;

	private LoginPanel login;
	private LobbyPanel lobby;
	private GamePanel game;
	
	private ClientConnection connection = new ClientConnection();
	
	public Client() {
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		getContentPane().setLayout(new CardLayout());
		setVisible(true);
		NotificationManager.setMargin(10,10,50,10);
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
					catch (Exception e2) {}
				}
				Client window = new Client();
				window.createLoginFrame();
			}
		});
	}

	//Load and display the login dialog
	private void createLoginFrame() {
		login = new LoginPanel(connection);
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
	private void createLobbyFrame() {
		setTitle("Set Lobby");
		setSize(800, 600);
		setResizable(true);
		setLocationRelativeTo(null);
		ActionListener a1 = new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				createGameFrame();
			}
		};
		lobby = new LobbyPanel(connection);
		lobby.setJoinListener(a1);
		JPanel c = (JPanel) getContentPane();
		c.add(lobby, "LOBBY");
		((CardLayout) c.getLayout()).show(c, "LOBBY");
	}

	//Load and display the game
	private void createGameFrame() {
		game = new GamePanel(1, 3, connection);
		game.addPlayer(23, "test", 453);
		game.addPlayer(34, "sable", 453);
		game.addPlayer(6, "hakner", 453);
		game.addPlayer(2, "fred", 453);
		setTitle("Set");
		setSize(960, 768);
		setResizable(true);
		setLocationRelativeTo(null);
		JPanel c = (JPanel) getContentPane();
		c.setBorder(BorderFactory.createEmptyBorder(3,3,3,3));
		c.add(game, "GAME");
		((CardLayout) c.getLayout()).show(c, "GAME");
	}

	private void addLoginListeners(LoginPanel p) {
		//Listener for login button
		ActionListener a1 = new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				if(login.getUser().isEmpty()) {
					login.showUserPopup("this is garbage");
				} else if(login.getPassword().isEmpty()) {
					login.showPassPopup("geez, you cant leave this empty");
				} else {
					System.out.println("S`"+login.getUser()+"`"+login.getPassword());
					createLobbyFrame();
				}
			}
		};
		//Listener for register button
		ActionListener a2 = new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				System.out.println("R`"+login.getUser()+"`"+login.getPassword());
				createLobbyFrame();
			}
		};
		p.addListeners(a1, a2);
	}
}