package de.whz.mobile.client;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by mindia on 2/18/16.
 */
public class NearByPlacesActivity extends Activity {

    public static String mCurrentLocation;
    public static String mCurrentCoordinates;

    private ListView mNearByPLaces;
    private TextView mStatus;
    private NearByPlacesAdapter mNearByPlacesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_near_by_places);
        mNearByPLaces = (ListView) findViewById(R.id.near_by_places);
        mNearByPlacesAdapter = new NearByPlacesAdapter(this, 0, new ArrayList<NearByPlaces>());
        mNearByPLaces.setAdapter(mNearByPlacesAdapter);

        mStatus = (TextView) findViewById(R.id.please_wait);


        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://46.101.232.55:8080/get-near-by-places?location=" + mCurrentCoordinates, new AsyncHttpResponseHandler() {


            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {

                try {
                    JSONObject jsonObject = new JSONObject(new String(response));
                    JSONArray jsonArray = jsonObject.getJSONArray("results");

                    if (jsonArray.length() > 0) {
                        mNearByPLaces.setVisibility(View.VISIBLE);
                        mStatus.setVisibility(View.GONE);
                    }

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject _i = jsonArray.getJSONObject(i);
                        NearByPlaces nearByPlaces = new NearByPlaces();
                        nearByPlaces.setLegalName(_i.get("vicinity").toString());
                        nearByPlaces.setName(_i.get("name").toString());
                        nearByPlaces.setLat(_i.getJSONObject("geometry").getJSONObject("location").getString("lat"));
                        nearByPlaces.setLng(_i.getJSONObject("geometry").getJSONObject("location").getString("lng"));
                        mNearByPlacesAdapter.add(nearByPlaces);
                    }

                } catch (Exception ex) {
                    mStatus.setText("Unable tp parse data, Please try again or Check your Internet Connection");
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                mStatus.setText("Unable tp parse data, Please try again or Check your Internet Connection");
            }

        });

        mNearByPLaces.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                NearByPlaces nearByPlaces = mNearByPlacesAdapter.getItem(position);

                DistanceActivity.mCurrentCoordinates = mCurrentCoordinates;
                DistanceActivity.mCurrentLocation = mCurrentLocation;

                DistanceActivity.mDestinationCoordinates = nearByPlaces.getLatLng();
                DistanceActivity.mDestination = nearByPlaces.getName();

                Intent intent = new Intent(NearByPlacesActivity.this, DistanceActivity.class);
                startActivity(intent);
            }
        });


    }

    class NearByPlacesAdapter extends BaseArrayAdapter<NearByPlaces> {


        public NearByPlacesAdapter(Context context, int textViewResourceId, ArrayList<NearByPlaces> values) {
            super(context, textViewResourceId, values);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = mInflater.inflate(R.layout.view_near_by_places_item, parent, false);

            NearByPlaces nearByPlaces = getItem(position);

            ((TextView) view.findViewById(R.id.n_p_name)).setText(nearByPlaces.getName());
            ((TextView) view.findViewById(R.id.n_p_coordinates)).setText(nearByPlaces.getCoordinates());
            ((TextView) view.findViewById(R.id.n_p_legal_name)).setText(nearByPlaces.getLegalName());

            return view;
        }
    }
}
