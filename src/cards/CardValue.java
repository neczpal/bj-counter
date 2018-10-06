package cards;

public enum CardValue {

    _2("Deuce", 2), _3("Three", 3), _4("Four", 4), _5("Five", 5),
    _6("Six", 6), _7("Seven", 7), _8("Eight", 8), _9("Nine", 9),
    _10("Ten", 10), J("Jack", 10), Q("Queen", 10), K("King", 10),
    A("Ace", 11/1);

    public String name;
    public int value;

    CardValue(String name, int value){
        this.name = name;
        this.value = value;
    }

    @Override
    public String toString() {
        return name;
    }
}
