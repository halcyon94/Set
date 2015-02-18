package gui;

import java.util.HashMap;
import javax.swing.*;
import java.awt.*;

/**
 *	Set GUI in-game Player panel. Shows player names, stats and scores.
 *	@author Dolen Le
 *	@version 1.0
 */
public class PlayerPanel extends JPanel {
		
	private HashMap<Integer,PlayerStat> playerList = new HashMap<Integer,PlayerStat>();
	
	/**
	 *	Card grid panel constructor
	 */
	public PlayerPanel() {
		super();
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		setBorder(BorderFactory.createTitledBorder("Players"));
	}
	
	/**
	 *	Add a player to the player panel
	 *	@param color The player color
	 *	@param name The player name
	 *	@param rating The player rating
	 */
	public void addPlayer(int id, Color color, String name, int rating) {
		if(playerList.containsKey(new Integer(id))) {
			System.out.println("Warning - player with id "+id+" already added");
		} else {
			PlayerStat player = new PlayerStat(color, name, rating);
			add(player);
			playerList.put(id, player);
			revalidate();
		}
	}
	
	/**
	 *	Remove a player from the panel and list.
	 *	@param id Player id
	 */
	public void removePlayer(int id) {
		PlayerStat temp = playerList.remove(id);
		remove(temp);
		revalidate();
		System.out.println("Removed player"+id);
	}
	
	/**
	 *	Update a player's score to a given value
	 *	@param id Player id
	 *	@param score New player score
	 */
	public void setScore(int id, int score) {
		PlayerStat p = playerList.get(id);
		p.score = score;
		p.scoreLabel.setText("<html><p><font size=-3><center>SCORE</center></p><p><font size=+2><center>"+score+"</center></p></html>");
	}

	/**
	 *	Increase a player's score
	 *	@param id Player id
	 */
	public void increaseScore(int id) {
		PlayerStat p = playerList.get(id);
		p.score = ++p.score;
		p.scoreLabel.setText("<html><p><font size=-3><center>SCORE</center></p><p><font size=+2><center>"+p.score+"</center></p></html>");
	}
	
	/**
	 *	Decrease a player's score
	 *	@param id Player id
	 */
	public void decreaseScore(int id) {
		PlayerStat p = playerList.get(id);
		p.score = --p.score;
		p.scoreLabel.setText("<html><p><font size=-3><center>SCORE</center></p><p><font size=+2><center>"+p.score+"</center></p></html>");
	}
	
	/**
	 *	Get the player's color
	 *	@param id Player id
	 *	@return Player color
	 */
	public Color getColor(int id) {
		return playerList.get(id).playerColor;
	}
	
	/**
	 *	Get the player's icon
	 *	@param id Player id
	 *	@return Player icon
	 */
	public Icon getIcon(int id) {
		return playerList.get(id).icon;
	}
	
	//Nested class for the player cards
	private class PlayerStat extends JPanel {
		
		public JLabel scoreLabel;
		public int score = 0;
		public Color playerColor;
		public ImageIcon icon;
		
		public PlayerStat(Color color, String name, int rating) {
			super();
			setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
			JPanel temp = new JPanel();
			temp.setLayout(new BoxLayout(temp, BoxLayout.X_AXIS));
			temp.setBorder(BorderFactory.createMatteBorder(1, 8, 1, 1, color));
			playerColor = color;
			icon = new ImageIcon(getClass().getResource("doge.jpeg"));
			JLabel userLabel = new JLabel("<html><p><font size=+2>"+name+"</p><p><font size=-2>Rating:&nbsp;"+rating+"</p></html>", icon, JLabel.LEFT);
			scoreLabel = new JLabel("<html><p><font size=-3><center>SCORE</center></p><p><font size=+2><center>"+score+"</center></p></html>", JLabel.RIGHT);
			temp.add(userLabel);
			temp.add(Box.createHorizontalGlue());
			temp.add(scoreLabel);
			temp.add(Box.createRigidArea(new Dimension(5,0)));
			add(Box.createRigidArea(new Dimension(5,0)));
			add(temp);
			//setMaximumSize(new Dimension(300, 70));
		}
	}

	

}