package gui;

import java.util.ArrayList;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

/**
 *	Set GUI in-game Player panel. Shows player names, stats and scores.
 *	@author Dolen Le
 *	@version 1.0
 */
public class LobbyPanel extends JPanel {	
	
	private static int COL_WIDTH = 200; //Table width
	
	private JPanel games;
	private JPanel users;
	private JPanel info;
	private ChatPanel chat;
	private ClientConnection connection;
	private int myID;
	public int visibleGame;
	
	private JTable userTable;
	private JTable gameTable;
	private TableModel userModel;
	private TableModel gameModel;
	
	private JButton refreshUsers = new JButton("Refresh");
	private JButton refreshGames = new JButton("Refresh");
	private JButton createButton = new JButton("Create");
	public JButton createGame = new JButton("Create Game");
	public JButton logoutButton = new JButton("Logout");
	public JButton joinButton = new JButton("Join Game");
	private JButton msgButton = new JButton("Send Message");
	
	private JTextField nameField = new JTextField(20);
		
	String[] userColumns = {"ID", "Name",
                        	"Ranking"};
    String[] gameColumns = {"ID", "Name",
                        	"Players"};
                        	
	Object[][] userData = {
    	{new Integer(1), "Skorpion", new Integer(5)},
    	{new Integer(2), "Eugene", new Integer(3)},
    	{new Integer(6), "Sheryan", new Integer(8)},
    	{new Integer(9), "Dolen", new Integer(9000)}
    };
    Object[][] gameData = {
    	{new Integer(23), "CU-NERDS", "2/2"},
    	{new Integer(5), "FF Crew Bubbies", "2/5"},
    	{new Integer(2), "Dolen was hier", "1/5"}
    };
	
	/**
	 *	Lobby screen constructor
	 */
	public LobbyPanel(ClientConnection conn, final int uid) {
		//intialize
		super(new BorderLayout(5,5));
		setBorder(BorderFactory.createEmptyBorder(3,3,3,3));
		games = new JPanel();
		games.setLayout(new BoxLayout(games, BoxLayout.Y_AXIS));
		users = new JPanel();
		users.setLayout(new BoxLayout(users, BoxLayout.Y_AXIS));
		info = new JPanel(new GridLayout(1,1,0,0));
		this.connection = conn;
		this.myID = uid;
				
		//build games panel
		gameModel = new DefaultTableModel(gameData, gameColumns);
		gameTable = new JTable(gameModel) {
			@Override //Disable editing
			public boolean isCellEditable(int r, int c) {return false;}
		};
		formatTable(gameTable);
		JScrollPane scrollPane2 = new JScrollPane(gameTable);
		scrollPane2.setPreferredSize(new Dimension(COL_WIDTH, 200));
		JLabel gamesLabel = new JLabel("Games Listing");
		gamesLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		games.add(gamesLabel);
		games.add(scrollPane2);
		JPanel temp = new JPanel();
		temp.add(refreshGames);
		temp.add(createButton);
		temp.setAlignmentX(Component.CENTER_ALIGNMENT);
		temp.setMaximumSize(new Dimension(COL_WIDTH, 10));
		games.add(temp);
		add(games, BorderLayout.WEST);
		
		//build users panel
		userModel = new DefaultTableModel(userData, userColumns);
		userTable = new JTable(userModel) {
			@Override //Disable editing
			public boolean isCellEditable(int r, int c) {return false;}
		};
		formatTable(userTable);
		JScrollPane scrollPane1 = new JScrollPane(userTable);
		scrollPane1.setPreferredSize(new Dimension(COL_WIDTH, 200));
		JLabel usersLabel = new JLabel("Users Online");
		usersLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		refreshUsers.setAlignmentX(Component.CENTER_ALIGNMENT);
		users.add(usersLabel);
		users.add(scrollPane1);
		temp = new JPanel();
		temp.add(refreshUsers);
		temp.add(logoutButton);
		temp.setAlignmentX(Component.CENTER_ALIGNMENT);
		temp.setMaximumSize(new Dimension(COL_WIDTH, 10));
		users.add(temp);
		add(users, BorderLayout.EAST);
		
		//add center info section
		info.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
		//info.setBackground(new Color(159, 94, 230));
		info.add(getWelcomeMsgPanel());
		add(info, BorderLayout.CENTER);
		
		//add chat
		chat = new ChatPanel(150, connection, myID);
		add(chat, BorderLayout.SOUTH);
		
		addListeners();
		createButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				info.removeAll();
				info.add(getGameCreationPanel());
				info.revalidate();
			}
		});
		
		refreshGames.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				connection.refreshGames(myID);
			}
		});
		refreshUsers.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				connection.refreshUsers(myID);
				System.out.println("refresh users clicked");
			}
		});
		logoutButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				connection.logout(myID);
			}
		});
		createGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				connection.createGame(myID);
			}
		});
	}
	
	public void refreshGamesList(Object[][] gameData, boolean isEmpty) {
		System.out.println("[LobbyPanel] GameList refreshed");
		if(!isEmpty) {
			gameModel = new DefaultTableModel(gameData, gameColumns);
			gameTable.setModel(gameModel);
			formatTable(gameTable);
		} else {
			((DefaultTableModel) gameTable.getModel()).setRowCount(0);
		}
	}
	
	public void refreshUserList(Object[][] userData) {
		System.out.println("[LobbyPanel] UserList refreshed");
		userModel = new DefaultTableModel(userData, userColumns);
		userTable.setModel(userModel);
		formatTable(userTable);
	}
	
	public void addLobbyChatMsg(String username, String msg) {
		chat.userMessage(username, msg, Color.black);
	}
	
	private void addListeners() {
		//add selection listeners for the JTables
		userTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if (e.getValueIsAdjusting()) return;

				ListSelectionModel lsm =
					(ListSelectionModel)e.getSource();
				if (!lsm.isSelectionEmpty()) {
					gameTable.clearSelection();
					int selectedRow = lsm.getMinSelectionIndex();
					int id = ((Integer) userTable.getModel().getValueAt(selectedRow, 0)).intValue();
					//System.out.println(id);
					info.removeAll();
					info.add(getPlayerInfoPanel(id, (String) userTable.getModel().getValueAt(selectedRow, 1)));
					info.revalidate();
				}
			}
		});
		
		gameTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if (e.getValueIsAdjusting()) return;

				ListSelectionModel lsm = (ListSelectionModel)e.getSource();
				if (!lsm.isSelectionEmpty()) {
					userTable.clearSelection();
					int selectedRow = lsm.getMinSelectionIndex();
					int id = ((Integer) gameTable.getModel().getValueAt(selectedRow, 0)).intValue();
					//System.out.println(id);
					info.removeAll();
					info.add(getGameInfoPanel(id, (String) gameTable.getModel().getValueAt(selectedRow, 1)));
					info.revalidate();
				}
			}
		});
	}
	
	//Player info panel
	private JPanel getPlayerInfoPanel(int id, String name) {
		JPanel temp = new JPanel();
		temp.setLayout(new BoxLayout(temp, BoxLayout.Y_AXIS));
		temp.add(Box.createRigidArea(new Dimension(0,20)));
		temp.add(Box.createRigidArea(new Dimension(20,0)));
		ImageIcon icon = new ImageIcon(getClass().getResource("doge.jpeg"));
		temp.add(new JLabel("<html><h1>"+name+"</h1>", icon, JLabel.LEFT));
		temp.add(Box.createVerticalGlue());
		temp.add(msgButton);
		return temp;
	}
	
	//Game info panel
	private JPanel getGameInfoPanel(int id, String name) {
		JPanel temp = new JPanel();
		visibleGame = id;
		temp.setLayout(new BoxLayout(temp, BoxLayout.Y_AXIS));
		temp.add(Box.createRigidArea(new Dimension(20,20)));
		//temp.add(Box.createRigidArea(new Dimension(20,0)));
		temp.add(new JLabel("<html><h1>"+name+"</h1></html>"));
		//add(Box.createRigidArea(new Dimension(0,20)));
		temp.add(new JLabel("<html><p>Players:</p></html>"));
		temp.add(Box.createVerticalGlue());
		temp.add(joinButton);
		return temp;
	}
	
	private JPanel getGameCreationPanel() {
		JPanel temp = new JPanel();
		temp.add(Box.createRigidArea(new Dimension(0,20)));
		temp.add(Box.createRigidArea(new Dimension(20,0)));
		temp.setLayout(new BoxLayout(temp, BoxLayout.Y_AXIS));
		temp.add(new JLabel("<html><h1>Create Game</h1></html>"));
		JPanel names = new JPanel();
		names.add(new JLabel("Enter a name: "));
		names.add(nameField);
		temp.add(names);
		temp.add(Box.createVerticalGlue());
		temp.add(createGame);
		return temp;
	}
	
	//Splash screen
	private JLabel getWelcomeMsgPanel() {
		ImageIcon set = new ImageIcon(getClass().getResource("set.jpg"));
		String msg = "<html><h1>Welcome to Set!</h1>"
						+"<p>To join a game, select one on the left or create your own.</p>"
						+"<p>Or, choose a player on the right and view their stats.</p></html>";
		JLabel label = new JLabel(msg, set, JLabel.CENTER);
		label.setHorizontalTextPosition(JLabel.CENTER);
		label.setVerticalTextPosition(JLabel.BOTTOM);
		return label;
	}
	
	//format table
	private void formatTable(JTable t) {
		t.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		t.removeColumn(t.getColumnModel().getColumn(0)); //hide IDs
		t.getColumnModel().getColumn(0).setPreferredWidth(150);
		t.getColumnModel().getColumn(1).setPreferredWidth(50);
		t.setAutoCreateRowSorter(true);
	}

}