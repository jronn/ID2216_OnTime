package id2216.ict.kth.se.ontime;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Vibrator;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import com.astuetz.PagerSlidingTabStrip;

import java.util.concurrent.TimeUnit;

/**
 * MainActivity, only activity used in the app
 * Implements TimerInterface for callbacks from the fragments. See interface for documentation
 * on related methods.
 */
public class MainActivity extends FragmentActivity implements TimerInterface {

    private CountDownTimer cdt;
    private MyPageAdapter adapter;
    private Vibrator vibrator;
    private ViewPager pager;
    private SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        settings = getSharedPreferences("OnTimeSettings",0);

        vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);

        pager = (ViewPager) findViewById(R.id.pager);
        pager.setOffscreenPageLimit(5);
        adapter = new MyPageAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);

        // Bind the tabs to the ViewPager
        PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabs.setViewPager(pager);
        pager.setCurrentItem(3);
    }

    public void startTimer(long time, long interval) {
        pager.setCurrentItem(0);
        final FragmentTimer a = (FragmentTimer)adapter.getItem(pager.getCurrentItem());

        // Get interval
        int intervalNotification = settings.getInt("interval", 0);
        final int notificationTime = (intervalNotification+1) * 60 * 1000;


        cancelTimer();
        cdt = new CountDownTimer(time, interval) {
            // Called on every tick
            public void onTick(long millisUntilFinished) {
                a.changeText(formatTimeLeft(millisUntilFinished));

                // Vibrera vid inställt intervall
                if(millisUntilFinished > notificationTime - 500 && millisUntilFinished < notificationTime + 500) {
                    vibrate();
                    playSound();
                }
            }

            // Called when the timer finishes
            public void onFinish() {
                vibrate();
                playSound();
                a.changeText("00:00:00");
            }
        }.start();
    }


    public void cancelTimer() {
        if(cdt != null) {
            cdt.cancel();
            cdt = null;
        }
    }


    /**
     * Plays a basic notification sound
     */
    private void playSound() {
        if(settings.getBoolean("sound",false)) {
            try {
                Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
                r.play();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Vibrates in a specific pattern
     */
    private void vibrate() {
        if(settings.getBoolean("vibrations", true)) {
            long[] pattern = {0,1000,500,1000,500,1000};
            vibrator.vibrate(pattern, -1);
        }
    }

    /**
     * Formats the time in milliseconds to the form HH:MM:SS
     * @param millis Time left in milliseconds
     * @return String of formatted time
     */
    private String formatTimeLeft(long millis) {

        long seconds = millis / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        StringBuilder secondsStr = new StringBuilder(Long.toString(seconds % 60));
        StringBuilder minutesStr = new StringBuilder(Long.toString(minutes % 60));
        StringBuilder hoursStr = new StringBuilder(Long.toString(hours));

        if(secondsStr.length() == 1)
            secondsStr.insert(0,"0");

        if(minutesStr.length() == 1)
            minutesStr.insert(0,"0");

        if(hoursStr.length() == 1)
            hoursStr.insert(0,"0");

        return hoursStr + ":" + minutesStr + ":" + secondsStr;
    }
}

