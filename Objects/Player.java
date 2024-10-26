package Objects;

public class Player {
    boolean[] keysDown = new boolean[4];

    public int x;
    public int y;

    public int vx = 0;
    public int vy = 0;

    public Player(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void update(int delta) {
        if (keysDown[1]) {
            vx = 1;
        } else if (keysDown[3]) {
            vx = -1;
        } else {
            vx = 0;
        }
        if (keysDown[0]) {
            vy = -1;
        } else if (keysDown[2]) {
            vy = 1;
        } else {
            vy = 0;
        }

        this.x += vx * delta;
        this.y += vy * delta;
    }

    public void keyDown(int key) {
        this.keysDown[key] = true;
    }

    public void keyUp(int key) {
        this.keysDown[key] = false;
    }
}