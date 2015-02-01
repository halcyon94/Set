/**
 *	Set GUI in-game Player listing panel
 *	@author Dolen Le
 *	@version 1.0
 */
package gui;

import java.util.ArrayList;
import javax.swing.*;
import java.awt.*;

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
		playerList.get(index).setScore(score);
	}

	/**
	 *	Increase a player's score
	 *	@param index Player index (zero-indexed from left to right)
	 */
	public void increaseScore(int index) {
		playerList.get(index).increaseScore();
	}
	
	/**
	 *	Decrease a player's score
	 *	@param index Player index (zero-indexed from left to right)
	 */
	public void decreaseScore(int index) {
		playerList.get(index).decreaseScore();
	}
	
	//Nested class for the player cards
	private class PlayerStat extends JPanel {
		
		private JLabel scoreLabel;
		private int score = 0;
		
		public PlayerStat(Color color, String name, int rating) {
			super();
			setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
			setBorder(BorderFactory.createMatteBorder(1, 8, 1, 1, color));
			ImageIcon icon = new ImageIcon(getClass().getResource("doge.jpeg"));
			JLabel userLabel = new JLabel("<html><p><font size=+2>"+name+"</p><p><font size=-2>Rating:&nbsp;"+rating+"</p></html>", icon, JLabel.LEFT);
			scoreLabel = new JLabel("<html><p><font size=-3><center>SCORE</center></p><p><font size=+2><center>"+score+"</center></p></html>", JLabel.RIGHT);
			add(userLabel);
			add(Box.createHorizontalGlue());
			add(scoreLabel);
			add(Box.createRigidArea(new Dimension(5,0)));
			//setMaximumSize(new Dimension(300, 70));
		}
		
		public void setScore(int score) {
			this.score = score;
			scoreLabel.setText("<html><p><font size=-3><center>SCORE</center></p><p><font size=+2><center>"+score+"</center></p></html>");
			System.out.println("Score changed to " +this.score);
		}
		
		public void increaseScore() {
			setScore(score+1);
		}
		
		public void decreaseScore() {
			setScore(score-1);
		}
	}

	

}