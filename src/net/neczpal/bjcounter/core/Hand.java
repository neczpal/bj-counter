package net.neczpal.bjcounter.core;

import net.neczpal.bjcounter.cards.Card;
import net.neczpal.bjcounter.cards.Shoe;
import net.neczpal.bjcounter.core.betstrategies.BetStrategy;

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

    private double wager;
    private BetStrategy betStrategy;
    private double coins = 0;

    private double hitEV = -999;
    private double doubledownEV = -999;
    private double standEV = -999;
    private double splitEV = -999;
    private double bestEV = -999;
    private double surrenderEV = -999;
    private Action bestAction = Action.STAND;


    private PlayStrategy playStrategy = PlayStrategy.NONE;


    private final int BIG_N = 10000;

    public Hand(List<Card> cards, String handName, int currentSplitHand, List<Hand> splitHands, boolean splitted, boolean soft, boolean standing, boolean bj, boolean busted, boolean surrendered, boolean splitChildHand, int value, double wager, BetStrategy betStrategy, double coins) {
        this.cards = cards;
        this.handName = handName;
        this.currentSplitHand = currentSplitHand;
        this.splitHands = splitHands;
        this.splitted = splitted;
        this.soft = soft;
        this.standing = standing;
        this.bj = bj;
        this.busted = busted;
        this.surrendered = surrendered;
        this.splitChildHand = splitChildHand;
        this.value = value;
        this.wager = wager;
        this.betStrategy = betStrategy;
        this.coins = coins;
    }

    public Hand(String handName) {
        this(handName, false);
    }

    public Hand(String handName, BetStrategy betStrategy) {
        this(handName, betStrategy, PlayStrategy.BASIC_STRAT);
    }
    public Hand(String handName, BetStrategy betStrategy, PlayStrategy playStrategy) {
        this(handName, false);

        this.betStrategy = betStrategy;
        this.playStrategy = playStrategy;
    }

    public Hand(String handName, boolean splitChildHand) {
        this.handName = handName;
        cards = new ArrayList<>();
        soft = false;
        splitHands = new ArrayList<>();
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
        splitHands.clear();
    }

    //BET STATE
    public void setWagerBet() {
        wager = betStrategy.calculateNextBet();
    }

    public void setWager(double wager) {
        this.wager = wager;
    }

    //PLAY STATE
    public void surrender() {
        standing = true;
        surrendered = true;
    }

    public void stand() {
        if (splitted) {
            Hand iHand = splitHands.get(currentSplitHand);
            iHand.stand();

            currentSplitHand++;

            //Van meg splittelt keze
            if (currentSplitHand < splitHands.size()) {
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
        if (splitted) {
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

    public void doubleDown(Card card) {
        wager *= 2;
        hitOnce(card);
    }

    public void split(Card card1, Card card2) { //#TODO only once split
        if (splitHands.size() >= 4) { //Rules
            System.out.println("Tul sok kez meghaladja a maximum ketteosztast");
            return;
        }
        //Ha meg nincs splittelve
        if (splitHands.size() == 0 && isPair()) {
            Hand hand1 = new Hand(handName + ": 1 ", true);
            Hand hand2 = new Hand(handName + ": 2 ", true);

            hand1.hit(cards.get(0));
            hand1.hit(card1);
            hand2.hit(cards.get(1));
            hand2.hit(card2);

            hand1.setWager(wager);
            hand2.setWager(wager);

            splitHands.add(hand1);
            splitHands.add(hand2);

            splitted = true;
            currentSplitHand = 0;

            if (cards.get(0).getValue().value == 11) {
                standing = true;
            }

            removeCard(1);
            removeCard(0);
        } else
            //Ha 1x splittelt mar
            if (splitHands.size() == 2) {
                if (splitHands.get(currentSplitHand).isPair()) {
                    Hand hand3 = new Hand(handName + ": 3 ", true);
                    Card iCard = splitHands.get(currentSplitHand).removeCard(1);

                    hand3.hit(iCard);

                    hand3.setWager(wager);

                    splitHands.add(hand3);
                } else {
                    System.out.println("Nem par, nem lehet ketteosztani");
                }
            } else
                //Ha 2x splittelt mar
                if (splitHands.size() == 3) {
                    if (splitHands.get(currentSplitHand).isPair()) {
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

    public int getValue() {
        if (splitted && currentSplitHand < splitHands.size()) {
            return splitHands.get(currentSplitHand).getValue();
        } else
            return value;
    }

    public boolean isPlaying() {
        return wager > 0;
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
        if (splitted)
            return splitHands.get(currentSplitHand).isPair();
        else
            return cards.size() == 2 && cards.get(0).getValue().value == cards.get(1).getValue().value;
    }

    public Card getCard(int index) {
        if (splitted) {
            return splitHands.get(currentSplitHand).getCard(index);
        } else {
            return cards.get(index);
        }
    }

    public int getCardCount() {
        if (splitted) {
            return splitHands.get(currentSplitHand).getCardCount();
        } else {
            return cards.size();
        }
    }

    public int splitNumber() {
        return Math.max(splitHands.size() - 1, 0);
    }

    public void match(int dealerSum, boolean dealerBusted, boolean dealerBj) {
        if (surrendered) {
            coins -= wager / 2;
        } else if (splitted) {
            int wagerSum = 0;
            for (Hand hand :
                    splitHands) {
                hand.match(dealerSum, dealerBusted, dealerBj);
                wagerSum += hand.getCoins();
            }
            coins += wagerSum;
        } else if (!busted) {
            if (bj && !dealerBj) {
                coins += wager * 3 / 2;
            } else if (dealerBj) {
                coins -= wager;
            } else if (dealerBusted) {
                coins += wager;
            } else if (value > dealerSum) {
                coins += wager;
            } else if (value < dealerSum) {
                coins -= wager;
            }
        } else {
            coins -= wager;
        }
    }

    private Card removeCard(int index) {
        return cards.remove(index);
    }


    private int pGetValue() {
        int sum = 0;
        for (Card card : cards) {
            int cVal = card.getValue().value;
            if (cVal == 11) {
                if (sum + cVal > 21) {
                    cVal = 1;
                } else {
                    soft = true;
                }
            }
            sum += cVal;

            if (sum > 21) {
                if (soft) {
                    sum -= 10;
                    soft = false;
                } else {
                    busted = true;
                    standing = true;
                }
            }
        }

        if (sum == 21) {
            standing = true;
            if (isStartingHand() && !splitChildHand) {
                bj = true;
            }
        }

        return sum;
    }

    public String getHandName() {
        return wager > 0 ? handName + " $" + wager + "" : handName;
    }

    public String getCardsString() {
        StringBuilder stringBuilder = new StringBuilder();
        if (splitted) {
            for (Hand hand : splitHands) {
                stringBuilder.append(hand.getCardsString());
            }
        } else {
            stringBuilder.append('[');
            for (Card card : cards) {
                stringBuilder.append(card + " ");
            }
            stringBuilder.append(']');

        }
        return stringBuilder.toString();
    }

    public double getCoins() {
        return coins;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        if (splitted) {
            for (Hand hand : splitHands) {
                stringBuilder.append(hand + "\n");
            }
        } else {

            if (bj) {
                stringBuilder.append(handName + " BLACKJACK \n");
            } else if (soft) {
                stringBuilder.append(handName + " " + value + " (" + (value - 10) + ")\n");
            } else if (busted) {
                stringBuilder.append(handName + " " + value + " (BUSTED)\n");
            } else if (surrendered) {
                stringBuilder.append(handName + " " + value + " (SURRENDERED)\n");
            } else {
                stringBuilder.append(handName + " " + value + "\n");
            }

            for (Card card : cards) {
                stringBuilder.append(card + " ");
            }
            stringBuilder.append("\n");
        }

        return stringBuilder.toString();
    }

    //#TODO
    public List<Action> getAvailableAction(RuleConfig config, Card dealerCard) {
        List<Action> ret = new ArrayList<>();

        if (isStanding() || isBusted() || isBj() || surrendered || getValue() == 21) {
            return ret;// NO ACTION MORE
        } else {
            if (isStartingHand()) {
                if ((config.doubleDownOptions == 2 && this.getValue() <= 11 && this.getValue() >= 10) ||
                        (config.doubleDownOptions == 1 && this.getValue() <= 11 && this.getValue() >= 9) ||
                        (config.doubleDownOptions == 0)) {

                    ret.add(Action.DOUBLE_DOWN);

                }
                if ((config.surrenderOptions == 2) ||
                        (config.surrenderOptions == 1 && dealerCard.getValue().value != 11)) {

                    ret.add(Action.SURRENDER);
                }

                if (isPair()) {
                    ret.add(Action.SPLIT);
                }
            }

            if (splitted && splitHands.get(0).getValue() == 11) {
                ret.add(Action.HIT_SPLIT_ACE);
            } else {
                ret.add(Action.HIT);
            }

            ret.add(Action.STAND);
        }

        return ret;
    }

    @Override
    public Hand clone() {
        List<Hand> clonedSplitHands = new ArrayList<>();
        for(Hand hand : splitHands) {
            clonedSplitHands.add(hand.clone());
        }

        return new Hand(new ArrayList<>(cards), handName, currentSplitHand, new ArrayList<>(clonedSplitHands), splitted, soft, standing, bj, busted, surrendered, splitChildHand, value, 1, betStrategy, 0);
    }



    private double calcStandEV(Hand dealerHand, Shoe shoe, RuleConfig config) {
        double sum = 0.0;
        Shoe iShoe;
        Hand iHand;
        Hand iDealerHand;

        for (int i = 0; i < BIG_N; i++) {
            //CLONE ALL
            iShoe = shoe.clone();
            iShoe.shuffleCards();
            iHand = this.clone();
            iDealerHand = dealerHand.clone();

            iDealerHand.dealDealerHand(iShoe, config);

            iHand.match(iDealerHand.getValue(), iDealerHand.isBusted(), iDealerHand.isBj());

            sum += iHand.getCoins() / iHand.wager;
        }

        return sum / (double) BIG_N;
    }

    private double calcHitEV(Hand dealerHand, Shoe shoe, RuleConfig config) {
        if (isBj()) {
            return -999.;
        }

        double sum = 0.0;
        Shoe iShoe;
        Hand iHand;
        Hand iDealerHand;

        for (int i = 0; i < BIG_N; i++) {
            //CLONE ALL
            iShoe = shoe.clone();
            iShoe.shuffleCards();
            iHand = this.clone();
            iDealerHand = dealerHand.clone();

            iHand.hit(iShoe.dealCard());

            while (iHand.getValue() <= 11 || dealerHand.getValue() >= 7 && iHand.getValue() <= 17) {
                iHand.hit(iShoe.dealCard());
            }

            iDealerHand.dealDealerHand(iShoe, config);

            iHand.match(iDealerHand.getValue(), iDealerHand.isBusted(), iDealerHand.isBj());

            sum += iHand.getCoins() / iHand.wager;
        }

        return sum / (double) BIG_N;
    }

    private double calcDoubleDownEV(Hand dealerHand, Shoe shoe, RuleConfig config) {
        if (isBj() || !isStartingHand()) {
            return -999.;
        }

        double sum = 0.0;
        Shoe iShoe;
        Hand iHand;
        Hand iDealerHand;

        for (int i = 0; i < BIG_N; i++) {
            //CLONE ALL
            iShoe = shoe.clone();
            iShoe.shuffleCards();
            iHand = this.clone();
            iDealerHand = dealerHand.clone();

            iHand.doubleDown(iShoe.dealCard());

            iDealerHand.dealDealerHand(iShoe, config);

            iHand.match(iDealerHand.getValue(), iDealerHand.isBusted(), iDealerHand.isBj());

            sum += iHand.getCoins() / iHand.wager * 2;
        }

        return sum / (double) BIG_N;
    }

    private double calcSplitEV(Hand dealerHand, Shoe shoe, RuleConfig config) {
        if (!isPair() || !isStartingHand()) {
            return -999.;
        }

        double sum = 0.0;
        Shoe iShoe;
        Hand iHand;
        Hand iDealerHand;

        for (int i = 0; i < BIG_N; i++) {
            //CLONE ALL
            iShoe = shoe.clone();
            iShoe.shuffleCards();
            iHand = this.clone();
            iDealerHand = dealerHand.clone();

            iHand.split(iShoe.dealCard(), iShoe.dealCard());

            for (Hand iSplitHand : iHand.splitHands) {

                while (!iSplitHand.standing && (iSplitHand.getValue() == 9 && iDealerHand.getValue() >= 3 && iDealerHand.getValue() <= 6) ||
                        (iSplitHand.getValue() == 10 && iDealerHand.getValue() <= 9) ||
                        (iSplitHand.getValue() == 11 && iDealerHand.getValue() <= 10)) {
                    iSplitHand.doubleDown(iShoe.dealCard());
                }

                while (!iSplitHand.standing && (iSplitHand.getValue() <= 11 || dealerHand.getValue() >= 7 && iSplitHand.getValue() < 17)) {
                    iSplitHand.hit(iShoe.dealCard());
                }
            }


            iDealerHand.dealDealerHand(iShoe, config);

            iHand.match(iDealerHand.getValue(), iDealerHand.isBusted(), iDealerHand.isBj());

            sum += iHand.getCoins() / iHand.wager;

        }

        return sum / BIG_N;
    }

    private double calcSurrenderEV(Hand dealerHand, RuleConfig config) {
        if (isStartingHand() && ((config.surrenderOptions == 2) ||
                (config.surrenderOptions == 1 && dealerHand.getCard(0).getValue().value != 11))) {
            return -0.5;
        } else {
            return -999.;
        }
    }

    public void calcWinningChances(Hand dealerHand, Shoe shoe, RuleConfig config) {
        hitEV = calcHitEV(dealerHand, shoe, config);
        standEV = calcStandEV(dealerHand, shoe, config);
        doubledownEV = calcDoubleDownEV(dealerHand, shoe, config);
        splitEV = calcSplitEV(dealerHand, shoe, config);
        surrenderEV = calcSurrenderEV(dealerHand, config);

        bestEV = Math.max(hitEV, Math.max(standEV, Math.max(doubledownEV, Math.max(splitEV, surrenderEV))));
        bestAction = standEV == bestEV ? Action.STAND :
                hitEV == bestEV ? Action.HIT :
                        doubledownEV == bestEV ? Action.DOUBLE_DOWN :
                                splitEV == bestEV ? Action.SPLIT : surrenderEV == bestEV ? Action.SURRENDER : Action.STAND;

    }

    public Action getBestEVAction() {
        return bestAction;
    }

    public double getBestEV () {
        return bestEV;
    }

    public double getHitEV() {
        return hitEV;
    }

    public double getDoubledownEV() {
        return doubledownEV;
    }

    public double getStandEV() {
        return standEV;
    }

    public double getSplitEV() {
        return splitEV;
    }

    public double getSurrenderEV() {
        return surrenderEV;
    }

    public PlayStrategy getPlayStrategy() {
        return playStrategy;
    }

    public void setPlayStrategy(PlayStrategy playStrategy) {
        this.playStrategy = playStrategy;
    }

    private void dealDealerHand(Shoe shoe, RuleConfig ruleConfig) {
        while ((this.getValue() < 17) ||
                (this.getValue() == 17 && this.isSoft() && ruleConfig.hitsSoft17)) {
            this.hit(shoe.dealCard());
        }
    }
}
