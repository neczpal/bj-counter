package core;

import cards.Shoe;

import java.util.ArrayList;
import java.util.List;

public class Table {

    private RuleConfig config;

    private List<Hand> hands;
    private Hand dealerHand;

    private Shoe shoe;
    private HandStrategy handStrategy;


    public Table(RuleConfig config) {
        this(config, new ArrayList<>());
    }

    public Table(RuleConfig config, List<Hand> hands) {
        this.config = config;
        this.hands = hands;
        this.handStrategy = new HandStrategy(config);

        this.dealerHand = new Hand("Dealer");
        newShoe();
    }

    private void newShoe() {
        this.shoe = new Shoe(config.numberOfDecks);
        shoe.shuffleCards();
        System.out.println("New Shoe");
    }

    public void addHand(String handname) {
        hands.add(new Hand(handname));
    }

    public void deal() {
        if(shoe.getCardsLeftCount() / 52 < 3){
            newShoe();
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
                System.out.println(hand);
            }
            System.out.println(dealerHand);
        }

        System.out.println("CL:" +
                shoe.getCardsLeftCount());
        System.out.println("RC:" +
                shoe.getRunningCount());

        System.out.println("TC:" + shoe.getTrueCount() + "\n");

        for(Hand hand : hands) {
            System.out.println(hand.getHandName() + "(" + hand.getCoins() + ")");
            hand.clearHand();
        }
        System.out.println();

        dealerHand.clearHand();
    }

}
