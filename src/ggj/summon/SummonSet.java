package ggj.summon;

public class SummonSet {

    private final RedSummon red;
    private final GreenSummon green;
    private final BlueSummon blue;
    private final YellowSummon yellow;
    private final PurpleSummon purple;

    public SummonSet() {
        red = new RedSummon();
        green = new GreenSummon();
        blue = new BlueSummon();
        yellow = new YellowSummon();
        purple = new PurpleSummon();
    }

    public RedSummon getRed() {
        return red;
    }

    public GreenSummon getGreen() {
        return green;
    }

    public BlueSummon getBlue() {
        return blue;
    }

    public YellowSummon getYellow() {
        return yellow;
    }

    public PurpleSummon getPurple() {
        return purple;
    }
}
