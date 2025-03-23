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

    public int getPeriod() {
        return period;
    }

    public int getTurned() {
        return turned;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public void turn() {
        this.turned++;
        this.remainingTime = this.period;
        this.initCountdown();
    }

    public int getRemainingTime() {
        return remainingTime;
    }

    public int getTotalElapsed() {
        return period * (turned + 1) - remainingTime;
    }

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

    public void stopCountdown() {
        if (currentTask != null) {
            currentTask.cancel();
            currentTask = null;
        }
    }
}
