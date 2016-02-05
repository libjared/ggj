package ggj;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.newdawn.slick.ControllerListener;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;

class Controls {

    static GameContainer gameContainer;
    static MyControllerListener myControllerListener;
    static Input gcInput;
    
    final private static int SNES_B = 2; //18 on mac
    final private static int SNES_A = 1; //19 on mac
    final private static int SNES_X = 0; //16 on mac
    
    final private static int CONTROLLERBUTTONSHUFFLE = SNES_B;
    final private static int CONTROLLERBUTTONSUMMON = SNES_A;
    final private static int CONTROLLERBUTTONDRAIN = SNES_X;
    
    static void start(GameContainer gc) {
        gameContainer = gc;
        myControllerListener = new MyControllerListener();
        gcInput = gameContainer.getInput();
        gcInput.addControllerListener(myControllerListener);
    }
    
    static boolean getDown() {
        return gcInput.isControllerDown(myControllerListener.getLastControllerToGetUsed());
    }
    
    static boolean getLeft() {
        return gcInput.isControllerLeft(myControllerListener.getLastControllerToGetUsed());
    }
    
    static boolean getRight() {
        return gcInput.isControllerRight(myControllerListener.getLastControllerToGetUsed());
    }
    
    static boolean getShuf() {
        return gcInput.isButtonPressed(CONTROLLERBUTTONSHUFFLE,
                myControllerListener.getLastControllerToGetUsed());
    }
    
    static boolean getSummon() {
        return gcInput.isButtonPressed(CONTROLLERBUTTONSUMMON,
                myControllerListener.getLastControllerToGetUsed());
    }
    
    static boolean getDrain() {
        return gcInput.isButtonPressed(CONTROLLERBUTTONDRAIN,
                myControllerListener.getLastControllerToGetUsed());
    }
}

class MyControllerListener implements ControllerListener {
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
        Logger.getLogger(MyControllerListener.class.getName()).log(Level.WARNING, String.format("Controller#%d pressed button with code %d", controllerId, buttonCode-1));
    }

    @Override
    public void setInput(Input input) {
        Logger.getLogger(MyControllerListener.class.getName()).log(Level.WARNING, "Set input!?");
    }

    @Override
    public boolean isAcceptingInput() {
        return true;
    }

    @Override
    public void inputStarted() {}
    @Override
    public void inputEnded() {}
    @Override
    public void controllerButtonReleased(int i, int i1) {}
    @Override
    public void controllerUpReleased(int i) {}
    @Override
    public void controllerDownReleased(int i) {}
    @Override
    public void controllerLeftReleased(int i) {}
    @Override
    public void controllerRightReleased(int i) {}
}