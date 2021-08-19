package de.energy.optimax.trading.bot.auctioneer;

import auction.Bidder;
import com.google.gson.Gson;
import de.energy.optimax.trading.bot.data.BidRound;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.Objects;


public class TestBidder implements Bidder {

    private final Mapping mapping;
    private final LinkedList<BidRound> rounds = new LinkedList<>();
    private int indexCounter = 0;
    private int cashCounter = getInitialCash();

    public TestBidder(Path filePath) throws IOException {
        this.mapping = fileToMapping(filePath);
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
        if (bidValue <= cashCounter) {
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

    private Mapping fileToMapping(Path filePath) throws IOException {
        return new Gson().fromJson(Files.readString(filePath), Mapping.class);
    }

    public int getInitialQuantity() {
        return this.mapping.getInitialQuantity();
    }

    public int getInitialCash() {
        return Objects.requireNonNull(this.mapping).getInitialCash();
    }
}
