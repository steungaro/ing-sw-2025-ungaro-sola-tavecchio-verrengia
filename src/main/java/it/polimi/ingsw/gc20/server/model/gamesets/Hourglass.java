package it.polimi.ingsw.gc20.server.model.gamesets;

/**
 * @author GC20
 * Represents an hourglass that can be turned to measure time.
 */
public class Hourglass {
    private int period;
    private int turned;
    private long timestamp;

    /**
     * Default constructor
     */
    public Hourglass(int period) {
        this.period = period;
        this.turned = 0;
    }

    /** Getter function for the period of the hourglass
     *
     * @return period of teh hourglass
     */
    public int getPeriod() {
        return period;
    }

    /** Getter function for the number of times the hourglass has been turned
     *
     * @return number of times the hourglass has been turned
     */
    public int getTurned() {
        return turned;
    }
    /** Setter function for the period of the hourglass
     *
     * @param period period of the hourglass
     */
    public void setPeriod(int period) {
        this.period = period;
    }

    /** Function to turn the hourglass
     *
     */
    public void turn() {
        this.turned++;
        this.timestamp = System.currentTimeMillis();
        this.initCountdown();
    }

    /** Function to get the remaining time of the hourglass
     *
     * @return remaining time of the hourglass
     */
    public int getRemainingTime() {
        return period - (System.currentTimeMillis() - timestamp) > 0 ? (int) (period - (System.currentTimeMillis() - timestamp) / 1000) : 0;
    }

    /** Function to get the total elapsed time of the hourglass
     *
     * @return total elapsed time of the hourglass
     */
    public int getTotalElapsed() {
        return period * (turned + 1) - getRemainingTime();
    }

    /** Function to start the countdown of the hourglass
     *
     */
    public void initCountdown() {
        timestamp = System.currentTimeMillis();
    }

    /** Getter function for the timestamp of the hourglass
     *
     * @return timestamp of the hourglass
     */
    public long getTimestamp() {
        return timestamp;
    }

    /** Function to stop the countdown of the hourglass
     *
     */
    @Deprecated
    public void stopCountdown() {
        // This method is deprecated and should not be used.
    }
}
