package ggj;

import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

public class MyGame extends BasicGame {

    Board left;
    Board right;
    
    int WINDOWW;
    int WINDOWH;

    public MyGame(String gamename, int windowW, int windowH) throws SlickException {
        super(gamename);
        WINDOWW = windowW;
        WINDOWH = windowH;
        
        left = new Board(0);
        right = new Board(WINDOWW - Board.WIDTH*32);
    }

    @Override
    public void init(GameContainer gc) throws SlickException {
    }

    @Override
    public void update(GameContainer gc, int i) throws SlickException {
        left.update(gc);
        right.update(gc);
    }

    @Override
    public void render(GameContainer gc, Graphics g) throws SlickException {
        Input inp = gc.getInput();
        if (inp.isKeyDown(Input.KEY_DOWN))
        {
            g.drawString("Hello!", 50, 50);
        }
        left.draw(g);
        right.draw(g);
    }
}
