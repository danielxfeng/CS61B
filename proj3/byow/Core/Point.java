package byow.Core;

import java.io.Serializable;
import java.util.Random;

/** This class represent a x,y coordinate system. */
public class Point implements Serializable {
    private int x;
    private int y;

    /** Create an instance by given X and Y. */
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /** Create an instance by random x and y.
     *
     * @param rand the random.
     * @param xBound the bound of x.
     * @param yBound the bound of y.
     */
    public Point(Random rand, int xBound, int yBound) {
        this.x = rand.nextInt(xBound);
        this.y = rand.nextInt(yBound);
    }

    /** return the X */
    public int getX() {
        return this.x;
    }

    /** return the Y */
    public int getY() {
        return this.y;
    }

    public Point getShiftPoint(int dx, int dy) {
        return new Point(this.x + dx, this.y + dy);
    }
}
