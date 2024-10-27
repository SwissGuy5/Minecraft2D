package Objects;

public class Light {
    public int id;
    public int x;
    public int y;
    public int strength;
    public boolean active = true;

    int dayDuration = 60;
    double currentTime = 55;

    public Light(int x, int y, int strength) {
        this.x = x;
        this.y = y;
        this.strength = strength;
        this.id = (int)(Math.random() * 100000);
    }
}
