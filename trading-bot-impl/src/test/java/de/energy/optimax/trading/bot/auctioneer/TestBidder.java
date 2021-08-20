package de.energy.optimax.trading.bot.auctioneer;

import auction.Bidder;
import com.google.gson.Gson;
import de.energy.optimax.trading.bot.data.BidRound;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.Objects;


/**
 * The type Test bidder. Gets his bids from mapping json.
 * <p>
 * See src/test/resources/bidders
 *
 * @author Smirnov Kirill
 */
public class TestBidder implements Bidder {

    /**
     * The Mapping.
     */
    private final Mapping mapping;
    /**
     * The Rounds.
     */
    private final LinkedList<BidRound> rounds = new LinkedList<>();
    /**
     * The Index counter.
     */
    private int indexCounter = 0;
    /**
     * The Cash counter.
     */
    private int cashCounter;

    /**
     * Instantiates a new Test bidder.
     *
     * @param filePath the file path
     *
     * @throws IOException the io exception
     */
    public TestBidder(Path filePath) throws IOException {
        this.mapping = fileToMapping(filePath);
        this.cashCounter = getInitialCash();
    }

    @Override
    public void init(int quantity, int cash) {
        // Do nothing because initial Params was got from mapping.
    }

    @Override
    public int placeBid() {
        var bid = mapping.getStages().get(indexCounter);

        int bidValue;

        switch (bid.getType()) {
            case SIMPLE_BID:
                bidValue = bid.getValue();
                break;
            case CASH_DIVIDE:
                bidValue = cashCounter / bid.getValue();
                break;
            case CASH_MINUS_PART:
                bidValue = cashCounter - cashCounter / bid.getValue();
                break;
            case ADD_TO_HIS_LAST_BID:
                bidValue = rounds.getLast().getYourBid() + bid.getValue();
                break;
            case ADD_TO_YOUR_LAST_BID:
                bidValue = rounds.getLast().getOpponentBid() + bid.getValue();
                break;
            default:
                throw new UnsupportedOperationException("Type is not recognized");
        }
        if (bidValue >= cashCounter) {
            bidValue = cashCounter;
        }
        cashCounter -= bidValue;
        indexCounter++;
        return bidValue;
    }

    @Override
    public void bids(int own, int other) {
        rounds.add(new BidRound(own, other));
    }

    /**
     * File to mapping mapping.
     *
     * @param filePath the file path
     *
     * @return the mapping
     *
     * @throws IOException the io exception
     */
    private Mapping fileToMapping(Path filePath) throws IOException {
        return new Gson().fromJson(Files.readString(filePath), Mapping.class);
    }

    /**
     * Gets initial quantity.
     *
     * @return the initial quantity
     */
    public int getInitialQuantity() {
        return this.mapping.getInitialQuantity();
    }

    /**
     * Gets initial cash.
     *
     * @return the initial cash
     */
    public int getInitialCash() {
        return Objects.requireNonNull(this.mapping).getInitialCash();
    }
}
