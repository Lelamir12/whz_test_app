package de.whz.mobile.client;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by mindia on 3/26/15.
 */
public abstract class BaseArrayAdapter<T> extends ArrayAdapter<T> {

    protected Context context;
    protected ArrayList<T> values;

    public BaseArrayAdapter(Context context, int textViewResourceId, ArrayList<T> values) {
        super(context, textViewResourceId, values);
        this.context = context;
        this.values = values;
    }

    public void addAll(T[] ts) {
        ArrayList<T> _t = new ArrayList<T>(Arrays.asList(ts));
        super.addAll(_t);
    }

    public int getCount() {
        return values.size();
    }

    public T getItem(int position) {
        return values.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public abstract View getView(int position, View convertView, ViewGroup parent);

}