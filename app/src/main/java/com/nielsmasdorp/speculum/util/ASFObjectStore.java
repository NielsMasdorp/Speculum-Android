package com.nielsmasdorp.speculum.util;

import java.lang.ref.WeakReference;
import java.util.HashMap;

/**
 * @author Niels Masdorp (NielsMasdorp)
 *
 * Credits: https://github.com/asifmujteba/AndroidObjectStore
 */
public class ASFObjectStore<T> {

    private static final String S = "S";
    private static final String W = "W";

    private String key = "0";
    private HashMap<String, T> strongMap = new HashMap<>();
    private HashMap<String, WeakReference<T>> weakMap = new HashMap<>();

    /**
     * Default Constructor
     */
    public ASFObjectStore() { }

    /**
     * Push/Store an Object to the Store and keep a strong reference of the Object until it is
     * popped, to avoid un-wanted Garbage Collection
     *
     * @param obj An Object to Push
     * @return Unique key to access the Object
     */
    public synchronized String pushStrong(T obj) {
        while (strongMap.containsKey(S+key)) {
            incrementKey();
        }

        strongMap.put(S+key, obj);

        return S+key;
    }

    /**
     * Push/Store an Object to the Store and keep a weak reference of the Object so that it can be
     * garbage collected even if it has not been popped yet!
     *
     * @param obj An Object to Push
     * @return Unique key to access the Object
     */
    public synchronized String pushWeak(T obj) {
        while (weakMap.containsKey(W+key)) {
            incrementKey();
        }

        weakMap.put(W+key, new WeakReference<>(obj));

        return W+key;
    }

    /**
     * Pop/Remove an Object from the Store and return it back
     *
     * @param aKey Unique key of the Object
     * @return Popped Object
     */
    public synchronized Object pop(String aKey) {
        if (aKey == null) { return null; }

        T obj = null;

        if (strongMap.containsKey(aKey)) {
            obj = strongMap.get(aKey);
            strongMap.remove(aKey);
        }
        else if (weakMap.containsKey(aKey)) {
            obj = weakMap.get(aKey).get();
            weakMap.remove(aKey);
        }

        return obj;
    }

    /**
     * Get an Object from the Store without removing it
     * Use {@link #pop(String)} instead if you want remove the object from the Store after
     * retrieving.
     *
     * @param aKey Unique key of the Object
     * @return Object
     */
    public synchronized Object get(String aKey) {
        if (strongMap.containsKey(aKey)) {
            return strongMap.get(aKey);
        }

        if (weakMap.containsKey(aKey)) {
            return weakMap.get(aKey).get();
        }

        return null;
    }

    /**
     * Increment the key variable
     */
    private void incrementKey() {
        long ln = Long.parseLong(key);
        if (ln == Long.MAX_VALUE-1) key = "0";
        key = ((int)(ln+1)) + "";
    }
}