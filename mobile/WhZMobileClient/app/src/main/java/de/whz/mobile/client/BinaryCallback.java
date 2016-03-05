package de.whz.mobile.client;

/**
 * Created by mindia on 10/21/15.
 */
public interface BinaryCallback<T, K> {
    void onDone(T t, K k);
}
