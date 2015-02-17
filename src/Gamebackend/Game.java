/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package GameBackEnd;

import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
/**
 *
 * @author Skorpion
 */
public class Game {
    private static int gameNum;
    private int gid;
    public HashMap <Integer,Player> playerCollection = new HashMap<Integer,Player>(); 
    public GameBoard board = new GameBoard();
    int setLock; //the uid of the player who holds the Set lock
    
    
    public Game(){
        gameNum++;
        gid = gameNum; 
        board.initialize();
    }
    
    public void setLock(int uid){
        setLock = uid;
    }
    
    public void resetLock(){
        setLock = 0;
    }
    
    public int lockOwner(){
        return setLock;
    }
    
    public int returnGid(){
        return gid;
    }
    
    public String block(int seconds){
        String message = "B" + Integer.toString(seconds);
        return message;
    }
    
    public String unblock(){
        String message = "U";
        return message;
    }
    public String onSubmit(int uid, int c1, int c2, int c3){
        String message;
        Player player;
        switch (board.processSubmit(c1,c2,c3)){
            case 0: //send message to uid saying its a valid set, increase score by 1, and send new scoreboard and new board to all
                    player = findPlayer(uid);
                    player.incScore();
                    message = board.returnCardsOnBoard() + "*" + returnScoreBoard();
                    break;
            case 1: //send message to uid saying that its an invalid set, decrease score by 1, send new scoreboard to all
                    player = findPlayer(uid);
                    player.decScore();
                    message = returnScoreBoard();
                    break;
            case 2: //send message saying its a valid set, increase score by 1, update the scoreboard, and send message thats its Game Over, blocking their buttons
                    player = findPlayer(uid);
                    player.incScore();
                    message = returnScoreBoard() + "*" + "O";
                    break;
            default:
                message = "";
                System.err.println("Error: game.onSubmit");
                System.exit(-1);         
        }
        return message;
    }
    
    public String returnScoreBoard(){
        String message = "S"+gid;
        int tempScore;
        int tempUid;
        //iterate over all players, returning their uids and scores in a message as a string
        for(Map.Entry<Integer,Player> entry : playerCollection.entrySet()){
            tempScore = entry.getValue().returnScore();
            tempUid = entry.getKey();
            message = message + "`" + Integer.toString(tempUid) + "`" + Integer.toString(tempScore);
        }
        return message;
    }
    
    //pretty much done
    //add player to the scoreboard, send the player the gameboard, update the scoreboard for all players
    public void join(int uid, Player player){
        addPlayer(uid,player);
        player.resetScore();
    }
    
    public void leave(int uid){
        disconnectPlayer(uid);
        
        //send message to the game/lobby chat, saying that this player left
        //sends message to the rest of the players, updating their scoreboards
    }
    
    public int playerNum(){
        return playerCollection.size();
    }
    
    public Player findPlayer(int uid){
        if(playerCollection.containsKey(uid)){
            return playerCollection.get(uid);
        }
        else{
            System.err.println("error in game.findPlayer with uid: "+uid);
            System.exit(-1);
            return null; //is this ok to do?
        }
    }
    //whenever someone signs in, they get a player uid that is associated with a player object
    private void addPlayer(int uid, Player player){
        playerCollection.put(uid,player);
    }
    
    private void disconnectPlayer(int uid){
        playerCollection.remove(uid);
    }
 
}
