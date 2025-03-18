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

    /**
     * Default constructor
     */
    public Hourglass(int period) {
        this.period = period;
        this.turned = 0;
        this.remainingTime = period;
        this.timer = new Timer();
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
        return period * turned - remainingTime;
    }

    public void initCountdown() {
        timer.schedule(new TimerTask() {
            public void run() {
                if(remainingTime == 0) {
                    timer.cancel();
                } else {
                    remainingTime--;
                }
            }
        }, 0, 1000);
    }

    public void stopCountdown() {
        timer.cancel();
    }
}