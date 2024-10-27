package Objects;

/**
 * Sun object that extends light is the source of light that
 * illuminates the entire world during the day.
 */
public class Sun extends Light {
    Player player;

    /**
     * Constructor for the sun object.
     * @param player The player object that the sun is centered around.
     */
    public Sun(Player player) {
        super(0, 0, 0);
        this.player = player;
        this.active = true;
        this.y = -1000;
    }

    /**
     * Gets the coordinates of the sun for the animation.
     * @return The x and y coordinates of the sun.
     */
    public int[] getAnimationCoordinates() {
        double progress = 1 - this.currentTime / this.dayDuration;
        int sunX = (int) (progress * 1250) - 25;
        int sunY = (int) (-Math.sin(progress * Math.PI) * 300) + 200;
        return new int[]{sunX, sunY};
    }

    /**
     * Updates the sun object.
     * @param delta The time since the last update.
     */
    public void update(double delta) {
        currentTime += delta;
        if (currentTime > dayDuration && currentTime < dayDuration * 2) {
            this.active = false;
        }
        if (currentTime > dayDuration * 2) {
            this.active = true;
            currentTime = 0;
        }
        double progress = 1 - this.currentTime / this.dayDuration;
        this.x = (int) (progress * 5000) - 2500 + (int)(player.x * 48);
        this.y = (int) (-Math.sin(progress * Math.PI) * 2000) + 400;

    }
}
