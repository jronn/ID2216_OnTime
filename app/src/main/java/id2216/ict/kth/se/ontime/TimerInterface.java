package id2216.ict.kth.se.ontime;

/**
 * Created by jronn on 2015-02-13.
 */
public interface TimerInterface {

    /**
     * Starts a countdown timer that lasts for a specified time, and "ticks" at a certain interval
     *
     * @param time     Time to count down from
     * @param interval Interval at which the "onTick" function is called
     * @param journeyInfo Text info to update the info text on timer tab, ex. "Bus towards Kista"
     */
    public void startTimer(long time, long interval, String journeyInfo);

    /**
     * Cancels any current timer if one exists. Should be called before creating a new timer or
     * when discarding an existing one
     */
    public void cancelTimer();
}
