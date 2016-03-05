package de.whz.mobile.client;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by mindia on 2/18/16.
 */
public class HomeActivity extends Activity {

    private String location = "0,0";
    private String locationAsString = "0,0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        PlayServicesWrapper playServicesWrapper = new PlayServicesWrapper(this);
        playServicesWrapper.getCurrentLocation(false, new BinaryCallback<Location, String>() {
            @Override
            public void onDone(Location l, String s) {
                ((TextView) findViewById(R.id.coordinates)).setText(s);
                location = s;
                reverseGeoCoder(s);
                findViewById(R.id.near_by).setEnabled(true);
            }
        });

        findViewById(R.id.near_by).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NearByPlacesActivity.mCurrentCoordinates = location;
                NearByPlacesActivity.mCurrentLocation = locationAsString;
                Intent intent = new Intent(HomeActivity.this, NearByPlacesActivity.class);
                startActivity(intent);
            }
        });
    }


    void reverseGeoCoder(String location) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://46.101.232.55:8080/get-location-info?location=" + location, new AsyncHttpResponseHandler() {


            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                String body = new String(response);
                try {
                    JSONObject jsonObject = new JSONObject(body);
                    JSONArray jsonArray = jsonObject.getJSONArray("results");
                    if (jsonArray != null && jsonArray.length() > 0) {
                        JSONObject _r = jsonArray.getJSONObject(0);
                        locationAsString = _r.get("formatted_address").toString();
                        ((TextView) findViewById(R.id.reverse_geo_coder)).setText(locationAsString);
                    }
                } catch (Exception ex) {
                    ((TextView) findViewById(R.id.reverse_geo_coder)).setText("Unable tp parse data, Please try again or Check your Internet Connection");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                ((TextView) findViewById(R.id.reverse_geo_coder)).setText("Unable tp parse data, Please try again or Check your Internet Connection");
            }

        });
    }
}
