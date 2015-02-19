package id2216.ict.kth.se.ontime.Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import id2216.ict.kth.se.ontime.ActivityInterface;
import id2216.ict.kth.se.ontime.R;

/**
 * Fragment modelling the timer tab
 */
public class FragmentTimer extends Fragment {

    private ActivityInterface mCallback;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_timer, container, false);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mCallback = (ActivityInterface) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement ActivityInterface");
        }
    }

    public void changeText(String text) {
        TextView textView = (TextView) getView().findViewById(R.id.timerText);
        textView.setText(text);
    }

    public void setJourneyInfo(String text) {
        TextView textView = (TextView) getView().findViewById(R.id.journeyInfoText);
        textView.setText(text);
    }

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Button cancelButton = (Button) getView().findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.cancelTimer();
                TextView textView = (TextView) getView().findViewById(R.id.timerText);
                textView.setText("00:00:00");
            }
        });
    }
}
