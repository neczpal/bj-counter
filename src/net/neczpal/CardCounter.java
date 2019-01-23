package net.neczpal;

import net.neczpal.cards.Card;
import net.neczpal.cards.CardValue;
import net.neczpal.cards.Shoe;
import net.neczpal.cards.SuitValue;

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
                String[] cardNames = command.split(" ");
                List<Card> cards = new ArrayList<>();

                for (String cardName : cardNames) {
                    Card card = new Card(CardValue.toCardValue(cardName), SuitValue.ANY);
                    if (card.getValue() == null)
                        throw new Exception("Helytelen formatum");

                    cards.add(card);
                }

                for (Card card : cards) {
                    mainShoe.dealSpecificCard(card);
                }

//                System.out.println("\n");
                mainShoe.writeAllCards();
                System.out.printf("Hi-Lo: %.2f Omega: %.2f Wong: %.2f Aces: %.3f Cards: %d Ins: %b\n",
                        mainShoe.getHiLoTrueCount(),
                        mainShoe.getOmegaTrueCount(),
                        mainShoe.getWongHalvesTrueCount(),
                        mainShoe.getAcesCount(),
                        mainShoe.getCardsLeftCount(),
                        mainShoe.isInsuranceWorthIt());
            } catch (Exception ex) {
                System.err.println(ex.getMessage());
            }
        }
    }
}
