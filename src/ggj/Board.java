package ggj;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Board {

    public static final int WIDTH = 10;
    public static final int HEIGHT = 18;

    //none,R,G,B,Y,P is 0 to 5
    int[][] spaces;
    Image[] gemsGfx;

    public Board() throws SlickException {
        spaces = new int[HEIGHT][WIDTH];
        
        //test board
        spaces[0][0] = 1;
        spaces[1][1] = 2;
        spaces[2][2] = 3;
        spaces[3][3] = 4;
        spaces[4][4] = 5;
        spaces[5][5] = 1;
        spaces[6][6] = 2;
        loadGfx();
    }

    private void loadGfx() throws SlickException {
        gemsGfx = new Image[6];
        gemsGfx[0] = null; //none
        gemsGfx[1] = new Image("content/gemred.png");
        gemsGfx[2] = new Image("content/gemgreen.png");
        gemsGfx[3] = new Image("content/gemblue.png");
        gemsGfx[4] = new Image("content/gemyellow.png");
        gemsGfx[5] = new Image("content/gempurple.png");
    }

    public void draw(Graphics g, int offsetx) throws SlickException {
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                int whichGem = spaces[y][x];
                if (whichGem == 0) {
                    return;
                }
                g.drawImage(gemsGfx[whichGem], x*32, y*32);
            }
        }
    }
}
