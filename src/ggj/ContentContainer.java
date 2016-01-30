/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ggj;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

/**
 *
 * @author Jotham Callaway
 */
public class ContentContainer {
    private Image red = new Image("content//red.png");
    private Image blue = new Image("content//blue.png");
    private Image green = new Image("content//green.png");
    private Image purple = new Image("content//purple.png");
    private Image yellow = new Image("content//yellow.png");
    
    public ContentContainer() throws SlickException{
        
    }
    
    public Image getRed(){
        return red;
    }
    
    public Image getBlue(){
        return blue;
    }
    
    public Image getGreen(){
        return green;
    }
    
    public Image getPurple(){
        return purple;
    }
    
    public Image getYellow(){
        return yellow;
    }

}
