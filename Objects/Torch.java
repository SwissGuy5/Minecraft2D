package Objects;

import Objects.Player;

public class Torch extends Light {
    Player player;

    public Torch(int strength, Player player) {
        super(0, 0, strength);
        this.player = player;
        this.active = false;
    }

    public void update(double delta) {
        currentTime += delta;
        if (currentTime > dayDuration && currentTime < dayDuration * 2) {
            this.active = true;
        }
        if (currentTime > dayDuration * 2) {
            this.active = false;
            currentTime = 0;
        }
        this.x = (int)(player.x * 48);
        this.y = (int)((64 - player.y) * 48);
    }
}
