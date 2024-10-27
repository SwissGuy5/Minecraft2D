package Objects;

/**
 * Light object that can be placed in the world to illuminate the area around it.
 */
public class Light {
    public int id;
    public int x;
    public int y;
    public int strength;
    public boolean active = true;

    int dayDuration = 40;
    double currentTime = 20;

    /** 
     * Constructor for the Light object.
     * @param x The x-coordinate of the light.
     * @param y The y-coordinate of the light.
     * @param strength The strength of the light.
     */
    public Light(int x, int y, int strength) {
        this.x = x;
        this.y = y;
        this.strength = strength;
        this.id = (int) (Math.random() * 100000);
    }
}
