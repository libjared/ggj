package ggj;

import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

public class MyGame extends BasicGame {

    static Board left;
    static Board right;
    static boolean disableSecondPlayer = true;
    
    int WINDOWW;
    int WINDOWH;

    public MyGame(String gamename, int windowW, int windowH) throws SlickException {
        super(gamename);
        WINDOWW = windowW;
        WINDOWH = windowH;
    }

    @Override
    public void init(GameContainer gc) throws SlickException {
        left = new Board(true);
        right = new Board(false);
        gc.getInput().initControllers();
        
        ContentContainer.LoadAllContent();
    }

    @Override
    public void update(GameContainer gc, int i) throws SlickException {
        left.update(gc);
        right.update(gc);
        SpecialEffects.update();
    }

    @Override
    public void render(GameContainer gc, Graphics g) throws SlickException {
        g.drawImage(ContentContainer.getBoardGui(), 0f, 0f);
        left.draw(g, 30, 30);
        right.draw(g, WINDOWW - Board.WIDTH*32 - 30, 30);
        SpecialEffects.draw(g);
    }
}
