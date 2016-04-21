/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ggj;

/**
 *
 * @author Jotham Callaway
 */
public abstract class Players {
    private Board board;
    
    
    public Players(Board board){
        this.board = board;
    }
    
    public Board getPlayersBoard(){
        return board;
    }
}
