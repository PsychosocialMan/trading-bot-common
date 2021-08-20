package de.energy.optimax.trading.bot.auctioneer;

import java.util.List;

/**
 * The type Mapping. Setters are used by Gson.
 *
 * @author Smirnov Kirill
 */
public class Mapping {

    /**
     * The Initial quantity.
     */
    private int initialQuantity;

    /**
     * The Initial cash.
     */
    private int initialCash;

    /**
     * The Stages.
     */
    private List<Stage> stages;

    /**
     * Gets initial quantity.
     *
     * @return the initial quantity
     */
    public int getInitialQuantity() {
        return initialQuantity;
    }

    /**
     * Sets initial quantity.
     *
     * @param initialQuantity the initial quantity
     */
    public void setInitialQuantity(int initialQuantity) {
        this.initialQuantity = initialQuantity;
    }

    /**
     * Gets initial cash.
     *
     * @return the initial cash
     */
    public int getInitialCash() {
        return initialCash;
    }

    /**
     * Sets initial cash.
     *
     * @param initialCash the initial cash
     */
    public void setInitialCash(int initialCash) {
        this.initialCash = initialCash;
    }

    /**
     * Gets stages.
     *
     * @return the stages
     */
    public List<Stage> getStages() {
        return stages;
    }

    /**
     * Sets stages.
     *
     * @param stages the stages
     */
    public void setStages(List<Stage> stages) {
        this.stages = stages;
    }

    /**
     * The type Stage.
     *
     * @author Smirnov Kirill
     */
    public static class Stage {
        /**
         * The Type.
         */
        private BidType type;

        /**
         * The Value.
         */
        private int value;

        /**
         * Gets type.
         *
         * @return the type
         */
        public BidType getType() {
            return type;
        }

        /**
         * Sets type.
         *
         * @param type the type
         */
        public void setType(BidType type) {
            this.type = type;
        }

        /**
         * Gets value.
         *
         * @return the value
         */
        public int getValue() {
            return value;
        }

        /**
         * Sets value.
         *
         * @param value the value
         */
        public void setValue(int value) {
            this.value = value;
        }
    }

}
