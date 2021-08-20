package de.energy.optimax.trading.bot.auctioneer;

/**
 * The enum Bid type.
 *
 * @author Smirnov Kirill
 */
public enum BidType {
    /**
     * Simple bid bid type.
     */
    SIMPLE_BID,
    /**
     * Add to your last bid bid type.
     */
    ADD_TO_YOUR_LAST_BID,
    /**
     * Add to his last bid bid type.
     */
    ADD_TO_HIS_LAST_BID,
    /**
     * Cash minus part bid type.
     */
    CASH_MINUS_PART,
    /**
     * Cash divide bid type.
     */
    CASH_DIVIDE
}
