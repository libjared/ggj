package ggj;

import java.util.ArrayList;
import java.util.Random;
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

    int fallingGemX = WIDTH / 2;
    float fallingGemY = 0;
    int[] fallingGems;

    ArrayList<int[]> markedForDeath;

    public Board(int offsetx) throws SlickException {
        this.markedForDeath = new ArrayList<>();
        OFFSETX = offsetx;
        spaces = new int[HEIGHT][WIDTH];

        testBoard();
        loadGfx();
        generateFallingGems();
    }

    private void testBoard() {
        //test board
        for (int x = 0; x < WIDTH; x++) {
            int col = randomColor();
            for (int y = 6; y < HEIGHT; y++) {
                spaces[y][x] = col;
            }
        }
    }

    private boolean kLeftLast;
    private boolean kRightLast;
    private boolean kShufLast;
    private boolean kDown;
    private boolean kLeft;
    private boolean kRight;
    private boolean kShuf;
    
    public void update(GameContainer gc) {
        Input inp = gc.getInput();
        kDown = inp.isKeyDown(Input.KEY_DOWN);
        kLeft = inp.isKeyDown(Input.KEY_LEFT);
        kRight = inp.isKeyDown(Input.KEY_RIGHT);
        kShuf = inp.isKeyDown(Input.KEY_Z);

        updateFallingGems();

        findMatches();

        processMarkedForDeath();

        if (gravityTimer()) {
            applyGravity();
        }

        //last
        kLeftLast = kLeft;
        kRightLast = kRight;
        kShufLast = kShuf;
    }
    
    final int GRAVTIMERMAX = 30;
    int gravTimer = GRAVTIMERMAX;
    private boolean gravityTimer() {
        gravTimer--;
        if (gravTimer <= 0) {
            gravTimer = GRAVTIMERMAX;
            return true;
        } else {
            return false;
        }
    }

    private void findMatches() {
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                if (gemExistsAt(x, y)) {
                    checkAnyMatchesHere(x, y);
                }
            }
        }
    }

    private void checkAnyMatchesHere(int x, int y) {
        for (int dir = 0; dir < 8; dir++) {
            ArrayList<int[]> matchPoints = new ArrayList<>();

            int matchColor = -1;
            for (int i = 0; i < HEIGHT; i++) {
                int newX = withDirection(dir, i)[0] + x;
                int newY = withDirection(dir, i)[1] + y;
                if (!isValid(newX, newY)) {
                    break;
                }

                int colorHere = spaces[newY][newX];

                if (matchColor == -1 || matchColor == colorHere) {
                    matchColor = colorHere;
                    matchPoints.add(new int[]{newX, newY});
                } else { //not the color we're looking for, and said color is already set
                    break;
                }
            }

            if (matchPoints.size() >= 3) {
                //break them
                for (int i = 0; i < matchPoints.size(); i++) {
                    int[] gem = matchPoints.get(i);
                    destroyGem(gem[0], gem[1]);
                }
            }
        }
    }

    private void destroyGem(int x, int y) {
        markedForDeath.add(new int[]{x, y});
    }

    private void applyGravity() {
        for (int x = 0; x < WIDTH; x++) {
            fallColumn(x);
        }
    }

    private void fallColumn(int x) {
        //convert column to arraylist
        ArrayList<Integer> newCol = new ArrayList<>();
        for (int y = HEIGHT - 1; y >= 0; y--) {
            newCol.add(spaces[y][x]);
        }
        
        //delete the first 0 from the bottom
        for (int i = 0; i < newCol.size(); i++) {
            if (newCol.get(i) == 0) {
                newCol.remove(i);
                break;
            }
        }
        
        //pad with 0s to get HEIGHT-1 elements
        while (newCol.size() != HEIGHT) {
            newCol.add(0);
        }
        
        //copy back to column
        for (int i = 0; i < HEIGHT; i++) {
            int destY = HEIGHT - i - 1;
            spaces[destY][x] = newCol.get(i);
        }
    }
    
    private void processMarkedForDeath() {
        for (int i = 0; i < markedForDeath.size(); i++) {
            int[] gem = markedForDeath.get(i);
            spaces[gem[1]][gem[0]] = 0;
        }
        markedForDeath.clear();
    }

    /**
     * 567 4 0 321
     *
     * @param dir in range [0, 8). 0 is right, 1 is downright.
     * @return new int[] {x,y}
     */
    private int[] withDirection(int dir, int n) {
        int retX = 0;
        int retY = 0;

        if (dir == 0 || dir == 1 || dir == 7) {
            retX = n;
        }
        if (dir == 3 || dir == 4 || dir == 5) {
            retX = -n;
        }
        if (dir == 1 || dir == 2 || dir == 3) {
            retY = n;
        }
        if (dir == 5 || dir == 6 || dir == 7) {
            retY = -n;
        }

        return new int[]{retX, retY};
    }

    private boolean isValid(int x, int y) {
        return x >= 0 && x < WIDTH && y >= 0 && y < HEIGHT;
    }

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
        fallingGems = new int[]{randomColor(), randomColor(), randomColor()};
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
        if (kLeft && !kLeftLast) {
            fallingGemX--;
        }
        if (kRight && !kRightLast) {
            fallingGemX++;
        }

        fallingGemX = Math.min(Math.max(fallingGemX, 0), WIDTH - 1);

        //collision
        collideFallingGems();
    }

    private void collideFallingGems() {
        int hitY = (int) (fallingGemY + 3);
        int hitX = fallingGemX;
        boolean collideGem = !isValid(hitX, hitY) || gemExistsAt(hitX, hitY);
        boolean collideBottom = fallingGemY > HEIGHT;

        if (collideGem || collideBottom) {
            spaces[hitY - 1][fallingGemX] = fallingGems[2];
            spaces[hitY - 2][fallingGemX] = fallingGems[1];
            spaces[hitY - 3][fallingGemX] = fallingGems[0];

            generateFallingGems();

            fallingGemX = WIDTH / 2;
            fallingGemY = 0;
        }
    }

    private boolean gemExistsAt(int x, int y) {
        return spaces[y][x] != 0;
    }

    private void drawFallingGems(Graphics g) {
        drawGem(g, fallingGems[0], fallingGemX, fallingGemY);
        drawGem(g, fallingGems[1], fallingGemX, fallingGemY + 1);
        drawGem(g, fallingGems[2], fallingGemX, fallingGemY + 2);
    }

    private void drawGem(Graphics g, int color, int x, float y) {
        int finalX = x * 32 + OFFSETX;
        float finalY = y * 32;
        //g.setColor(intToColor(color));
        //g.setLineWidth(2);
        //g.drawOval(finalX, finalY, 32, 32);
        g.drawImage(gemsGfx[color], finalX, finalY, finalX + 32, finalY + 32, 0, 0, 64, 64);
    }

    private int randomColor() {
        Random rng = new Random();
        return rng.nextInt(5) + 1;
    }

    private void loadGfx() throws SlickException {
        gemsGfx = new Image[6];
        gemsGfx[0] = null; //none
        gemsGfx[1] = new Image("content/red.png");
        gemsGfx[2] = new Image("content/green.png");
        gemsGfx[3] = new Image("content/blue.png");
        gemsGfx[4] = new Image("content/yellow.png");
        gemsGfx[5] = new Image("content/purple.png");
    }

}
