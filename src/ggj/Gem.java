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


public class Gem {
    
    private GemType color;
    
    public Gem(GemType color){
        this.color = color;
    }
    
    public GemType getColor(){
        return color;
    }
}
