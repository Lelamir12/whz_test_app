package de.whz.mobile.client;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.AutocompletePredictionBuffer;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.nearby.Nearby;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mindia on 10/31/15.
 */
public class PlayServicesWrapper {

    private GoogleApiClient mGoogleApiClient;
    private Context mContext;
    private GoogleApiClient.Builder mBuilder;

    public PlayServicesWrapper(Context context) {
        mContext = context;
    }

    private void connect(final SimpleCallback<Boolean> callback) {
        mGoogleApiClient.registerConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
            @Override
            public void onConnected(Bundle bundle) {
                callback.onDone(true);
            }

            @Override
            public void onConnectionSuspended(int i) {
                callback.onDone(false);
            }
        });
        if (mGoogleApiClient == null || !mGoogleApiClient.isConnected()) {
            mGoogleApiClient.connect();
        } else {
            callback.onDone(true);
        }
    }

    public void disconnect() {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    private void build() {
        mGoogleApiClient = mBuilder.build();
    }

    public void configureForAutocompleteService() {
        mBuilder = new GoogleApiClient.Builder(mContext).addApiIfAvailable(Places.GEO_DATA_API).
                addApiIfAvailable(Places.PLACE_DETECTION_API);
    }

    public void configureForNearByService() {
        mBuilder = new GoogleApiClient.Builder(mContext).addApiIfAvailable(Nearby.CONNECTIONS_API).
                addApiIfAvailable(Places.PLACE_DETECTION_API);
    }

    public void configureForLocationAPI() {
        mBuilder = new GoogleApiClient.Builder(mContext).addApiIfAvailable(LocationServices.API);
    }


    public void getAutoCompleteData(final String text, final LatLng latLng, final SimpleCallback<List<TLocation>> callback) {

        configureForAutocompleteService();
        build();
        final List<TLocation> autoCompleteResults = new ArrayList<TLocation>();
        connect(new SimpleCallback<Boolean>() {
            @Override
            public void onDone(Boolean value) {
                new SimpleAsyncTask() {
                    @Override
                    void doInBackground() {

                        //priority area
                        LatLngBounds latLngBounds = null;
                        if (latLng != null)
                            latLngBounds = new LatLngBounds(new LatLng(latLng.latitude - 0.25, latLng.longitude - 0.25), new LatLng(latLng.latitude + 0.25, latLng.longitude + 0.25));
                        PendingResult<AutocompletePredictionBuffer> re = Places.GeoDataApi.getAutocompletePredictions(mGoogleApiClient, text, latLngBounds, null);
                        AutocompletePredictionBuffer autocompletePredictions = re.await();

                        ArrayList<String> placeIds = new ArrayList<String>();
                        for (AutocompletePrediction autocompletePrediction : autocompletePredictions) {
                            placeIds.add(autocompletePrediction.getPlaceId());
                        }
                        String[] ids = new String[placeIds.size()];
                        placeIds.toArray(ids);

                        if (ids != null && ids.length > 0) {
                            PendingResult<PlaceBuffer> places = Places.GeoDataApi.getPlaceById(mGoogleApiClient, ids);
                            PlaceBuffer p = places.await();

                            for (AutocompletePrediction autocompletePrediction : autocompletePredictions) {
                                TLocation result = new TLocation();
                                result.setStreet(autocompletePrediction.getPrimaryText(null).toString());
                                result.setAdminArea(autocompletePrediction.getSecondaryText(null).toString());
                                for (int i = 0; i < p.getCount(); i++) {
                                    if (autocompletePrediction.getPlaceId().equals(p.get(i).getId())) {
                                        result.setLatLng(p.get(i).getLatLng());
                                        break;
                                    }
                                }
                                autoCompleteResults.add(result);
                            }
                            p.release();
                        }
                        autocompletePredictions.release();

                    }

                    @Override
                    void onPostExecute() {
                        if (callback != null)
                            callback.onDone(autoCompleteResults);
                    }
                }.start();
            }
        });

    }

    public void getCurrentLocation(final boolean repeatable, final BinaryCallback<Location, String> locationSimpleCallback) {


        configureForLocationAPI();
        build();

        connect(new SimpleCallback<Boolean>() {
                    @Override
                    public void onDone(Boolean value) {

                        int UPDATE_INTERVAL = 10 * 1000; // 10 sec

                        LocationRequest mLocationRequest = new LocationRequest();
                        mLocationRequest.setInterval(UPDATE_INTERVAL);

                        if (mGoogleApiClient.isConnected()) {
                            LocationServices.FusedLocationApi.requestLocationUpdates(
                                    mGoogleApiClient, mLocationRequest, new LocationListener() {
                                        @Override
                                        public void onLocationChanged(Location location) {
                                            if (!repeatable && mGoogleApiClient.isConnected()) {
                                                LocationServices.FusedLocationApi.removeLocationUpdates(
                                                        mGoogleApiClient, this);
                                            }
                                            locationSimpleCallback.onDone(location, location.getLatitude() + "," + location.getLongitude());
                                        }
                                    });
                        }
                    }
                }

        );

    }

}
