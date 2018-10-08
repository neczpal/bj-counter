package core;

import cards.Shoe;

import java.util.ArrayList;
import java.util.List;

public class Table {

    private RuleConfig config;

    private List<Hand> hands;
    private List<Hand> sitOutHands;
    private Hand dealerHand;

    private Shoe shoe;
    private HandStrategy handStrategy;


    public Table(RuleConfig config) {
        this(config, new ArrayList<>());
    }

    public Table(RuleConfig config, List<Hand> hands) {
        this.config = config;
        this.hands = hands;
        this.sitOutHands = new ArrayList<>();
        this.handStrategy = new HandStrategy(config);

        this.dealerHand = new Hand("Dealer");
        this.shoe = new Shoe(config.numberOfDecks);
    }

    public Shoe getShoe() {
        return shoe;
    }

    private void resetShoe() {
        this.shoe.resetShoe(config.numberOfDecks);
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
        if(shoe.getCardsLeftCount() / 52 < 4){
            resetShoe();
        }

        if (hands.isEmpty()) {
            System.out.println("Nincs ember az asztalnal");
        } else {
            for(int i= 0; i < 2; ++i) {
                for(Hand hand : hands) {
                    hand.hit(shoe.dealCard());
                }

                dealerHand.hit(shoe.dealCard());
            }

            //Hand strategy

            for(Hand hand : hands) {
                while(!hand.isStanding()) {
                    switch (handStrategy.getNextAction(hand, dealerHand.getCard(0))) {
                        case HIT:
//                            System.out.println("HIT " + hand.getValue());
                            hand.hit(shoe.dealCard());
                            break;
                        case DOUBLE_DOWN:
//                            System.out.println("DOUBLE_DOWN " + hand.getValue());
                            hand.doubleDown(shoe.dealCard());
                            break;
                        case HIT_SPLIT_ACE:
//                            System.out.println("HIT_SPLIT_ACE " + hand.getValue());
                            hand.hitOnce(shoe.dealCard());
                            break;
                        case SPLIT:
//                            System.out.println("SPLIT " + hand.getValue());
                            hand.split();
                            break;
                        case STAND:
//                            System.out.println("STAND " + hand.getValue());
                            hand.stand();
                            break;
                        case SURRENDER:
//                            System.out.println("SURRENDER " + hand.getValue());
                            hand.surrender();
                            break;
                    }
                }
//                System.out.println("STOOD ON " + hand.getValue());
            }

            while ((dealerHand.getValue() < 17) ||
                    (dealerHand.getValue() == 17 && dealerHand.isSoft() && config.hitsSoft17)) {
                dealerHand.hit(shoe.dealCard());
            }
//            System.out.println("DEALER HAS " + dealerHand.getValue());


            for(Hand hand : hands) {
                hand.match(dealerHand.getValue(), dealerHand.isBusted(), dealerHand.isBj());
//                System.out.println(hand);
            }
//            System.out.println(dealerHand);
        }

//        System.out.println("Hi-Lo:" +
//                shoe.getHiLoTrueCount());
//        System.out.println("Omega:" +
//                shoe.getOmegaTrueCount());
//        System.out.println("Wong:" + shoe.getWongHalvesTrueCount() + "\n");

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
