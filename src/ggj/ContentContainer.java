package ggj;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

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
    }
    
    public static Image getRed(){
        return red;
    }
    
    public static Image getBlue(){
        return blue;
    }
    
    public static Image getGreen(){
        return green;
    }
    
    public static Image getPurple(){
        return purple;
    }
    
    public static Image getYellow(){
        return yellow;
    }

    public static Image getBoardGui() {
        return boardGui;
    }

    static Image summonFromColor(int col) {
        if (col == 0)
            assert Boolean.TRUE;
        
        //_rgbyp
        switch (col) {
            case 0:
                return null;
            case 1:
                return redS;
            case 2:
                return greenS;
            case 3:
                return blueS;
            case 4:
                return yellowS;
            case 5:
                return purpleS;
            default:
                return null;
        }
    }
    
    static Image imageFromColor(int col) {
        if (col == 0)
            assert Boolean.TRUE;
        
        //_rgbyp
        switch (col) {
            case 0:
                return null;
            case 1:
                return getRed();
            case 2:
                return getGreen();
            case 3:
                return getBlue();
            case 4:
                return getYellow();
            case 5:
                return getPurple();
            default:
                return null;
        }
    }
}
