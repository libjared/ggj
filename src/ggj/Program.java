package ggj;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.SlickException;

public class Program {

    public static void main(String[] args) {
        JavaBoilerplate.setupLibrarySupport();

        try {
            AppGameContainer appgc;
            int internalw = 800;
            int internalh = 600;
            int realwindoww = 800;
            int realwindowh = 600;

            MyGame mg = new MyGame("Gem Monsters", internalw, internalh, realwindoww, realwindowh);
            appgc = new AppGameContainer(mg);
            appgc.setDisplayMode(realwindoww, realwindowh, false);
            appgc.setTargetFrameRate(60);
            appgc.start();
        } catch (SlickException ex) {
            Logger.getLogger(MyGame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
