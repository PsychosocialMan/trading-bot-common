package de.energy.optimax.trading.bot.data;

/**
 * The type Bid round. Round of the auction. Shows profit, all the bids, status, delta of the auction round.
 *
 * @author Smirnov Kirill
 */
public class BidRound {

    /**
     * The constant QUANTITY_PER_ROUND. Responsible for the number of played QU.
     */
    public static final int QUANTITY_PER_ROUND = 2;

    /**
     * The Your bid. Bid of the object creator.
     */
    private final int yourBid;
    /**
     * The Opponent bid.
     */
    private final int opponentBid;
    /**
     * The Bid status. Can be LOST, WON and DRAW.
     */
    public final Status bidStatus;

    /**
     * The Delta between your and opponent bids.
     */
    private final int delta;
    /**
     * The Profit of the round to the object creator.
     */
    private final int profit;

    /**
     * Instantiates a new Bid round.
     *
     * @param yourBid     the your bid
     * @param opponentBid the opponent bid
     */
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


    /**
     * Gets your bid (object creator's).
     *
     * @return the your bid
     */
    public int getYourBid() {
        return yourBid;
    }

    /**
     * Gets opponent bid.
     *
     * @return the opponent bid
     */
    public int getOpponentBid() {
        return opponentBid;
    }

    /**
     * Gets bid status.
     *
     * @return the bid status
     */
    public Status getBidStatus() {
        return bidStatus;
    }

    /**
     * Gets delta.
     *
     * @return the delta
     */
    public int getDelta() {
        return delta;
    }

    /**
     * Gets profit.
     *
     * @return the profit
     */
    public int getProfit() {
        return profit;
    }

    /**
     * The enum Status.
     *
     * @author Smirnov Kirill
     */
    public enum Status {
        /**
         * Won status.
         */
        WON,
        /**
         * Lost status.
         */
        LOST,
        /**
         * Draw status.
         */
        DRAW
    }

}
