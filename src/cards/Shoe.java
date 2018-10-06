package cards;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Shoe {

    private List<Card> cards;

    private int runningCount;
    private int trueCount;

    public Shoe() {
        this(1);
    }

    public Shoe(int numberOfDecks) {
        cards  = new ArrayList<>();

        for(int i = 0; i < numberOfDecks; ++i) {
            for (CardValue value : CardValue.values()){
                for(SuitValue suit : SuitValue.values()) {
                    cards.add(new Card(value, suit));
                }
            }
        }
        runningCount = 0;
        trueCount = 0;
    }

    public Shoe(List<Card> cards) {
        this.cards = cards;
    }


    public boolean removeCard (Card card) {
        return cards.remove(card);
    }

    //Fisher–Yates shuffle
    public void shuffleCards() {
        // If running on Java 6 or older, use `new Random()` on RHS here
        Random rnd = ThreadLocalRandom.current();
        for (int i = cards.size() - 1; i > 0; i--)
        {
            int index = rnd.nextInt(i + 1);
            // Simple swap
            Card iCard = cards.get(index);
            cards.set(index, cards.get(i));
            cards.set(i, iCard);
        }
    }

    public Card dealCard() {
        Card iCard = cards.get(0);
        cards.remove(0);

        if(iCard.getValue().value >= 10) {
            runningCount--;
        } else
        if(iCard.getValue().value <= 6) {
            runningCount++;
        }

        return iCard;
    }

    public int getRunningCount(){
        return runningCount;
    }

    public int getTrueCount () {
        return runningCount / (cards.size()/52);
    }
    public int getCardsLeftCount () {
        return cards.size();
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        for (Card card : cards) {
            stringBuilder.append(card + "\n");
        }

        return stringBuilder.toString();
    }

}
