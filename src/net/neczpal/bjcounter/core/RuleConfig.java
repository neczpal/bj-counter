package net.neczpal.bjcounter.core;

public class RuleConfig {
    public double bjPays; // 3/2, 7/5, 6/5, 1/1
    public boolean dealerPeeks;
    public boolean hitsSoft17;
    public int doubleDownOptions; // 0 - any card, 1 - 9,10,11, 2 - 10, 11
    public int splitX; // 0 - not allowed, 1 - once, 2 - twice, 3 - three times
    public int splitA; // 0 - not allowed, 1 - once, 2 - twice, 3 - three times
    public boolean drawToSplitAces;
    public boolean doubleAfterSplit;
    public int surrenderOptions; // 0 - not allowed, 1 - allowed against anything except Ace, 2 - allowed against anything
    public int numberOfDecks;
    public boolean continuesShuffling;
    public int cutDeck;

    public static RuleConfig DEFAULT = new RuleConfig(3 / 2., false, false, 0, 1, 1, false, true, 1, 8, 3, false);

    public RuleConfig(double bjPays, boolean dealerPeeks, boolean hitsSoft17, int doubleDownOptions, int splitX, int splitA, boolean drawToSplitAces, boolean doubleAfterSplit, int surrenderOptions, int numberOfDecks, int cutDeck, boolean continuesShuffling) {
        this.bjPays = bjPays;
        this.dealerPeeks = dealerPeeks;
        this.hitsSoft17 = hitsSoft17;
        this.doubleDownOptions = doubleDownOptions;
        this.splitX = splitX;
        this.splitA = splitA;
        this.drawToSplitAces = drawToSplitAces;
        this.doubleAfterSplit = doubleAfterSplit;
        this.surrenderOptions = surrenderOptions;
        this.numberOfDecks = numberOfDecks;
        this.continuesShuffling = continuesShuffling;
        this.cutDeck = cutDeck;
    }
}
