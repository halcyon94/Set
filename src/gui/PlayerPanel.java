package gui;

import java.util.ArrayList;
import javax.swing.*;
import java.awt.*;

/**
 *	Set GUI in-game Player panel. Shows player names, stats and scores.
 *	@author Dolen Le
 *	@version 1.0
 */
public class PlayerPanel extends JPanel {
		
	private ArrayList<PlayerStat> playerList = new ArrayList<PlayerStat>();
	
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
	public void addPlayer(Color color, String name, int rating) {
		add(Box.createRigidArea(new Dimension(5,0)));
		PlayerStat player = new PlayerStat(color, name, rating);
		add(player);
		playerList.add(player);
		revalidate();
	}
	
	/**
	 *	Remove a player from the panel and list.
	 *	@param index Player index (zero-indexed from left to right)
	 */
	public void removePlayer(int index) {
		playerList.remove(index);
		remove(2*index+1);
		remove(2*index);
		revalidate();
		System.out.println("Removed player"+index);
	}
	
	/**
	 *	Update a player's score to a given value
	 *	@param index Player index (zero-indexed from left to right)
	 *	@param score New player score
	 */
	public void setScore(int index, int score) {
		PlayerStat p = playerList.get(index);
		p.score = score;
		p.scoreLabel.setText("<html><p><font size=-3><center>SCORE</center></p><p><font size=+2><center>"+score+"</center></p></html>");
	}

	/**
	 *	Increase a player's score
	 *	@param index Player index (zero-indexed from left to right)
	 */
	public void increaseScore(int index) {
		PlayerStat p = playerList.get(index);
		p.score = ++p.score;
		p.scoreLabel.setText("<html><p><font size=-3><center>SCORE</center></p><p><font size=+2><center>"+p.score+"</center></p></html>");
	}
	
	/**
	 *	Decrease a player's score
	 *	@param index Player index (zero-indexed from left to right)
	 */
	public void decreaseScore(int index) {
		PlayerStat p = playerList.get(index);
		p.score = --p.score;
		p.scoreLabel.setText("<html><p><font size=-3><center>SCORE</center></p><p><font size=+2><center>"+p.score+"</center></p></html>");
	}
	
	/**
	 *	Get the player's color
	 *	@return Player color
	 */
	public Color getColor(int index) {
		return playerList.get(index).playerColor;
	}
	
	//Nested class for the player cards
	private class PlayerStat extends JPanel {
		
		public JLabel scoreLabel;
		public int score = 0;
		public Color playerColor;
		
		public PlayerStat(Color color, String name, int rating) {
			super();
			setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
			setBorder(BorderFactory.createMatteBorder(1, 8, 1, 1, color));
			playerColor = color;
			ImageIcon icon = new ImageIcon(getClass().getResource("doge.jpeg"));
			JLabel userLabel = new JLabel("<html><p><font size=+2>"+name+"</p><p><font size=-2>Rating:&nbsp;"+rating+"</p></html>", icon, JLabel.LEFT);
			scoreLabel = new JLabel("<html><p><font size=-3><center>SCORE</center></p><p><font size=+2><center>"+score+"</center></p></html>", JLabel.RIGHT);
			add(userLabel);
			add(Box.createHorizontalGlue());
			add(scoreLabel);
			add(Box.createRigidArea(new Dimension(5,0)));
			//setMaximumSize(new Dimension(300, 70));
		}
	}

	

}