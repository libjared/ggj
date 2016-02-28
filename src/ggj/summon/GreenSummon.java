package ggj.summon;

import ggj.Board;
import ggj.Gem;
import ggj.GemType;
import java.util.HashMap;
import java.util.Map.Entry;
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

        GemType biggestColor = getBiggestColor(me);

        for (int y = 0; y < Board.HEIGHT; y++) {
            for (int x = 0; x < Board.WIDTH; x++) {
                Gem gemHere = me.getSpace(y, x);

                if (gemHere == null) {
                    continue;
                }

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

    public GemType getBiggestColor(Board which) {
        HashMap<GemType, Integer> counts = new HashMap<>();

        for (int y = 0; y < Board.HEIGHT; y++) {
            for (int x = 0; x < Board.WIDTH; x++) {
                GemType colorHere = which.getSpace(y, x).getColor();

                Integer thisColor = counts.get(colorHere);
                if (thisColor == null) {
                    thisColor = 0;
                }

                thisColor++;

                counts.put(colorHere, thisColor);
            }
        }

        Entry<GemType, Integer> maxEntry = null;

        for (Entry<GemType, Integer> entry : counts.entrySet()) {
            assert entry.getValue() != null; //if I could use int, I would. no nulls, please.
            if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0) {
                maxEntry = entry;
            }
        }
        assert maxEntry != null;
        return maxEntry.getKey();
    }

}
