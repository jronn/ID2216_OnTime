package id2216.ict.kth.se.ontime;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * Fragment modelling the custom tab
 */
public class FragmentCustom extends Fragment {

    TimerInterface mCallback;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_custom, container, false);
    }

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final TimePicker timePicker = (TimePicker) getView().findViewById(R.id.timePicker);
        final Calendar c = Calendar.getInstance();

        Button startButton = (Button) getView().findViewById(R.id.customTimeButton);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String time = timePicker.getCurrentHour() + ":" + timePicker.getCurrentMinute();

                long currTime = System.currentTimeMillis();

                Calendar calendar = Calendar.getInstance();
                calendar.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH),
                        timePicker.getCurrentHour(), timePicker.getCurrentMinute(), 0);
                long tarTime = calendar.getTimeInMillis();

                mCallback.startTimer(tarTime - currTime, 1000);
            }
        });
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
}
