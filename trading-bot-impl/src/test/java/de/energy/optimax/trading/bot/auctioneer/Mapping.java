package de.energy.optimax.trading.bot.auctioneer;

import java.util.List;

public class Mapping {

    private int initialQuantity;

    private int initialCash;

    private List<Stage> stages;

    public int getInitialQuantity() {
        return initialQuantity;
    }

    public void setInitialQuantity(int initialQuantity) {
        this.initialQuantity = initialQuantity;
    }

    public int getInitialCash() {
        return initialCash;
    }

    public void setInitialCash(int initialCash) {
        this.initialCash = initialCash;
    }

    public List<Stage> getStages() {
        return stages;
    }

    public void setStages(List<Stage> stages) {
        this.stages = stages;
    }

    public static class Stage {
        private BidType type;

        private int value;

        public BidType getType() {
            return type;
        }

        public void setType(BidType type) {
            this.type = type;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }
    }

}
