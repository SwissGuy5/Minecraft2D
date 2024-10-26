package Objects;

public class Inventory {
    public static final int inventorySize = 9;
    public int[] items;
    public int currentlySelected;

    public Inventory() {
        items = new int[inventorySize];
        currentlySelected = 0;
    }

    public void setSelected(int keyCode) {
        currentlySelected = keyCode - 49;
    }
}