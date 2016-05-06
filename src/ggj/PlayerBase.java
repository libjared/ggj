package ggj;

/**
 *
 * @author Jotham Callaway
 */
public abstract class PlayerBase {
    private final Board board;
    
    public PlayerBase(Board board) {
        this.board = board;
    }
    
    public Board getBoard() {
        return board;
    }
}
