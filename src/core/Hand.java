package core;

import cards.Card;

import java.util.ArrayList;
import java.util.List;

public class Hand {
    private List<Card> cards;
    private String handName;

    private int currentSplitHand;
    private List<Hand> splitHands;
    private boolean splitted;
    private boolean soft;
    private boolean standing;
    private boolean bj;
    private boolean busted;
    private boolean surrendered;
    private boolean splitChildHand;
    private int value;

    private int wager;
    private int wager_bet;

    private int coins = 0;

    public Hand(String handName) {
        this(handName, false);
    }

    public Hand (String handName, boolean splitChildHand) {
        this.handName = handName;
        cards = new ArrayList<>();
        soft = false;
        splitHands = new ArrayList<>();
        wager_bet = 2;
        this.splitChildHand = splitChildHand;

        clearHand();
    }

    public void clearHand() {
        cards.clear();
        soft = false;
        standing = false;
        bj = false;
        busted = false;
        value = 0;
        splitted = false;
        surrendered = false;
        currentSplitHand = 0;
        wager = wager_bet;
        splitHands.clear();
    }

    public void surrender () {
        standing = true;
        surrendered = true;
    }

    public void stand () {
        if (splitted) {
            Hand iHand = splitHands.get(currentSplitHand);
            iHand.stand();

            currentSplitHand++;

            //Van meg splittelt keze
            if(currentSplitHand < splitHands.size()) {
                Hand cHand = splitHands.get(currentSplitHand);
                value = cHand.pGetValue();
            } else {
                standing = true;
            }
        } else {
            standing = true;
        }
    }

    public void hit(Card card) {
        if(splitted) {
            Hand iHand = splitHands.get(currentSplitHand);
            iHand.hit(card);
            value = iHand.pGetValue();//A kez erteke az aktualis splitted hand erteke lesz
        } else {
            cards.add(card);
            value = pGetValue();
        }
    }

    public void hitOnce(Card card) {
        hit(card);
        stand();
    }

    public void doubleDown (Card card) {
        wager *= 2;
        hitOnce(card);
    }

    public void split () {
        if(splitHands.size() >= 4) { //Rules
            System.out.println("Tul sok kez meghaladja a maximum ketteosztast");
            return;
        }
        //Ha meg nincs splittelve
        if(splitHands.size() == 0 && isPair()) {
            Hand hand1 = new Hand(handName + ": 1 ", true);
            Hand hand2 = new Hand(handName + ": 2 ", true);

            hand1.hit(cards.get(0));
            hand2.hit(cards.get(1));

            hand1.setWager(wager);
            hand2.setWager(wager);

            splitHands.add(hand1);
            splitHands.add(hand2);

            splitted = true;
            currentSplitHand = 0;

            removeCard(1);
            removeCard(0);
        } else
        //Ha 1x splittelt mar
        if(splitHands.size() == 2) {
            if(splitHands.get(currentSplitHand).isPair()){
                Hand hand3 = new Hand(handName + ": 3 ", true);
                Card iCard = splitHands.get(currentSplitHand).removeCard(1);

                hand3.hit(iCard);

                hand3.setWager(wager);

                splitHands.add(hand3);
            } else {
                System.out.println("Nem par, nem lehet ketteosztani");
            }
        }else
        //Ha 2x splittelt mar
        if(splitHands.size() == 3) {
            if(splitHands.get(currentSplitHand).isPair()){
                Hand hand4 = new Hand(handName + ": 4 ", true);
                Card iCard = splitHands.get(currentSplitHand).removeCard(1);

                hand4.hit(iCard);

                hand4.setWager(wager);

                splitHands.add(hand4);
            } else {
                System.out.println("Nem par, nem lehet ketteosztani");
            }
        }
    }
    public void setWager(int wager) {
        this.wager = wager;
    }
    public int getValue () {
        if(splitted && currentSplitHand < splitHands.size()) {
            return splitHands.get(currentSplitHand).getValue();
        } else
        return value;
    }

    public boolean isStanding() {
        return standing;
    }

    public boolean isSoft() {
        return soft;
    }

    public boolean isBusted() {
        return busted;
    }

    public boolean isBj() {
        return bj;
    }

    public boolean isStartingHand() {
        return cards.size() == 2;
    }

    public boolean isPair() {
        if(splitted)
            return splitHands.get(currentSplitHand).isPair();
        else
            return cards.size() == 2 && cards.get(0).getValue() == cards.get(1).getValue();
    }

    public Card getCard(int index) {
        if (splitted){
            return splitHands.get(currentSplitHand).getCard(index);
        } else {
            return cards.get(index);
        }
    }

    public int getCardCount() {
        if (splitted){
            return splitHands.get(currentSplitHand).getCardCount();
        } else {
            return cards.size();
        }
    }

    public int splitNumber () {
        return Math.max(splitHands.size()-1, 0);
    }

    public void match(int dealerSum, boolean dealerBusted, boolean dealerBj) {
        if (surrendered) {
            coins -= wager/2;
            wager_bet *= 2;
        } else
        if (splitted) {
            int wagerSum = 0;
            for (Hand hand :
                    splitHands) {
                hand.match(dealerSum, dealerBusted, dealerBj);
                wagerSum += hand.getCoins();
            }
            coins += wagerSum;
            if(wagerSum < 0){
                wager_bet *= 2;
            } else {
                wager_bet = 2;
            }
        } else
        if(!busted) {
            if(bj && !dealerBj) {
                coins += wager * 3 / 2;
                wager_bet = 2;
            } else
            if (dealerBj) {
                coins -= wager;
                wager_bet *= 2;
            } else
            if (dealerBusted) {
                coins += wager;
                wager_bet = 2;
            } else
            if (value > dealerSum) {
                coins += wager;
                wager_bet = 2;
            } else if (value < dealerSum) {
                coins -= wager;
                wager_bet *= 2;
            }
        } else {
            coins -= wager;
            wager_bet *= 2;
        }
    }

    private Card removeCard(int index) {
        return cards.remove(index);
    }


    private int pGetValue() {
        int sum = 0;
        for(Card card : cards) {
            int cVal = card.getValue().value;
            if (cVal == 11){
                if(sum+cVal > 21){
                    cVal = 1;
                } else {
                    soft = true;
                }
            }
            sum += cVal;

            if(sum > 21){
                if(soft) {
                    sum -= 10;
                    soft = false;
                }else {
                    busted = true;
                    standing = true;
                }
            }
        }

        if (sum == 21){
            standing = true;
            if( isStartingHand() && !splitChildHand) {
                bj = true;
            }
        }

        return sum;
    }

    public String getHandName() {
        return handName;
    }

    public int getCoins() {
        return coins;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        if(splitted) {
            for(Hand hand: splitHands) {
                stringBuilder.append(hand + "\n");
            }
        } else {

            if (bj) {
                stringBuilder.append(handName + " BLACKJACK \n");
            } else if (soft) {
                stringBuilder.append(handName + " " + value + " (" + (value - 10) + ")\n");
            } else if (busted) {
                stringBuilder.append(handName + " " + value + " (BUSTED)\n");

            } else {
                stringBuilder.append(handName + " " + value + "\n");
            }

            for (Card card : cards) {
                stringBuilder.append(card + "\n");
            }
        }

        return stringBuilder.toString();
    }
}
