package net.neczpal.bjcounter.cards;

import net.neczpal.bjcounter.countings.CountingType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Shoe {


    private double[] allCount = new double[CountingType.values().length];
    private CountingType[] allCountingTypes = CountingType.values();

    private List<Card> cards;
    private int numberOfDecks;

    public Shoe() {
        this(1);
    }

    public Shoe(int numberOfDecks) {
        this.numberOfDecks = numberOfDecks;
        this.cards = new ArrayList<>();
        resetShoe();
    }

    public Shoe(List<Card> cards) {
        this.cards = cards;
    }

    private void resetCounts() {
        Arrays.fill(allCount, 0);
        for (int i = 0; i < allCount.length; i++) {
            if (allCountingTypes[i].isBalanced()) {
                allCount[i] = 0;
            } else {
                allCount[i] = numberOfDecks * -2;
            }
        }
    }

    private void updateCounts(int cardValue) {
        for (int i = 0; i < allCountingTypes.length; i++) {
            if (cardValue == 11) {
                allCount[i] += allCountingTypes[i].getValue(0);
            } else {
                allCount[i] += allCountingTypes[i].getValue(cardValue - 1);
            }
        }
    }

    public void resetShoe() {
        cards.clear();

        for (CardValue value : CardValue.values()) {
            for (SuitValue suit : SuitValue.values()) {
                for (int i = 0; i < numberOfDecks; ++i) {
                    if (suit != SuitValue.ANY)
                        cards.add(new Card(value, suit));
                }
            }
        }

        resetCounts();


        shuffleCards();
    }

    public boolean removeCard(Card card) {
        return cards.remove(card);
    }

    //Fisher–Yates shuffle
    public void shuffleCards() {
        Random rnd = ThreadLocalRandom.current();
        for (int i = cards.size() - 1; i > 0; i--) {
            int index = rnd.nextInt(i + 1);
            // Simple swap
            Card iCard = cards.get(index);
            cards.set(index, cards.get(i));
            cards.set(i, iCard);
        }
    }

    public Card dealCard() {
        Card card = cards.remove(0);

        updateCounts(card.getValue().value);

        return card;
    }

    public Card dealSpecificCard(Card card) throws Exception {
        if (!cards.remove(card)) {
            throw new Exception("Nincs mar az adott lap a pakliban!");
        }

        updateCounts(card.getValue().value);

        return card;
    }

    public double getCount(CountingType type) {
        if (allCountingTypes[type.ordinal()].isBalanced()) {
            return allCount[type.ordinal()] / (cards.size() / 52.0);
        } else {
            return allCount[type.ordinal()];
        }
    }

    public boolean isInsuranceWorthIt() {
        double numTens = 0.;
        for (Card card : cards) {
            if (card.getValue().value == 10)
                numTens++;
        }

        return numTens / cards.size() >= 0.3333334;
    }

    public int getCardsLeftCount() {
        return cards.size();
    }

    public void writeAllCards() {
        int[] counts = new int[10];

        for (Card card : cards) {
            counts[card.getValue().value - 2]++;
        }

        for (int i = 0; i < 10; i++) {
            System.out.print((i + 2) + ": " + counts[i] + "\t");
        }
        System.out.println();
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        for (Card card : cards) {
            stringBuilder.append(card + "\n");
        }

        return stringBuilder.toString();
    }

    @Override
    public Shoe clone() {
        return new Shoe(new ArrayList<>(cards));
    }
}
