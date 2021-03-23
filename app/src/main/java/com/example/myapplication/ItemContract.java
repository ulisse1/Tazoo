package com.example.myapplication;

import android.provider.BaseColumns;

public class ItemContract {

    public static final class ItemEntry implements BaseColumns {

        private ItemEntry() {}

        public static final String TABLE_NAME = "contactsList";
        public static final String COLUMN_NUMBER = "number";
        public static final String COLUMN_MSG = "message";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_TIME = "time";
        public static final String COLUMN_ALARMID = "alarmId";
//        public static final String COLUMN_MILLS ="mills";
    }



}
