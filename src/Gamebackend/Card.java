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
public class Card {

    public int color;
    public int shape;
    public int fill;
    public int num;
    
    public Card(int color, int shape, int fill, int numOfSyms){

        this.color=color;
        this.shape=shape;
        this.fill=fill;
        this.num=numOfSyms;
    }
}
