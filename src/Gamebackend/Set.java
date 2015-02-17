/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package GameBackEnd;

import static GameBackEnd.GameLobby.playerCollection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

/**
 *
 * @author Skorpion
 */
public class Set {

    /**
     * @param args the command line arguments
     */
    
    
    public static void main(String[] args) throws IOException, Exception {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String s;
        while((s=in.readLine())!=null && (s.length() !=0)){
            processMessage(s);
        }
        // TODO code application logic here
    }
    
    public static void processMessage(String message) throws Exception{
         
        switch(message.substring(0,1)){
            case "S": 
                {
                    String username, password;
                    int result;
                    String[] data = message.substring(1,message.length()).split("`");
                    username = data[0];
                    password = data[1];
                    result = GameLobby.enterLobby(username,password,false);
                    if(result >1){
                        System.out.println(result);
                        System.out.println("\t" + GameLobby.returnGames());
                        System.out.println("\t" + GameLobby.returnPlayers());
                        for(Map.Entry<Integer,Player> entry : GameLobby.playerCollection.entrySet()){
                            System.out.println(entry.getKey());
                            System.out.println("\t"+GameLobby.returnPlayers());
                        }
                    }
                    else{
                        System.out.println(result);
                        System.out.println("I");
                    }
                    break;
                }
            case "R":
            {
                    String username, password;
                    int result;
                    String[] data = message.substring(1,message.length()).split("`");
                    username = data[0];
                    password = data[1];
                    result = GameLobby.enterLobby(username,password,true);
                    System.out.println(result);
                    System.out.println("\t" + GameLobby.returnGames());
                    System.out.println("\t" + GameLobby.returnPlayers());
                    for(Map.Entry<Integer,Player> entry : GameLobby.playerCollection.entrySet()){
                        System.out.println(entry.getKey());
                        System.out.println("\t"+GameLobby.returnPlayers());
                    }
                    break;
                               
            } 
            case "C":
                {   
                    String uid_s = message.substring(1,message.length());
                    int uid =Integer.parseInt(uid_s);
                    Game game = GameLobby.createGame(uid);
                    System.out.println(uid); 
                        System.out.println("\t" + game.board.returnCardsOnBoard());
                        System.out.println("\t" + game.returnScoreBoard());
                    for(Map.Entry<Integer,Player> entry : GameLobby.playerCollection.entrySet()){
                        System.out.println(entry.getKey());
                        System.out.println("\t" + GameLobby.returnGames());
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
                    System.out.println(uid); 
                        System.out.println("\t" + game.board.returnCardsOnBoard());
                    for(Map.Entry<Integer,Player> entry : game.playerCollection.entrySet()){
                        System.out.println(entry.getKey());
                        System.out.println("\t" + game.returnScoreBoard());
                    }
                    for(Map.Entry<Integer,Player> entry : GameLobby.playerCollection.entrySet()){
                        System.out.println(entry.getKey());
                        System.out.println("\t" + GameLobby.returnGames());
                    }
                    break;
                }

            case "D":
                {   
                    
                    String[] data = message.substring(1,message.length()).split("`");
                    int gid = Integer.parseInt(data[0]);
                    int uid = Integer.parseInt(data[1]);
                    Game game = GameLobby.findGame(gid);
                    game.leave(uid);
                    GameLobby.db.updateUserScore(uid,game.findPlayer(uid).returnScore());
                    for(Map.Entry<Integer,Player> entry : game.playerCollection.entrySet()){
                        System.out.println(entry.getKey());
                        System.out.println("\t" + game.returnScoreBoard());
                    }
                    for(Map.Entry<Integer,Player> entry : GameLobby.playerCollection.entrySet()){
                        System.out.println(entry.getKey());
                        System.out.println("\t" + GameLobby.returnGames());
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
                            System.out.println(entry.getKey());
                            System.out.println("\t" + game.block(5));
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
                        System.out.println(entry.getKey());
                        System.out.println("\t" + game.returnScoreBoard());
                        if(entry.getKey() == uid){
                            continue;
                        }
                        System.out.println("\t" + game.unblock());
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
                            System.out.println(entry.getKey());
                            for (String cmd : cmds) {
                                System.out.println("\t" + cmd);
                            }
                        } 
                        game.resetLock();
                        for(Map.Entry<Integer,Player> entry : game.playerCollection.entrySet()){
                            if(entry.getKey() == uid){
                                continue;
                            }
                            System.out.println(entry.getKey());
                            System.out.println("\t" + game.unblock());
                        }
                    }
                    break;
                }

            case "M": 
                {   int uid,gid;
                    String actual_msg;
                    if(message.substring(1,2) == "`"){
                        String[] data = message.substring(2,message.length()).split("`");
                        gid = Integer.parseInt(data[0]);
                        uid = Integer.parseInt(data[1]);
                        actual_msg = data[2];
                        Player player = GameLobby.findPlayer(uid);
                        Game game = GameLobby.findGame(gid);
                        for(Map.Entry<Integer,Player> entry : game.playerCollection.entrySet()){
                            System.out.println(entry.getKey());
                            System.out.println("\t"+uid + " " + actual_msg);
                        }
                    }                       
                    else{
                        String[] data = message.substring(1,message.length()).split("`");
                        uid = Integer.parseInt(data[0]);
                        actual_msg = data[1];
                        for(Map.Entry<Integer,Player> entry : GameLobby.playerCollection.entrySet()){
                            System.out.println(entry.getKey());
                            System.out.println("\t"+uid + " " + actual_msg);
                        }
                    }
                    break;
                }
            case "L": 
                {
                    int uid;
                    uid = Integer.parseInt(message.substring(1,message.length()));      
                    System.out.println(uid);
                    System.out.println(GameLobby.db.returnRankings());
                    break;
                }
            case "K":
            {
                int uid = Integer.parseInt(message.substring(1,message.length()));
                GameLobby.delPlayer(uid);
                System.out.println(uid);
                System.out.println("\t Logged out" );
            
            }

        }
    
    }
}