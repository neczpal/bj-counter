import cards.Shoe;
import core.Hand;
import core.RuleConfig;
import core.Table;
import core.betstrategies.ConstantBet;
import core.betstrategies.CountingBet;

public class Main {

    public static void main(String[] args)
    {
        Shoe mainShoe = new Shoe(8);
//
//
//        //TEST Remove
//        if (mainShoe.removeCard(new Card(CardValue._2, SuitValue.CLUBS))) {
//            System.out.println("kaki");
//        }else{
//            System.out.println("kuki");
//        }
//
//        //TEST Shuffle
//
//        mainShoe.shuffleCards();

        Table mainTable = new Table(RuleConfig.DEFAULT);
        mainTable.addHand(new Hand("CNSTNT_1a", new ConstantBet(5.0)));
        mainTable.addHand(new Hand("CNSTNT_1b", new ConstantBet(5.0)));
        mainTable.addHand(new Hand("CNSTNT_1c", new ConstantBet(5.0)));
        mainTable.addHand(new Hand("HI_LO_0_25",
                new CountingBet(5.0,
                        mainTable.getShoe(),
                        1, 15,
                        CountingBet.HI_LO_COUNT)));
//        mainTable.addHand(new Hand("HI_LO_0_12",
//                new CountingBet(1.0,
//                        mainTable.getShoe(),
//                        0, 12,
//                        CountingBet.HI_LO_COUNT)));
//        mainTable.addHand(new Hand("HI_LO_0_30",
//                new CountingBet(1.0,
//                        mainTable.getShoe(),
//                        0, 30,
//                        CountingBet.HI_LO_COUNT)));
        mainTable.addHand(new Hand("OMEGA_0_25",
                new CountingBet(5.0,
                        mainTable.getShoe(),
                        1, 15,
                        CountingBet.OMEGA_2_COUNT)));
//        mainTable.addHand(new Hand("OMEGA_1_12",
//                new CountingBet(1.0,
//                        mainTable.getShoe(),
//                        1, 12,
//                        CountingBet.OMEGA_2_COUNT)));
//        mainTable.addHand(new Hand("OMEGA_1_30",
//                new CountingBet(1.0,
//                        mainTable.getShoe(),
//                        1, 30,
//                        CountingBet.OMEGA_2_COUNT)));
        mainTable.addHand(new Hand("WONG_0_25",
                new CountingBet(5.0,
                        mainTable.getShoe(),
                        1, 15,
                        CountingBet.WONG_HALVES_COUNT)));
//        mainTable.addHand(new Hand("WONG_1_12",
//                new CountingBet(1.0,
//                        mainTable.getShoe(),
//                        1, 12,
//                        CountingBet.WONG_HALVES_COUNT)));
//        mainTable.addHand(new Hand("WONG_1_30",
//                new CountingBet(1.0,
//                        mainTable.getShoe(),
//                        1, 30,
//                        CountingBet.WONG_HALVES_COUNT)));


        for(int i=0;i <200; i++) {
            mainTable.setWagers();
            mainTable.deal();
        }

        mainTable.writeCoins();
//        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
//        String command = "lovacska";
//
//        while(!command.equals("x")){
//            try {
//                command = br.readLine();
//
//                if(command.equals("rs")){
//                    mainShoe.resetShoe(8);
//                    continue;
//                }
//                String[] cardNames = command.split(" ");
//                List<Card> cards = new ArrayList<>();
//
//                for(String cardName : cardNames) {
//                    Card card = new Card(CardValue.toCardValue(cardName), SuitValue.ANY);
//                    if(card.getValue() == null)
//                        throw new Exception("Helytelen formatum");
//
//                    cards.add(card);
//                }
//
//                for(Card card : cards) {
//                    mainShoe.dealSpecificCard(card);
//                }
//
//                System.out.println("\n\n");
//                System.out.printf("Hi-Lo: %.2f Omega: %.2f Wong: %.2f\n",
//                        mainShoe.getHiLoTrueCount(),
//                        mainShoe.getOmegaTrueCount(),
//                        mainShoe.getWongHalvesTrueCount());
//            } catch (Exception ex){
//                System.err.println(ex.getMessage());
//            }
//        }

    }
}
