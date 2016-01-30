package ggj;

import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

public class MyGame extends BasicGame {

    Board left;
    Board right;

    public MyGame(String gamename) throws SlickException {
        super(gamename);
        left = new Board();
        right = new Board();
    }

    @Override
    public void init(GameContainer gc) throws SlickException {
    }

    @Override
    public void update(GameContainer gc, int i) throws SlickException {

    }

    @Override
    public void render(GameContainer gc, Graphics g) throws SlickException {
        g.drawString("Hello!", 50, 50);
        left.draw(g, 0);
        right.draw(g, Board.WIDTH*32 + 20);
    }
}
