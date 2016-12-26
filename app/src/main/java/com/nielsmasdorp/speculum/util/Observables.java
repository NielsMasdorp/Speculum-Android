package com.nielsmasdorp.speculum.util;

import rx.Observable;
import rx.functions.Func1;

import java.util.concurrent.TimeUnit;
/**
 * @author Niels Masdorp (NielsMasdorp)
 * http://blog.danlew.net/2016/01/25/rxjavas-repeatwhen-and-retrywhen-explained/
 */
public enum Observables {;

    public static Func1<Observable<? extends Throwable>, Observable<?>> exponentialBackoff(
            int maxRetryCount, long delay, TimeUnit unit) {
        return errors -> errors
                .zipWith(Observable.range(1, maxRetryCount), (error, retryCount) -> retryCount)
                .flatMap(retryCount -> Observable.timer((long) Math.pow(delay, retryCount), unit));
    }
}