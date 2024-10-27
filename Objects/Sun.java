package Objects;

import Objects.Player;

public class Sun extends Light {
    Player player;
    int displacementFromPlayer = 2000;
    public int dayDuration = 20;
    public double currentTime = 0;

    public Sun(Player player) {
        super(0, 0, 0);
        this.player = player;
        this.active = true;
    }

    public int[] getAnimationCoordinates() {
        double progress = 1 - this.currentTime / this.dayDuration;
        int sunX = (int)(progress * 1200);
        int sunY = (int)(-Math.sin(progress * Math.PI) * 300) + 200;
        return new int[]{sunX, sunY};
    }

    public void update(double delta) {
        currentTime += delta;
        if (currentTime > dayDuration && currentTime < dayDuration * 2) {
            this.active = false;
        }
        if (currentTime > dayDuration * 2) {
            this.active = true;
            currentTime = 0;
        }
        displacementFromPlayer -= delta / 10;
        // this.x = (int)(player.x + displacementFromPlayer);
    }
}
