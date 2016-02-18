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
public abstract class Summon {
    private GemType color;
    
    public Summon(GemType color){
        this.color = color;
    }
    
    public abstract void activateSummon();
}
