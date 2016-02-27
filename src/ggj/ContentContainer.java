package ggj;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

/**
 *
 * @author Jotham Callaway
 */
public class ContentContainer {

    private static Image red;
    private static Image blue;
    private static Image green;
    private static Image purple;
    private static Image yellow;
    private static Image boardGui;
    private static Image redS;
    private static Image blueS;
    private static Image greenS;
    private static Image purpleS;
    private static Image yellowS;

    private static Sound crash;
    private static Sound dink;
    private static Sound gong;
    private static Sound vanish;
    private static Sound sRed;
    private static Sound sBlue;
    private static Sound sGreen;
    private static Sound sPurple;
    private static Sound sYellow;

    public static void LoadAllContent() throws SlickException {
        red = new Image("content/red.png");
        blue = new Image("content/blue.png");
        green = new Image("content/green.png");
        purple = new Image("content/purple.png");
        yellow = new Image("content/yellow.png");

        boardGui = new Image("content/board.png");

        redS = new Image("content/redRex.png");
        blueS = new Image("content/blueCrab.png");
        greenS = new Image("content/greenSentry.png");
        purpleS = new Image("content/purplePuma.png");
        yellowS = new Image("content/yellowAngel.png");

        crash = new Sound("content/snd/crash.ogg");
        dink = new Sound("content/snd/dink.ogg");
        gong = new Sound("content/snd/gong.ogg");
        vanish = new Sound("content/snd/vanish.ogg");
        sRed = new Sound("content/snd/sred.ogg");
        sGreen = new Sound("content/snd/sgreen.ogg");
        sBlue = new Sound("content/snd/sblue.ogg");
        sYellow = new Sound("content/snd/syellow.ogg");
        sPurple = new Sound("content/snd/spurple.ogg");
    }

    public static Image getRed() {
        return red;
    }

    public static Image getBlue() {
        return blue;
    }

    public static Image getGreen() {
        return green;
    }

    public static Image getPurple() {
        return purple;
    }

    public static Image getYellow() {
        return yellow;
    }

    public static Image getBoardGui() {
        return boardGui;
    }

    static Sound summonSoundFromColor(GemType col) {
        if (col == null) {
            throw new IllegalArgumentException();
        }

        //_rgbyp
        switch (col) {
            case RED:
                return sRed;
            case GREEN:
                return sGreen;
            case BLUE:
                return sBlue;
            case YELLOW:
                return sYellow;
            case PURPLE:
                return sPurple;
            default:
                throw new IndexOutOfBoundsException();
        }
    }

    static Image summonFromColor(GemType col) {
        if (col == null) {
            throw new IllegalArgumentException();
        }

        //_rgbyp
        switch (col) {
            case RED:
                return redS;
            case GREEN:
                return greenS;
            case BLUE:
                return blueS;
            case YELLOW:
                return yellowS;
            case PURPLE:
                return purpleS;
            default:
                throw new IndexOutOfBoundsException();
        }
    }

    static Image imageFromColor(GemType col) {
        if (col == null) {
            throw new IllegalArgumentException();
        }

        //_rgbyp
        switch (col) {
            case RED:
                return getRed();
            case GREEN:
                return getGreen();
            case BLUE:
                return getBlue();
            case YELLOW:
                return getYellow();
            case PURPLE:
                return getPurple();
            default:
                throw new IndexOutOfBoundsException();
        }
    }

    static Sound getVanish() {
        return vanish;
    }

    static Sound getGong() {
        return gong;
    }

    static Sound getCrash() {
        return crash;
    }

    static Sound getDink() {
        return dink;
    }
}
