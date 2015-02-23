package id2216.ict.kth.se.ontime.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import id2216.ict.kth.se.ontime.ActivityInterface;
import id2216.ict.kth.se.ontime.DelayAutoCompleteTextView;
import id2216.ict.kth.se.ontime.R;
import id2216.ict.kth.se.ontime.Station;


public class FragmentSearch1 extends Fragment {

    private static final String LOG_TAG = "SEARCH TAB 1";

    private static final String PLACES_API_BASE = "http://api.sl.se/api2/typeahead.Json";
    private static final String API_KEY = "";

    private ActivityInterface mCallback;
    private SharedPreferences settings;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search1, container, false);
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

        settings = getActivity().getSharedPreferences("OnTimeSettings", 0);

        // Load recent searches from preferences.
        updateRecentSearches();

        final ListView myListView = (ListView) getView().findViewById(R.id.recentSearches);

        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // Set station name in activity to be accessed from next search fragment
                mCallback.setSearchStation((String) myListView.getItemAtPosition(position));

                nextView();
            }
        });

        final DelayAutoCompleteTextView autoCompView = (DelayAutoCompleteTextView)getView().findViewById(R.id.delayAutoCompleteTextView);
        autoCompView.setAdapter(new PlacesAutoCompleteAdapter(this,R.layout.custom_list_item));
        autoCompView.setThreshold(3);

        Button searchButton = (Button) getView().findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Set recent searches
                final SharedPreferences.Editor editor = settings.edit();
                for(int i = 5; i > 0; i--) {
                    String s = settings.getString("RS" + i, "");
                    if(i != 5 && !s.equals("")) {
                        editor.putString("RS" + (i + 1), s);
                        Log.i(LOG_TAG, "Put " + s + " at RS" + (i+1));
                    }
                }
                editor.putString("RS1", autoCompView.getText().toString());
                editor.commit();
                Log.i(LOG_TAG, "Put " + autoCompView.getText().toString() + " at RS1");
                updateRecentSearches();

                // Set station name in activity to be accessed from next search fragment
                mCallback.setSearchStation(autoCompView.getText().toString());

                // Hide keyboard
                InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(
                                            Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(autoCompView.getWindowToken(),0);

                nextView();
            }
        });
    }


    /**
     * Queries the SL API with the input string and converts the result JSON to a list of Stations
     * that is returned
     * @param input Search input
     * @return List of stations
     */
    private ArrayList<Station> autocomplete(String input) {
        ArrayList<Station> resultList = null;

        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            StringBuilder sb = new StringBuilder(PLACES_API_BASE);
            sb.append("?key=" + API_KEY);
            sb.append("&searchstring=" + URLEncoder.encode(input, "utf8"));

            URL url = new URL(sb.toString());
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error processing Places API URL", e);
            return resultList;
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error connecting to Places API", e);
            Log.e(LOG_TAG, e.getMessage());
            return resultList;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        try {
            // Create a JSON object hierarchy from the results
            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            JSONArray jsonArray = jsonObj.getJSONArray("ResponseData");

            // Extract the Place descriptions from the results
            resultList = new ArrayList<Station>(jsonArray.length());
            for (int i = 0; i < jsonArray.length(); i++) {
                Station station = new Station(jsonArray.getJSONObject(i).getString("Name"),
                                                jsonArray.getJSONObject(i).getInt("SiteId"));
                resultList.add(station);
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Cannot process JSON results", e);
        }

        return resultList;
    }

    /**
     * Adapter class for our DelayAutoCompleteTextView, calls autocomplete to get values
     */
    private class PlacesAutoCompleteAdapter extends ArrayAdapter<Station> implements Filterable {
        private ArrayList<Station> resultList;

        public PlacesAutoCompleteAdapter(FragmentSearch1 context, int textViewResourceId) {
            super(context.getActivity(),textViewResourceId);
        }

        @Override
        public int getCount() {
            return resultList.size();
        }

        @Override
        public Station getItem(int index) {
            return resultList.get(index);
        }

        @Override
        public Filter getFilter() {
            Filter filter = new Filter() {

                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults filterResults = new FilterResults();
                    if(constraint != null) {
                        // Retrieve the autocomplete results.
                        resultList = autocomplete(constraint.toString());

                        // Assign the data to the FilterResults
                        if(resultList != null) {
                            filterResults.values = resultList;
                            filterResults.count = resultList.size();
                        }
                    }
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    if (results != null && results.count > 0) {
                        notifyDataSetChanged();
                    } else {
                        notifyDataSetInvalidated();
                    }
                }
            };
            return filter;
        }
    }

    private void nextView() {
        // Go to next view
        FragmentTransaction trans = getFragmentManager()
                .beginTransaction();

        trans.replace(R.id.root_frame, new FragmentSearch2());

        trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        trans.addToBackStack(null);

        trans.commit();
    }

    private void updateRecentSearches() {
        // Load recent searches from preferences
        List<String> recentSearches = new ArrayList<>();

        for(int i = 1; i < 6; i++) {
            String s = settings.getString("RS" + i, "");
            if(!s.equals(""))
                recentSearches.add(s);
        }
        ListView myListView = (ListView) getView().findViewById(R.id.recentSearches);

        myListView.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, recentSearches));

    }
}
