package Objects;

import Objects.Player;

public class Sun extends Light {
    Player player;
    int displacementFromPlayer = 2000;
    int dayDuration = 1000;
    int currentTime = 0;

    public Sun(int x, int y, int strength, Player player) {
        super(x, y, strength);
        this.player = player;
        this.active = false;
    }

    public void update(int delta) {
        currentTime += delta;
        if (currentTime > dayDuration) {
            this.active = false;
        }
        if (currentTime > dayDuration * 2) {
            this.active = true;
        }
        displacementFromPlayer -= delta / 10;
        this.x = player.x + displacementFromPlayer;
    }
}
