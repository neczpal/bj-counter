package net.neczpal.bjcounter;

import net.neczpal.bjcounter.cards.Card;
import net.neczpal.bjcounter.cards.CardValue;
import net.neczpal.bjcounter.cards.Shoe;
import net.neczpal.bjcounter.cards.SuitValue;
import net.neczpal.bjcounter.core.Hand;
import net.neczpal.bjcounter.core.RuleConfig;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CardCounter {

    public static void main(String[] args) {

        Shoe mainShoe = new Shoe(8);
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String command = "habzsibabzsi";

        while (!command.equals("x")) {
            try {
                command = br.readLine();

                if (command.equals("rs")) {
                    mainShoe.resetShoe();
                    continue;
                }

                if (command.startsWith("chance")) {
                    String[] cards = command.split(" ");
                    Hand myHand = new Hand("MyHand");
                    Hand dealerHand = new Hand("Dealer");


                    for(int i = 1; i < cards.length; i++) {
                        Card card = new Card(CardValue.toCardValue(cards[i].toUpperCase()), SuitValue.ANY);
                        if (card.getValue() == null)
                            throw new Exception("Invalid format");

                        myHand.hit(card);
                    }

                    String dealerCardString = cards[0].substring(cards[0].length() - 1);

                    Card dealerCard = new Card(CardValue.toCardValue(dealerCardString.toUpperCase()), SuitValue.ANY);

                    if (dealerCard.getValue() == null)
                        throw new Exception("Invalid format");

                    dealerHand.hit(dealerCard);

                    myHand.calcWinningChances(dealerHand, mainShoe, RuleConfig.DEFAULT);

                    System.out.printf("%s\t%s\tBEST:\t%s(%.4f)\tHIT:\t%.4f\tSTND:\t%.4f\tDBL:\t%.4f\tSPLT:\t%.4f\tSUR:%.4f\n",
                            myHand.getHandName(),
                            myHand.getCardsString(),
                            myHand.getBestEVAction(),
                            myHand.getBestEV(),
                            myHand.getHitEV() < -100 ? 0 : myHand.getHitEV(),
                            myHand.getStandEV(),
                            myHand.getDoubledownEV() < -100 ? 0 : myHand.getDoubledownEV(),
                            myHand.getSplitEV () < -100 ? 0 : myHand.getSplitEV (),
                            myHand.getSurrenderEV () < -100 ? 0 : myHand.getSurrenderEV ());

                    continue;
                }

                String[] cardNames = command.split(" ");
                List<Card> cards = new ArrayList<>();

                for (String cardName : cardNames) {
                    Card card = new Card(CardValue.toCardValue(cardName.toUpperCase()), SuitValue.ANY);
                    if (card.getValue() == null)
                        throw new Exception("Invalid format");

                    cards.add(card);
                }

                for (Card card : cards) {
                    mainShoe.dealSpecificCard(card);
                }

                mainShoe.writeAllCards();
            } catch (Exception ex) {
                System.err.println(ex.getMessage());
            }
        }
    }
}
