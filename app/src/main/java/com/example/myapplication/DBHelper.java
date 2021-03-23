package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import com.example.myapplication.ItemContract.*;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "contacts";
    public static final int DATABASE_VERSION = 1;

    public DBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }



    //TODO: String hashing so it has an unique attribute.
    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_CONTACTS_TABLE = "CREATE TABLE " + ItemEntry.TABLE_NAME + " (" + ItemEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ItemEntry.COLUMN_NUMBER + " VARCHAR(30) NOT NULL, " + ItemEntry.COLUMN_TIME + " TIME NOT NULL, " + ItemEntry.COLUMN_DATE + " DATE NOT NULL, " + ItemEntry.COLUMN_MSG + " TEXT NOT NULL, " + ItemEntry.COLUMN_ALARMID + " LONG NOT NULL);";
//        final String SQL_CREATE_CONTACTS_TABLE = "CREATE TABLE " + ItemEntry.TABLE_NAME + " (" + ItemEntry._ID + " VARCHAR(40) PRIMARY KEY AUTOINCREMENT, " +
//                ItemEntry.COLUMN_NUMBER + " VARCHAR(30) NOT NULL, " + ItemEntry.COLUMN_TIME + " TIME NOT NULL, " + ItemEntry.COLUMN_DATE + " DATE NOT NULL, " + ItemEntry.COLUMN_MSG + " TEXT NOT NULL);";
//        final String SQL_CREATE_CONTACTS_TABLE = "CREATE TABLE " + ItemEntry.TABLE_NAME + " (" +
//                ItemEntry.COLUMN_NUMBER + " VARCHAR(30) NOT NULL, " + ItemEntry.COLUMN_TIME + " TIME NOT NULL, " + ItemEntry.COLUMN_DATE + " DATE NOT NULL, " + ItemEntry.COLUMN_MSG + " TEXT NOT NULL,  PRIMARY KEY(" + ItemEntry.COLUMN_NUMBER + ", " + ItemEntry.COLUMN_TIME +", " + ItemEntry.COLUMN_DATE + "));";


        db.execSQL(SQL_CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ItemEntry.TABLE_NAME);
        onCreate(db);
    }


    //TODO: addData should be in one full String cointaining info, insert into table (like)
    // Didn't work

    public boolean addData(String number, String time, String date, String text, long alarmId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(ItemEntry.COLUMN_NUMBER, number);
        cv.put(ItemEntry.COLUMN_TIME, time);
        cv.put(ItemEntry.COLUMN_DATE, date);
        cv.put(ItemEntry.COLUMN_MSG, text);
        cv.put(ItemEntry.COLUMN_ALARMID, alarmId);
//          db.execSQL("INSERT INTO " + ItemEntry.TABLE_NAME + " VALUES(" + number + ", " + time + ", " + date + ", " + text + ")");
        long res = db.insert(ItemEntry.TABLE_NAME, null, cv);
        return res != -1;
    }

//    public boolean updateId(boolean res, )

    public Cursor getData() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + ItemEntry.TABLE_NAME;
        return db.rawQuery(query, null);
    }

    public boolean removeData(String number, String time, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
//        String query = "DELETE FROM " + ItemEntry.TABLE_NAME + " WHERE " + ItemEntry.COLUMN_NUMBER + " = '" + number + "' AND " + ItemEntry.COLUMN_DATE + " = '" + date + "' AND " + ItemEntry.COLUMN_TIME + " = '" + time + "' ";
//        return db.delete(ItemEntry.TABLE_NAME, ItemEntry.COLUMN_NUMBER +"=? and " + ItemEntry.COLUMN_TIME + "=? and " + ItemEntry.COLUMN_DATE + "=?",new String[]{number, time, date}) > 0;
//        db.rawQuery(query, null);
        long res = db.delete(ItemEntry.TABLE_NAME, ItemEntry.COLUMN_DATE + "=? AND " + ItemEntry.COLUMN_TIME + "=? AND " + ItemEntry.COLUMN_NUMBER + "=?", new String[]{date, time, number});
        return res != -1;
    }

    public boolean removeDataById(long alarmId) {
        SQLiteDatabase db = this.getWritableDatabase();
        long res = db.delete(ItemEntry.TABLE_NAME, ItemEntry.COLUMN_ALARMID + "=?", new String[] {String.valueOf(alarmId)});
        return res != -1;
    }

    //
//    public void deleteAllData() {
//        SQLiteDatabase db = this.getWritableDatabase();
//        String query = "DELETE FROM " + ItemEntry.TABLE_NAME;
//        db.rawQuery(query, null);
//    }


}
