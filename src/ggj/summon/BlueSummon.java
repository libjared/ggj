package ggj.summon;

import ggj.Board;
import ggj.GemType;
import java.util.Random;

public class BlueSummon extends Summon {

    public BlueSummon() {
        super(GemType.BLUE);
    }

    Random blueRng = new Random();
    private int getBlueRandomRange() {
        int max = 15; //TODO magic numbers
        int min = 7;
        return blueRng.nextInt(max - min + 1) + min;
    }
    
    @Override
    public void activateSummon(Board me, Board them) {
        // destroys gems in three diagonal lines (no duplicates)
        boolean duplicatesCheck = true;

        int diagonalS1 = getBlueRandomRange();
        int diagonalS2 = getBlueRandomRange();
        int diagonalS3 = getBlueRandomRange();

        do {
            if (diagonalS1 == diagonalS2) {
                diagonalS2 = getBlueRandomRange();
                continue;
            }
            if (diagonalS2 == diagonalS3) {
                diagonalS3 = getBlueRandomRange();
                continue;
            }
            if (diagonalS3 == diagonalS1) {
                diagonalS1 = getBlueRandomRange();
                continue;
            }
            duplicatesCheck = false;
        } while (duplicatesCheck);

        for (int i = 0; i < Board.WIDTH; i++) {
            me.destroyGem(i, diagonalS1);
            me.destroyGem(i, diagonalS2);
            me.destroyGem(i, diagonalS3);

            diagonalS1--;
            diagonalS2--;
            diagonalS3--;
        }
    }

}
