package net.neczpal.bjcounter.core;

import net.neczpal.bjcounter.cards.Card;
import net.neczpal.bjcounter.cards.Shoe;

import java.util.Random;

public class BasicStrategy {

    private RuleConfig config;

    public BasicStrategy() {
        this(RuleConfig.DEFAULT);
    }

    public BasicStrategy(RuleConfig config) {
        this.config = config;
    }

    public Action getNextAction(Hand currentHand, Card dealerCard) {
        int card1Val = currentHand.getCard(0).getValue().value;
        int numOfCards = currentHand.getCardCount();
        int handSum = currentHand.getValue();
        boolean soft = currentHand.isSoft();
        int dealerCardVal = dealerCard.getValue().value;


        if (currentHand.isPair() && currentHand.splitNumber() < config.splitX) {
            switch (card1Val) {
                case 2:
                case 3:
                    if ((dealerCardVal >= 4 || config.doubleAfterSplit) && dealerCardVal <= 7)
                        return Action.SPLIT;
                    else
                        return Action.HIT;
                case 4:
                    if (config.doubleAfterSplit && (dealerCardVal == 5 || dealerCardVal == 6))
                        return Action.SPLIT;
                    else
                        return Action.HIT;
                case 5:
                    if ((currentHand.isStartingHand() || (config.doubleAfterSplit && numOfCards == 2))
                            && dealerCardVal <= 9)
                        return Action.DOUBLE_DOWN;
                    else
                        return Action.HIT;
                case 6:
                    if ((config.doubleAfterSplit || dealerCardVal >= 3) && dealerCardVal <= 6) {
                        return Action.SPLIT;
                    } else {
                        return Action.HIT;
                    }
                case 7:
                    if (dealerCardVal <= 7)
                        return Action.SPLIT;
                    else if (!config.dealerPeeks && config.surrenderOptions >= 1 && dealerCardVal == 10 && currentHand.isStartingHand())
                        return Action.SURRENDER;
                    else
                        return Action.HIT;
                case 8:
                    if (dealerCardVal <= 9)
                        return Action.SPLIT;
                    else if (!config.dealerPeeks && config.surrenderOptions >= 1 && dealerCardVal == 10 && currentHand.isStartingHand())
                        return Action.SURRENDER;
                    else
                        return Action.HIT;
                case 9:
                    if (dealerCardVal <= 9 && dealerCardVal != 7) {
                        return Action.SPLIT;
                    } else {
                        return Action.STAND;
                    }
                case 10:
                    return Action.STAND;
                case 11:
                    if (dealerCardVal <= 10 || config.dealerPeeks)
                        return Action.SPLIT;
                    else
                        return Action.HIT;
                default:
                    System.out.println("Egy kartya erteke nem megfelelo, megallunk");
                    return Action.STAND;
            }

        } else if (soft) {
            if (handSum >= 20) {
                return Action.STAND;
            } else if (handSum == 19) {
                if ((currentHand.isStartingHand() || (config.doubleAfterSplit && numOfCards == 2)) &&
                        config.doubleDownOptions == 0 &&
                        dealerCardVal == 6 && config.hitsSoft17) {
                    return Action.DOUBLE_DOWN;
                } else {
                    return Action.STAND;
                }
            } else if (handSum == 18) {
                if ((currentHand.isStartingHand() || (config.doubleAfterSplit && numOfCards == 2)) &&
                        config.doubleDownOptions == 0 &&
                        dealerCardVal <= 6) {
                    return Action.DOUBLE_DOWN;
                } else if (dealerCardVal <= 8) {
                    return Action.STAND;
                } else {
                    return Action.HIT;
                }
            } else if (handSum == 17) {
                if ((currentHand.isStartingHand() || (config.doubleAfterSplit && numOfCards == 2)) &&
                        config.doubleDownOptions == 0 &&
                        dealerCardVal <= 6 && dealerCardVal >= 3) {
                    return Action.DOUBLE_DOWN;
                } else {
                    return Action.HIT;
                }
            } else if (handSum >= 15) {
                if ((currentHand.isStartingHand() || (config.doubleAfterSplit && numOfCards == 2)) &&
                        config.doubleDownOptions == 0 &&
                        dealerCardVal <= 6 && dealerCardVal >= 4) {
                    return Action.DOUBLE_DOWN;
                } else {
                    return Action.HIT;
                }
            } else if (handSum >= 13) {
                if ((currentHand.isStartingHand() || (config.doubleAfterSplit && numOfCards == 2)) &&
                        config.doubleDownOptions == 0 &&
                        dealerCardVal <= 6 && dealerCardVal >= 5) {
                    return Action.DOUBLE_DOWN;
                } else {
                    return Action.HIT;
                }
            } else if (handSum == 11) {
                if (config.drawToSplitAces) {
                    return Action.HIT;
                } else {
                    return Action.HIT_SPLIT_ACE;
                }
            }
        } else {
            if (handSum >= 17) {
                return Action.STAND;
            } else if (handSum >= 16) {
                if (dealerCardVal <= 6) {
                    return Action.STAND;
                } else if (config.surrenderOptions >= 1 && currentHand.isStartingHand() && dealerCardVal >= 9 && dealerCardVal <= 10) {
                    return Action.SURRENDER;
                } else {
                    return Action.HIT;
                }
            } else if (handSum >= 15) {
                if (dealerCardVal <= 6) {
                    return Action.STAND;
                } else if (config.surrenderOptions >= 1 && currentHand.isStartingHand() && dealerCardVal == 10) {
                    return Action.SURRENDER;
                } else {
                    return Action.HIT;
                }
            } else if (handSum >= 14) {
                if (dealerCardVal <= 6) {
                    return Action.STAND;
                } else if (config.surrenderOptions >= 1 && currentHand.isStartingHand() && dealerCardVal == 10 && !config.dealerPeeks) {
                    return Action.SURRENDER;
                } else {
                    return Action.HIT;
                }
            } else if (handSum >= 13) {
                if (dealerCardVal <= 6) {
                    return Action.STAND;
                } else {
                    return Action.HIT;
                }
            } else if (handSum == 12) {
                if (dealerCardVal >= 4 && dealerCardVal <= 6) {
                    return Action.STAND;
                } else {
                    return Action.HIT;
                }
            } else if (handSum == 11) {
                if (numOfCards == 2 && (dealerCardVal <= 9 ||
                        (dealerCardVal <= 10 && config.dealerPeeks) ||
                        config.dealerPeeks && config.hitsSoft17)) {
                    return Action.DOUBLE_DOWN;
                } else {
                    return Action.HIT;
                }
            } else if (handSum == 10) {
                if ((currentHand.isStartingHand() || (config.doubleAfterSplit && numOfCards == 2))
                        && dealerCardVal <= 9) {
                    return Action.DOUBLE_DOWN;
                } else {
                    return Action.HIT;
                }
            } else if (handSum == 9) {
                if ((currentHand.isStartingHand() || (config.doubleAfterSplit && numOfCards == 2))
                        && config.doubleDownOptions <= 1
                        && dealerCardVal <= 6 && dealerCardVal >= 3) {
                    return Action.DOUBLE_DOWN;
                } else {
                    return Action.HIT;
                }
            } else {
                return Action.HIT;
            }
        }
        System.out.println("Nem kene ennek tortennie: getNextAction");
        return Action.STAND;
    }

}
