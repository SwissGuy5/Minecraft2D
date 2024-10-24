package Objects;

public class Rectangle {
    private int[][] points = new int[4][2];
    private int[] center = new int[2];

    // HAS TO BE TOP (LEFT, RIGHT), BOTTOM (LEFT RIGHT)
    public Rectangle(int[] edges) {

        for (int i = 0; i < 4; i++) {
            points[i][0] = edges[i * 2];
            points[i][1] = edges[i * 2 + 1];
        }

        center[0] = points[0][0] + (points[1][0] - points[0][0]) / 2;
        center[1] = points[0][1] + (points[2][1] - points[0][1]) / 2;
    }

    public double distanceToPoint(int x, int y) {
        return Math.sqrt(Math.pow((x - center[0]), 2) + Math.pow(y - center[1], 2));
    }
    
    public int[] getPointsReachableFrom(int x, int y) {
        boolean isToLeft = x < points[0][0];
        boolean isToRight = x > points[1][0];
        boolean isAbove = y > points[0][1];
        boolean isBelow = y < points[2][1];

        // if (isAbove && isToLeft) {
        //     return new int[]{points[0][0], points[0][1], points[1][0], points[1][1], points[2][0], points[2][1]};
        // } else if (isAbove && isToRight) {
        //     return new int[]{points[1][0], points[1][1], points[0][0], points[0][1], points[3][0], points[3][1]};
        // } else if (isBelow && isToLeft) {
        //     return new int[]{points[2][0], points[2][1], points[0][0], points[0][1], points[3][0], points[3][1]};
        // } else if (isBelow && isToRight) {
        //     return new int[]{points[3][0], points[3][1], points[1][0], points[1][1], points[2][0], points[2][1]};
        // } else if (isAbove) {
        //     return new int[]{points[0][0], points[0][1], points[1][0], points[1][1]};
        // } else if (isBelow) {
        //     return new int[]{points[2][0], points[2][1], points[3][0], points[3][1]};
        // } else if (isToLeft) {
        //     return new int[]{points[0][0], points[0][1], points[2][0], points[2][1]};
        // } else if (isToRight) {
        //     return new int[]{points[1][0], points[1][1], points[3][0], points[3][1]};
        // } else {
        //     return new int[]{};
        // }

        if (isAbove && isToLeft) {
            return new int[]{points[1][0], points[1][1], points[2][0], points[2][1], points[3][0], points[3][1]};
        } else if (isAbove && isToRight) {
            return new int[]{points[0][0], points[0][1], points[2][0], points[2][1], points[3][0], points[3][1]};
        } else if (isBelow && isToLeft) {
            return new int[]{points[0][0], points[0][1], points[1][0], points[1][1], points[3][0], points[3][1]};
        } else if (isBelow && isToRight) {
            return new int[]{points[3][0], points[3][1], points[0][0], points[0][1], points[1][0], points[1][1]};
        } else if (isAbove) {
            return new int[]{points[2][0], points[2][1], points[3][0], points[3][1]};
        } else if (isBelow) {
            return new int[]{points[0][0], points[0][1], points[1][0], points[1][1]};
        } else if (isToLeft) {
            return new int[]{points[1][0], points[1][1], points[3][0], points[3][1]};
        } else if (isToRight) {
            return new int[]{points[0][0], points[0][1], points[2][0], points[2][1]};
        } else {
            return new int[]{points[0][0], points[0][1], points[1][0], points[1][1], points[2][0], points[2][1], points[3][0], points[3][1]};
        }
    }
}
