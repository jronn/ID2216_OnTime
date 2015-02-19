package id2216.ict.kth.se.ontime;

/**
 * Handles rest calls to the SL API
 * Created by jronn on 2015-02-19.
 */

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

//SAMPLE USE

/*SLRestClient.get("http://api.sl.se/api2/typeahead.Json?key=" + platsUppslagKey +
    			"&searchstring=" + input, null, new JsonHttpResponseHandler() {

			@Override
	        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				try {
					JSONArray responseData = response.getJSONArray("ResponseData");
					for(int i = 0; i < responseData.length(); i++) {
						JSONObject jo = (JSONObject) responseData.get(i);
						resultList.add(jo.getString("Name"));
					}
					System.out.println(resultList.toString());

				} catch (JSONException e) {
					e.printStackTrace();
				}
	        }

		});*/

public class SLRestClient {

    private static final String BASE_URL = "";

    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.setTimeout(60*1000);
        client.get(url, params, responseHandler);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }
}

