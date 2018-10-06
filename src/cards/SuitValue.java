package cards;


import static cards.ColorValue.BLACK;
import static cards.ColorValue.RED;

public enum SuitValue {

    SPADES("Spades", BLACK), HEARTS("Hearts", RED), DIAMONDS("Diamonds", RED), CLUBS("Clubs", BLACK);

    public String name;
    public ColorValue color;

    SuitValue (String name, ColorValue color) {
        this.name = name;
        this.color = color;
    }

    @Override
    public String toString() {
        return name;
    }
}
