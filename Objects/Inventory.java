package Objects;

/**
 * Inventory class that holds the items that the player has in their inventory.
 */
public class Inventory {
    public static final int INV_SIZE = 9;
    public byte[] items;
    public int currentlySelected;

    /**
     * Constructor for the Inventory class.
     */
    public Inventory() {
        items = new byte[INV_SIZE];
        items[0] = 9;
        items[1] = 10;
        items[2] = 28;
        items[3] = 16;
        items[4] = 13;
        items[5] = 90;
        items[6] = 91;
        items[7] = 0;
        items[8] = 0;
        currentlySelected = 0;
    }

    public void setSelected(int keyCode) {
        currentlySelected = keyCode - 49;
    }
}