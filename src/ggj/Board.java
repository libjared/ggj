package ggj;

import ggj.gameLogic.GemFactory;
import ggj.gameLogic.Gem;
import ggj.gameLogic.GemType;
import ggj.summon.Summon;
import ggj.summon.SummonSet;
import java.util.ArrayList;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Board {

    public static final int WIDTH = 8;
    public static final int HEIGHT = 16;
    private final GemFactory gf;
    private final SummonSet summonSet;

    //none,R,G,B,Y,P is 0 to 5
    Gem[][] spaces;

    int fallingGemX = WIDTH / 2;
    float fallingGemY = -3;
    Gem[] fallingGems;

    ArrayList<Point2D> markedForDeath;
    
    /*
    * Going to start using these terms 
    *
    * Break - the destruction of singular gems
    *
    * Match - the destruction of multiple gems
    */
    
    final int BREAKSTOSUMMON = 20; //rename BREAKSTOSUMMON
    int gemBreaks = 0; //rename gemBreaks
    private GemType summonColor = null;

    public boolean PLAYERONE;// bad.

    private int hasteTimer = 0;
    public static final int HASTETIMERMAX = 7 * 60; //7s       //moving to purple

    public Board() throws SlickException {
        //PLAYERONE = playerOne;

        this.markedForDeath = new ArrayList<>();
        spaces = new Gem[HEIGHT][WIDTH];

        gf = new GemFactory();
        summonSet = new SummonSet();
        fallingGems = gf.generateFallingGems();
    }

    //could these booleans go somewhere else?
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

        checkForGameOver();

        updateSummon();
    }

    public GemFactory getGemFactory() {
        return gf;
    }

    public SummonSet getSummonSet() {
        return summonSet;
    }

    private void checkForGameOver() { 
        if (gemExistsAt(WIDTH / 2, 0) && !doesNeedGravity()) {
            MyGame.winner = getOtherBoard();
            ContentContainer.getGong().play();
        }
    }

    private void updateSummon() {
        if (gemBreaks >= BREAKSTOSUMMON && kSummon && !kSummonLast) {
            //do the summon
            applySummon();

            //then reset
            gemBreaks = 0;
            summonColor = null;
        }

        //if you want your summon meter reset
        if (gemBreaks > 0 && kDrain && !kDrainLast) {
            gemBreaks = 0;
            summonColor = null;
            ContentContainer.getVanish().play();
        }
    }

    private void applySummon() {
        //012345
        //_rgbyp

        ContentContainer.summonSoundFromColor(summonColor).play();

        Summon currSummon;

        switch (summonColor) {
            default:
                throw new ArrayIndexOutOfBoundsException();
            case RED:
                currSummon = summonSet.getRed();
                break;
            case GREEN:
                currSummon = summonSet.getGreen();
                break;
            case BLUE:
                currSummon = summonSet.getBlue();
                break;
            case YELLOW:
                currSummon = summonSet.getYellow();
                break;
            case PURPLE:
                currSummon = summonSet.getPurple();
                break;
        }

        currSummon.activateSummon(this, getOtherBoard());

        SpecialEffects.setSummon(summonColor);
    }

    public void remakeBoardWithHeight(int h) {
        for (int y = h; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                setSpace(y, x, new Gem(gf.randomGemColor()));
            }
        }
    }

    private Board getOtherBoard() {
        if (PLAYERONE) {
            return MyGame.right;
        }
        return MyGame.left;
    }

    //could gravity be it's own class?
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
            ArrayList<Gem> thisCol = columnToArrayList(x);
            if (columnNeedsGravity(thisCol)) {
                return true;
            }
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

        kDown = control.getDown();
        kLeft = control.getLeft();
        kRight = control.getRight();
        kShuf = control.getShuf();
        kSummon = control.getSummon();
        kDrain = control.getDrain();
    }

    final int SHUFFLEANIMMAX = 10;
    int shuffleAnim = 0;

    private void updateFallingGems() {
        shuffleAnim = Math.max(shuffleAnim - 1, 0);

        //shuffle
        if (kShuf && !kShufLast) {
            Gem[] newGems = new Gem[3];
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
        int realBottomY = (int) (fallingGemY + 3);
        if (kLeft && !kLeftLast && isValid(fallingGemX - 1, realBottomY)
                && !gemExistsAt(fallingGemX - 1, realBottomY)) {
            fallingGemX--;
        }
        if (kRight && !kRightLast && isValid(fallingGemX + 1, realBottomY)
                && !gemExistsAt(fallingGemX + 1, realBottomY)) {
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
        for (int dir = 0; dir < 4; dir++) {
            ArrayList<Point2D> matchPoints = new ArrayList<>();

            GemType matchColor = null;
            for (int i = 0; i < HEIGHT; i++) {
                int dx = withDirection(dir, i).getX();
                int dy = withDirection(dir, i).getY();
                int newX = x + dx;
                int newY = y + dy;
                if (!isValid(newX, newY)) {
                    break;
                }

                Gem something = getSpace(newY, newX);

                if (something == null) {
                    continue;
                }

                GemType colorHere = something.getColor();

                if (matchColor == null || matchColor == colorHere) {
                    matchColor = colorHere;
                    matchPoints.add(new Point2D(newX, newY));
                } else { //not the color we're looking for, and said color is already set
                    break;
                }
            }

            if (matchPoints.size() >= 3) {
                //break them
                for (int i = 0; i < matchPoints.size(); i++) {
                    Point2D gem = matchPoints.get(i);
                    destroyGem(gem.getX(), gem.getY());
                }
            }
        }
    }

    public void destroyGem(int x, int y) {
        //check if in bounds
        if (x < 0 || x >= WIDTH || y < 0 || y >= HEIGHT) {
            System.out.format("(x:%d, y:%d) is shit%n", x, y);
            return;
        }

        //check if empty
        if (getSpace(y, x) == null) {
            return;
        }

        //check if already marked
        for (Point2D ded : markedForDeath) {
            if (ded.getX() == x && ded.getY() == y) {
                return;
            }
        }

        markedForDeath.add(new Point2D(x, y));
    }

    /**
     * returns true when something fell
     */
    private boolean fallColumn(int x) {
        boolean didFall = false;

        ArrayList<Gem> newCol = columnToArrayList(x);

        boolean needGravity = columnNeedsGravity(newCol);

        if (!needGravity) {
            return false;
        }

        //delete the first 0 from the bottom
        for (int i = 0; i < newCol.size(); i++) {
            if (newCol.get(i) == null) {
                newCol.remove(i);
                didFall = true;
                break;
            }
        }

        //pad with 0s to get HEIGHT-1 elements
        while (newCol.size() != HEIGHT) {
            newCol.add(null);
        }

        //copy back to column
        for (int i = 0; i < HEIGHT; i++) {
            int destY = HEIGHT - i - 1;
            setSpace(destY, x, newCol.get(i));
        }

        return didFall;
    }

    private ArrayList<Gem> columnToArrayList(int x) {
        //convert column to arraylist
        ArrayList<Gem> newCol = new ArrayList<>();
        for (int y = HEIGHT - 1; y >= 0; y--) {
            newCol.add(getSpace(y, x));
        }
        return newCol;
    }

    private boolean columnNeedsGravity(ArrayList<Gem> newCol) {
        //check if the column is settled already
        boolean needGravity = false;
        boolean hitANothing = false;
        for (int i = 0; i < newCol.size(); i++) {
            if (newCol.get(i) == null) {
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
        if (markedForDeath.size() > 0) {
            ContentContainer.getCrash().play(1f + (float) Math.random() * 0.3f, 1f);
        }

        for (int i = 0; i < markedForDeath.size(); i++) {
            Point2D gem = markedForDeath.get(i);
            int theX = gem.getX();
            int theY = gem.getY();
            GemType theColor = getSpace(theY, theX).getColor();

            if (summonColor == null) //first hit, set summon color
            {
                summonColor = theColor;
            }

            SpecialEffects.addCrash(theX, theY, offsetx, offsety, theColor);

            setSpace(theY, theX, null);
        }
        gemBreaks += markedForDeath.size();
        markedForDeath.clear();
    }

    private Point2D withDirection(int dir, int n) {
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

        return new Point2D(retX, retY);
    }

    private boolean isValid(int x, int y) {
        return x >= 0 && x < WIDTH && y >= 0 && y < HEIGHT;
    }

    //is this global?
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

    //what if we added numbers on the bar?
    private void drawSummonBars(Graphics g) {
        Color pushCol = g.getColor();
        float pushLW = g.getLineWidth();

        g.setLineWidth(2f);

        //outline        
        g.setColor(Color.blue);
        g.drawRect(this.offsetx, HEIGHT * 32 + 48f, WIDTH * 32, 32);

        float colScalar = 1f;

        if (gemBreaks != 0) {
            float ratio = gemBreaks / (float) BREAKSTOSUMMON;
            if (ratio >= 1f) {
                ratio = 1f;
                wooshyWooshColorThing += 0.1f;
                colScalar = (float) (Math.cos(wooshyWooshColorThing) + 1) / 2;
            } else {
                wooshyWooshColorThing = 0f;
            }
            Color newCol = colorFromGemType(summonColor).scaleCopy(colScalar);
            newCol.a = 1f;
            g.setColor(newCol);
            g.fillRect(this.offsetx, HEIGHT * 32 + 48f, ratio * WIDTH * 32, 32);
        }

        g.setLineWidth(pushLW);
        g.setColor(pushCol);
    }

    //???
    private float wooshyWooshColorThing = 0f;

    private static Color colorFromGemType(GemType col) {
        switch (col) {
            default:
                return Color.white;
            case RED:
                return Color.red;
            case GREEN:
                return Color.green;
            case BLUE:
                return Color.cyan;
            case YELLOW:
                return Color.yellow;
            case PURPLE:
                return new Color(102, 51, 153); //purple
        }
    }

    private void drawBoardGems(Graphics g) {
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                Gem gem = getSpace(y, x);
                if (gem == null) {
                    continue;
                }
                drawGem(g, gem, x, y);
            }
        }
    }

    private void drawFallingGems(Graphics g) {
        if (shuffleAnim == 0) {
            drawGem(g, fallingGems[0], fallingGemX, fallingGemY);
            drawGem(g, fallingGems[1], fallingGemX, fallingGemY + 1);
            drawGem(g, fallingGems[2], fallingGemX, fallingGemY + 2);
        } else {
            float ratio = shuffleAnim / (float) SHUFFLEANIMMAX;
            int finalX = fallingGemX * 32 + offsetx;
            float finalY;

            //first (from third)
            Gem color1 = fallingGems[0];
            finalY = fallingGemY * 32 + offsety;
            float finalBottomY = (fallingGemY + 1 - ratio) * 32 + offsety;
            g.drawImage(imageCol(color1.getColor()),
                    finalX, finalY,
                    finalX + 32, finalBottomY,
                    0, 64 * ratio, 64, 64);

            //second (from first)
            Gem color2 = fallingGems[1];
            finalY = (fallingGemY + 1 - ratio) * 32 + offsety;
            finalBottomY = finalY + 32;
            g.drawImage(imageCol(color2.getColor()),
                    finalX, finalY,
                    finalX + 32, finalBottomY,
                    0, 0, 64, 64);

            //third (from second)
            Gem color3 = fallingGems[2];
            finalY = (fallingGemY + 2 - ratio) * 32 + offsety;
            finalBottomY = finalY + 32;
            g.drawImage(imageCol(color3.getColor()),
                    finalX, finalY,
                    finalX + 32, finalBottomY,
                    0, 0, 64, 64);

            //fourth! (from third)
            finalY = (fallingGemY + 3 - ratio) * 32 + offsety;
            finalBottomY = (fallingGemY + 3) * 32 + offsety;
            g.drawImage(imageCol(color1.getColor()),
                    finalX, finalY,
                    finalX + 32, finalBottomY,
                    0, 0, 64, ratio * 64);
        }
    }

    private void drawNextGems(Graphics g) {
        int nextX;
        int nextY = 55;
        if (PLAYERONE) {
            nextX = 310;
        } else {
            nextX = 458;
        }

        GemType[] nextThree = gf.peekNextThree();

        for (int i = 0; i < nextThree.length; i++) {
            g.drawImage(imageCol(nextThree[i]),
                    nextX, nextY + i * 32,
                    nextX + 32, nextY + (i + 1) * 32,
                    0, 0,
                    64, 64);
        }
    }

    private void drawGem(Graphics g, Gem color, int x, float y) {
        int finalX = x * 32 + offsetx;
        float finalY = y * 32 + offsety;
        g.drawImage(imageCol(color.getColor()), finalX, finalY, finalX + 32, finalY + 32, 0, 0, 64, 64);
    }

    private void collideFallingGems() {
        int hitY = (int) (fallingGemY + 3);
        int hitX = fallingGemX;
        boolean collideGem = !isValid(hitX, hitY) || gemExistsAt(hitX, hitY);
        boolean collideBottom = fallingGemY > HEIGHT;

        if (collideGem || collideBottom) {
            if (isValid(hitX, hitY - 1)) {
                setSpace(hitY - 1, fallingGemX, fallingGems[2]);
            }
            if (isValid(hitX, hitY - 2)) {
                setSpace(hitY - 2, fallingGemX, fallingGems[1]);
            }
            if (isValid(hitX, hitY - 3)) {
                setSpace(hitY - 3, fallingGemX, fallingGems[0]);
            }

            ContentContainer.getDink().play(1f + (float) Math.random() * 0.3f, 1f);

            gf.generateFallingGems();

            fallingGemX = WIDTH / 2;
            fallingGemY = -3;
        }
    }

    public boolean gemExistsAt(int x, int y) {
        return getSpace(y, x) != null;
    }

    private Image imageCol(GemType col) {
        return ContentContainer.imageFromColor(col);
    }

    public Gem getSpace(int y, int x) {
        return spaces[y][x];
    }

    public void setSpace(int y, int x, Gem col) {
        spaces[y][x] = col;
    }

    public void setHasteTimer(int newTimer) {
        this.hasteTimer = newTimer;
    }

    IController control;

    public void setController(IController controller) {
        this.control = controller;
    }
}
