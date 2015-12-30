package com.nielsmasdorp.speculum.services;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.CalendarContract;
import android.text.TextUtils;
import android.util.Log;

import com.nielsmasdorp.speculum.R;
import com.nielsmasdorp.speculum.util.Constants;

import java.lang.ref.WeakReference;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import rx.Observable;
import rx.Subscriber;

/**
 * @author Niels Masdorp (NielsMasdorp)
 */
public class GoogleCalendarService {

    private WeakReference<Context> mContext;

    public GoogleCalendarService(Context context) {
        this.mContext = new WeakReference<>(context);
    }

    public Observable<String> getLatestCalendarEvent() {
        if (mContext.get() != null) {
            String details, title;
            Cursor cursor;
            ContentResolver contentResolver = mContext.get().getContentResolver();
            final String[] colsToQuery = new String[]{
                    CalendarContract.EventsEntity.TITLE,
                    CalendarContract.EventsEntity.DTSTART,
                    CalendarContract.EventsEntity.DTEND,
                    CalendarContract.EventsEntity.EVENT_LOCATION};

            Calendar now = Calendar.getInstance();
            SimpleDateFormat startFormat = new SimpleDateFormat(Constants.SIMPLEDATEFORMAT_DDMMYY, Locale.getDefault());
            String dateString = startFormat.format(now.getTime());
            long start = now.getTimeInMillis();

            SimpleDateFormat endFormat = new SimpleDateFormat(Constants.SIMPLEDATEFORMAT_HHMMSSDDMMYY, Locale.getDefault());
            Calendar endOfDay = Calendar.getInstance();
            Date endOfDayDate;
            try {
                endOfDayDate = endFormat.parse(Constants.END_OF_DAY_TIME + dateString);
                endOfDay.setTime(endOfDayDate);
            } catch (ParseException e) {
                Log.e(GoogleCalendarService.class.getSimpleName(), e.toString());
                throw new RuntimeException(String.format("ParseException occured: %s", e.getLocalizedMessage()));
            }

            cursor = contentResolver.query(CalendarContract.Events.CONTENT_URI, colsToQuery,
                    Constants.CALENDAR_QUERY_FIRST + start + Constants.CALENDAR_QUERY_SECOND + endOfDay.getTimeInMillis() + Constants.CALENDAR_QUERY_THIRD,
                    null, Constants.CALENDAR_QUERY_FOURTH);

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
                    cursor.close();
                    return Observable.just(title + ", " + details);
                } else {
                    cursor.close();
                    return Observable.just(mContext.get().getString(R.string.no_events_today));
                }
            } else {
                throw new RuntimeException(mContext.get().getString(R.string.no_events_error));
            }
        } else {
            throw new RuntimeException("View has been garbage collected.");
        }
    }
}
