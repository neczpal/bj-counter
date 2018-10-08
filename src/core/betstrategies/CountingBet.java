package core.betstrategies;

import cards.Shoe;

public class CountingBet extends ConstantBet {

    private Shoe shoe;
    private int minBetSpread;
    private int maxBetSpread;
    private int countingStrategy = 0;

    public static final int HI_LO_COUNT=0, OMEGA_2_COUNT=1, WONG_HALVES_COUNT = 2;

    public CountingBet(double betSize, Shoe shoe, int minBetSpread, int maxBetSpread) {
        this(betSize, shoe, minBetSpread, maxBetSpread, HI_LO_COUNT);
    }

    public CountingBet(double betSize, Shoe shoe, int minBetSpread, int maxBetSpread, int countingStrategy) {
        super(betSize);
        this.shoe = shoe;
        this.minBetSpread = minBetSpread;
        this.maxBetSpread = maxBetSpread;
        this.countingStrategy = countingStrategy;
    }

    @Override
    public double calculateNextBet() {
        double trueCount;
        switch (countingStrategy){
            case HI_LO_COUNT:
                trueCount = shoe.getHiLoTrueCount();
                break;
            case OMEGA_2_COUNT:
                trueCount = shoe.getOmegaTrueCount();
                break;
            case WONG_HALVES_COUNT:
                trueCount = shoe.getWongHalvesTrueCount();
                break;
            default:
                trueCount = shoe.getHiLoTrueCount();
        }

        int roundedTrueCount = (int)Math.floor(trueCount);

        double betSize = getBetSize();

        return Math.max(betSize * minBetSpread, Math.min(betSize * (roundedTrueCount - 1), betSize * maxBetSpread));
    }
}
