package Objects;

import Objects.Player;

public class Sun extends Light {
    Player player;

    public Sun(Player player) {
        super(0, 0, 0);
        this.player = player;
        this.active = true;
        this.y = -1000;
    }

    public int[] getAnimationCoordinates() {
        double progress = 1 - this.currentTime / this.dayDuration;
        int sunX = (int)(progress * 1250) - 25;
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
        double progress = 1 - this.currentTime / this.dayDuration;
        this.x = (int)(progress * 5000) - 2500 + (int)(player.x * 48);
        this.y = (int)(-Math.sin(progress * Math.PI) * 2000) + 400;

    }
}
