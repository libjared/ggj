package ggj;

import java.util.ArrayList;
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
    Random rng = new Random();

    //none,R,G,B,Y,P is 0 to 5
    int[][] spaces;

    int fallingGemX = WIDTH / 2;
    float fallingGemY = -3;
    int[] fallingGems;

    ArrayList<int[]> markedForDeath;
    
    final int KILLSTOSUMMON = 6;
    int kills = 0;
    int summonColor = 0;
    
    public boolean PLAYERONE;
    
    int hasteTimer = 0;
    final int HASTETIMERMAX = 7*60; //7s

    public Board(boolean playerOne) throws SlickException {
        PLAYERONE = playerOne;
        
        this.markedForDeath = new ArrayList<>();
        spaces = new int[HEIGHT][WIDTH];
        rngBuf = new ArrayList<>();
        ensureBuffer(100);

        testBoard();
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
    private boolean kSummonLast;
    private boolean kDrainLast;
    
    private boolean kDown;
    private boolean kLeft;
    private boolean kRight;
    private boolean kShuf;
    private boolean kSummon;
    private boolean kDrain;
    
    public void update(GameContainer gc) {
        updateInputs(gc);

        updateFallingGems();

        findMatches();

        processMarkedForDeath();
        
        updateGravity();
        
        checkForDeath();
        
        updateSummon();
    }
    
    private void checkForDeath() {
        if (gemExistsAt(WIDTH/2, 0) && !doesNeedGravity()) {
            MyGame.winner = getOtherBoard();
            ContentContainer.getGong().play();
        }
    }

    private void updateSummon() {
        if (kills >= KILLSTOSUMMON && kSummon && !kSummonLast) {
            //do the summon
            applySummon();
            
            //then reset
            kills = 0;
            summonColor = 0;
        }
        
        //if you want your summon meter reset
        if (kills > 0 && kDrain && !kDrainLast) {
            kills = 0;
            summonColor = 0;
            ContentContainer.getVanish().play();
        }
    }
    
    private void applySummon() {
        //012345
        //_rgbyp
        
        ContentContainer.summonSoundFromColor(summonColor).play();
        
        switch (summonColor) {
            case 0:
            default:
                throw new ArrayIndexOutOfBoundsException();
            case 1:
                getOtherBoard().doRedMagic();
                break;
            case 2:
                doGreenMagic();
                break;
            case 3:
                doBlueMagic();
                break;
            case 4:
                doYellowMagic();
                break;
            case 5:
                getOtherBoard().doPurpleMagic();
                break;
        }
        
        SpecialEffects.setSummon(summonColor);
    }

    private void doYellowMagic() {
        int averageHeight = (findHeight() + getOtherBoard().findHeight()) / 2;
        remakeBoardWithHeight(averageHeight);
        getOtherBoard().remakeBoardWithHeight(averageHeight);
    }
    
    private void remakeBoardWithHeight(int h) {
        for (int y = h; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                setSpace(y, x, randomColor());
            }
        }
    }
    
    private int findHeight() {
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                if (gemExistsAt(x, y)) {
                    return y;
                }
            }
        }
        return HEIGHT;
    }
    
    private void doPurpleMagic() {
        hasteTimer = HASTETIMERMAX;
    }
    
    private void doBlueMagic() {
        //shift all to right
        //int[][] newSpaces = cloneSpaces();
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = WIDTH - 1; x >= 0; x--) {
                int colorThere;
                if (isValid(x - 1, y)) {
                    colorThere = getSpace(y, x - 1);
                } else {
                    colorThere = 0;
                }
                setSpace(y, x, colorThere);
            }
        }
    }
    
    private void doGreenMagic() {
        //color snipe
        int biggestColor = getBiggestColor();
        
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                int colorHere = getSpace(y, x);
                if (colorHere == biggestColor) {
                    markedForDeath.add(new int[] { x, y });
                }
            }
        }
    }
    
    private void doRedMagic() {
        //spawn junk on the top row
        for (int x = 0; x < WIDTH; x++) {
            setSpace(0, x, randomColor());
        }
    }

    private int getBiggestColor() {
        int biggestColor;
        int[] colorCounts = new int[6]; //dont use index [0]
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                int colorHere = getSpace(y, x);
                colorCounts[colorHere]++;
            }
        }
        biggestColor = -1;
        int biggestColorCount = 0;
        for (int i = 1; i < colorCounts.length; i++) {
            int countHere = colorCounts[i];
            if (countHere > biggestColorCount) {
                biggestColorCount = countHere;
                biggestColor = i;
            }
        }
        return biggestColor;
    }
    
    private Board getOtherBoard() {
        if (PLAYERONE)
            return MyGame.right;
        return MyGame.left;
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
        kSummonLast = kSummon;
        kDrainLast = kDrain;
        
        Input inp = gc.getInput();
        if (PLAYERONE) {
            kDown = inp.isKeyDown(Input.KEY_DOWN);
            kLeft = inp.isKeyDown(Input.KEY_LEFT);
            kRight = inp.isKeyDown(Input.KEY_RIGHT);
            kShuf = inp.isKeyDown(Input.KEY_Z);
            kSummon = inp.isKeyDown(Input.KEY_A);
            kDrain = inp.isKeyDown(Input.KEY_S);
        } else {
            kDown = inp.isControllerDown(0);
            kLeft = inp.isControllerLeft(0);
            kRight = inp.isControllerRight(0);
            kShuf = inp.isButtonPressed(18, 0);
            kSummon = inp.isButtonPressed(17, 0);
            kDrain = inp.isButtonPressed(16, 0);
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
            collideFallingGems();
        }
    }

    private void updateFallingGemsPos() {
        //update y
        float fallSpd = 0f;
        if (kDown || (--hasteTimer) > 0) {
            fallSpd = 10f / 60f;
        } else if (!MyGame.disableAutoFall) {
            fallSpd = 5f / 60f;
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

                int colorHere = getSpace(newY, newX);

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
        //check if already marked
        for (int[] ded : markedForDeath) {
            if (ded[0] == x && ded[1] == y)
                return;
        }
        
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
            setSpace(destY,x, newCol.get(i));
        }
        
        return didFall;
    }

    private ArrayList<Integer> columnToArrayList(int x) {
        //convert column to arraylist
        ArrayList<Integer> newCol = new ArrayList<>();
        for (int y = HEIGHT - 1; y >= 0; y--) {
            newCol.add(getSpace(y,x));
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
        if (markedForDeath.size() > 0)
            ContentContainer.getCrash().play(1f + (float)Math.random() * 0.3f, 1f);
        
        for (int i = 0; i < markedForDeath.size(); i++) {
            int[] gem = markedForDeath.get(i);
            int theX = gem[0];
            int theY = gem[1];
            int theColor = getSpace(theY,theX);
            
            if (summonColor == 0) //first hit, set summon color
                summonColor = theColor;
            
            SpecialEffects.addCrash(theX, theY, offsetx, offsety, theColor);
            
            setSpace(theY, theX, 0);
        }
        kills += markedForDeath.size();
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

    int offsetx = 0;
    int offsety = 0;
    
    public void draw(Graphics g, int offsetx, int offsety) throws SlickException {
        this.offsetx = offsetx;
        this.offsety = offsety;
        
        drawBoardGems(g);

        drawFallingGems(g);
        
        drawNextGems(g);
        
        drawSummonBars(g);
    }

    private void drawSummonBars(Graphics g) {
        Color pushCol = g.getColor();
        float pushLW = g.getLineWidth();
        
        g.setLineWidth(2f);
        
        //outline        
        g.setColor(Color.blue);
        g.drawRect(this.offsetx, HEIGHT*32 + 48f, WIDTH*32, 32);
        
        float colScalar = 1f;
        
        if (kills != 0) {
            float ratio = kills / (float)KILLSTOSUMMON;
            if (ratio >= 1f) {
                ratio = 1f;
                wooshyWooshColorThing += 0.1f;
                colScalar = (float) (Math.cos(wooshyWooshColorThing)+1)/2;
            } else {
                wooshyWooshColorThing = 0f;
            }
            Color newCol = colorFromInt(summonColor).scaleCopy(colScalar);
            newCol.a = 1f;
            g.setColor(newCol);
            g.fillRect(this.offsetx, HEIGHT*32 + 48f, ratio*WIDTH*32, 32);
        }
        
        g.setLineWidth(pushLW);
        g.setColor(pushCol);
    }
    
    private float wooshyWooshColorThing = 0f;

    private static Color colorFromInt(int i) {
        switch (i) {
            case 0:
            default:
                return Color.white;
            case 1:
                return Color.red;
            case 2:
                return Color.green;
            case 3:
                return Color.cyan;
            case 4:
                return Color.yellow;
            case 5:
                return new Color(102, 51, 153); //purple
        }
    }

    private void drawBoardGems(Graphics g) {
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                int color = getSpace(y, x);
                if (color == 0) {
                    continue;
                }
                drawGem(g, color, x, y);
            }
        }
    }

    private void drawFallingGems(Graphics g) {
        if (shuffleAnim == 0) {
            drawGem(g, fallingGems[0], fallingGemX, fallingGemY);
            drawGem(g, fallingGems[1], fallingGemX, fallingGemY + 1);
            drawGem(g, fallingGems[2], fallingGemX, fallingGemY + 2);
        } else {
            float ratio = shuffleAnim / (float)SHUFFLEANIMMAX;
            int finalX = fallingGemX * 32 + offsetx;
            float finalY;
            
            //first (from third)
            int color1 = fallingGems[0];
            finalY = fallingGemY * 32 + offsety;
            float finalBottomY = (fallingGemY + 1 - ratio) * 32 + offsety;
            g.drawImage(imageCol(color1),
                    finalX, finalY,
                    finalX + 32, finalBottomY,
                    0, 64*ratio, 64, 64);
            
            //second (from first)
            int color2 = fallingGems[1];
            finalY = (fallingGemY + 1 - ratio) * 32 + offsety;
            finalBottomY = finalY + 32;
            g.drawImage(imageCol(color2),
                    finalX, finalY,
                    finalX + 32, finalBottomY,
                    0, 0, 64, 64);
            
            //third (from second)
            int color3 = fallingGems[2];
            finalY = (fallingGemY + 2 - ratio) * 32 + offsety;
            finalBottomY = finalY + 32;
            g.drawImage(imageCol(color3),
                    finalX, finalY,
                    finalX + 32, finalBottomY,
                    0, 0, 64, 64);
            
            //fourth! (from third)
            finalY = (fallingGemY + 3 - ratio) * 32 + offsety;
            finalBottomY = (fallingGemY + 3) * 32 + offsety;
            g.drawImage(imageCol(color1),
                    finalX, finalY,
                    finalX + 32, finalBottomY,
                    0, 0, 64, ratio*64);
        }
    }
    
    private void drawNextGems(Graphics g){
        int nextX;
        int nextY = 55;
        if (PLAYERONE) {
            nextX = 310;
        } else {
            nextX = 458;
        }
        
        int[] nextThree = peekNextThree();
        
        for (int i = 0; i < nextThree.length; i++) {
            g.drawImage(imageCol(nextThree[i]),
                    nextX, nextY + i * 32,
                    nextX + 32, nextY + (i + 1) * 32,
                    0, 0,
                    64, 64);
        }
    }

    private void drawGem(Graphics g, int color, int x, float y) {
        int finalX = x * 32 + offsetx;
        float finalY = y * 32 + offsety;
        g.drawImage(imageCol(color), finalX, finalY, finalX + 32, finalY + 32, 0, 0, 64, 64);
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
            if (isValid(hitX, hitY - 1))
                setSpace(hitY - 1,fallingGemX,fallingGems[2]);
            if (isValid(hitX, hitY - 2))
                setSpace(hitY - 2,fallingGemX,fallingGems[1]);
            if (isValid(hitX, hitY - 3))
                setSpace(hitY - 3,fallingGemX,fallingGems[0]);
            
            ContentContainer.getDink().play(1f + (float)Math.random() * 0.3f, 1f);

            generateFallingGems();

            fallingGemX = WIDTH / 2;
            fallingGemY = -3;
        }
    }

    private boolean gemExistsAt(int x, int y) {
        return getSpace(y, x) != 0;
    }

    private Image imageCol(int col) {
        return ContentContainer.imageFromColor(col);
    }
    
    ArrayList<Integer> rngBuf;
    private int randomColor() {
        int newRand = useRngToMakeANewColor();
        rngBuf.add(newRand);
        return rngBuf.remove(0);
    }

    private int useRngToMakeANewColor() {
        return rng.nextInt(5) + 1;
    }
    
    private void ensureBuffer(int n) {
        while (rngBuf.size() < n) {
            rngBuf.add(useRngToMakeANewColor());
        }
    }
    
    public int[] peekNextThree() {
        return new int[] { rngBuf.get(0), rngBuf.get(1), rngBuf.get(2) };
    }
    
    int getSpace(int y, int x) {
        return spaces[y][x];
    }
    
    void setSpace(int y, int x, int col) {
        spaces[y][x] = col;
    }
}
