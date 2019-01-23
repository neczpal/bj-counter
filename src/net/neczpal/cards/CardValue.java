package net.neczpal.cards;

public enum CardValue {

    _2("Deuce", "2", 2), _3("Three", "3", 3), _4("Four", "4", 4), _5("Five", "5", 5),
    _6("Six", "6", 6), _7("Seven", "7", 7), _8("Eight", "8", 8), _9("Nine", "9", 9),
    _10("Ten", "10", 10), J("Jack", "J", 10), Q("Queen", "Q", 10), K("King", "K", 10),
    A("Ace", "A", 11/1);

    public String name;
    public String shortName;
    public int value;

    CardValue(String name, String shortName, int value){
        this.name = name;
        this.shortName = shortName;
        this.value = value;
    }

    public static CardValue toCardValue (String shortName) {
        for(CardValue cardValue : values()){
            if(cardValue.shortName.equals(shortName)){
                return cardValue;
            }
        }
        return null;
    }

    public String getName() {
        return name;
    }
    @Override
    public String toString() {
        return shortName;
    }
}
