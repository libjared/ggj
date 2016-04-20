package ggj;

import org.newdawn.slick.Input;
//Useable?
public class ControllerController implements IController {

    final private static int SNES_B = 2; //18 on mac
    final private static int SNES_A = 1; //19 on mac
    final private static int SNES_X = 0; //16 on mac

    final private static int CONTROLLERBUTTONSHUFFLE = SNES_B;
    final private static int CONTROLLERBUTTONSUMMON = SNES_A;
    final private static int CONTROLLERBUTTONDRAIN = SNES_X;

    private final AutoControllerSelector autoCont;
    private final Input gcInput;

    public ControllerController(Input gcInput) {
        this.gcInput = gcInput;
        this.autoCont = new AutoControllerSelector();

        this.gcInput.addControllerListener(autoCont);
    }

    @Override
    public boolean getDown() {
        return gcInput.isControllerDown(autoCont.getLastControllerToGetUsed());
    }

    @Override
    public boolean getLeft() {
        return gcInput.isControllerLeft(autoCont.getLastControllerToGetUsed());
    }

    @Override
    public boolean getRight() {
        return gcInput.isControllerRight(autoCont.getLastControllerToGetUsed());
    }

    @Override
    public boolean getShuf() {
        return gcInput.isButtonPressed(CONTROLLERBUTTONSHUFFLE,
                autoCont.getLastControllerToGetUsed());
    }

    @Override
    public boolean getSummon() {
        return gcInput.isButtonPressed(CONTROLLERBUTTONSUMMON,
                autoCont.getLastControllerToGetUsed());
    }

    @Override
    public boolean getDrain() {
        return gcInput.isButtonPressed(CONTROLLERBUTTONDRAIN,
                autoCont.getLastControllerToGetUsed());
    }

}
