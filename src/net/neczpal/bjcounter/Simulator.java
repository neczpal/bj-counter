package net.neczpal.bjcounter;

import net.neczpal.bjcounter.core.Hand;
import net.neczpal.bjcounter.core.RuleConfig;
import net.neczpal.bjcounter.core.Table;
import net.neczpal.bjcounter.core.betstrategies.ConstantBet;
import net.neczpal.bjcounter.core.betstrategies.CountingBet;
import net.neczpal.bjcounter.countings.CountingType;


public class Simulator {

    public static void main(String[] args) {

        Table mainTable = new Table(RuleConfig.DEFAULT);
        mainTable.addHand(new Hand("CNSTNT_1", new ConstantBet(5.0)));


        mainTable.addHand(new Hand("HI_LO_0_25",
                new CountingBet(100.0,
                        mainTable.getShoe(),
                        0, 24,
                        CountingType.HI_LO)));
        mainTable.addHand(new Hand("OMEGA_0_25",
                new CountingBet(100.0,
                        mainTable.getShoe(),
                        0, 24,
                        CountingType.OMEGA_2)));
        mainTable.addHand(new Hand("WONG_0_25",
                new CountingBet(100.0,
                        mainTable.getShoe(),
                        0, 24,
                        CountingType.WONG_HALVES)));


        for (int i = 0; i < 100000; i++) {
            mainTable.setWagers();
            mainTable.deal();
        }

        mainTable.writeCoins();


    }
}
