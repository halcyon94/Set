/**
 *	Set GUI Card panel
 *	@author Dolen Le
 *	@version 1.0
 */
package gui;

import javax.swing.*;
import java.awt.*;

public class CardGrid extends JPanel {
	
	private int rows;
	
	/**
	 *	Card grid panel constructor
	 *	@param rows The number of rows in the grid
	 *	@param margin Pixel spacing between cards
	 */
	public CardGrid(int rows, int margin) {
		super(new GridLayout(1, 0, margin, margin));
		this.rows = rows;
		add(new JPanel(new GridLayout(rows, 1, margin, margin))); //initial column
	}
	
	/**
	 *	Add a card to the GUI and update the display
	 *	@param c Card object to be added
	 */
	public void addCard(Card c) {
		JPanel lastCol = (JPanel) getComponent(getComponentCount()-1);
		if(lastCol.getComponentCount() < rows) {
			lastCol.add(c);
		} else { //column full - add a new column
			JPanel temp = new JPanel(new GridLayout(rows, 1, 3, 3));
			temp.add(c);
			add(temp);
		}
		revalidate();
	}
	
	/**
	 *	Remove the card in the given position. The origin is at the top left.
	 *	@param x Row of the card
	 *	@param y Column of the card
	 */
	public void removeCard(int x, int y) {
	}
	
	/**
	 *	Get the card with the given ID. Returns null if the card is not in the grid.
	 *	@param id Unique card identifier
	 *	@return Card with provided ID or NULL
	 */
	public Card getCard(int id) {
		return null;
	}
	
	/**
	 *	Clears the card grid.
	 */
	public void clear() {
		removeAll();
		add(new JPanel(new GridLayout(rows, 1, 3, 3)));
		revalidate();
	}

}