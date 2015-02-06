/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package GameBackEnd;

/**
 *
 * @author Skorpion
 */
public class Player {
    
    public int uid;
    public int gameScore;
    public String Username;
    
    public void initialize(int id, String username){
        this.uid = id;
        this.Username = username;
    }
    
    
    public void incScore(){
        gameScore++;
    }
    
    public void decScore(){
        gameScore--;
    }
    
    public void resetScore(){
        gameScore = 0;
    }
    
    public int returnScore(){
        return gameScore;
    }
}
