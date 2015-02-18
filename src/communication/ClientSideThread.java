/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package communication;

import gui.Client;

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
                                }

                            }

                    }
                    finally{
                            sock.close();
                    }
            }catch(Exception X){X.printStackTrace();}
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
					System.out.println("Smurfed up at I");
				}
                break;
            }
            case "C":
            {   //data is a string array of current table card IDs. loop through
                //and populate the table
                String[] data = message.substring(1,message.length()).split("`");
                for(int i=0;i<data.length;i++){
                    ;
                }
                break;
            }
            case "B":
            {   //gray out the set/submit button for the specified period of time
                int blockTime;
                blockTime = Integer.parseInt(message.substring(1,message.length()));
                break;
            }
            case "U":
            {
                //unblock the buttons
                break;
            }
            case "O":
            {
                //Game is over, basically a pop up that tells players to leave
                //does not close off the game, however
                break;
            }
            case "S":
            {   //loop through the array, find username for each uid, 
                //and populate the scoreboard
                int gid;
                String[] data = message.substring(1,message.length()).split("`");
                gid = Integer.parseInt(data[0]); //make sure the game youre currently in corresponds
                
                break;
            }
            case "G":
            {   //used to populate the gamechart in gamelobby
                int gid;
                int numOfPlayers;
                System.out.println(message);
//                String[] data = message.substring(1,message.length()).split("`");
//                gid = Integer.parseInt(data[0]); 
//                numOfPlayers = Integer.parseInt(data[1]); 
                break;
            }
            case "P":
            {   //used to populate the playerchar in gamelobby
                String[] data = message.substring(1,message.length()).split("`");
                final Object[][] userData = new Object[data.length/2][3];
                for(int i=0;i<data.length;i+=2){
                	System.out.println(i);
	                userData[i/2] = new Object[] {new Integer(data[i]), data[i+1], new Integer(9000)};
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
                String actual_msg;
                if(message.substring(1,2).equals("`")){
                    String[] data = message.substring(2,message.length()).split("`");
                    gid = Integer.parseInt(data[0]);
                    uid = Integer.parseInt(data[1]);
                    actual_msg = data[2];
                }                       
                else{
                    String[] data = message.substring(1,message.length()).split("`");
                    uid = Integer.parseInt(data[0]);
                    actual_msg = data[1];
                }
                
                break;
            }
            case "L":
            {   //kick player into the same state as someone trying to login 
                
                break;
            }
            case "R":
            {   //
                int uid,score;
                String username;
                String[] data = message.substring(1,message.length()).split("`");
                uid = Integer.parseInt(data[0]);
                username = data[1];
                score = Integer.parseInt(data[2]);
                
                break;
            }
            default:
            {
                System.out.println("Error: message did not match Protocol on ClientSideThread.decypherMessage");
            }
        }
    }
}
