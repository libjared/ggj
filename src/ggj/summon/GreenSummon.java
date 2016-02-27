package ggj.summon;

import ggj.Board;
import ggj.Gem;
import ggj.GemType;
import java.util.Random;

public class GreenSummon extends Summon {
    
    Random greenRng = new Random();

    public GreenSummon() {
        super(GemType.GREEN);
    }

    @Override
    public void activateSummon(Board me, Board them) {
        //color snipe or X Attack
        boolean xattack = greenRng.nextInt(10) == 1;

        GemType biggestColor = me.getBiggestColor();

        for (int y = 0; y < Board.HEIGHT; y++) {
            for (int x = 0; x < Board.WIDTH; x++) {
                Gem gemHere = me.getSpace(y, x);
                
                if (gemHere == null)
                    continue;
                
                GemType colorHere = gemHere.getColor();
                
                if (colorHere == biggestColor) {
                    if (xattack) {
                        me.destroyGem(x - 1, y - 1);
                        me.destroyGem(x - 1, y + 1);
                        me.destroyGem(x + 1, y - 1);
                        me.destroyGem(x + 1, y + 1);
                    }
                    me.destroyGem(x, y);
                }
            }
        }
    }

}
