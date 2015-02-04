package gui;

import java.util.ArrayList;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;

/**
 *	Set GUI in-game Player panel. Shows player names, stats and scores.
 *	@author Dolen Le
 *	@version 1.0
 */
public class LobbyPanel extends JPanel {	
	
	private static int COL_WIDTH = 200;
	
	private JPanel games;
	private JPanel users;
	private JPanel info;
	private ChatPanel chat;
	
	private JTable userTable;
	private JTable gameTable;
	
	private JButton refreshUsers = new JButton("Refresh");
	private JButton refreshGames = new JButton("Refresh");
	private JButton createButton = new JButton("Create");
	
	String[] userColumns = {"ID", "Name",
                        	"Ranking"};
    String[] gameColumns = {"ID", "Name",
                        	"Players"};
                        	
	Object[][] userData = {
    	{new Integer(1), "Mary", new Integer(5)},
    	{new Integer(2), "Alison", new Integer(3)},
    	{new Integer(3), "Dolen", new Integer(9000)}
    };
    Object[][] gameData = {
    	{new Integer(23), "Mary's Game", "0/2"},
    	{new Integer(5), "Alison's Game", "2/5"}
    };
	
	/**
	 *	Lobby screen constructor
	 */
	public LobbyPanel() {
		super(new BorderLayout());
		games = new JPanel();
		games.setLayout(new BoxLayout(games, BoxLayout.Y_AXIS));
		users = new JPanel();
		users.setLayout(new BoxLayout(users, BoxLayout.Y_AXIS));
		info = new JPanel(new CardLayout()); 
				
		//build games panel
		gameTable = new JTable(gameData, gameColumns);
		gameTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		gameTable.removeColumn(gameTable.getColumnModel().getColumn(0));
		JScrollPane scrollPane2 = new JScrollPane(gameTable);
		scrollPane2.setPreferredSize(new Dimension(COL_WIDTH, 200));
		games.add(new JLabel("Games Listing"));
		games.add(scrollPane2);
		JPanel temp = new JPanel();
		temp.add(refreshGames);
		temp.add(createButton);
		temp.setMaximumSize(new Dimension(COL_WIDTH, 10));
		games.add(temp);
		add(games, BorderLayout.WEST);
		
		//build users panel
		userTable = new JTable(userData, userColumns);
		userTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		userTable.removeColumn(userTable.getColumnModel().getColumn(0));
		JScrollPane scrollPane1 = new JScrollPane(userTable);
		scrollPane1.setPreferredSize(new Dimension(COL_WIDTH, 200));
		users.add(new JLabel("Users Online"));
		users.add(scrollPane1);
		users.add(refreshUsers);
		add(users, BorderLayout.EAST);
		
		//add info
		info.add(new PlayerInfo("Test"), "GAME");
		info.setBorder(BorderFactory.createTitledBorder("Info"));
		add(info, BorderLayout.CENTER);
		
		//add chat
		chat = new ChatPanel(100);
		add(chat, BorderLayout.SOUTH);
		
		addListeners();
	}
	
	private void addListeners() {
		ListSelectionModel rowSM = userTable.getSelectionModel();
		rowSM.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if (e.getValueIsAdjusting()) return;

				ListSelectionModel lsm =
					(ListSelectionModel)e.getSource();
				if (!lsm.isSelectionEmpty()) {
					int selectedRow = lsm.getMinSelectionIndex();
					int id = ((Integer) userTable.getModel().getValueAt(selectedRow, 0)).intValue();
					System.out.println(id);
				}
			}
		});
		
		ListSelectionModel rowSM2 = gameTable.getSelectionModel();
		rowSM2.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if (e.getValueIsAdjusting()) return;

				ListSelectionModel lsm = (ListSelectionModel)e.getSource();
				if (!lsm.isSelectionEmpty()) {
					int selectedRow = lsm.getMinSelectionIndex();
					int id = ((Integer) gameTable.getModel().getValueAt(selectedRow, 0)).intValue();
					System.out.println(id);
				}
			}
		});
	}
	
	public void addListener(ActionListener l) {
		createButton.addActionListener(l);
	}

	private class PlayerInfo extends JPanel {
		
		public JButton button = new JButton("Send Message");
		
		public PlayerInfo(String name) {
			super();
			setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			add(Box.createRigidArea(new Dimension(0,10)));
			ImageIcon icon = new ImageIcon(getClass().getResource("doge.jpeg"));
			JLabel userLabel = new JLabel("<html><p><font size=+2>"+name+"</p></html>", icon, JLabel.LEFT);
			add(Box.createRigidArea(new Dimension(10,0)));
			add(userLabel);
		}
	}
	
	private class GameInfo extends JPanel {
	
		public JButton button = new JButton("Join");
		
		public GameInfo(String name) {
			super();
			setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			add(Box.createRigidArea(new Dimension(0,10)));
			JLabel userLabel = new JLabel("<html><p><font size=+2>"+name+"</p></html>");
			add(Box.createRigidArea(new Dimension(10,0)));
			add(userLabel);
		}
	
	}

}