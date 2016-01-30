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
    
    public static void LoadAllContent() throws SlickException {
        red = new Image("content/red.png");
        blue = new Image("content/blue.png");
        green = new Image("content/green.png");
        purple = new Image("content/purple.png");
        yellow = new Image("content/yellow.png");
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
}
