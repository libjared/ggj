package ggj;

import java.util.ArrayList;
import java.util.Random;
import org.newdawn.slick.ControllerListener;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

public class Board {

    public static final int WIDTH = 8;
    public static final int HEIGHT = 16;
    public final int OFFSETX;
    Random rng = new Random();

    //none,R,G,B,Y,P is 0 to 5
    int[][] spaces;

    Image[] gemsGfx;

    int fallingGemX = WIDTH / 2;
    float fallingGemY = -3;
    int[] fallingGems;

    ArrayList<int[]> markedForDeath;
    
    boolean PLAYERONE;

    public Board(int offsetx, boolean playerOne) throws SlickException {
        OFFSETX = offsetx;
        PLAYERONE = playerOne;
        
        this.markedForDeath = new ArrayList<>();
        spaces = new int[HEIGHT][WIDTH];

        testBoard();
        loadGfx();
        generateFallingGems();
    }

    private void testBoard() {
        //test board
//        for (int x = 0; x < WIDTH; x++) {
//            int col = randomColor();
//            for (int y = 6; y < HEIGHT; y++) {
//                spaces[y][x] = col;
//            }
//        }
    }

    private boolean kLeftLast;
    private boolean kRightLast;
    private boolean kShufLast;
    
    private boolean kDown;
    private boolean kLeft;
    private boolean kRight;
    private boolean kShuf;
    
    public void update(GameContainer gc) {
        updateInputs(gc);

        updateFallingGems();

        findMatches();

        processMarkedForDeath();
        
        updateGravity();
    }

    private void updateGravity() {
        if (doesNeedGravity()) {
            gravTimer++;
        } else {
            gravTimer = 0;
        }
        
        if (gravTimer >= GRAVTIMERMAX) {
            gravTimer = 0;
            applyGravity();
        }
    }

    private void applyGravity() {
        for (int x = 0; x < WIDTH; x++) {
            fallColumn(x);
        }
    }
    
    final int GRAVTIMERMAX = 15;
    int gravTimer = 0;
    
    private boolean doesNeedGravity() {
        for (int x = 0; x < WIDTH; x++) {
            ArrayList<Integer> thisCol = columnToArrayList(x);
            if (columnNeedsGravity(thisCol))
                return true;
        }
        return false;
    }

    private void updateInputs(GameContainer gc) {
        //last
        kLeftLast = kLeft;
        kRightLast = kRight;
        kShufLast = kShuf;
        
        Input inp = gc.getInput();
        if (PLAYERONE) {
            kDown = inp.isKeyDown(Input.KEY_DOWN);
            kLeft = inp.isKeyDown(Input.KEY_LEFT);
            kRight = inp.isKeyDown(Input.KEY_RIGHT);
            kShuf = inp.isKeyDown(Input.KEY_Z);
        } else {
            kDown = inp.isControllerDown(0);
            kLeft = inp.isControllerLeft(0);
            kRight = inp.isControllerRight(0);
            kShuf = inp.isButtonPressed(18, 0);
        }
    }

    final int SHUFFLEANIMMAX = 10;
    int shuffleAnim = 0;
    
    private void updateFallingGems() {
        shuffleAnim = Math.max(shuffleAnim - 1, 0);
        
        //shuffle
        if (kShuf && !kShufLast) {
            int[] newGems = new int[3];
            newGems[0] = fallingGems[2];
            newGems[1] = fallingGems[0];
            newGems[2] = fallingGems[1];
            fallingGems = newGems;
            shuffleAnim = SHUFFLEANIMMAX;
        }
        
        if (!doesNeedGravity()) {
            updateFallingGemsPos();
        }
        
        collideFallingGems();
    }

    private void updateFallingGemsPos() {
        //update y
        float fallSpd = 0f;
        if (kDown) {
            fallSpd = 10f / 60f;
        } else {
            fallSpd = 2f / 60f;
        }
        fallingGemY += fallSpd;
        
        //update x
        int realBottomY = (int)(fallingGemY+3);
        if (kLeft && !kLeftLast && isValid(fallingGemX-1, realBottomY)
                && !gemExistsAt(fallingGemX-1, realBottomY)) {
            fallingGemX--;
        }
        if (kRight && !kRightLast && isValid(fallingGemX+1, realBottomY)
                && !gemExistsAt(fallingGemX+1, realBottomY)) {
            fallingGemX++;
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

    /**
     * returns true when something fell
     */
    private boolean fallColumn(int x) {
        boolean didFall = false;
        
        ArrayList<Integer> newCol = columnToArrayList(x);
        
        boolean needGravity = columnNeedsGravity(newCol);
        
        if (!needGravity) return false;
        
        //delete the first 0 from the bottom
        for (int i = 0; i < newCol.size(); i++) {
            if (newCol.get(i) == 0) {
                newCol.remove(i);
                didFall = true;
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
        
        return didFall;
    }

    private ArrayList<Integer> columnToArrayList(int x) {
        //convert column to arraylist
        ArrayList<Integer> newCol = new ArrayList<>();
        for (int y = HEIGHT - 1; y >= 0; y--) {
            newCol.add(spaces[y][x]);
        }
        return newCol;
    }

    private boolean columnNeedsGravity(ArrayList<Integer> newCol) {
        //check if the column is settled already
        boolean needGravity = false;
        boolean hitANothing = false;
        for (int i = 0; i < newCol.size(); i++) {
            if (newCol.get(i) == 0) {
                hitANothing = true;
            } else if (hitANothing) {
                //we've hit a space before, and the current gem exists above it
                //so we have to apply gravity in this column
                needGravity = true;
            }
        }
        return needGravity;
    }
    
    private void processMarkedForDeath() {
        for (int i = 0; i < markedForDeath.size(); i++) {
            int[] gem = markedForDeath.get(i);
            spaces[gem[1]][gem[0]] = 0;
        }
        markedForDeath.clear();
    }

    private int[] withDirection(int dir, int n) {
        //dir in range [0, 8). 0 is right, 1 is downright.
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
        
        g.drawString("needsGravity: " + doesNeedGravity(), OFFSETX + 64, 64);
    }

    private void drawFallingGems(Graphics g) {
        if (shuffleAnim == 0) {
            drawGem(g, fallingGems[0], fallingGemX, fallingGemY);
            drawGem(g, fallingGems[1], fallingGemX, fallingGemY + 1);
            drawGem(g, fallingGems[2], fallingGemX, fallingGemY + 2);
        } else {
            float ratio = shuffleAnim / (float)SHUFFLEANIMMAX;
            int finalX = fallingGemX * 32 + OFFSETX;
            float finalY;
            
            //first (from third)
            int color1 = fallingGems[0];
            finalY = fallingGemY * 32;
            float finalBottomY = (fallingGemY + 1 - ratio) * 32;
            g.drawImage(gemsGfx[color1],
                    finalX, finalY, finalX + 32, finalBottomY,
                    0, 64*ratio, 64, 64);
            
            //second (from first)
            int color2 = fallingGems[1];
            finalY = (fallingGemY + 1 - ratio) * 32;
            g.drawImage(gemsGfx[color2], finalX, finalY, finalX + 32, finalY + 32, 0, 0, 64, 64);
            
            //third (from second)
            int color3 = fallingGems[2];
            finalY = (fallingGemY + 2 - ratio) * 32;
            g.drawImage(gemsGfx[color3], finalX, finalY, finalX + 32, finalY + 32, 0, 0, 64, 64);
            
            //fourth! (from third)
            finalY = (fallingGemY + 3 - ratio) * 32;
            finalBottomY = (fallingGemY + 3 - ratio) * 32;
            g.drawImage(gemsGfx[color1], finalX, finalY, finalX + 32, (fallingGemY + 3) * 32,
                    0, 0, 64, ratio*64);
        }
    }

    private void drawGem(Graphics g, int color, int x, float y) {
        int finalX = x * 32 + OFFSETX;
        float finalY = y * 32;
        g.drawImage(gemsGfx[color], finalX, finalY, finalX + 32, finalY + 32, 0, 0, 64, 64);
    }

    private void generateFallingGems() {
        fallingGems = new int[]{randomColor(), randomColor(), randomColor()};
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
            fallingGemY = -3;
        }
    }

    private boolean gemExistsAt(int x, int y) {
        return spaces[y][x] != 0;
    }

    private int randomColor() {
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
