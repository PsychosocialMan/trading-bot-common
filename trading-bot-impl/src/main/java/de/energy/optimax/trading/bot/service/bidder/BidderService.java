package de.energy.optimax.trading.bot.service.bidder;

/**
 * The interface Bidder service. Performs logic of the bidder.
 *
 * @author Smirnov Kirill
 */
public interface BidderService {
    /**
     * Predicting next bid. Offers to bidder bid value.
     *
     * @return value in MU.
     */
    int predictNextBid();

    /**
     * Add bid statistics.
     *
     * @param ownBid      the own bid
     * @param opponentBid the opponent bid
     *
     * @see auction.Bidder#bids(int, int)
     */
    void addBidStatistics(int ownBid, int opponentBid);

    /**
     * Init the service.
     *
     * @param quantity the quantity (in QU)
     * @param cash     the cash (in MU)
     */
    void init(int quantity, int cash);

    /**
     * Gets result cash. Checks how much MU we have in the end.
     *
     * @return the result cash
     */
    int getResultCash();

    /**
     * Gets result quantity. Shows how much QU we have after auction.
     *
     * @return the result quantity
     */
    int getResultQuantity();
}
