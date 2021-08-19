package de.energy.optimax.trading.bot.data;

public class BidRound {

    public static final int QUANTITY_PER_ROUND = 2;

    private final int yourBid;
    private final int opponentBid;
    public final Status bidStatus;

    private final int delta;
    private final int profit;

    public BidRound(int yourBid, int opponentBid) {
        this.yourBid = yourBid;
        this.opponentBid = opponentBid;

        if (yourBid == opponentBid) {
            this.bidStatus = Status.DRAW;
            this.profit = QUANTITY_PER_ROUND / 2;
        } else if (yourBid > opponentBid) {
            this.bidStatus = Status.WON;
            this.profit = QUANTITY_PER_ROUND;
        } else {
            this.bidStatus = Status.LOST;
            this.profit = 0;
        }

        this.delta = yourBid - opponentBid;
    }


    public int getYourBid() {
        return yourBid;
    }

    public int getOpponentBid() {
        return opponentBid;
    }

    public Status getBidStatus() {
        return bidStatus;
    }

    public int getDelta() {
        return delta;
    }

    public int getProfit() {
        return profit;
    }

    public enum Status {
        WON, LOST, DRAW
    }

}
