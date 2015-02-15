package id2216.ict.kth.se.ontime;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Fragment modelling the settings tab
 */
public class FragmentSettings extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Init
        SharedPreferences settings = getActivity().getSharedPreferences("OnTimeSettings", 0);
        final SharedPreferences.Editor editor = settings.edit();

        final CheckBox vCB = (CheckBox) getView().findViewById(R.id.vibrationCB);
        vCB.setChecked(settings.getBoolean("vibrations", true));

        final CheckBox sCB = (CheckBox) getView().findViewById(R.id.soundCB);
        sCB.setChecked(settings.getBoolean("sound", false));

        final Spinner intervalSpinner = (Spinner) getView().findViewById(R.id.settingsSpinner);
        intervalSpinner.setSelection(settings.getInt("interval", 0));

        Button saveButton = (Button) getView().findViewById(R.id.saveSettingsButton);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putBoolean("vibrations", vCB.isChecked());
                editor.putBoolean("sound", sCB.isChecked());
                editor.putInt("interval", intervalSpinner.getSelectedItemPosition());
                editor.commit();

                Toast.makeText(getActivity(), "settings saved", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
