package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import java.util.ArrayList;

/**
 *	Set GUI Card panel. Responsible for displaying cards and performing miscellaneous logic.
 *	@author Dolen Le
 *	@version 1.0
 */
public class CardGrid extends JPanel {
	
	private int cards = 0;
	private int rows;
	private int setSize;
	private Color playerColor;
	private boolean selectEnabled = false;
	private ArrayList<Card> selList = new ArrayList<Card>(); //List of selected cards
	
	/**
	 *	Card grid panel constructor
	 *	@param rows The number of rows in the grid
	 *	@param margin Pixel spacing between cards
	 *	@param playerColor Player highlight color
	 */
	public CardGrid(int rows, int margin, int setSize, Color playerColor) {
		super(new GridLayout(1, 0, margin, margin));
		this.rows = rows;
		this.setSize = setSize;
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
				Card cx = (Card) e.getSource();
				if(selectEnabled) {
					if(selList.contains(cx)) {
						cx.deselect();
						selList.remove(cx);
					} else if(selList.size() < setSize) {
						cx.toggleSelection(playerColor);
						selList.add(cx);
					}
				}
			}
		});
		cards++;
		revalidate();
		//System.out.println(c.toString());
	}
	
	/**
	 *	Remove the card in the given position and replaces it with the last card. The origin is at the top left.
	 *	If the card to be removed is in the same column as the last card, the cards are shifted up.
	 *	@param x Row of the card
	 *	@param y Column of the card
	 */
	public void removeCard(int x, int y) {
		JPanel column = ((JPanel) getComponent(x));
		column.remove(y);
		cards--;
		if(x*rows+y%rows<cards-1 && cards>0) {
			if(x!=cards/rows) {
				column.add((Card) ((JPanel) getComponent(cards/rows)).getComponent(cards%rows), y);
				if(cards%rows == 0) { //remove last column if needed
					remove(cards/rows);
				}
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
		selList.clear();
		cards = 0;
		add(new JPanel(new GridLayout(rows, 1, 3, 3)));
		revalidate();
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
		return new Card(rand.nextInt(setSize)+1, patA[rand.nextInt(setSize)], colA[rand.nextInt(setSize)], symA[rand.nextInt(setSize)]);
	}
	
	/**
	 *	Toggles the selection mode for the cards.
	 */
	public void toggleSelection() {
		selectEnabled = !selectEnabled;
	}
	
	/**
	 *	Checks if the currently selected cards form a Set.
	 */
	public boolean isSet() {
		return selList.size() == setSize && selList.get(0).equals(completeSet(new ArrayList<Card>(selList.subList(1, setSize))));
	}
	
	/**
	 *	Returns the list of selected Cards.
	 *	@return ArrayList of selected Cards
	 */
	public ArrayList<Card> getSelected() {
		return selList;
	}
	
	/**
	 *	Clear the current selection
	 */
	public void clearSelected() {
		for(Card c : selList)
			c.deselect();
		selList.clear();
	}
	
	/**
	 *	Determines the Card which turns the currently selected cards into a set
	 *	@return The missing card
	 */
	public Card completeSet(ArrayList<Card> cardList) {
		int num, pat, col, sym;
		num = pat = col = sym = 0;
		for(Card c : cardList) {
			num -= c.getNum()-1;
			pat -= c.getPatID();
			col -= c.getColorID();
			sym -= c.getSymID();
		}
		num%=setSize;
		pat%=setSize;
		col%=setSize;
		sym%=setSize;
		return new Card(num == 0 ? num+1 : num+setSize+1, pat == 0 ? pat : pat+setSize, col == 0 ? col : col+setSize, sym == 0 ? sym : sym+setSize);
	}
}
