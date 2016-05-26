package com.nielsmasdorp.speculum.util;

import java.util.HashMap;

/**
 * @author Niels Masdorp (NielsMasdorp)
 *
 * Credits: https://github.com/asifmujteba/AndroidObjectStore
 */
public class ASFObjectStore<T> {

    private String key = "configuration_object";
    private HashMap<String, T> strongMap = new HashMap<>();

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

        if (strongMap.size() > 0) {
            strongMap.clear();
        }

        strongMap.put(key, obj);

        return key;
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

        return null;
    }
}