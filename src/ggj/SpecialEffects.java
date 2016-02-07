package ggj;

import java.util.ArrayList;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class SpecialEffects {
    static ArrayList<CrashFx> crash = new ArrayList<>();
    
    static SummonFx currentSummon;
    
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
    
    public static void setSummon(int color) {
        currentSummon = new SummonFx();
        currentSummon.img = ContentContainer.summonFromColor(color).copy();
        currentSummon.ticker = tickerFromColor(color);
    }
    
    public static String tickerFromColor(int col) {
        switch (col) {
            case 0:
            default:
                return "AN ACCIDENT...";
            case 1:
                return "PYROCLASTIC ROAR";
            case 2:
                return "WIND SLASH";
            case 3:
                return "TIDAL CRASH";
            case 4:
                return "JUSTICE'S LIGHT";
            case 5:
                return "GRIM GRAVITY";
        }
    }
    
    public static void update() {
        for (CrashFx c : crash) {
            c.x += c.vx;
            c.y += c.vy;
            
            c.vy += 1;
        }
        
        crash.removeIf((CrashFx t) -> t.y > MyGame.INTERNALH);
        
        if (currentSummon != null) {
            currentSummon.summonTimer--;
            if (currentSummon.summonTimer <= 0) {
                currentSummon.scale += 0.02f;
                currentSummon.img.setAlpha(currentSummon.img.getAlpha() - 0.04f);
                if (currentSummon.scale >= currentSummon.MAXSCALE) {
                    currentSummon = null;
                }
            }
        }
    }
    
    public static void draw(Graphics g) throws SlickException {
        for (int i = 0; i < crash.size(); i++) {
            CrashFx c = crash.get(i);
            g.drawImage(c.img, c.x, c.y);
        }
        
        drawSummonText(g);
        
        drawSummon(g);
    }

    private static void drawSummonText(Graphics g) {
        if (currentSummon != null) {
            Font f = g.getFont();
            String str = currentSummon.ticker;

            int strW = f.getWidth(str);
            int centerOnX = MyGame.INTERNALW / 2;
            int centerOnY = MyGame.INTERNALH / 2 - 40;
            int finalX = centerOnX - strW / 2;
            int finalY = centerOnY;
            f.drawString(finalX, finalY, str);
        }
    }
    
    private static void drawSummon(Graphics g) {
        if (currentSummon != null) {
            Image img = currentSummon.img;
            float scale = currentSummon.scale;
            int targetX = MyGame.INTERNALW / 2;
            int targetY = MyGame.INTERNALH / 2 + 100;
            int imgW = img.getWidth();
            int imgH = img.getHeight();
            float finalX = targetX - (imgW/2) * scale;
            float finalY = targetY - (imgH/2) * scale;
            float finalW = imgW * scale;
            float finalH = imgH * scale;
            
            g.drawImage(img,
                    finalX, finalY, finalX+finalW, finalY+finalH,
                    0, 0, imgW, imgH);
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

class SummonFx {
    Image img;
    float scale = 0.6f;
    final float MAXSCALE = 1.8f;
    int summonTimer = 120;
    String ticker;
}
