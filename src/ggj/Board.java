package ggj;

import java.util.Random;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

public class Board {
    public static final int WIDTH = 8;
    public static final int HEIGHT = 16;
    public final int OFFSETX;

    //none,R,G,B,Y,P is 0 to 5
    int[][] spaces;
    
    Image[] gemsGfx;
    
    int fallingGemX = WIDTH/2;
    float fallingGemY = 0;
    int[] fallingGems;

    public Board(int offsetx) throws SlickException {
        OFFSETX = offsetx;
        spaces = new int[HEIGHT][WIDTH];
        
        testBoard();
        loadGfx();
        generateFallingGems();
    }

    private void testBoard() {
        //test board
        for (int y = HEIGHT-1; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                spaces[y][x] = randomColor();
            }
        }
    }

    public void update(GameContainer gc) {
        Input inp = gc.getInput();
        kDown = inp.isKeyDown(Input.KEY_DOWN);
        kLeft = inp.isKeyDown(Input.KEY_LEFT);
        kRight = inp.isKeyDown(Input.KEY_RIGHT);
    
        updateFallingGems();
        
        //last
        kLeftLast = kLeft;
        kRightLast = kRight;
    }
    
    private boolean kLeftLast;
    private boolean kRightLast;
    private boolean kDown;
    private boolean kLeft;
    private boolean kRight;
    
    public void draw(Graphics g) throws SlickException {
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                int color = spaces[y][x];
                if (color == 0) {
                    continue;
                }
                drawGem(g, color, x, y);
            }
        }
        
        drawFallingGems(g);
    }

    private void generateFallingGems() {
        fallingGems = new int[] { randomColor(), randomColor(), randomColor() };
    }
    
    private void updateFallingGems() {
        //update y
        float fallSpd;
        if (kDown) {
            fallSpd = 10f / 60f;
        } else {
            fallSpd = 2f / 60f;
        }
        fallingGemY += fallSpd;
        
        //update x
        if (kLeft && !kLeftLast)
            fallingGemX--;
        if (kRight && !kRightLast)
            fallingGemX++;
        
        fallingGemX = Math.min(Math.max(fallingGemX, 0), WIDTH-1);
        
        //collision
        collideFallingGems();
    }

    private void collideFallingGems() {
        float bottomRealY = fallingGemY + 3;
        boolean collideGem = gemExistsAt(fallingGemX, (int)bottomRealY);
        boolean collideBottom = fallingGemY > HEIGHT;
        
        if (collideGem || collideBottom) {
            int snapBottomY = (int)bottomRealY;
            spaces[snapBottomY-1][fallingGemX] = fallingGems[2];
            spaces[snapBottomY-2][fallingGemX] = fallingGems[1];
            spaces[snapBottomY-3][fallingGemX] = fallingGems[0];
            
            generateFallingGems();
            
            fallingGemX = WIDTH/2;
            fallingGemY = 0;
        }
    }
    
    private boolean gemExistsAt(int x, int y) {
        return spaces[y][x] != 0;
    }

    private void drawFallingGems(Graphics g) {
        drawGem(g, fallingGems[0], fallingGemX, fallingGemY);
        drawGem(g, fallingGems[1], fallingGemX, fallingGemY+1);
        drawGem(g, fallingGems[2], fallingGemX, fallingGemY+2);
    }

    private void drawGem(Graphics g, int color, int x, float y) {
        int finalX = x*32+OFFSETX;
        float finalY = y*32;
        g.setColor(intToColor(color));
        g.setLineWidth(2);
        g.drawOval(finalX, finalY, 32, 32);
    }
    
    private Color intToColor(int what) {
        switch (what)
        {
            case 0:
                return null;
            case 1:
                return Color.red;
            case 2:
                return Color.green;
            case 3:
                return Color.blue;
            case 4:
                return Color.yellow;
            case 5:
                return Color.magenta;
            default:
                return null;
        }
    }
    
    private int randomColor() {
        Random rng = new Random();
        return rng.nextInt(5)+1;
    }

    private void loadGfx() throws SlickException {
        gemsGfx = new Image[6];
//        gemsGfx[0] = null; //none
//        gemsGfx[1] = new Image("content/gemred.png");
//        gemsGfx[2] = new Image("content/gemgreen.png");
//        gemsGfx[3] = new Image("content/gemblue.png");
//        gemsGfx[4] = new Image("content/gemyellow.png");
//        gemsGfx[5] = new Image("content/gempurple.png");
    }
}
