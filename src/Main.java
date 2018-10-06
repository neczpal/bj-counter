import core.RuleConfig;
import core.Table;

public class Main {

    public static void main(String[] args)
    {
//        Shoe mainShoe = new Shoe(8);
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
        mainTable.addHand("Abel");
        mainTable.addHand("Denes");
        mainTable.addHand("Matyi");
        mainTable.addHand("Izi");
        mainTable.addHand("Olcsi");
        mainTable.addHand("Mama");
        mainTable.addHand("Hangyas");

        for(int i=0;i <1000; i++)
            mainTable.deal();


    }
}
