package ggj.summon;

import ggj.Board;
import ggj.gameLogic.Gem;
import ggj.gameLogic.GemType;

public class RedSummon extends Summon {

    static int redCall = 0;

    public RedSummon() {
        super(GemType.RED);
    }

    @Override
    public void activateSummon(Board me, Board them) {
        //spawn junk on their top rows
        redCall++;
        int redRows = 1;
        if (redCall > 4) {
            redRows = 2;
            if (redCall > 8) {
                redRows = 3;
            }
        }
        for (int i = 0; i < redRows; i++) {
            for (int x = 0; x < Board.WIDTH; x++) {
                them.setSpace(i, x, new Gem(them.getGemFactory().randomGemColor()));
            }
        }
    }

}
