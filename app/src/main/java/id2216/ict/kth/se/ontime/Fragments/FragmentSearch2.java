package id2216.ict.kth.se.ontime.Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import id2216.ict.kth.se.ontime.ActivityInterface;
import id2216.ict.kth.se.ontime.R;
import id2216.ict.kth.se.ontime.SLRestClient;


/**
 * Fragment representing the seconds search screen, presenting a list of departures
 */
public class FragmentSearch2 extends Fragment {

    private static final String PLATSUPPSLAG_KEY = "";
    private static final String REALTID_KEY = "";
    ActivityInterface mCallback;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search2, container, false);
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

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        searchDepartures();

        Button backButton = (Button) getView().findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Go back to previous view
                FragmentTransaction trans = getFragmentManager()
                        .beginTransaction();
                trans.replace(R.id.root_frame, new FragmentSearch1());
                trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                trans.addToBackStack(null);
                trans.commit();
            }
        });
    }


    /**
     * Searches for siteId based on search name and then departures base on that siteId
     */
    private void searchDepartures() {

        String stationName = mCallback.getSearchStation();
        if(stationName == null ||stationName.length() < 1) {
            return;
        }

        SLRestClient.get("http://api.sl.se/api2/typeahead.Json?key=" + PLATSUPPSLAG_KEY +
                "&searchstring=" + stationName, null, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONArray responseData = response.getJSONArray("ResponseData");

                    if(responseData.length() < 1) {
                        return;
                    }

                    // Get first station from search
                    JSONObject jo = (JSONObject) responseData.get(0);

                    // Search for realtime information
                    searchRealtime(jo.getInt("SiteId"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    /**
     * Searches for departures realtime information from a specified siteId
     * @param siteId
     */
    private void searchRealtime(int siteId) {

        SLRestClient.get("http://api.sl.se/api2/realtimedepartures.Json?key=" + REALTID_KEY +
                "&siteid=" + siteId + "&timewindow=30", null, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    List<String> listContents = new ArrayList<String>();

                    JSONObject responseData = response.getJSONObject("ResponseData");
                    JSONArray buses = responseData.getJSONArray("Buses");
                    JSONArray trains = responseData.getJSONArray("Trains");

                    for (int i = 0; i < buses.length(); i++) {
                        JSONObject jo = (JSONObject) buses.get(i);
                        StringBuilder sb = new StringBuilder();
                        sb.append(jo.getString("TransportMode") + " : ");
                        sb.append(jo.getString("Destination") + " : ");

                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                        Date expectedDateTime = sdf.parse(jo.getString("ExpectedDateTime"));

                        SimpleDateFormat printSdf = new SimpleDateFormat("HH:mm:ss");

                        sb.append(printSdf.format(expectedDateTime));
                        listContents.add(sb.toString());
                    }

                    for (int i = 0; i < trains.length(); i++) {
                        JSONObject jo = (JSONObject) trains.get(i);
                        StringBuilder sb = new StringBuilder();
                        sb.append(jo.getString("TransportMode") + " : ");
                        sb.append(jo.getString("Destination") + " : ");

                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                        Date expectedDateTime = sdf.parse(jo.getString("ExpectedDateTime"));

                        SimpleDateFormat printSdf = new SimpleDateFormat("HH:mm:ss");

                        sb.append(printSdf.format(expectedDateTime));
                        listContents.add(sb.toString());
                    }

                    ListView myListView = (ListView) getView().findViewById(R.id.departuresList);
                    myListView.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, listContents));
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
