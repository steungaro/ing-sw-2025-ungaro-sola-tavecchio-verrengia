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

    /**
     * Retrieves the period of the hourglass.
     *
     * @return the period of the hourglass, representing the duration it measures in seconds
     */
    public int getPeriod() {
        return period;
    }

    /**
     * Returns the number of times the hourglass has been turned.
     *
     * @return the count of turns performed on the hourglass
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

    /**
     * Turns the hourglass, effectively resetting its timing mechanism.
     * Increments the count of turns performed on the hourglass and
     * initiates the countdown process by recording the current timestamp.
     */
    public void turn() {
        this.turned++;
        this.initCountdown();
    }

    /**
     * Calculates and returns the remaining time for the hourglass in seconds.
     * The remaining time is determined by subtracting the elapsed time since the last turn
     * from the total period of the hourglass. If the calculated remaining time is less than
     * or equal to zero, zero is returned.
     *
     * @return the remaining time in seconds until the hourglass runs out,
     *         or 0 if the time has already expired
     */
    public int getRemainingTime() {
        return period - ((System.currentTimeMillis() - timestamp)/1000) > 0 ? (int) (period - (System.currentTimeMillis() - timestamp) / 1000) : 0;
    }

    /**
     * Calculates and returns the total time elapsed since the initialization of the hourglass.
     * The total elapsed time is computed by considering the total period of the hourglass,
     * the number of times it has been turned, and the remaining time.
     *
     * @return the total elapsed time in seconds since the hourglass was initialized or last reset
     */
    public int getTotalElapsed() {
        return period * (turned + 1) - getRemainingTime();
    }

    /**
     * Initializes the countdown by recording the current system timestamp.
     * This method sets the starting point for measuring elapsed time.
     */
    public void initCountdown() {
        timestamp = System.currentTimeMillis();
    }

    /**
     * Retrieves the timestamp recorded for the hourglass.
     *
     * @return the timestamp indicating the start of the most recent countdown process
     */
    public long getTimestamp() {
        return timestamp;
    }

}
