package id2216.ict.kth.se.ontime;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * Custom timePickerDialog, sets the timer once a time is picked
 *
 * Created by Greedy on 2015-02-15.
 */
public class FragmentTimePicker extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {

    TimerInterface mCallback;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mCallback = (TimerInterface) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement TimerInterface");
        }
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

        long currTime = System.currentTimeMillis();
        final Calendar c = Calendar.getInstance();

        Calendar calendar = Calendar.getInstance();
        calendar.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH),
                hourOfDay, minute, 0);
        long tarTime = calendar.getTimeInMillis();

        mCallback.startTimer(tarTime - currTime, 1000, "Custom timer");
    }
}