package de.whz.mobile.client;

import android.os.AsyncTask;

/**
 * Created by mindia on 10/31/15.
 */
public class SimpleAsyncTask<T> extends AsyncTask<Void, Void, T> {

    protected Object mData;

    @Override
    protected T doInBackground(Void... params) {
        doInBackground();
        return null;
    }

    @Override
    protected void onPostExecute(T aVoid) {
        onPostExecute();
    }

    public void start() {
        execute();
    }

    void doInBackground() {

    }

    void onPostExecute() {

    }

}
