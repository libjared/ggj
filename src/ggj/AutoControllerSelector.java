package ggj;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.newdawn.slick.ControllerListener;
import org.newdawn.slick.Input;

class AutoControllerSelector implements ControllerListener {

    private int lastControllerToGetUsed = 0;

    public int getLastControllerToGetUsed() {
        return lastControllerToGetUsed;
    }

    @Override
    public void controllerLeftPressed(int controllerId) {
        lastControllerToGetUsed = controllerId;
    }

    @Override
    public void controllerRightPressed(int controllerId) {
        lastControllerToGetUsed = controllerId;
    }

    @Override
    public void controllerUpPressed(int controllerId) {
        lastControllerToGetUsed = controllerId;
    }

    @Override
    public void controllerDownPressed(int controllerId) {
        lastControllerToGetUsed = controllerId;
    }

    @Override
    public void controllerButtonPressed(int controllerId, int buttonCode) {
        lastControllerToGetUsed = controllerId;
        Logger.getLogger(AutoControllerSelector.class.getName()).log(Level.WARNING, String.format("Controller#%d pressed button with code %d", controllerId, buttonCode - 1));
    }

    @Override
    public void setInput(Input input) {
        Logger.getLogger(AutoControllerSelector.class.getName()).log(Level.WARNING, "Set input!?");
    }

    @Override
    public boolean isAcceptingInput() {
        return true;
    }

    @Override
    public void inputStarted() {
    }

    @Override
    public void inputEnded() {
    }

    @Override
    public void controllerButtonReleased(int i, int i1) {
    }

    @Override
    public void controllerUpReleased(int i) {
    }

    @Override
    public void controllerDownReleased(int i) {
    }

    @Override
    public void controllerLeftReleased(int i) {
    }

    @Override
    public void controllerRightReleased(int i) {
    }
}
