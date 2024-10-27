package Objects;

/**
 * Torch object that extends light is the source of light
 * that follows the player during the night.
 */
public class Torch extends Light {
    Player player;

    /**
     * Constructor for the torch object.
     * @param strength The strength of the light.
     * @param player The player object that the torch follows.
     */
    public Torch(int strength, Player player) {
        super(0, 0, strength);
        this.player = player;
        this.active = false;
    }

    /**
     * Updates the torch object.
     * @param delta The time since the last update.
     */
    public void update(double delta) {
        currentTime += delta;
        if (currentTime > dayDuration && currentTime < dayDuration * 2) {
            this.active = true;
        }
        if (currentTime > dayDuration * 2) {
            this.active = false;
            currentTime = 0;
        }
        this.x = (int) (player.x * 48);
        this.y = (int) ((64 - player.y) * 48);
    }
}
