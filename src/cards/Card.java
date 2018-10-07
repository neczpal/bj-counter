package cards;

public class Card {

    private CardValue value;
    private SuitValue suit;

    public Card(CardValue value, SuitValue suit) {
        this.value = value;
        this.suit = suit;
    }

    public CardValue getValue() {
        return value;
    }

    public SuitValue getSuit() {
        return suit;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Card) {
            return value.equals(((Card)obj).value) && suit.equals(((Card)obj).suit);
        }

        return false;
    }

    public String getLongName() {
        return value.getName() + " of " + suit.getName();
    }

    @Override
    public String toString() {
        return value +""+ suit;
    }
}
