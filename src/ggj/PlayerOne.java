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
public class PlayerOne extends Players{

    public PlayerOne(Board board) {
        super(board);
    }
    
    @Override
    public String toString(){
        return "Player 1";
    }
    
}
