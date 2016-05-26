package com.nielsmasdorp.speculum.util;

/**
 * @author Niels Masdorp (NielsMasdorp)
 *
 * Credits: https://github.com/asifmujteba/AndroidObjectStore
 */
public class ASFObjectStore<T> {

    private T object;

    public ASFObjectStore() {
    }

    public synchronized void setObject(T obj) {
        if (null != this.object) {
            this.object = null;
        }
        this.object = obj;
    }

    public synchronized T get() {
        return this.object;
    }
}