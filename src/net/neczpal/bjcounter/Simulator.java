package net.neczpal.bjcounter;

import net.neczpal.bjcounter.core.Hand;
import net.neczpal.bjcounter.core.RuleConfig;
import net.neczpal.bjcounter.core.Table;
import net.neczpal.bjcounter.core.betstrategies.ConstantBet;
import net.neczpal.bjcounter.core.betstrategies.CountingBet;


public class Simulator {

    public static void main(String[] args) {

        Table mainTable = new Table(RuleConfig.DEFAULT);
        mainTable.addHand(new Hand("CNSTNT_1", new ConstantBet(5.0)));
        mainTable.addHand(new Hand("HI_LO_0_25",
                new CountingBet(100.0,
                        mainTable.getShoe(),
                        0, 24,
                        CountingBet.HI_LO_COUNT)));
        mainTable.addHand(new Hand("OMEGA_0_25",
                new CountingBet(100.0,
                        mainTable.getShoe(),
                        0, 24,
                        CountingBet.OMEGA_2_COUNT)));
        mainTable.addHand(new Hand("WONG_0_25",
                new CountingBet(100.0,
                        mainTable.getShoe(),
                        0, 24,
                        CountingBet.WONG_HALVES_COUNT)));


        for (int i = 0; i < 10000; i++) {
            mainTable.setWagers();
            mainTable.deal();
        }

        mainTable.writeCoins();


    }
}
