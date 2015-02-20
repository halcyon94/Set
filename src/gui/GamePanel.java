package gui;

import javax.swing.*;

import com.alee.managers.notification.NotificationManager;
import com.alee.managers.notification.WebNotificationPopup;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

/**
 *	Set GUI tester
 *	@author Dolen Le
 *	@version 1.0
 */
public class GamePanel extends JPanel {

	private int setSize;
	private int myID;
	private int gameID;

	private PlayerPanel players = new PlayerPanel();
	private ChatPanel chat;
	private JPanel buttons = new JPanel(); //button panel
	private CardGrid grid;
	
	public JButton logoutButton = new JButton("Logout");
	public JButton setButton = new JButton("<html>&nbsp;<br>SET<br>&nbsp;</html>");
	public JButton quitButton = new JButton("Quit");
	
	private ClientConnection connection;

	private Timer t = new Timer(1, null);
	private int timer;
	public int colorIndex = 0;
	private Color[] colors = new Color[] {Color.blue, Color.red, Color.green,
												Color.orange, Color.magenta, Color.gray, 
												Color.pink, Color.yellow, Color.cyan};


	/**
	 *	Client GUI frame constructor - Initializes the various frames
	 */
	public GamePanel(int playerID, int setSize, ClientConnection connection) {
		super(new BorderLayout(3,3));
		this.setSize = 3;
		myID = playerID;
		this.connection = connection;
		setButton.setHorizontalAlignment(SwingConstants.CENTER);
		buildGameGUI();
	}
	
	public void addCard(int id) {
		grid.addCard(new Card(id, setSize));
	}
	
	public void addPlayer(int id, String name, int score, int rating) {
		players.addPlayer(id, colors[colorIndex++], name, score, rating);
	}
	
	public void removePlayer(int id) {
		players.removePlayer(id);
	}
	
	public void increaseScore(int id) {
		players.increaseScore(id);
	}
	
	public void decreaseScore(int id) {
		players.increaseScore(id);
	}
	
	public CardGrid getCards() {
		return grid;
	}
	
	public PlayerPanel getPlayers() {
		return players;
	}
	
	public ChatPanel getChat() {
		return chat;
	}
	
	public void clearGrid() {
		grid.clear();
	}

	//Assembles components of Set game GUI
	private void buildGameGUI() {
		chat = new ChatPanel(80, connection, myID);
		add(players, BorderLayout.NORTH); //add player panel to gui

		buttons.setLayout(new BoxLayout(buttons, BoxLayout.Y_AXIS));
		addGameButtons();
		add(buttons, BorderLayout.EAST); //add button panel to gui
		
		add(chat, BorderLayout.SOUTH); //add chat panel to gui

		grid = new CardGrid(setSize, 3, setSize, Color.BLACK);
		add(grid, BorderLayout.CENTER); //add card grid to gui
	}

	/**
	 * Disables the player's SET button for the specified amount of time.
	 * @param time Time (in seconds) to disable the button
	 */
	public void blockTimer(final int time) {
		timer = time;
		setButton.setEnabled(false);
		ActionListener counter = new ActionListener() {
			public void actionPerformed(ActionEvent evt) 
			{ 
				setButton.setText("<html>&nbsp;<br>"+timer+"<br>&nbsp;</html>");
				timer--;
				if(timer < 0) {
					t.stop();
					setButton.setText("<html>&nbsp;<br>SET<br>&nbsp;</html>");
					setButton.setEnabled(true);
				}
			}};
			t = new Timer(1000, counter);
			t.setInitialDelay(0);
			t.start();
	}

	//timer countdown for SET
	private void setTimer(final int time) {
		timer = time;
		grid.toggleSelection();
		ActionListener counter = new ActionListener() {
			public void actionPerformed(ActionEvent evt) 
			{
				setButton.setText("<html><center>OK<br>"+timer+"<br>&nbsp;</center></html>");
				timer--;
				if(timer < 0) {
					submitSet();
				}
			}
		};
		t = new Timer(1000, counter);
		t.setInitialDelay(0);
		t.start();
	}

	//Add in-game buttons to the provided panel
	private void addGameButtons() {
		setButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				System.out.println(t.isRunning());
				if(!t.isRunning()) {
					connection.beginSet(myID, gameID);
					setTimer(5);
				} else {
					submitSet();
				}
			}
		});
		buttons.add(setButton); //set button

		logoutButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				connection.leaveGame(gameID, myID);
				connection.logout(myID);
			}
		});
		buttons.add(logoutButton); //logout button
		buttons.add(quitButton); //quit button
	}
	
	private void submitSet() {
		ArrayList<Card> selList = grid.getSelected();
		if(selList.size() == setSize) {
			setButton.setEnabled(false);
			String ids = "";
			for(Card c : selList)
				ids+=(c.getID(setSize)+"`");
			connection.submitSet(myID, gameID, ids);
		} else {
			connection.setFail(myID, gameID);
		}
		t.stop();
		grid.toggleSelection();
		setButton.setText("<html>&nbsp;<br>SET<br>&nbsp;</html>");
		grid.clearSelected();
	}

	//Adds test button panel
	public void addTestButton(JButton b) {
		buttons.add(b);
	}
	
	public void setGameID(int gid) {
		gameID = gid;
	}
	
	public void setSelectionColor(Color c) {
		grid.setPlayerColor(c);
	}
	
	public void setPlayerColor() {
		grid.setPlayerColor(players.getColor(myID));
	}
	
	public void unblockButton() {
		t.stop();
		setButton.setEnabled(true);
		setButton.setText("<html>&nbsp;<br>SET<br>&nbsp;</html>");
	}
	
	public void showNotification(String message, Icon icon, boolean hasIcon) {
		WebNotificationPopup notify = new WebNotificationPopup();
		notify.setDisplayTime(3000);
		notify.setContent(message);
		if(hasIcon && icon!=null)
			notify.setIcon(players.getIcon(1));
		NotificationManager.showNotification (notify);
	}
}