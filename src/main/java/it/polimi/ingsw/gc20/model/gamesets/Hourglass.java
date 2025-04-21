package it.polimi.ingsw.gc20.model.gamesets;

import java.util.*;

/**
 * @author GC20
 */
public class Hourglass {
    private int period;
    private int turned;
    private int remainingTime;
    private final Timer timer;
    private TimerTask currentTask;

    /**
     * Default constructor
     */
    public Hourglass(int period) {
        this.period = period;
        this.turned = 0;
        this.remainingTime = period;
        this.timer = new Timer(true);  // To run the countdown in a separate thread
        this.currentTask = null;
    }

    /** Getter function for the period of the hourglass
     *
     * @return period of teh hourglass
     */
    public int getPeriod() {
        return period;
    }

    /** Getter function for the number of times the hourglass hase been turned
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
        this.remainingTime = period;
    }

    /** function to turn the hourglass
     *
     */
    public void turn() {
        this.turned++;
        this.remainingTime = this.period;
        this.initCountdown();
    }

    /** function to get the remaining time of the hourglass
     *
     * @return remaining time of the hourglass
     */
    public int getRemainingTime() {
        return remainingTime;
    }

    /** function to get the total elapsed time of the hourglass
     *
     * @return total elapsed time of the hourglass
     */
    public int getTotalElapsed() {
        return period * (turned + 1) - remainingTime;
    }

    /** function to start the countdown of the hourglass
     *
     */
    public void initCountdown() {
        // If there is a task running, cancel it
        if (currentTask != null) {
            currentTask.cancel();
        }
        this.remainingTime = this.period;
        currentTask = new TimerTask() {
            @Override
            public void run() {
                if (remainingTime == 0) {
                    cancel();  // Stop the task
                } else {
                    remainingTime--;
                }
            }
        };

        // Schedule the task to run every second
        timer.scheduleAtFixedRate(currentTask, 0, 1000);
    }

    /** function to stop the countdown of the hourglass
     *
     */
    public void stopCountdown() {
        if (currentTask != null) {
            currentTask.cancel();
            currentTask = null;
        }
    }
}
