package gui;

import javax.swing.*;

import com.alee.managers.notification.NotificationManager;
import com.alee.managers.notification.WebNotificationPopup;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Stack;

/**
 *	Set Game Panel
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
	
	private Timer t = new Timer(1, null);
	private int timer;
	private Stack<Color> colorList = new Stack<Color>();
	
	private ArrayList<Card> cardList = new ArrayList<Card>(); //FOR TESTING ONLY, I SWEAR!!!
	private ArrayList<Integer> cardIDs = new ArrayList<Integer>();

	/**
	 *	Client GUI frame constructor - Initializes the various frames
	 */
	public GamePanel(int playerID, int setSize) {
		super(new BorderLayout(3,3));
		this.setSize = 3;
		myID = playerID;
		setButton.setHorizontalAlignment(SwingConstants.CENTER);
		buildColorList();
		buildGameGUI();
	}

	//Assembles components of Set game GUI
	private void buildGameGUI() {
		chat = new ChatPanel(80, myID, 0);
		add(players, BorderLayout.NORTH); //add player panel to gui

		buttons.setLayout(new BoxLayout(buttons, BoxLayout.Y_AXIS));
		addGameButtons();
		add(buttons, BorderLayout.EAST); //add button panel to gui
		
		chat.addNelodListener(new ActionListener() { //FOR TESTING ONLY, I SWEAR!!!
			public void actionPerformed(ActionEvent e) {
				if(chat.getMsg().equals("Dolen is the best"))
					nelod(true);
			}
		});
		add(chat, BorderLayout.SOUTH); //add chat panel to gui

		grid = new CardGrid(setSize, 3, setSize, Color.BLACK, myID);
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
			}
		};
		t = new Timer(1000, counter);
		t.setInitialDelay(0);
		t.start();
	}

	//timer countdown for SET
	public void setTimer(final int time) {
		timer = time;
		grid.toggleSelection();
		ActionListener counter = new ActionListener() {
			public void actionPerformed(ActionEvent evt) 
			{
				setButton.setText("<html><center>OK<br>"+timer+"<br>&nbsp;</center></html>");
				timer--;
				if(timer < 0) {
					t.stop();
					submitSet(grid.getSelected());
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
				if(!t.isRunning()) {
					ClientConnection.beginSet(myID, gameID);
				} else {
					submitSet(grid.getSelected());
				}
			}
		});
		buttons.add(setButton); //set button

		logoutButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				ClientConnection.leaveGame(gameID, myID);
				ClientConnection.logout(myID);
			}
		});
		buttons.add(logoutButton); //logout button
		buttons.add(quitButton); //quit button
	}
	
	private void submitSet(ArrayList<Card> selList) {
		if(selList.size() == setSize) {
			setButton.setEnabled(false);
			String ids = "";
			for(Card c : selList)
				ids+=(c.getID(setSize)+"`");
			//System.out.println("[GamePanel] Submitting cards "+ids);
			ClientConnection.submitSet(myID, gameID, ids);
		} else {
			ClientConnection.setFail(myID, gameID);
		}
		t.stop();
		grid.toggleSelection();
		setButton.setText("<html>&nbsp;<br>SET<br>&nbsp;</html>");
		grid.clearSelected();
	}
	
	public void buildColorList() {
		colorList.removeAllElements();
		colorList.add(Color.cyan);
		colorList.add(Color.yellow);
		colorList.add(Color.pink);
		colorList.add(Color.gray);
		colorList.add(Color.magenta);
		colorList.add(Color.orange);
		colorList.add(Color.green);
		colorList.add(Color.red);
		colorList.add(Color.blue);
	}

	//Adds test button panel
	public void addTestButton(JButton b) {
		buttons.add(b);
	}
	
	public void setGameID(int gid) {
		gameID = gid;
		chat.setGameID(gid);
		grid.setGameID(gid);
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
			notify.setIcon(icon);
		NotificationManager.showNotification (notify);
	}
	
	public void addCard(int id) {
		Card c = new Card(id, setSize); //FOR TESTING ONLY, I SWEAR!!!
		cardIDs.add(id);
		cardList.add(c);
		grid.addCard(c);
	}
	
	public void addPlayer(int id, String name, int score, int rating, boolean notify) {
		players.addPlayer(id, colorList.pop(), name, score, rating);
		if(notify)
			showNotification(name+" joined the game.", players.getIcon(id), true);
	}
	
	public void removePlayer(int id) {
		showNotification(players.getName(id)+" left the game.", players.getIcon(id), true);
		colorList.push(players.getColor(id));
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
		cardList.clear(); //FOR TESTING ONLY, I SWEAR!!!
		cardIDs.clear();
		grid.clear();
	}
	
	/**
	 *	cheat function; finds and submits a valid set (if one exists)
	 *	@param block Whether to block other users
	 */
	public void nelod(boolean block) { //FOR TESTING ONLY, I SWEAR!!!
		System.out.println("[GamePanel] Nelod function activated!");
		ArrayList<Card> temp = new ArrayList<Card>();
		if(block)
			ClientConnection.beginSet(myID, gameID);
		outer:
		for(int i=0; i<cardList.size(); i++) {
			temp.clear();
			temp.add(cardList.get(i));
			for(int j=i+1; j<cardList.size(); j++) {
				temp.add(cardList.get(j));
				Card x = grid.completeSet(temp);
				//System.out.println(x.toString()+" i="+i+" j="+j);
				if(cardIDs.contains(x.getID(setSize))) {
					System.out.println("[GamePanel] a set was found");
					temp.add(x);
					submitSet(temp);
					grid.toggleSelection();
					break outer;
				} else {
					temp.remove(temp.size()-1);
				}
			}
		}
	}
}