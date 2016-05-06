package ggj;

public interface IController {
    
    /**
     * re-gather required information for this controller
     */
    void update();

    /**
     * @return true if fastfalling
     */
    boolean hasFastFall();

    /**
     * @return true if moved left this update-interval
     */
    boolean hasLeft();

    /**
     * @return true if moved right this update-interval
     */
    boolean hasRight();
    
    /**
     * @return true if shuffled gems this update-interval
     */
    boolean hasShuf();

    /**
     * @return true if summoned this update-interval
     */
    boolean hasSummon();

    /**
     * @return true if drained this update-interval
     */
    boolean hasDrain();
}
