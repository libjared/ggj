package ggj;

import org.newdawn.slick.Input;

/**
 *
 * @author Jotham Callaway
 */
public class HumanPlayer extends PlayerBase {
    private final HumanKeyboardController keyControls;

    public HumanPlayer(Input gcInput) {
        this.keyControls = new HumanKeyboardController(gcInput);
        Board board = new Board();
        super(board);
    }
    
    @Override
    public String toString() {
        return "Human Player";
    }
    
    public HumanKeyboardController getKeyControls() {
        return keyControls;
    }
    
}
