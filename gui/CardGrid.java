package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

/**
 *	Set GUI Card panel.
 *	@author Dolen Le
 *	@version 1.0
 */
public class CardGrid extends JPanel {
	
	private int cards = 0;
	private int rows;
	private Color playerColor;
	
	/**
	 *	Card grid panel constructor
	 *	@param rows The number of rows in the grid
	 *	@param margin Pixel spacing between cards
	 *	@param playerColor Player highlight color
	 */
	public CardGrid(int rows, int margin, Color playerColor) {
		super(new GridLayout(1, 0, margin, margin));
		this.rows = rows;
		this.playerColor = playerColor;
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
		c.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Card test = (Card) e.getSource();
       			test.toggleSelection(playerColor);
			}
		});
		cards++;
		revalidate();
	}
	
	/**
	 *	Remove the card in the given position and replaces it with the last card. The origin is at the top left.
	 *	@param x Row of the card
	 *	@param y Column of the card
	 */
	public void removeCard(int x, int y) {
		JPanel column = ((JPanel) getComponent(x));
		column.remove(y);
		cards--;
		if(x*rows+y%rows<cards-1 && cards>0) {
			column.add(getLastCard(), y);
			if(cards%rows == 0) { //remove last column if needed
				remove(cards/rows);
			}
		}
		revalidate();
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
		cards = 0;
		add(new JPanel(new GridLayout(rows, 1, 3, 3)));
		revalidate();
	}
	
	/**
	 *	Get the last card on the grid
	 *	@return The last card, null if no cards are on the grid
	 */
	public Card getLastCard() {
		if(cards>0) {
			return (Card) ((JPanel) getComponent(cards/rows)).getComponent(cards%rows);
		} else {
			return null;
		}
	}
	
	/**
	 *	Generate a Card with random color, shape, and fill
	 *	@return Randomly generated Card
	 */
	public Card generateCard() {
		Random rand = new Random();
		Card.Pattern[] patA = Card.Pattern.values();
		Card.Symbol[] symA = Card.Symbol.values();
		Card.SetColor[] colA = Card.SetColor.values();
		//Color tempColor = new Color(rand.nextInt(255),rand.nextInt(255),rand.nextInt(255));
		//return new Card(rand.nextInt(4)+1, patA[rand.nextInt(patA.length)], tempColor, symA[rand.nextInt(symA.length)]);
		return new Card(rand.nextInt(4)+1, patA[rand.nextInt(patA.length)], colA[rand.nextInt(colA.length)], symA[rand.nextInt(symA.length)]);
	}
}

// class MouseAdapterMod extends MouseAdapter {
//    public void mouseClicked(MouseEvent e) {
//        Card test = (Card) e.getSource();
//        System.out.println(test.toString());
//    }
// }