package de.energy.optimax.trading.bot.bidder;

import auction.Bidder;
import de.energy.optimax.trading.bot.service.bidder.BidderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The type Abstract bidder. Abstract bidder contains logging, the simplest logic. All basic algorithms are delegated to
 * the service layer. For inherited classes, it is enough to pass the service to the constructor.
 *
 * @author Smirnov Kirill
 * @see BidderService
 */
public abstract class AbstractBidder implements Bidder {

    /**
     * The Logger.
     */
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * The Initial quantity (in QU).
     */
    private int initialQuantity;
    /**
     * The Initial cash (in MU).
     */
    private int initialCash;

    /**
     * The Bidder service. Main logic delegate.
     */
    private final BidderService bidderService;

    /**
     * Instantiates a new Abstract bidder.
     *
     * @param bidderService the bidder service
     */
    protected AbstractBidder(BidderService bidderService) {
        logger.info("AbstractBidder() - Creating bidder with service {}", bidderService.getClass());

        this.bidderService = bidderService;

        logger.info("AbstractBidder() - Bidder created successfully");
    }

    @Override
    public void init(int quantity, int cash) {
        logger.info("init() - Initialize bidder with quantity [{}] and cash [{}]", quantity, cash);

        this.initialQuantity = quantity;
        this.initialCash = cash;
        this.bidderService.init(quantity, cash);

        logger.info("init() - Initializing ended successfully");
    }

    @Override
    public int placeBid() {
        logger.info("placeBid() - Requested bid. Computing next bid...");

        int bid;
        if (this.initialQuantity <= 2) {
            bid = this.initialCash;
        } else {
            bid = this.bidderService.predictNextBid();
        }

        logger.info("placeBid() - Computing ended. Bid is [{}]", bid);
        return bid;
    }

    @Override
    public void bids(int own, int other) {
        if (own > other) {
            logger.info("bids() - Adding statistics to bidder. Opponent's bid was [{}]. It was lower than our [{}]. We won.", other, own);
        } else {
            logger.info("bids() - Adding statistics to bidder. Opponent's bid was [{}]. It was greater than our [{}]. He won.", other, own);
        }
        this.bidderService.addBidStatistics(own, other);

        if (logger.isInfoEnabled()) {
            logger.info("bids() - Remaining cash: [{}]. Won quantity: [{}]",
                    this.bidderService.getResultCash(),
                    this.bidderService.getResultQuantity());
        }
    }
}
