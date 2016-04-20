package ggj.summon;

import ggj.Board;
import ggj.gameLogic.GemType;

public class PurpleSummon extends Summon {

    public PurpleSummon() {
        super(GemType.PURPLE);
    }

    @Override
    public void activateSummon(Board me, Board them) {
        them.setHasteTimer(Board.HASTETIMERMAX);
    }

}
