package gui;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.*;

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
	private int myID;
	private int gameID;
	private Color playerColor;
	private boolean selectEnabled = false;
		
	private ArrayList<Card> selList = new ArrayList<Card>(); //List of selected cards
	private HashMap<Integer,Location> cardMap = new HashMap<Integer,Location>();
	private ExecutorService animationPool = Executors.newSingleThreadScheduledExecutor();
		
	/**
	 *	Card grid panel constructor
	 *	@param rows The number of rows in the grid
	 *	@param margin Pixel spacing between cards
	 *	@param playerColor Player highlight color
	 */
	public CardGrid(int rows, int margin, int setSize, Color playerColor, int uid) {
		super(new GridLayout(1, 0, margin, margin));
		this.rows = rows;
		this.setSize = setSize;
		this.playerColor = playerColor;
		this.myID = uid;
		add(new JPanel(new GridLayout(rows, 1, margin, margin))); //initial column
	}
	
	/**
	 *	Add a card to the end of the grid
	 *	@param c Card object to be added
	 */
	public void addCard(final Card c) {
		int columns = getComponentCount();
		final JPanel lastCol = (JPanel) getComponent(columns-1);
		final ComponentTransition ct = new ComponentTransition();
//		SlideTransitionEffect effect = new SlideTransitionEffect();
//		effect.setSpeed(80/columns);
//		effect.setType(SlideType.moveBoth);
//		effect.setFade(false);
//		effect.setDirection ( Direction.horizontal );
//		ct.setTransitionEffect(effect);
//		ct.setContent(new JPanel());
		ct.setContent(c);
		c.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				Card cx = (Card) e.getSource();
				if(selectEnabled) {
					if(selList.contains(cx)) {
						cx.deselect();
						selList.remove(cx);
					} else if(selList.size() < setSize) {
						cx.toggleSelection(playerColor);
						selList.add(cx);
					}
					String ids = "";
					for(Card i : selList)
						ids+=i.getID(setSize)+"`";
					ClientConnection.updateSelection(myID, gameID, ids);
				}
			}
		});
		cards++;
		int cardsInCol = lastCol.getComponentCount();
//		if(cardMap.containsKey(c.getID(setSize)))
//			System.out.println("Card already in grid");
		if(cardsInCol < rows) {
			lastCol.add(ct);
			cardMap.put(c.getID(setSize), new Location(columns-1,cardsInCol));
		} else { //column full - add a new column
			JPanel temp = new JPanel(new GridLayout(rows, 1, 3, 3));
			temp.add(ct);
			add(temp);
			cardMap.put(c.getID(setSize), new Location(columns,0));
		}
		//System.out.println("New card at "+cardMap.get(c.getID(setSize)).col+", "+cardMap.get(c.getID(setSize)).row);
    	revalidate();
//	    SwingWorker worker = new SwingWorker<Integer, Void>() {
//	        @Override
//	        public Integer doInBackground() {
//	        	if(lastCol.getComponentCount() < rows) {
//	    			lastCol.add(ct);
//	    		} else { //column full - add a new column
//	    			JPanel temp = new JPanel(new GridLayout(rows, 1, 3, 3));
//	    			temp.add(ct);
//	    			add(temp);
//	    		}
//	        	revalidate();
//	        	return new Integer(0);
//	        }
//	 
//	        @Override
//	        public void done() {
//	        	ct.performTransition(c);
//	        }
//	    };
//	    animationPool.execute(worker);
//	    worker.execute();
//		revalidate();
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
		final ComponentTransition ct = (ComponentTransition) column.getComponent(y);
		final int id = ((Card) ct.getContent()).getID(setSize);
		ct.setTransitionEffect(effect);
		ct.performTransition(new JPanel());
		ct.addTransitionListener ( new TransitionListener ()
        {
            @Override
            public void transitionStarted () {}

            @Override
            public void transitionFinished ()
            {
            	//int id = ((Card) ct.getContent()).getID(setSize);
            	column.remove(y);
            	cards--;
            	cardMap.remove(id);
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
	 *	Animated removal animation for Card with the given ID from the grid. Card is not replaced.
	 *	@param id Unique card identifier
	 */
	public void animateRemoveCard(int id) {
		if(cardMap.containsKey(id)) {
			Location l = cardMap.get(id);
			ComponentTransition ct = ((ComponentTransition) ((JPanel) getComponent(l.col)).getComponent(l.row));
			ZoomTransitionEffect effect = new ZoomTransitionEffect ();
	        effect.setMinimumSpeed ( 0.03f );
	        effect.setSpeed ( 0.15f );
	        effect.setType(ZoomType.zoomOut);
			ct.setTransitionEffect(effect);
			ct.performTransition(new JPanel());
		}
	}
	
	/**
	 *	Remove the Card with the given ID from the grid.
	 *	@param id Unique card identifier
	 */
	public void removeCard(int id) {
		//Optionally, re-implement for better efficiency
		if(cardMap.containsKey(id)) {
			Location l = cardMap.get(id);
			removeCard(l.col, l.row);
		}
	}
	
	/**
	 *	Clears the card grid.
	 */
	public void clear() {
		removeAll();
		selList.clear();
		cardMap.clear();
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
	 *	Enables card selection.
	 */
	public void enableSelection() {
		selectEnabled = true;
	}
	
	/**
	 *	Disables card selection.
	 */
	public void disableSelection() {
		selectEnabled = false;
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
	 *	Clear the current selection
	 */
	public void setPlayerColor(Color c) {
		playerColor = c;
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
	
	public void setGameID(int id) {
		this.gameID = id;
	}
	
	public Card getCard(Location l) {
		return (Card) ((ComponentTransition) ((JPanel) getComponent(l.col)).getComponent(l.row)).getContent();
	}
	
	public void markSelected(Color selColor, int[] ids) {
		clearSelected();
		for(int i : ids) {
			Card c = getCard(cardMap.get(i));
			c.select(selColor);
			selList.add(c);
		}
	}
	
	private class Location {
		public int col;
		public int row;
		
		public Location(int c, int r) {
			this.col = c;
			this.row = r;
		}
	}
}
