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
public class HumanKeyboardController implements IController{
    
    private boolean kLeftLast;
    private boolean kRightLast;
    private boolean kShufLast;
    private boolean kSummonLast;
    private boolean kDrainLast;

    private boolean kDown;
    private boolean kLeft;
    private boolean kRight;
    private boolean kShuf;
    private boolean kSummon;
    private boolean kDrain;
    private boolean kDownLast;

    @Override
    public void update() {
        
    }

    @Override
    public boolean hasDown() {
        return kDown && !kDownLast;
    }

    @Override
    public boolean hasLeft() {
        return kLeft && !kLeftLast; 
    }

    @Override
    public boolean hasRight() {
        return kRight && !kRightLast;
    }

    @Override
    public boolean hasShuf() {
        return kShuf && !kShufLast;
    }

    @Override
    public boolean hasSummon() {
       return kSummon && !kSummonLast;
    }

    @Override
    public boolean hasDrain() {
        return kDrain && !kDrainLast;
    }
    
    
}
