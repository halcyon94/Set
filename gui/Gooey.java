/**
 *	Set GUI tester
 *	@author Dolen Le
 *	@version 1.0
 */
package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import java.util.Random;

public class Gooey extends JFrame {

	private GridLayout grid;
	private JPanel cardGrid;
	
	private static final int ROWS = 3;
	
	/**
	 *	Test GUI constructor
	 */
	public Gooey() {
       	setTitle("Simple example");
       	setSize(640, 640);
       	setLocationRelativeTo(null);
       	setDefaultCloseOperation(EXIT_ON_CLOSE);    
       
    	Container c = getContentPane(); //get the content pane
    	//c.setLayout(new FlowLayout()); //flow layout is like CSS floats
    	c.setLayout(new BorderLayout(2,2));
    	c.add(new JLabel("lolol dolen was here hurhurhur", SwingConstants.CENTER), BorderLayout.NORTH); //add a label
    	
    	JPanel footer = new JPanel();
    	JButton butt = new JButton("Quit");
    	butt.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                System.exit(0);
            }
        });
    	footer.add(butt); //quit button
    	
    	JButton butt2 = new JButton("Add");
    	butt2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
               addCard(generateCard());
            }
        });
    	footer.add(butt2); //add card button
    	c.add(footer, BorderLayout.SOUTH);
    	
    	JButton butt3 = new JButton("Clear");
    	butt3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
               clear();
            }
        });
    	footer.add(butt3); //clear button
    	c.add(footer, BorderLayout.SOUTH);
    	
    	grid = new GridLayout(1, 0, 3, 3); //fixed 3 cols
    	cardGrid = new JPanel(grid);
    	cardGrid.add(new JPanel(new GridLayout(3, 1, 3, 3)));
    	JPanel temp = (JPanel) cardGrid.getComponent(cardGrid.getComponentCount()-1);
    	
    	//Start with 12 cards
    	for(int i=1; i<=12; i++) {
    		addCard(generateCard());
    	}
    	
    	c.add(cardGrid, BorderLayout.CENTER);
    }
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				Gooey test = new Gooey();
				test.setVisible(true);
			}
		});
	}
	
	/**
	 *	Add a card to the GUI and update the display
	 *	@param c Card object to be added
	 */
	private void addCard(Card c) {
		JPanel lastCol = (JPanel) cardGrid.getComponent(cardGrid.getComponentCount()-1);
		if(lastCol.getComponentCount() < ROWS) {
			lastCol.add(c);
			System.out.println("card added");
		} else { //column full
			JPanel temp = new JPanel(new GridLayout(3, 1, 3, 3));
			temp.add(c);
			cardGrid.add(temp);
			System.out.println("column added");
		}
		cardGrid.revalidate();
	}
	
	/**
	 *	Generate a Card with random color, shape, and fill
	 *	@return Randomly generated Card
	 */
	private Card generateCard() {
		Random rand = new Random();
		Card.Pattern[] patA = Card.Pattern.values();
		Card.Symbol[] symA = Card.Symbol.values();
		Color tempColor = new Color(rand.nextInt(255),rand.nextInt(255),rand.nextInt(255));
		System.out.println(tempColor.toString());
		return new Card(rand.nextInt(3)+1, patA[rand.nextInt(patA.length)], tempColor, symA[rand.nextInt(symA.length)]);
	}
	
	/**
	 *	Clears the card grid.
	 */
	private void clear() {
		cardGrid.removeAll();
		cardGrid.add(new JPanel(new GridLayout(3, 1, 3, 3)));
		cardGrid.revalidate();
	}
	
	
}