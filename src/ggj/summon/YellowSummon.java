package ggj.summon;

import ggj.Board;
import ggj.GemType;

public class YellowSummon extends Summon {

    public YellowSummon() {
        super(GemType.YELLOW);
    }

    @Override
    public void activateSummon(Board me, Board them) {
        int averageHeight = (me.findAverageHeight() + them.findAverageHeight()) / 2;
        me.remakeBoardWithHeight(averageHeight);
        them.remakeBoardWithHeight(averageHeight);
    }

}
