package ggj;

import java.util.ArrayList;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class SpecialEffects {
    static ArrayList<CrashFx> crash = new ArrayList<>();
    
    public static void addCrash(int x, int y, int offsetx, int offsety, int color) {
        CrashFx fx = new CrashFx();
        fx.x = x*32+offsetx;
        fx.y = y*32+offsety;
        fx.vx = 2;
        fx.vy = -2;
        Image image = ContentContainer.imageFromColor(color);
        fx.img = image.getSubImage(0, 0, 64, 64);
        crash.add(fx);
    }
    
    public static void update() {
        for (int i = 0; i < crash.size(); i++) {
            CrashFx c = crash.get(i);
            c.x += c.vx;
            c.y += c.vy;
        }
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
