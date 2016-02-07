package ggj;

import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.opengl.pbuffer.FBOGraphics;

public class MyGame extends BasicGame {

    static Board left;
    static Board right;
    static boolean disableAutoFall = false;
    static Board winner;
    
    static int INTERNALW;
    static int INTERNALH;
    
    static int WINDOWW;
    static int WINDOWH;

    public MyGame(String gamename, int internalW, int internalH,
            int windowW, int windowH) throws SlickException {
        super(gamename);
        INTERNALW = internalW;
        INTERNALH = internalH;
        WINDOWW = windowW;
        WINDOWH = windowH;
    }

    Image img;
    FBOGraphics fbo;
    
    @Override
    public void init(GameContainer gc) throws SlickException {
        img = new Image(INTERNALW, INTERNALH);
        fbo = new FBOGraphics(img);
        
        left = new Board(true);
        right = new Board(false);
        gc.getInput().initControllers();
        Controls.start(gc);
        
        ContentContainer.LoadAllContent();
        
        gc.setShowFPS(false);
    }

    boolean kPause;
    boolean kPauseLast;
    boolean isPausing;
    
    @Override
    public void update(GameContainer gc, int i) throws SlickException {
        Input theInput = gc.getInput();
        
        kPauseLast = kPause;
        kPause = theInput.isKeyDown(Input.KEY_P);
        if (kPause && !kPauseLast)
            isPausing = !isPausing;
        
        if (winner == null && !isPausing) {
            left.update(gc);
            right.update(gc);
        } else {
            if (theInput.isKeyDown(Input.KEY_ENTER))
                gc.exit();
        }
        SpecialEffects.update();
    }

    @Override
    public void render(GameContainer gc, Graphics g) throws SlickException {
        drawWithGraphics(fbo);
        g.drawImage(img, 0, 0, WINDOWW, WINDOWH, 0, 0, INTERNALW, INTERNALH);
    }

    private void drawWithGraphics(Graphics g) throws SlickException {
        g.drawImage(ContentContainer.getBoardGui(), 0f, 0f);
        left.draw(g, 30, 30);
        right.draw(g, INTERNALW - Board.WIDTH*32 - 30, 30);
        SpecialEffects.draw(g);
        
        if (winner != null) {
            String str = "PLAYER ";
            if (winner == left)
                str += "1";
            else
                str += "2";
            str += " IS THE WINNER!";
            
            Font f = g.getFont();
            int strW = f.getWidth(str);
            int centerOnX = MyGame.INTERNALW / 2;
            int centerOnY = MyGame.INTERNALH - 30;
            int finalX = centerOnX - strW / 2;
            int finalY = centerOnY;
            g.drawString(str, finalX, finalY);
        }
    }
}
