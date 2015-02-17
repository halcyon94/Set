/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package communication;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
/**
 *
 * @author Skorpion
 */
class ClientSideThread implements Runnable {

    Socket sock;
    Scanner input;

    ClientSideThread(Socket SOCK) {
        this.sock = SOCK;
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
                                    //here, the received message from server is MESSAGE, so call process message here
                                    //update gui here
                                    //messageDecypher(MESSAGE);
                                    System.out.println("RECEIVED FROM SERVER: "+MESSAGE);
                                }

                            }

                    }
                    finally{
                            sock.close();
                    }
            }catch(Exception X){System.out.print(X);}
    }//String[] data = message.substring(1,message.length()).split("`");
    public void messageDecypher(String message){
        switch(message.substring(0,1)){
            case "V":
            {   //Gives the uid, store it somewhere, alongside with the username
                int uid;
                uid = Integer.parseInt(message.substring(1,message.length()));
                break;
            }
            case "I":
            {   //Retry to connect, wrong login/pw 
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
                String[] data = message.substring(1,message.length()).split("`");
                gid = Integer.parseInt(data[0]); 
                numOfPlayers = Integer.parseInt(data[1]); 
                break;
            }
            case "P":
            {   //used to populate the playerchar in gamelobby
                int uid;
                String username;
                String[] data = message.substring(1,message.length()).split("`");
                uid = Integer.parseInt(data[0]); 
                username = data[1]; 
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
