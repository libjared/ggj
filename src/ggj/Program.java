package ggj;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.SlickException;

public class Program {
    public static void main(String[] args) {
        try {
            AppGameContainer appgc;
            int w = 800;
            int h = 600;
            appgc = new AppGameContainer(new MyGame("Simple Slick Game", w, h));
            appgc.setDisplayMode(w, h, false);
            appgc.setVSync(true);
            appgc.start();
        } catch (SlickException ex) {
            Logger.getLogger(MyGame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
