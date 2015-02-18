/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package communication;

import GameBackEnd.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author Skorpion
 */
public class MessageProcessor implements Runnable {

    @Override
    public void run() {
        String message;
        while(true){
            try {
                message = SetServer.bqueue.take();
                try {
                    processMessage(message);
                } catch (Exception ex) {
                    System.out.println("ERROR: "+ex+" in MessageProcessor.run() (1) "); 
                }
            } catch (InterruptedException ex) {
                    System.out.println("ERROR: "+ex+" in MessageProcessor.run() (1) ");
            }
        }
    }
    public static void processMessage(String message) throws Exception{
         
        switch(message.substring(0,1)){

            case "C":
                {   
                    String uid_s = message.substring(1,message.length());
                    int uid =Integer.parseInt(uid_s);
                    Game game = GameLobby.createGame(uid);
                    sendMessage(uid,game.board.returnCardsOnBoard());
                    sendMessage(uid,game.returnScoreBoard());                    
                    for(Map.Entry<Integer,Player> entry : GameLobby.playerCollection.entrySet()){
                        sendMessage(entry.getKey(),GameLobby.returnGames());
                    }
                    break;
                }

            case "J": 
                {
                    String[] data = message.substring(1,message.length()).split("`");
                    int gid = Integer.parseInt(data[0]);
                    int uid = Integer.parseInt(data[1]);
                    GameLobby.joinGame(uid, gid);
                    Game game = GameLobby.findGame(gid);
                    sendMessage(uid,game.board.returnCardsOnBoard());
                    for(Map.Entry<Integer,Player> entry : game.playerCollection.entrySet()){
                        sendMessage(entry.getKey(),game.returnScoreBoard());
                    }
                    for(Map.Entry<Integer,Player> entry : GameLobby.playerCollection.entrySet()){
                        sendMessage(entry.getKey(),GameLobby.returnGames());
                    }
                    break;
                }

            case "D":
                {       
                    String[] data = message.substring(1,message.length()).split("`");
                    int gid = Integer.parseInt(data[1]);
                    int uid = Integer.parseInt(data[2]);
                    Game game = GameLobby.findGame(gid);
                    GameLobby.db.updateUserScore(uid, game.findPlayer(uid).returnScore());
                    game.leave(uid);
                    for(Map.Entry<Integer,Player> entry : game.playerCollection.entrySet()){
                        sendMessage(entry.getKey(),game.returnScoreBoard());
                    }
                    for(Map.Entry<Integer,Player> entry : GameLobby.playerCollection.entrySet()){
                        sendMessage(entry.getKey(),GameLobby.returnGames());
                        sendMessage(entry.getKey(),GameLobby.returnPlayers());
                    }
                    break;
                }

            case "B":
                {   
                    String[] data = message.substring(1,message.length()).split("`");
                    int gid = Integer.parseInt(data[0]);
                    int uid = Integer.parseInt(data[1]);
                    Game game = GameLobby.findGame(gid);
                    for(Map.Entry<Integer,Player> entry : game.playerCollection.entrySet()){
                        if(entry.getKey()!=uid){
                            sendMessage(entry.getKey(),game.block(5));
                        }
                    }
                    game.setLock(uid);  
                    break;
                }

            case "F": 
                {        
                    String[] data = message.substring(1,message.length()).split("`");
                    int gid = Integer.parseInt(data[0]);
                    int uid = Integer.parseInt(data[1]);
                    Player player = GameLobby.findPlayer(uid);
                    player.decScore();
                    Game game = GameLobby.findGame(gid);
                    game.resetLock();
                    for(Map.Entry<Integer,Player> entry : game.playerCollection.entrySet()){
                        sendMessage(entry.getKey(),game.returnScoreBoard());
                        if(entry.getKey() == uid){
                            continue;
                        }
                        sendMessage(entry.getKey(),game.unblock());
                    }
                    break;
                }

            case "P": 
                {
                    String new_message="";
                    String[] data = message.substring(1,message.length()).split("`");
                    int gid = Integer.parseInt(data[0]);
                    int uid = Integer.parseInt(data[1]);
                    int c1 = Integer.parseInt(data[2]);
                    int c2 = Integer.parseInt(data[3]);
                    int c3 = Integer.parseInt(data[4]);
                    Game game = GameLobby.findGame(gid);
                    if(uid == game.lockOwner()){
                        new_message = game.onSubmit(uid, c1, c2, c3);
                        String[] cmds = new_message.split("*");
                        for(Map.Entry<Integer,Player> entry : game.playerCollection.entrySet()){
                            for (String cmd : cmds) {
                                sendMessage(entry.getKey(),cmd);
                            }
                        } 
                        game.resetLock();
                        for(Map.Entry<Integer,Player> entry : game.playerCollection.entrySet()){
                            if(entry.getKey() == uid){
                                continue;
                            }
                            sendMessage(entry.getKey(),game.unblock());
                        }
                    }
                    break;
                }

            case "M": 
                {   int uid,gid;
                    String actual_msg;
                    System.out.println(message.substring(1,2));
                    if(message.substring(1,2).equals("`")){
                        String[] data = message.substring(2,message.length()).split("`");
                        gid = Integer.parseInt(data[0]);
                        uid = Integer.parseInt(data[1]);
                        actual_msg = data[2];
                        Player player = GameLobby.findPlayer(uid);
                        Game game = GameLobby.findGame(gid);
                        for(Map.Entry<Integer,Player> entry : game.playerCollection.entrySet()){
                            sendMessage(entry.getKey(),"M`"+gid+'`'+uid +'`'+ actual_msg);
                        }
                    }                       
                    else{
                        String[] data = message.substring(1,message.length()).split("`");
                        uid = Integer.parseInt(data[0]);
                        actual_msg = data[1];
                        for(Map.Entry<Integer,Player> entry : GameLobby.playerCollection.entrySet()){
                            sendMessage(entry.getKey(),"M"+uid +'`'+ actual_msg);
                        }
                    }
                    break;
                }
            case "L": 
                {
                    int uid;
                    uid = Integer.parseInt(message.substring(1,message.length())); 
                    sendMessage(uid,GameLobby.db.returnRankings());
                    break;
                }
            case "K":
            {
                int uid = Integer.parseInt(message.substring(1,message.length()));
                GameLobby.delPlayer(uid);
                sendMessage(uid,"L");
                Socket sock = SetServer.SocketList.get(uid);
                sock.close();
                SetServer.SocketList.remove(uid);
                break;
            
            }
            case "E":
            {
                int uid;
                uid = Integer.parseInt(message.substring(1,message.length()));
                sendMessage(uid,GameLobby.returnPlayers());
                break;
            }
            case "G":
            {
                int uid;
                uid = Integer.parseInt(message.substring(1,message.length())); 
                sendMessage(uid,GameLobby.returnGames());
                break;
            }
            default:{
                System.err.println("Error: Received message is not in protocol: " + message);                
            }
        }
    }
    
    private static void sendMessage(int uid, String message) throws IOException{
        Socket sock = SetServer.SocketList.get(uid);
        PrintWriter TEMP_OUT=new PrintWriter(sock.getOutputStream());
        TEMP_OUT.println(message);
        TEMP_OUT.flush();
    }    
}
