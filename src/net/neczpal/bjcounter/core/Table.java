package net.neczpal.bjcounter.core;

import net.neczpal.bjcounter.cards.Shoe;

import java.util.ArrayList;
import java.util.List;

public class Table {

    private RuleConfig config;

    private List<Hand> hands;
    private List<Hand> sitOutHands;
    private Hand dealerHand;

    private Shoe shoe;
    private BasicStrategy basicStrategy;


    public double TTL = 0;
    public double CNT = 0;

    public Table(RuleConfig config) {
        this(config, new ArrayList<>());
    }

    public Table(RuleConfig config, List<Hand> hands) {
        this.config = config;
        this.hands = hands;
        this.sitOutHands = new ArrayList<>();
        this.basicStrategy = new BasicStrategy(config);

        this.dealerHand = new Hand("Dealer");
        this.shoe = new Shoe(config.numberOfDecks);
    }

    public Shoe getShoe() {
        return shoe;
    }

    private void resetShoe() {
        this.shoe.resetShoe();
    }

    public void addHand(String handName) {
        hands.add(new Hand(handName));
    }

    public void addHand(Hand hand) {
        hands.add(hand);
    }
    public void setWagers() {
        for (Hand hand : hands) {
            hand.setWagerBet();
            if(!hand.isPlaying()){
                sitOutHands.add(hand);
            }
        }
        hands.removeAll(sitOutHands);
    }
    public void deal() {
        if (shoe.getCardsLeftCount() / 52 < config.cutDeck) {
            resetShoe();
        }

        if (hands.isEmpty()) {
            System.out.println("Nincs ember az asztalnal");
        } else {
            for(int i= 0; i < 2; ++i) {
                for(Hand hand : hands) {
                    hand.hit(shoe.dealCard());
                }
                if(i==0)
                    dealerHand.hit(shoe.dealCard());
            }

            //Hand strategy

            for(Hand hand : hands) {
                hand.calcWinningChances(dealerHand, shoe, config);

                TTL += hand.getBestEV();
                CNT += 1.0;

                while(!hand.isStanding()) {

                    hand.calcWinningChances(dealerHand, shoe, config);


                    double bestEV = hand.getBestEV();
                    Action bestEVAction = hand.getBestEVAction();
                    Action basicStrategyAction = basicStrategy.getNextAction(hand, dealerHand.getCard(0));


                    if(bestEVAction != basicStrategyAction) {
                        System.out.printf("%s\t%s\tBEST:\t%.4f\tHIT:\t%.4f\tSTND:\t%.4f\tDBL:\t%.4f\tSPLT:\t%.4f\tSUR:%.4f",
                                hand.getHandName(),
                                hand.getCardsString(),
                                bestEV,
                                hand.getHitEV() < -100 ? 0 : hand.getHitEV(),
                                hand.getStandEV(),
                                hand.getDoubledownEV() < -100 ? 0 : hand.getDoubledownEV(),
                                hand.getSplitEV () < -100 ? 0 : hand.getSplitEV (),
                                hand.getSurrenderEV () < -100 ? 0 : hand.getSurrenderEV ());
                        System.out.printf(" Changed from basic strat: %s ---> %s\n", basicStrategyAction, bestEVAction);
                        shoe.writeAllCards();
                    }

                    Action nextAction = Action.STAND;

                    switch ( hand.getPlayStrategy()) {
                        case BASIC_STRAT:
                            nextAction = basicStrategy.getNextAction(hand, dealerHand.getCard(0));
                            break;
                        case BEST_EV:
                            nextAction = bestEVAction;
                            break;
                        case PLAYER_INPUT://#TODO
                            break;
                    }

                    switch (nextAction) {
                        case HIT:
                            System.out.println("HIT " + hand.getValue());
                            hand.hit(shoe.dealCard());
                            break;
                        case DOUBLE_DOWN:
                            System.out.println("DOUBLE_DOWN " + hand.getValue());
                            hand.doubleDown(shoe.dealCard());
                            break;
                        case HIT_SPLIT_ACE:
                            System.out.println("HIT_SPLIT_ACE " + hand.getValue());
                            hand.hitOnce(shoe.dealCard());
                            break;
                        case SPLIT:
                            System.out.println("SPLIT " + hand.getValue());
                            hand.split(shoe.dealCard(), shoe.dealCard());
                            break;
                        case STAND:
                            System.out.println("STAND " + hand.getValue());
                            hand.stand();
                            break;
                        case SURRENDER:
                            System.out.println("SURRENDER " + hand.getValue());
                            hand.surrender();
                            break;
                    }
                }
                System.out.println("STOOD ON " + hand.getValue());
            }
            System.out.println(dealerHand.getHandName() + " ["+dealerHand.getCardsString()+"]");


            while ((dealerHand.getValue() < 17) ||
                    (dealerHand.getValue() == 17 && dealerHand.isSoft() && config.hitsSoft17)) {
                dealerHand.hit(shoe.dealCard());
            }
//            System.out.println("DEALER HAS " + dealerHand.getValue());


            for(Hand hand : hands) {
                hand.match(dealerHand.getValue(), dealerHand.isBusted(), dealerHand.isBj());
                System.out.println(hand);
            }
            System.out.println(dealerHand);
        }

        hands.addAll(sitOutHands);
        sitOutHands.clear();

        for(Hand hand : hands) {
            hand.clearHand();
        }
//        System.out.println();

        dealerHand.clearHand();
    }

    public void writeCoins() {
        for(Hand hand : hands) {
            System.out.println(hand.getHandName() + "(" + hand.getCoins() + ")");
        }
        System.out.println();
    }

}
