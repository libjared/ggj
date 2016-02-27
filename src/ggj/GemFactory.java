package ggj;

import java.util.ArrayList;
import java.util.Random;

public class GemFactory {

    ArrayList<GemType> rngBuf;
    Random rng;

    public GemFactory() {
        this.rngBuf = new ArrayList<>();
        this.rng = new Random();
        ensureBuffer(100);
    }

    public GemType randomGemColor() {
        GemType newRand = useRngToMakeANewColor();
        rngBuf.add(newRand);
        return rngBuf.remove(0);
    }

    public GemType[] peekNextThree() {
        return new GemType[]{
            rngBuf.get(0),
            rngBuf.get(1),
            rngBuf.get(2)
        };
    }

    public Gem[] generateFallingGems() {
        return new Gem[]{
            new Gem(randomGemColor()),
            new Gem(randomGemColor()),
            new Gem(randomGemColor())
        };
    }

    private void ensureBuffer(int n) {
        while (rngBuf.size() < n) {
            rngBuf.add(useRngToMakeANewColor());
        }
    }

    private GemType useRngToMakeANewColor() {
        int result = rng.nextInt(5) + 1;
        switch (result) {
            case 1:
                return GemType.RED;
            case 2:
                return GemType.GREEN;
            case 3:
                return GemType.BLUE;
            case 4:
                return GemType.YELLOW;
            case 5:
                return GemType.PURPLE;
        }
        throw new IndexOutOfBoundsException();
    }
}
