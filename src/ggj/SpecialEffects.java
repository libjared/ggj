package ggj;

import java.util.ArrayList;
import java.util.Random;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class SpecialEffects {
    static ArrayList<CrashFx> crash = new ArrayList<>();
    
    public static void addCrash(int x, int y, int offsetx, int offsety, int color) {
        CrashFx fx = new CrashFx();
        fx.x = x*32+offsetx;
        fx.y = y*32+offsety;
        fx.vx = (float)(3*Math.random()) + 2;
        fx.vy = (float)(3*Math.random()) + -10;
        Image image = ContentContainer.imageFromColor(color);
        fx.img = image.getScaledCopy(32, 32).getSubImage(0, 0, 32, 32);
        crash.add(fx);
    }
    
    public static void update() {
        for (int i = 0; i < crash.size(); i++) {
            CrashFx c = crash.get(i);
            c.x += c.vx;
            c.y += c.vy;
            
            c.vy += 1;
        }
        
        //TODO: kill old crashfxs
    }
    
    public static void draw(Graphics g) throws SlickException {
        for (int i = 0; i < crash.size(); i++) {
            CrashFx c = crash.get(i);
            g.drawImage(c.img, c.x, c.y);
        }
    }
}

class CrashFx {
    float x;
    float y;
    float vx;
    float vy;
    Image img;
}
