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

	JPanel gameCard; //5px spacing
	
	private int setSize = 3;
	private final int ROWS = setSize;
	
	private LoginPanel login = new LoginPanel();
	private LobbyPanel lobby = new LobbyPanel();
	private CardGrid grid;
	private PlayerPanel players;
	private ChatPanel chat;
	
	/**
	 *	Client GUI frame constructor - Initializes the various frames
	 */
	public Gooey() {
       	setDefaultCloseOperation(EXIT_ON_CLOSE);
       	getContentPane().setLayout(new CardLayout());
       	addLoginListeners(login);
       	setVisible(true);
    }
    
    //Main Method
    public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				Gooey window = new Gooey();
				window.setLoginFrame();
			}
		});
	}
    
    //Build main game GUI
    private void buildGameGUI() {
    	players = new PlayerPanel();
    	gameCard = new JPanel(new BorderLayout(5,5));
    	players.addPlayer(Color.BLUE, "Dolen", 9000);
    	players.addPlayer(Color.RED, "Eugene", 9000);
    	players.addPlayer(Color.MAGENTA, "Cher", 9000);
    	players.addPlayer(Color.YELLOW, "Justin", -589);
    	gameCard.add(players, BorderLayout.NORTH);
    	
    	JPanel buttons = new JPanel();
    	buttons.setLayout(new BoxLayout(buttons, BoxLayout.Y_AXIS));
		addTestButtons(buttons);
    	gameCard.add(buttons, BorderLayout.EAST);
    	
    	chat = new ChatPanel(80);
    	gameCard.add(chat, BorderLayout.SOUTH);
    	
    	grid = new CardGrid(ROWS, 3, setSize, players.getColor(0));
    	
    	//Start with 21 cards
    	for(int i=0; i<setSize*setSize*setSize*setSize; i++) {
    		grid.addCard(new Card(i, setSize));
    	}
    	
    	gameCard.add(grid, BorderLayout.CENTER);
    	grid.toggleSelection();
    }
	
	//Load the login frame
	private void setLoginFrame() {
		setTitle("Login to Set");
		setSize(280, 150);
       	setResizable(false);
       	setLocationRelativeTo(null);
		JPanel c = (JPanel) getContentPane();
		c.add(login, "LOGIN");
		((CardLayout) c.getLayout()).show(c, "LOGIN");
	}
	
	private void setLobbyFrame() {
		setTitle("Set Lobby");
		setSize(800, 600);
       	setResizable(true);
       	setLocationRelativeTo(null);
       	ActionListener a1 = new ActionListener() {
        	public void actionPerformed(ActionEvent event) {
        		setGameFrame();
            }
        };
       	lobby.addListener(a1);
       	JPanel c = (JPanel) getContentPane();
		c.add(lobby, "LOBBY");
		((CardLayout) c.getLayout()).show(c, "LOBBY");
	}
	
	private void setGameFrame() {
		buildGameGUI();
		setTitle("Set");
		setSize(960, 768);
       	setResizable(true);
       	setLocationRelativeTo(null);
       	JPanel c = (JPanel) getContentPane();
		c.add(gameCard, "GAME");
		((CardLayout) c.getLayout()).show(c, "GAME");
	}
	
	private void addLoginListeners(LoginPanel p) {
		//Listener for login button
		ActionListener a1 = new ActionListener() {
        	public void actionPerformed(ActionEvent event) {
        		if(login.getUser().equals("dolen"))
            		setLobbyFrame();
            	else
            		JOptionPane.showMessageDialog(new JFrame(), "WRONG!");
            }
        };
        //Listener for register button
        ActionListener a2 = new ActionListener() {
        	public void actionPerformed(ActionEvent event) {
            	JOptionPane.showMessageDialog(new JFrame(), "No register for you!");
            }
        };
        p.addListeners(a1, a2);
	}
	
	//Adds test button panel
	private void addTestButtons(JPanel p) {
    	JButton butt0 = new JButton("Quit");
    	butt0.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                System.exit(0);
            }
        });
    	p.add(butt0); //quit button
    	
    	JButton logout = new JButton("Logout");
    	logout.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                setLoginFrame();
            }
        });
    	p.add(logout); //logout button
    	
    	JButton butt1 = new JButton("SET");
    	butt1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
            	chat.systemMessage("Card selection toggled");
                grid.toggleSelection();
            }
        });
    	p.add(butt1); //set button
    	
    	JButton butt2 = new JButton("+Card");
    	butt2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
               grid.addCard(grid.generateCard());
               chat.systemMessage("Add random card");
            }
        });
    	p.add(butt2); //add card button
    	
    	JButton butt3 = new JButton("Clear");
    	butt3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
               grid.clear();
               chat.systemMessage("Cleared grid");
            }
        });
    	p.add(butt3); //clear button    	
    	
    	JButton butt4 = new JButton("+3Card");
    	butt4.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
               for(int i=1; i<=3; i++) {
					grid.addCard(grid.generateCard());
				}
            }
        });
    	p.add(butt4); //add 3
    	
    	JButton butt5 = new JButton("+Score");
    	butt5.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
				players.increaseScore(0);
				chat.systemMessage("Player0 Score");
            }
        });
    	p.add(butt5);
    	
    	JButton butt6 = new JButton("+Player");
    	butt6.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
            	Random rand = new Random();
            	Color tempColor = new Color(rand.nextInt(255),rand.nextInt(255),rand.nextInt(255));
				players.addPlayer(tempColor, "PlayerName", 1234);
				chat.systemMessage("Added Player");
            }
        });
    	p.add(butt6);
    	
    	JButton butt7 = new JButton("-Player");
    	butt7.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
				players.removePlayer(players.getComponentCount()/2-1);
				chat.systemMessage("Removed Player");
				repaint();
            }
        });
    	p.add(butt7);
    	
    	JButton butt8 = new JButton("+Deck");
    	butt8.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
               for(int i=0; i<setSize*setSize*setSize*setSize; i++) {
					Card cx = new Card(i,setSize);
					grid.addCard(cx);
					//System.out.println(cx.getID(setSize));
				}
				chat.systemMessage("Added full deck");
            }
        });
    	p.add(butt8); //add 3
    	
    	JButton butt9 = new JButton("-(0,0)");
    	butt9.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
				try {
					grid.removeCard(0,0);
					chat.systemMessage("Removed Card at (0,0)");
					repaint();
				} catch (ArrayIndexOutOfBoundsException e) {
					chat.systemMessage("Could not remove card!");
				}
            }
        });
    	p.add(butt9);
    	
    	JButton butt10 = new JButton("Comp");
    	butt10.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
            	Card temp = grid.completeSet(grid.getSelected());
				grid.addCard(temp);
				chat.systemMessage("Completed Set: "+temp.toString());
				grid.clearSelected();
            }
        });
        p.add(butt10);
        
        JButton butt11 = new JButton("isSet");
    	butt11.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
            	if(grid.isSet())
					chat.systemMessage("Set found!");
				else
					chat.systemMessage("Not a set");
            }
        });
        p.add(butt11);
	}
}