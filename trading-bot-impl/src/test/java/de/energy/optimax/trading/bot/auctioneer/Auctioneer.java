package de.energy.optimax.trading.bot.auctioneer;

import auction.Bidder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.context.annotation.Scope;

import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.IntStream;

/**
 * The type Auctioneer.
 *
 * @author Smirnov Kirill
 */
@TestComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class Auctioneer {

    /**
     * The Bidder.
     */
    private final Bidder bidder;

    /**
     * The Test bidder.
     */
    private TestBidder testBidder;

    /**
     * The Initial quantity.
     */
    private int initialQuantity = 0;

    /**
     * The Bidder quantity.
     */
    private int bidderQuantity = 0;
    /**
     * The Bidder cash.
     */
    private int bidderCash = 0;

    /**
     * The Test bidder quantity.
     */
    private int testBidderQuantity = 0;
    /**
     * The Test bidder cash.
     */
    private int testBidderCash = 0;

    /**
     * The Initial cash.
     */
    private int initialCash = 0;

    /**
     * Instantiates a new Auctioneer.
     *
     * @param bidder the bidder
     */
    @Autowired
    public Auctioneer(Bidder bidder) {
        this.bidder = bidder;
    }

    /**
     * Init.
     *
     * @param testBidderFilePath the test bidder file path
     *
     * @throws IOException the io exception
     */
    public void init(Path testBidderFilePath) throws IOException {
        this.testBidder = new TestBidder(testBidderFilePath);
        this.initialCash = this.testBidder.getInitialCash();
        this.bidderCash = this.initialCash;
        this.testBidderCash = this.initialCash;

        this.initialQuantity = this.testBidder.getInitialQuantity();
        this.bidderQuantity = this.initialQuantity;
        this.testBidderQuantity = this.initialQuantity;
    }

    /**
     * Is our bidder won boolean.
     *
     * @return the boolean
     */
    public boolean isOurBidderWon() {
        return bidderQuantity > testBidderQuantity ||
                (bidderQuantity == testBidderQuantity && bidderCash >= testBidderCash);
    }

    /**
     * Play.
     */
    public void play() {
        bidder.init(initialQuantity, initialCash);
        // No init called in testBidder because of it's empty body.

        IntStream.range(0, initialQuantity / 2)
                .forEach(index -> playRound());
    }

    /**
     * Play round.
     */
    private void playRound() {
        var futureBidderBid = CompletableFuture.supplyAsync(bidder::placeBid);

        // No need to make dedicated thread because of method's simplicity.
        var testBidderBid = testBidder.placeBid();

        try {
            var bidderBid = futureBidderBid.get();

            bidderCash -= bidderBid;
            testBidderCash -= testBidderBid;

            if (testBidderBid > bidderBid) {
                testBidderQuantity += 2;
            } else if (testBidderBid < bidderBid) {
                bidderQuantity += 2;
            } else {
                testBidderQuantity += 1;
                bidderQuantity += 1;
            }

            bidder.bids(bidderBid, testBidderBid);
            testBidder.bids(testBidderBid, bidderBid);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}
