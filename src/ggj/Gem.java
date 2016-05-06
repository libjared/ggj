package ggj;

import org.newdawn.slick.Graphics;

/**
 *
 * @author Jotham Callaway
 */
public class Gem {

    private final GemType color;

    public Gem(GemType color) {
        this.color = color;
    }

    public GemType getColor() {
        return color;
    }

    void drawGem(Graphics g, int x, float y, Board board) {
        int finalX = x * 32 + board.offsetx;
        float finalY = y * 32 + board.offsety;
        g.drawImage(ContentContainer.imageFromColor(this.getColor()),
                finalX, finalY, finalX + 32, finalY + 32,
                0, 0, 64, 64);
    }
}
