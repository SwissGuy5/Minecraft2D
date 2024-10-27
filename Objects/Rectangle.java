package Objects;

/**
 * A class representing a rectangle with four points. Rectangles are used to
 * form polygons for shading in shadows.
 * The points are stored in a 2D array, where the first index is the point number 
 * and the second index is the x or y coordinate.
 * The points are stored in the following order:
 * 0: Top left
 * 1: Top right
 * 2: Bottom left
 * 3: Bottom right
 */
public class Rectangle {
    public int[][] points = new int[4][2];

    /**
     * Constructor for a rectangle with four points.
     * @param edges An array of 8 ints representing the x and y coords of the points.
     */
    public Rectangle(int[] edges) {
        for (int i = 0; i < 4; i++) {
            points[i][0] = edges[i * 2];
            points[i][1] = edges[i * 2 + 1];
        }
    }

    /**
     * Optimization method used to calculate if a point is in reach of light.
     * @param x The x coordinate of the light source.
     * @param y The y coordinate of the light source.
     * @param r The radius of the light source.
     */
    public boolean closestPointInReach(int x, int y, int r) {
        if (r == 0) {
            return true;
        }

        boolean isToLeft = x < points[0][0];
        boolean isToRight = x > points[1][0];
        boolean isAbove = y > points[0][1];
        boolean isBelow = y < points[2][1];

        int rSquared = r * r;
        int distance;

        if (isAbove && isToLeft) {
            distance = (x - points[0][0]) * (x - points[0][0])
                + (y - points[0][1]) * (y - points[0][1]);
            return distance < rSquared;
        } else if (isAbove && isToRight) {
            distance = (x - points[1][0]) * (x - points[1][0]) 
                + (y - points[1][1]) * (y - points[1][1]);
            return distance < rSquared;
        } else if (isBelow && isToLeft) {
            distance = (x - points[2][0]) * (x - points[2][0]) 
                + (y - points[2][1]) * (y - points[2][1]);
            return distance < rSquared;
        } else if (isBelow && isToRight) {
            distance = (x - points[3][0]) * (x - points[3][0]) 
                + (y - points[3][1]) * (y - points[3][1]);
            return distance < rSquared;
        } else if (isAbove) {
            return y - points[0][1] < r;
        } else if (isBelow) {
            return points[3][1] - y < r;
        } else if (isToLeft) {
            return points[0][0] - x < r;
        } else if (isToRight) {
            return x - points[3][0] < r;
        } else {
            return false;
        }
    }
    
    /**
     * Method used to calculate the points reachable from a light source.
     * These points change depending on the relative position of the light source.
     * @param x The x coordinate of the light source.
     * @param y The y coordinate of the light source.
     */
    public int[] getPointsReachableFrom(int x, int y) {
        boolean isToLeft = x <= points[0][0];
        boolean isToRight = x >= points[1][0];
        boolean isAbove = y <= points[0][1];
        boolean isBelow = y >= points[2][1];

        if (isAbove && isToLeft) {
            return new int[]{points[3][0], points[3][1], 
                points[1][0], points[1][1], points[0][0], points[0][1]};
        } else if (isAbove && isToRight) {
            return new int[]{points[2][0], points[2][1], 
                points[0][0], points[0][1], points[1][0], points[1][1]};
        } else if (isBelow && isToLeft) {
            return new int[]{points[2][0], points[2][1], 
                points[3][0], points[3][1], points[1][0], points[1][1]};
        } else if (isBelow && isToRight) {
            return new int[]{points[0][0], points[0][1], 
                points[2][0], points[2][1], points[3][0], points[3][1]};
        } else if (isAbove) {
            return new int[]{points[0][0], points[0][1], 
                points[1][0], points[1][1], points[2][0], points[2][1], points[3][0], points[3][1]};
        } else if (isBelow) {
            return new int[]{points[0][0], points[0][1], 
                points[1][0], points[1][1], points[2][0], points[2][1], points[3][0], points[3][1]};
        } else if (isToLeft) {
            return new int[]{points[0][0], points[0][1], 
                points[1][0], points[1][1], points[2][0], points[2][1], points[3][0], points[3][1]};
        } else if (isToRight) {
            return new int[]{points[0][0], points[0][1], 
                points[1][0], points[1][1], points[2][0], points[2][1], points[3][0], points[3][1]};
        } else {
            return new int[]{};
        }
    }
}
