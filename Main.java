import Graphics.Renderer;
import Objects.Game;

public class Main {
    public static void main(String[] args) {
        Game game = new Game();
        Renderer renderer = new Renderer(game);
        game.setRenderer(renderer);
    }
}