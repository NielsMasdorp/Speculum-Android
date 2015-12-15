package com.nielsmasdorp.speculum.services;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.CalendarContract;
import android.text.TextUtils;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.Subscriptions;

/**
 * Created by Niels on 12/14/2015.
 */
public class GoogleCalendarService {

    Context mContext;

    public GoogleCalendarService(Context context) {
        this.mContext = context;
    }

    public Observable<String> getLatestCalendarEvent() {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                String title = "";
                String details = "";
                Cursor cursor;
                ContentResolver contentResolver = mContext.getContentResolver();
                final String[] colsToQuery = new String[]{
                        CalendarContract.EventsEntity.TITLE,
                        CalendarContract.EventsEntity.DTSTART,
                        CalendarContract.EventsEntity.DTEND,
                        CalendarContract.EventsEntity.EVENT_LOCATION};

                Calendar now = Calendar.getInstance();
                SimpleDateFormat startFormat = new SimpleDateFormat("dd/MM/yy");
                String dateString = startFormat.format(now.getTime());
                long start = now.getTimeInMillis();

                SimpleDateFormat endFormat = new SimpleDateFormat("hh:mm:ss dd/MM/yy");
                Calendar endOfDay = Calendar.getInstance();
                Date endofDayDate;
                try {
                    endofDayDate = endFormat.parse("23:59:59 " + dateString);
                    endOfDay.setTime(endofDayDate);
                } catch (ParseException e) {
                    subscriber.onError(e);
                    Log.e("CalendarModule", e.toString());
                }

                cursor = contentResolver.query(CalendarContract.Events.CONTENT_URI, colsToQuery,
                        "( dtstart >" + start + ") and (dtend  <" + endOfDay.getTimeInMillis() + ")",
                        null, "dtstart ASC");

                if (cursor != null) {
                    if (cursor.getCount() > 0) {
                        cursor.moveToFirst();
                        title = cursor.getString(0);
                        Calendar startTime = Calendar.getInstance();
                        startTime.setTimeInMillis(cursor.getLong(1));
                        Calendar endTime = Calendar.getInstance();
                        endTime.setTimeInMillis(cursor.getLong(2));
                        DateFormat formatter = SimpleDateFormat.getTimeInstance(DateFormat.SHORT);
                        details = formatter.format(startTime.getTime()) + " - " + formatter.format(endTime.getTime());
                        if (!TextUtils.isEmpty(cursor.getString(3))) {
                            details += " ~ " + cursor.getString(3);
                        }
                    } else {
                        subscriber.onNext("No events today.");
                        subscriber.onCompleted();
                    }
                    cursor.close();
                    subscriber.onNext(title + ", " + details);
                    subscriber.onCompleted();
                } else {
                    subscriber.onError(new RuntimeException("Could not get events from calendar."));
                }
            }
        });
    }
}
