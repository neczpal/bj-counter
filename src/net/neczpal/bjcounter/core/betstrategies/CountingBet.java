package net.neczpal.bjcounter.core.betstrategies;

import net.neczpal.bjcounter.cards.Shoe;
import net.neczpal.bjcounter.countings.CountingType;

public class CountingBet extends ConstantBet {

    private Shoe shoe;
    private int minBetSpread;
    private int maxBetSpread;
    private CountingType countingStrategy;


    public CountingBet(double betSize, Shoe shoe, int minBetSpread, int maxBetSpread, CountingType countingStrategy) {
        super(betSize);
        this.shoe = shoe;
        this.minBetSpread = minBetSpread;
        this.maxBetSpread = maxBetSpread;
        this.countingStrategy = countingStrategy;
    }

    @Override
    public double calculateNextBet() {
        double roundedCount = (int) shoe.getCount (countingStrategy);

        double betSize = getBetSize();

        return Math.max(betSize * minBetSpread, Math.min(betSize * (roundedCount - 1), betSize * maxBetSpread));
    }
}
