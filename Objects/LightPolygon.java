package Objects;

public class LightPolygon {
    public int n;
    public int[] xCoords;
    public int[] yCoords;
    Double[] angles;


    public LightPolygon(int[] xCoords, int[] yCoords) {
        this.n = xCoords.length + 2;
        this.xCoords = new int[this.n];
        this.yCoords = new int[this.n];
        for (int i = 0; i < this.n - 2; i++) {
            this.xCoords[i] = xCoords[i];
            this.yCoords[i] = yCoords[i];
        }
    }

    public LightPolygon(int[] coords) {
        this.n = coords.length / 2 + 2;
        this.xCoords = new int[this.n];
        this.yCoords = new int[this.n];
        for (int i = 0; i < this.n - 2; i++) {
            this.xCoords[i] = coords[2 * i];
            this.yCoords[i] = coords[2 * i + 1];
        }
    }

    public void calculateShadowForLightSource(int x, int y) {
        this.angles = new Double[this.n - 2];
        for (int i = 0; i < this.n - 2; i++) {
            double deltaX = this.xCoords[i] - x;
            double deltaY = this.yCoords[i] - y;
            double angle = Math.atan2(deltaY, deltaX);
            angles[i] = angle;
        }

        for (int i = 0; i < this.angles.length - 1; i++) {
            for (int j = 0; j < this.angles.length - i - 1; j++) {
                if (angles[j] > angles[j + 1]) {
                    double tempA = angles[j];
                    angles[j] = angles[j + 1];
                    angles[j + 1] = tempA;

                    int tempX = xCoords[j];
                    xCoords[j] = xCoords[j + 1];
                    xCoords[j + 1] = tempX;

                    int tempY = yCoords[j];
                    yCoords[j] = yCoords[j + 1];
                    yCoords[j + 1] = tempY;
                }
            }
        }

        double minAngle = angles[0];
        double maxAngle = angles[angles.length - 1];

        int distance = 50000;
        int minX = x + (int) (distance * Math.cos(minAngle));
        int minY = y + (int) (distance * Math.sin(minAngle));
        int maxX = x + (int) (distance * Math.cos(maxAngle));
        int maxY = y + (int) (distance * Math.sin(maxAngle));

        this.xCoords[this.n - 1] = minX;
        this.yCoords[this.n - 1] = minY;
        this.xCoords[this.n - 2] = maxX;
        this.yCoords[this.n - 2] = maxY;
    }
}