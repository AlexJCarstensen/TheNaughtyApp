package com.alexcarstensen.weatherornotyoulikeit.helpers;

import android.provider.BaseColumns;

/**
 * Created by Lærke on 26-09-2016.
 */

public final class FeedReaderContract {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private FeedReaderContract() {}

    /* Inner class that defines the table contents */
    public static class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "WeatherTable";
        public static final String COLUMN_ID = "WeatherId";
        public static final String COLUMN_WEATHERSTATUS = "weatherStatus";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_TEMPERATURE = "temperature";
        public static final String COLUMN_RESULTCODE = "resultCode";
    }

}
