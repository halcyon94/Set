package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import java.util.Random;

/**
 *	Set GUI tester
 *	@author Dolen Le
 *	@version 1.0
 */
public class Gooey extends JFrame {

	private CardGrid grid;
	private PlayerPanel players;
	private ChatPanel chat = new ChatPanel();
	
	private static final int ROWS = 3;
	
	/**
	 *	Test GUI constructor
	 */
	public Gooey() {
       	setTitle("GUI Test");
       	setSize(960, 768);
       	setLocationRelativeTo(null);
       	setDefaultCloseOperation(EXIT_ON_CLOSE);    
       
    	Container c = getContentPane(); //get the content pane
    	c.setLayout(new BorderLayout(5,5));
    	
    	players = new PlayerPanel();
    	players.addPlayer(Color.BLUE, "Dolen", 9000);
    	players.addPlayer(Color.BLUE, "Eugene", 9000);
    	players.addPlayer(Color.MAGENTA, "Cher", 9000);
    	players.addPlayer(Color.YELLOW, "Justin", -589);
    	c.add(players, BorderLayout.NORTH);
    	//c.add(new JLabel("lolol dolen was here hurhurhur", SwingConstants.CENTER), BorderLayout.NORTH); //add a label
    	
    	JPanel footer = new JPanel();
    	footer.setLayout(new BoxLayout(footer, BoxLayout.Y_AXIS));
    	JButton butt = new JButton("Quit");
    	butt.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                System.exit(0);
            }
        });
    	footer.add(butt); //quit button
    	
    	JButton butt2 = new JButton("+Card");
    	butt2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
               grid.addCard(grid.generateCard());
            }
        });
    	footer.add(butt2); //add card button
    	
    	JButton butt3 = new JButton("Clear");
    	butt3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
               grid.clear();
               chat.systemMessage("Cleared grid");
            }
        });
    	footer.add(butt3); //clear button    	
    	
    	JButton butt4 = new JButton("+3Card");
    	butt4.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
               for(int i=1; i<=3; i++) {
					grid.addCard(grid.generateCard());
				}
            }
        });
    	footer.add(butt4); //add 3
    	
    	JButton butt5 = new JButton("+Score");
    	butt5.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
				players.increaseScore(0);
				chat.systemMessage("Player0 Score");
            }
        });
    	footer.add(butt5);
    	
    	JButton butt6 = new JButton("+Player");
    	butt6.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
            	Random rand = new Random();
            	Color tempColor = new Color(rand.nextInt(255),rand.nextInt(255),rand.nextInt(255));
				players.addPlayer(tempColor, "PlayerName", 1234);
				chat.systemMessage("Added Player");
            }
        });
    	footer.add(butt6);
    	
    	JButton butt7 = new JButton("-Player");
    	butt7.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
				players.removePlayer(players.getComponentCount()/2-1);
				chat.systemMessage("Removed Player");
				repaint();
            }
        });
    	footer.add(butt7);
    	
    	JButton butt8 = new JButton("+Deck");
    	butt8.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
               for(int i=0; i<81; i++) {
					grid.addCard(new Card(i,3));
				}
				chat.systemMessage("Added full deck");
            }
        });
    	footer.add(butt8); //add 3
    	
    	JButton butt9 = new JButton("-(1,1)");
    	butt9.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
				try {
					grid.removeCard(1,1);
					chat.systemMessage("Removed Card at (1,1)");
					repaint();
				} catch (ArrayIndexOutOfBoundsException e) {
					chat.systemMessage("Could not remove card!");
				}
            }
        });
    	footer.add(butt9);
    	
    	c.add(footer, BorderLayout.EAST);
    	
    	c.add(chat, BorderLayout.SOUTH);
    	
    	grid = new CardGrid(3, 3, players.getColor(0));
    	
    	//Start with 21 cards
    	for(int i=0; i<21; i++) {
    		grid.addCard(grid.generateCard());
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
}