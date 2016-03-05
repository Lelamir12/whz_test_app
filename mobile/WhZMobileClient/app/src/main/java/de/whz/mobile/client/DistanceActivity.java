package de.whz.mobile.client;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by mindia on 3/5/16.
 */
public class DistanceActivity extends Activity {


    public static String mCurrentLocation;
    public static String mCurrentCoordinates;


    public static String mDestination;
    public static String mDestinationCoordinates;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_distance);

        ((TextView) findViewById(R.id.current_location)).setText(mCurrentLocation);
        ((TextView) findViewById(R.id.destination_location)).setText(mDestination);
        calculate();

        findViewById(R.id.go_map).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigate();
            }
        });
    }


    void navigate() {
        String uriString = String.format("geo:%s?q=%s(Location)", mCurrentCoordinates, mDestinationCoordinates);
        Uri uri = Uri.parse(uriString);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(uri);
        startActivity(intent);
    }

    void calculate() {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://46.101.232.55:8080/get-distance-matrix?origin=" + mCurrentCoordinates + "&destination=" + mDestinationCoordinates, new AsyncHttpResponseHandler() {


            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                String body = new String(response);
                try {
                    JSONObject jsonObject = new JSONObject(body);

                    JSONArray rows = jsonObject.getJSONArray("rows");

                    JSONObject _preElement = rows.getJSONObject(0);

                    JSONArray elements = _preElement.getJSONArray("elements");

                    String distanceAsString = elements.getJSONObject(0).getJSONObject("distance").getString("text");
                    String durationAsString = elements.getJSONObject(0).getJSONObject("duration").getString("text");

                    ((TextView) findViewById(R.id.time)).setText(durationAsString);
                    ((TextView) findViewById(R.id.distance)).setText(distanceAsString);

                    findViewById(R.id.go_map).setEnabled(true);

                } catch (Exception ex) {
                    Toast.makeText(DistanceActivity.this, "Unable tp parse data, Please try again or Check your Internet Connection", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                Toast.makeText(DistanceActivity.this, "Unable tp parse data, Please try again or Check your Internet Connection", Toast.LENGTH_SHORT).show();
            }

        });
    }
}
