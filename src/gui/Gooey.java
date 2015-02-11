package gui;

import javax.swing.*;

import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import java.util.Random;

import javax.swing.UIManager.*;
import com.alee.laf.WebLookAndFeel;

import com.alee.managers.notification.*;

/**
 *	Set GUI tester
 *	@author Dolen Le
 *	@version 1.0
 */
public class Gooey extends JFrame {
	
	private int setSize = 3;
	private final int ROWS = setSize;
	private int myId;
	
	JPanel gameCard; //5px spacing
	private LoginPanel login = new LoginPanel();
	private LobbyPanel lobby = new LobbyPanel();
	private CardGrid grid;
	private PlayerPanel players;
	private ChatPanel chat;
	
	private Timer t;
	private int timer;
	
	/**
	 *	Client GUI frame constructor - Initializes the various frames
	 */
	public Gooey(int id) {
		myId = id;
       	setDefaultCloseOperation(EXIT_ON_CLOSE);
       	getContentPane().setLayout(new CardLayout());
       	addLoginListeners(login);
       	setVisible(true);
    }
    
    //Main Method
    public static void main(String[] args) {
			SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				WebLookAndFeel.initializeManagers();
				try {
				    for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				        if ("Nimbus".equals(info.getName())) {
				            UIManager.setLookAndFeel(info.getClassName());
				            break;
				        }
				    }
				} catch (Exception e) {
					try {UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());}
					catch (Exception e2) {}
				}
				Gooey window = new Gooey(1);
				window.createLoginFrame();
			}
		});
	}
    
    //Assembles components of Set game GUI
    private void buildGameGUI() {
    	players = new PlayerPanel();
    	gameCard = new JPanel(new BorderLayout(5,5));
    	players.addPlayer(1, Color.BLUE, "Dolen", 9000);
    	gameCard.add(players, BorderLayout.NORTH);
    	
    	JPanel buttons = new JPanel();
    	buttons.setLayout(new BoxLayout(buttons, BoxLayout.Y_AXIS));
		addTestButtons(buttons);
    	gameCard.add(buttons, BorderLayout.EAST);
    	
    	chat = new ChatPanel(80);
    	gameCard.add(chat, BorderLayout.SOUTH);
    	
    	grid = new CardGrid(ROWS, 3, setSize, players.getColor(1));
    	
    	//Start with 21 cards
    	for(int i=0; i<setSize*setSize*setSize*setSize; i++) {
    		grid.addCard(new Card(i, setSize));
    	}
    	
    	gameCard.add(grid, BorderLayout.CENTER);
    	//grid.toggleSelection();
    }
	
	//Load and display the login dialog
	private void createLoginFrame() {
		setTitle("Login to Set");
		setSize(280, 150);
       	setResizable(false);
       	setLocationRelativeTo(null);
       	getRootPane().setDefaultButton(login.getButt());
		JPanel c = (JPanel) getContentPane();
		c.add(login, "LOGIN");
		((CardLayout) c.getLayout()).show(c, "LOGIN");
	}
	
	//Load and display the game lobby
	private void createLobbyFrame() {
		setTitle("Set Lobby");
		setSize(800, 600);
       	setResizable(true);
       	setLocationRelativeTo(null);
       	ActionListener a1 = new ActionListener() {
        	public void actionPerformed(ActionEvent event) {
        		createGameFrame();
            }
        };
       	lobby.setJoinListener(a1);
       	JPanel c = (JPanel) getContentPane();
		c.add(lobby, "LOBBY");
		((CardLayout) c.getLayout()).show(c, "LOBBY");
	}
	
	//Load and display the game
	private void createGameFrame() {
		buildGameGUI();
		setTitle("Set");
		setSize(960, 768);
       	setResizable(true);
       	setLocationRelativeTo(null);
       	JPanel c = (JPanel) getContentPane();
       	c.setBorder(BorderFactory.createEmptyBorder(3,3,3,3));
		c.add(gameCard, "GAME");
		((CardLayout) c.getLayout()).show(c, "GAME");
	}
	
	private void addLoginListeners(LoginPanel p) {
		//Listener for login button
		ActionListener a1 = new ActionListener() {
        	public void actionPerformed(ActionEvent event) {
        		System.out.println("S`"+login.getUser()+"`"+login.getPassword());
        		if(login.getUser().isEmpty() || login.getPassword().isEmpty()) {
        			JOptionPane.showMessageDialog(login,
        					"Please enter a username and password.",
        					"Error",
        					JOptionPane.ERROR_MESSAGE);
        		} else {
        			createLobbyFrame();
        		}
            }
        };
        //Listener for register button
        ActionListener a2 = new ActionListener() {
        	public void actionPerformed(ActionEvent event) {
        		System.out.println("R`"+login.getUser()+"`"+login.getPassword());
            	createLobbyFrame();
            }
        };
        p.addListeners(a1, a2);
	}
	
	private void setTimer(final JButton b) {
		timer = 10;
		grid.toggleSelection();
		b.setEnabled(false);
		ActionListener counter = new ActionListener() {
			public void actionPerformed(ActionEvent evt) 
			{ 
			      b.setText("<html>&nbsp;<br>"+timer+"<br>&nbsp;</html>");
			      timer--;
			      if(timer < 0) {
			      	t.stop();
			      	grid.toggleSelection();
			      	b.setText("<html>&nbsp;<br>SET<br>&nbsp;</html>");
			      	b.setEnabled(true);
			      }
			}};
		 t = new Timer(1000, counter);
		 t.setInitialDelay(0);
		 t.start();
	}
	
	//Add in-game buttons to the provided panel
	private void addGameButtons(JPanel p) {
		JButton setButton = new JButton("<html>&nbsp;<br>SET<br>&nbsp;</html>");
    	setButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                setTimer((JButton) event.getSource());
            }
        });
    	p.add(setButton); //set button
    	
    	JButton logoutButton = new JButton("Logout");
    	logoutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                createLoginFrame();
            }
        });
    	p.add(logoutButton); //logout button
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
                createLoginFrame();
            }
        });
    	p.add(logout); //logout button
    	
    	JButton butt1 = new JButton("<html>&nbsp;<br>SET<br>&nbsp;</html>");
    	butt1.setPreferredSize(new Dimension(10,100));
    	butt1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
            	chat.systemMessage("Card selection toggled");
                setTimer((JButton) event.getSource());
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
               WebNotificationPopup notify = new WebNotificationPopup ();
               notify.setDisplayTime(1000);
               notify.setContent("All cards removed");
               NotificationManager.showNotification (notify);
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
				players.increaseScore(1);
				chat.systemMessage("Player0 Score");
            }
        });
    	p.add(butt5);
    	
    	JButton butt6 = new JButton("+Player");
    	butt6.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
            	Random rand = new Random();
            	Color tempColor = new Color(rand.nextInt(255),rand.nextInt(255),rand.nextInt(255));
				players.addPlayer(rand.nextInt(255), tempColor, "PlayerName", 1234);
				chat.systemMessage("Added Player");
            }
        });
    	p.add(butt6);
    	
    	JButton butt7 = new JButton("-Player");
    	butt7.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
				players.removePlayer(1);
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