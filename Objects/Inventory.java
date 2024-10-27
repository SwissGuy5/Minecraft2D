package Objects;

public class Inventory {
    public static final int inventorySize = 9;
    public byte[] items;
    public int currentlySelected;

    public Inventory() {
        items = new byte[inventorySize];
        items[0] = 9;
        items[1] = 10;
        items[2] = 28;
        items[3] = 59;
        items[4] = 13;
        items[5] = 90;
        items[6] = 0;
        items[7] = 0;
        items[8] = 0;
        currentlySelected = 0;
    }

    public void setSelected(int keyCode) {
        currentlySelected = keyCode - 49;
    }
}