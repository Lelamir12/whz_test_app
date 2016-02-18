package de.whz.mobile.client;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by mindia on 2/18/16.
 */
public class NearByPlacesActivity extends Activity {

    private ListView mNearByPLaces;
    private NearByPlacesAdapter mNearByPlacesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_near_by_places);
        mNearByPLaces = (ListView) findViewById(R.id.near_by_places);
        mNearByPlacesAdapter = new NearByPlacesAdapter(this, 0, new ArrayList<NearByPlaces>());
        mNearByPLaces.setAdapter(mNearByPlacesAdapter);


        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://192.168.43.114/whz-mobile-api/get-nearby-places", new AsyncHttpResponseHandler() {


            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                String body = new String(response);
                List<NearByPlaces> nearByPlaces = new Gson().fromJson(body, new TypeToken<List<NearByPlaces>>() {
                }.getType());

                mNearByPlacesAdapter.addAll(nearByPlaces);

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
            }

        });


    }

    class NearByPlacesAdapter extends BaseArrayAdapter<NearByPlaces> {


        public NearByPlacesAdapter(Context context, int textViewResourceId, ArrayList<NearByPlaces> values) {
            super(context, textViewResourceId, values);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater li = LayoutInflater.from(context);

            View view = li.inflate(R.layout.view_near_by_places_item, parent);

            NearByPlaces nearByPlaces = getItem(position);

            ((TextView) view.findViewById(R.id.n_p_name)).setText(nearByPlaces.getName());
            ((TextView) view.findViewById(R.id.n_p_coordinates)).setText(nearByPlaces.getCoordinates());
            ((TextView) view.findViewById(R.id.n_p_legal_name)).setText(nearByPlaces.getLegalName());

            return view;
        }
    }
}
