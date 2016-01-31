package ggj;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Shape;

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
    
    public static void LoadAllContent() throws SlickException {
        red = new Image("content/red.png");
        blue = new Image("content/blue.png");
        green = new Image("content/green.png");
        purple = new Image("content/purple.png");
        yellow = new Image("content/yellow.png");
        boardGui = new Image("content/board.png");
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

    static Image imageFromColor(int col) {
        //_rgbyp
        switch (col) {
            case 0:
                return null;
            case 1:
                return ContentContainer.getRed();
            case 2:
                return ContentContainer.getGreen();
            case 3:
                return ContentContainer.getBlue();
            case 4:
                return ContentContainer.getYellow();
            case 5:
                return ContentContainer.getPurple();
            default:
                return null;
        }
    }
}
