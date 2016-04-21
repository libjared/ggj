/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ggj;

import org.newdawn.slick.Input;

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
    
    private final Input gcInput;

    public HumanKeyboardController(Input gcInput) {
        this.gcInput = gcInput;
    }


    @Override
    public void update() {
        kLeftLast = kLeft;
        kRightLast = kRight;
        kShufLast = kShuf;
        kSummonLast = kSummon;
        kDrainLast = kDrain;

        kDown = gcInput.isKeyDown(Input.KEY_DOWN);
        kLeft = gcInput.isKeyDown(Input.KEY_LEFT);
        kRight = gcInput.isKeyDown(Input.KEY_RIGHT);
        kShuf = gcInput.isKeyDown(Input.KEY_UP);
        kSummon = gcInput.isKeyDown(Input.KEY_A);
        kDrain = gcInput.isKeyDown(Input.KEY_S);
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
