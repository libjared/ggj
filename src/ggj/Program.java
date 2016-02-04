package ggj;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.SlickException;

public class Program {
    public static void main(String[] args) {
        //helps with locating openAL
        System.setProperty("org.lwjgl.librarypath", new File("lib").getAbsolutePath());

        try {
            AppGameContainer appgc;
            int w = 800;
            int h = 600;
            appgc = new AppGameContainer(new MyGame("Gem Monsters", w, h));
            appgc.setDisplayMode(w, h, false);
            appgc.setVSync(true);
            appgc.start();
        } catch (SlickException ex) {
            Logger.getLogger(MyGame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
