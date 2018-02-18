package com.frederiksen.formidable.utils;

public class Timer {
    private double lastTime;

    private double trigger;
    private Runnable triggerEvent;

    private boolean resetOnCheck = false;
    private boolean autoReset = false;

    public Timer() {

    }

    /**
     * Returns the time in seconds (computed from
     * System.nanoTime())
     * @return time in seconds
     */
    public static double getTime() {
        return System.nanoTime() / 1_000_000_000.0;
    }

    public void reset() {
        lastTime = getTime();
    }

    public void update() {
        if (triggerEvent != null) {
            if (getElapsed() > trigger) {
                triggerEvent.run();
                if (autoReset) reset();
            }
        }
    }

    public double getElapsed() {
        double elapse = getTime() - lastTime;
        if (resetOnCheck) reset();
        return elapse;
    }

    public void setResetOnCheck(boolean resetOnCheck) {
        this.resetOnCheck = resetOnCheck;
    }

    public void setAutoReset(boolean autoReset) {
        this.autoReset = autoReset;
    }

    public void setTrigger(double trigger) {
        this.trigger = trigger;
    }

    public void setTriggerEvent(Runnable triggerEvent) {
        this.triggerEvent = triggerEvent;
    }
}
