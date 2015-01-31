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

	private CardGrid grid;
	
	private static final int ROWS = 3;
	
	/**
	 *	Test GUI constructor
	 */
	public Gooey() {
       	setTitle("GUI Test");
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
               grid.addCard(generateCard());
            }
        });
    	footer.add(butt2); //add card button
    	
    	JButton butt3 = new JButton("Clear");
    	butt3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
               grid.clear();
            }
        });
    	footer.add(butt3); //clear button    	
    	
    	JButton butt4 = new JButton("Add 12");
    	butt4.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
               for(int i=1; i<=12; i++) {
					grid.addCard(generateCard());
				}
            }
        });
    	footer.add(butt4); //add 12 button
    	
    	c.add(footer, BorderLayout.SOUTH);
    	
    	grid = new CardGrid(3, 3);
    	
    	//Start with 12 cards
    	for(int i=1; i<=12; i++) {
    		grid.addCard(generateCard());
    	}
    	
    	c.add(grid, BorderLayout.CENTER);
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