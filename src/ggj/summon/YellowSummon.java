package ggj.summon;

import ggj.Board;
import ggj.GemType;

public class YellowSummon extends Summon {

    public YellowSummon() {
        super(GemType.YELLOW);
    }

    @Override
    public void activateSummon(Board me, Board them) {
        int myH = findAverageHeight(me);
        int theirH = findAverageHeight(them);
        int averageHeight = (myH + theirH) / 2;
        me.remakeBoardWithHeight(averageHeight);
        them.remakeBoardWithHeight(averageHeight);
    }

    private int findAverageHeight(Board which) {
        for (int y = 0; y < Board.HEIGHT; y++) {
            for (int x = 0; x < Board.WIDTH; x++) {
                if (which.gemExistsAt(x, y)) {
                    return y;
                }
            }
        }
        return Board.HEIGHT;
    }

}
