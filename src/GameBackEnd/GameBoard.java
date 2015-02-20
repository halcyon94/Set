/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package GameBackEnd;

import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author Skorpion
 */
public class GameBoard {
    //need 2 datastructures, one for cards on board, and one for cards in deck
    private final ArrayList<Integer> cardsOnBoard = new ArrayList<>(); 
    private final ArrayList<Integer> deck = new ArrayList<>(); 

    public void initialize(){
        generateDeck();
        addCards(12);
        isBoardPlayable();     
    }
    
    public int processSubmit(int c1, int c2, int c3){
        if(checkSet(c1,c2,c3)){         //valid set
            removeSet(c1,c2,c3);        //remove set
            if(!isBoardPlayable()){    //game cant continue
                return 2;
            }
            else{
                addCards(3);
                return 0;
            }
        }
        else{ //invalid set
            return 1;
        }
        
    }
    
    //returns the cards on the board in this format 
    // "C7-23-54-26-61-13-80-71-..." 
    public String returnCardsOnBoard(){
        String cards = "C";        
        for (Integer cardsOnBoard1 : cardsOnBoard) {
            cards = cards + Integer.toString(cardsOnBoard1) + "`";
        }
        return cards;
    }
    
    //After a successful set, or maybe after a first deal (might need to addCards mult times)
    //done when number of cards on the board <12, or when the board is not playable
    private void addCards(int num){
        for(int i = 0;i<num;i++){
            int tempCard = deck.remove(0); //always removes the first card
            cardsOnBoard.add(tempCard); //adds onto the board
        }
    }
        
    //checks if there is a set on the board
    private boolean isBoardPlayable(){
        boolean playable = false;
        while(!playable && deck.size()>0){
            for(int i = 0;i<cardsOnBoard.size();i++){
                for(int j = 1;j<cardsOnBoard.size();j++){
                    if(j == i)
                        continue;
                    for(int k=2;k<cardsOnBoard.size();k++){
                        if((k == j) || (k == i))
                            continue;
                        if(checkSet(cardsOnBoard.get(i),cardsOnBoard.get(j),cardsOnBoard.get(k))){
                            playable = true;
                            return true;
                        }
                    }
                }
            }
            addCards(3);
        }
        return false;  
    }
    
    private boolean checkSet(int card1, int card2, int card3){
        Card c1 = new Card(card1/27,(card1%27)/9,(card1%9)/3,card1%3);
        Card c2 = new Card(card2/27,(card2%27)/9,(card2%9)/3,card2%3);
        Card c3 = new Card(card3/27,(card3%27)/9,(card3%9)/3,card3%3);
        if((((c1.color==c2.color)&&(c1.color==c3.color))||((c1.color!=c2.color)&&(c1.color!=c3.color)&&(c2.color!=c3.color)))
                        &&
                        (((c1.shape==c2.shape)&&(c1.shape==c3.shape))||((c1.shape!=c2.shape)&&(c1.shape!=c3.shape)&&(c2.shape!=c3.shape)))
                        &&
                        (((c1.fill==c2.fill)&&(c1.fill==c3.fill))||((c1.fill!=c2.fill)&&(c1.fill!=c3.fill)&&(c2.fill!=c3.fill)))
                        &&
                        (((c1.num==c2.num)&&(c1.num==c3.num))||((c1.num!=c2.num)&&(c1.num!=c3.num)&&(c2.num!=c3.num)))
                      )
        {
            return true;
        }
        return false;
    }
    
    private void removeSet(int c1, int c2, int c3){
        //loop through cardsOnBoard and remove the 3 cards
        System.out.println("removing cards "+c1+" "+" "+c2+ " "+c3);
        for(int i=0;i<cardsOnBoard.size();i++){
            if(cardsOnBoard.get(i).equals(c1) || cardsOnBoard.get(i).equals(c2) || cardsOnBoard.get(i).equals(c3)){
                cardsOnBoard.remove(i);
                i--;
            }
        }
    }
    
    //called at initialization of GameBoard
    //randomly shuffless the deck ArrayList, so that the cards returned are not in the same order everytime 
    private void generateDeck(){
        for(int i = 0; i<81;i++){
            deck.add(i);
        }
        Collections.shuffle(deck);
    }
}
