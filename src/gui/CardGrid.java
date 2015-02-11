package gui;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import java.util.ArrayList;

import com.alee.extended.transition.ComponentTransition;
import com.alee.extended.transition.TransitionListener;
import com.alee.extended.transition.effects.Direction;
import com.alee.extended.transition.effects.slide.SlideTransitionEffect;
import com.alee.extended.transition.effects.slide.SlideType;
import com.alee.extended.transition.effects.zoom.ZoomTransitionEffect;
import com.alee.extended.transition.effects.zoom.ZoomType;

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
	private Timer t;
	
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
	public void addCard(final Card c) {
		final JPanel lastCol = (JPanel) getComponent(getComponentCount()-1);
		final ComponentTransition ct = new ComponentTransition();
        SlideTransitionEffect effect = new SlideTransitionEffect();
    	effect.setSpeed(70/getComponentCount());
    	effect.setType(SlideType.moveBoth);
    	effect.setFade(false);
        ct.setTransitionEffect(effect);
		ct.setContent(new JPanel());
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
	    SwingWorker worker = new SwingWorker<Integer, Void>() {
	        @Override
	        public Integer doInBackground() {
	        	if(lastCol.getComponentCount() < rows) {
	    			lastCol.add(ct);
	    		} else { //column full - add a new column
	    			JPanel temp = new JPanel(new GridLayout(rows, 1, 3, 3));
	    			temp.add(ct);
	    			add(temp);
	    		}
	        	revalidate();
	        	return new Integer(0);
	        }
	 
	        @Override
	        public void done() {
	        	ct.performTransition(c);
	        }
	    };
	    worker.execute();
		revalidate();
	}
	
	/**
	 *	Remove the card in the given position and replaces it with the last card. The origin is at the top left.
	 *	If the card to be removed is in the same column as the last card, the cards are shifted up.
	 *	@param x Row of the card
	 *	@param y Column of the card
	 */
	public void removeCard(final int x, final int y) {
		final JPanel column = ((JPanel) getComponent(x));
		//column.remove(y);
		ZoomTransitionEffect effect = new ZoomTransitionEffect ();
        effect.setMinimumSpeed ( 0.03f );
        effect.setSpeed ( 0.15f );
        effect.setType(ZoomType.zoomOut);
		ComponentTransition ct = (ComponentTransition) column.getComponent(y);
		ct.setTransitionEffect(effect);
		ct.performTransition(new JPanel());
		ct.addTransitionListener ( new TransitionListener ()
        {
            @Override
            public void transitionStarted () {}

            @Override
            public void transitionFinished ()
            {
            	column.remove(y);
            	cards--;
        		if(x*rows+y%rows<cards-1 && cards>0) {
        			if(x!=cards/rows) {
        				column.add((ComponentTransition) ((JPanel) getComponent(cards/rows)).getComponent(cards%rows), y);
        				if(cards%rows == 0) { //remove last column if needed
        					remove(cards/rows);
        				}
        			}
        		}
        		repaint();
            }
        } );
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
