package ggj;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;

class Controls {

    static AutoControllerSelector autoCont;
    static Input gcInput;

    final private static int SNES_B = 2; //18 on mac
    final private static int SNES_A = 1; //19 on mac
    final private static int SNES_X = 0; //16 on mac

    final private static int CONTROLLERBUTTONSHUFFLE = SNES_B;
    final private static int CONTROLLERBUTTONSUMMON = SNES_A;
    final private static int CONTROLLERBUTTONDRAIN = SNES_X;

    static void start(GameContainer gc) {
        autoCont = new AutoControllerSelector();
        gcInput = gc.getInput();
        gcInput.addControllerListener(autoCont);
    }

    static boolean getDown() {
        return gcInput.isControllerDown(autoCont.getLastControllerToGetUsed());
    }

    static boolean getLeft() {
        return gcInput.isControllerLeft(autoCont.getLastControllerToGetUsed());
    }

    static boolean getRight() {
        return gcInput.isControllerRight(autoCont.getLastControllerToGetUsed());
    }

    static boolean getShuf() {
        return gcInput.isButtonPressed(CONTROLLERBUTTONSHUFFLE,
                autoCont.getLastControllerToGetUsed());
    }

    static boolean getSummon() {
        return gcInput.isButtonPressed(CONTROLLERBUTTONSUMMON,
                autoCont.getLastControllerToGetUsed());
    }

    static boolean getDrain() {
        return gcInput.isButtonPressed(CONTROLLERBUTTONDRAIN,
                autoCont.getLastControllerToGetUsed());
    }
}
