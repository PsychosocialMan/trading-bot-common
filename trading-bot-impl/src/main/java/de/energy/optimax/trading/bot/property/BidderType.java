package de.energy.optimax.trading.bot.property;

/**
 * The enum Bidder type. Defines the Bidder type.
 *
 * @author Smirnov Kirill
 */
public enum BidderType {
    /**
     * Mixed bidder type.
     */
    MIXED,
    /**
     * Neural bidder type.
     */
    NEURAL,
    /**
     * Game theory bidder type.
     */
    GAME_THEORY,
    /**
     * Dummy bidder type.
     */
    DUMMY;

    BidderType() {
    }
}
