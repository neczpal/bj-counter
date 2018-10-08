package cards;


import static cards.ColorValue.BLACK;
import static cards.ColorValue.RED;

public enum SuitValue {

    SPADES("Spades","♠", BLACK), HEARTS("Hearts", "♡", RED),
    DIAMONDS("Diamonds", "♢", RED), CLUBS("Clubs", "♣", BLACK),
    ANY ("Any", "x", null);

    public String name;
    public String icon;
    public ColorValue color;

    SuitValue (String name,String icon, ColorValue color) {
        this.name = name;
        this.color = color;
        this.icon = icon;
    }

    @Override
    public String toString() {
        return icon;
    }

    public String getName() {
        return name;
    }
}
