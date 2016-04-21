package ggj;

import org.newdawn.slick.Input;
//Recommending deletion of this class
public class KeyboardController implements IController {

    private final Input gcInput;

    public KeyboardController(Input gcInput) {
        this.gcInput = gcInput;
    }

    @Override
    public boolean getDown() {
        return gcInput.isKeyDown(Input.KEY_DOWN);
    }

    @Override
    public boolean getLeft() {
        return gcInput.isKeyDown(Input.KEY_LEFT);
    }

    @Override
    public boolean getRight() {
        return gcInput.isKeyDown(Input.KEY_RIGHT);
    }

    @Override
    public boolean getShuf() {
        return gcInput.isKeyDown(Input.KEY_Z);
    }

    @Override
    public boolean getSummon() {
        return gcInput.isKeyDown(Input.KEY_A);
    }

    @Override
    public boolean getDrain() {
        return gcInput.isKeyDown(Input.KEY_S);
    }

}
