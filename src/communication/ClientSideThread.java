/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package communication;

import gui.Client;
import gui.GamePanel;
import gui.PlayerPanel;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import javax.swing.SwingUtilities;

import gui.Client;
/**
 *
 * @author Skorpion
 */
class ClientSideThread implements Runnable {

    Socket sock;
    Scanner input;
    Client c;
    boolean threadDone=false;

    ClientSideThread(Socket SOCK) {
        this.sock = SOCK;
        System.out.println("Wrong constructor!");
    }
    
    ClientSideThread(Socket SOCK, Client c) {
        this.sock = SOCK;
        this.c=c;
        System.out.println("right constructor!");
    }

    public void run()
    {
            try{
                    try{
                            input=new Scanner(sock.getInputStream());
                            while(true)
                            {
                                if(input.hasNext())
                                {
                                    String MESSAGE =input.nextLine();
                                    messageDecypher(MESSAGE);
                                    System.out.println("RECEIVED FROM SERVER: "+MESSAGE);
                                    if(threadDone)
                                            break;
                                }

                            }

                    }
                    finally{
                            sock.close();
                    }
            }catch(Exception X){
                System.out.println("ERROR: "+X+" in ClientSideThread.run() "); 
                X.printStackTrace();
            }
    }//String[] data = message.substring(1,message.length()).split("`");
    public void messageDecypher(String message){
        switch(message.substring(0,1)){
            case "V":
            {   //Gives the uid, store it somewhere, alongside with the username
                final int uid = Integer.parseInt(message.substring(1,message.length()));
                SwingUtilities.invokeLater(new Runnable() {
        			public void run() {
        				c.setUser(uid);
        				c.createLobbyFrame();
        			}
        		});
                break;
            }
            case "I":
            {   //Retry to connect, wrong login/pw 
            	 SwingUtilities.invokeLater(new Runnable() {
         			public void run() {
         				c.badLogin();
         			}
         		});
            	try {
					sock.close();
				} catch (IOException e) {
                     System.out.println("ERROR: "+e+" in ClientSideThread.messageDecypher() "); 
				}
                threadDone=true;
                break;
            }
            case "E":
            {
            	 SwingUtilities.invokeLater(new Runnable() {
          			public void run() {
          				c.userExists();
          			}
          		});
             	try {
 					sock.close();
 				} catch (IOException e) {
                                     System.out.println("ERROR: "+e+" in ClientSideThread.messageDecypher() "); 
 				}
                threadDone=true;
                 break;
        	}
            case "C":
            {   //data is a string array of current table card IDs. loop through
                //and populate the table
                final String[] data = message.substring(1,message.length()).split("`");
                final int[] ids = new int[data.length];
                for(int i=0;i<data.length;i++) {
                    ids[i]=Integer.parseInt(data[i]);
                }
                SwingUtilities.invokeLater(new Runnable() {
         			public void run() {
         				c.enterGame(ids);
         			}
         		});
                break;
            }
            case "B":
            {   //gray out the set/submit button for the specified period of time
                int blockTime = Integer.parseInt(message.substring(1,message.length()));
                c.getGamePanel().blockTimer(blockTime);
                break;
            }
            
            case "A":
            {
                final String[] data = message.substring(1,message.length()).split("`");
                if(c.isPlayerValid(Integer.parseInt(data[1])) && c.isGameValid(Integer.parseInt(data[0]))) {
                	SwingUtilities.invokeLater(new Runnable() {
             			public void run() {
             				c.getGamePanel().setTimer(Integer.parseInt(data[2]));
             			}
             		});
                } else {
                	System.out.println("[CST] player or game invalid!");
                }
                //start the cd here
                break;
            }
            case "U":
            {
                //unblock the buttons
            	SwingUtilities.invokeLater(new Runnable() {
         			public void run() {
         				c.getGamePanel().unblockButton();
         			}
         		});
                break;
            }
            case "O":
            {
                //Game is over, basically a pop up that tells players to leave
                //does not close off the game, however
            	SwingUtilities.invokeLater(new Runnable() {
         			public void run() {
         				c.getGamePanel().showNotification("No sets remaining. Game over!", null, false);
         			}
         		});
                break;
            }
            case "S":
            {   //loop through the array, find username for each uid, 
                //and populate the scoreboard
                final String[] data = message.substring(1,message.length()).split("`");
                final int gid = Integer.parseInt(data[0]); //make sure the game youre currently in corresponds
                SwingUtilities.invokeLater(new Runnable() {
                	public void run() {
                		if(c.isGameActive()) { //reload playerPanel
                			GamePanel g = c.getGamePanel();
                			g.getPlayers().clearAll();
                			g.buildColorList();
                			for(int i=2; i<data.length; i+=4) {
                				g.addPlayer(Integer.parseInt(data[i]), data[i+1], Integer.parseInt(data[i+2]), Integer.parseInt(data[i+3]), false);
                			}
                			c.setGameID(gid);
                			g.setGameID(gid);
                			g.setPlayerColor();
                		} else { //add scoreboard to info panel
                			c.getLobbyPanel().updateScoreboard(gid, data);
                		}
                	}
                });
                break;
            }
            case "G":
            {   //used to populate the gamechart in gamelobby
            	String[] data = message.substring(1,message.length()).split("`");
                final Object[][] gameData = new Object[data.length/3][3];
                if(data.length>1) {
                	for(int i=0;i<data.length;i+=3){
                		gameData[i/3] = new Object[] {new Integer(data[i]), data[i+1], new Integer(data[i+2])};
                	}
                	SwingUtilities.invokeLater(new Runnable() {
                		public void run() {
                			c.getLobbyPanel().refreshGamesList(gameData, false);
                		}
                	});
                } else {
                	SwingUtilities.invokeLater(new Runnable() {
                		public void run() {
                			c.getLobbyPanel().refreshGamesList(null, true);
                		}
                	});
                }
                break;
            }
            case "P":
            {   //used to populate the playerchar in gamelobby
                String[] data = message.substring(1,message.length()).split("`");
                final Object[][] userData = new Object[data.length/3][3];
                for(int i=0;i<data.length;i+=3){
	                userData[i/3] = new Object[] {new Integer(data[i]), data[i+1], Integer.parseInt(data[i+2])};
                }
                SwingUtilities.invokeLater(new Runnable() {
        			public void run() {
        				c.getLobbyPanel().refreshUserList(userData);
        			}
        		});
                break;
            }
            case "M":
        {       int uid,gid;
        		String username;
                String actual_msg;
                if(message.substring(1,2).equals("`")){
                    String[] data = message.substring(2,message.length()).split("`");
                    username = data[2];
                    actual_msg = data[3];
                    c.getGamePanel().getChat().userMessage(username, actual_msg, null);
                }                       
                else{
                    String[] data = message.substring(1,message.length()).split("`");
                    username = data[1];
                    actual_msg = data[2];
                    c.getLobbyPanel().addLobbyChatMsg(username, actual_msg); //is this thread safe?
                }
                
                break;
            }
            case "L":
            {   //kick player into the same state as someone trying to login 
            	SwingUtilities.invokeLater(new Runnable() {
        			public void run() {
        				c.createLoginFrame();
        			}
        		});
                threadDone=true;
                break;
            }
            case "R":
            {   //
                int uid,score;
                String[] data = message.substring(1,message.length()).split("`");
                uid = Integer.parseInt(data[0]);
                String username = data[1];
                score = Integer.parseInt(data[2]);
                
                break;
            }
            case "X":
            {
            	String[] data = message.substring(1,message.length()).split("`");
            	final int uid = Integer.parseInt(data[1]);
            	final int newScore = Integer.parseInt(data[2]);
            	SwingUtilities.invokeLater(new Runnable() {
        			public void run() {
        				GamePanel g = c.getGamePanel();
        				PlayerPanel p = g.getPlayers();
        				if(newScore > p.getScore(uid))
        					g.showNotification(p.getName(uid)+" scored a SET!", p.getIcon(uid), true);
        				else
        					g.showNotification(p.getName(uid)+" didn't find SET!", p.getIcon(uid), true);
        				g.getCards().clearSelected();
        				g.getCards().setPlayerColor(p.getColor(c.getPlayerID()));
        				p.setScore(uid, newScore);
        			}
        		});
                break;
            }
            case "Y":
            {
            	final String[] data = message.substring(1,message.length()).split("`");
            	SwingUtilities.invokeLater(new Runnable() {
        			public void run() {
        				c.getGamePanel().removePlayer(Integer.parseInt(data[1]));
        			}
        		});
                break;
            }
            case "Z":
            {
            	final String[] data = message.substring(1,message.length()).split("`");
            	SwingUtilities.invokeLater(new Runnable() {
        			public void run() {
        				c.getGamePanel().addPlayer(Integer.parseInt(data[1]), data[2], Integer.parseInt(data[3]), Integer.parseInt(data[4]), true);
        			}
        		});
                break;
            }
            case "T":
            {
            	final String[] data = message.substring(1,message.length()).split("`");
            	final int[] cards = new int[data.length-2];
            	for(int i = 2; i < data.length; i++)
            		cards[i-2] = Integer.parseInt(data[i]);
            	SwingUtilities.invokeLater(new Runnable() {
        			public void run() {
        				c.getGamePanel().getCards().markSelected(c.getGamePanel().getPlayers().getColor(Integer.parseInt(data[1])), cards);
        			}
        		});
                break;
            }
            default:
            {
                System.out.println("Error: message did not match Protocol on ClientSideThread.decypherMessage");
            }
        }
    }
}
