package net.neczpal.bjcounter.core.betstrategies;

public class ConstantBet implements BetStrategy{

    private double betSize;

    public ConstantBet(double betSize) {
        this.betSize = betSize;
    }

    public double getBetSize() {
        return betSize;
    }

    @Override
    public double calculateNextBet() {
        return betSize;
    }
}
