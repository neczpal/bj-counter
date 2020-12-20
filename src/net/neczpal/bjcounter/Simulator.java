package net.neczpal.bjcounter;

import net.neczpal.bjcounter.core.Hand;
import net.neczpal.bjcounter.core.PlayStrategy;
import net.neczpal.bjcounter.core.RuleConfig;
import net.neczpal.bjcounter.core.Table;
import net.neczpal.bjcounter.core.betstrategies.ConstantBet;
import net.neczpal.bjcounter.core.betstrategies.CountingBet;
import net.neczpal.bjcounter.countings.CountingType;


public class Simulator {

    public static void main(String[] args) {
        
        Table mainTable = new Table(RuleConfig.DEFAULT);

        mainTable.addHand(new Hand("CNSTNT", new ConstantBet(5), PlayStrategy.BASIC_STRAT));

        for (CountingType countingType : CountingType.values()) {
            mainTable.addHand(new Hand(countingType.toString(),
                    new CountingBet(10,
                            mainTable.getShoe(),
                            0, 25,
                            countingType),
                    PlayStrategy.BEST_EV));
        }



        for (int i = 0; i < 100; i++) {
            System.out.println(i);
            mainTable.setWagers();
            mainTable.deal();
        }

        mainTable.writeCoins();

        System.out.printf("Total: %f , Avg: %f, Count: %d", mainTable.TTL, mainTable.TTL / mainTable.CNT, (int)mainTable.CNT);

    }
}
