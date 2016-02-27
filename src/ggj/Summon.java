package ggj;

/**
 *
 * @author Jotham Callaway
 */
public abstract class Summon {

    private GemType color;

    public Summon(GemType color) {
        this.color = color;
    }

    public abstract void activateSummon();
}
