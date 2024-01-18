package com.prototype.isbi.isbiprototype1.databaseHandlers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.prototype.isbi.isbiprototype1.LoginActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MRuto on 12/18/2016.
 */

public class ReceivablesHandler extends SQLiteOpenHelper{

        // All Static variables
        // Database Version
        private static final int DATABASE_VERSION = 1;

        // Database Name
        private static final String DATABASE_NAME = "ISBI";

        // Contacts table name
        private static String TABLE_NAME = "Receivable";

        // Contacts Table Columns names
        private static final String KEY_ID = "id";
        private static final String KEY_FROM = "customer";
        private static final String KEY_TOTAL = "total";
        private static final String KEY_DATE = "date";
        private static final String KEY_ORIGINAL_DATE = "originaldate";

        public ReceivablesHandler(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        // Creating Tables
        @Override
        public void onCreate(SQLiteDatabase db) {
            TABLE_NAME = "Receivable" + LoginActivity.database;
            String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                    + KEY_ID + " INTEGER PRIMARY KEY," + KEY_FROM + " TEXT,"
                    + KEY_TOTAL + " REAL," + KEY_DATE + " TEXT," + KEY_ORIGINAL_DATE + " TEXT" + ")";//AUTOINCREMENT
            db.execSQL(CREATE_CONTACTS_TABLE);

            //db.close();
        }

        // Upgrading database
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            TABLE_NAME = "Receivable" + LoginActivity.database;
            // Drop older table if existed
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

            // Create tables again
            onCreate(db);
        }

        public void dropTable() {
            TABLE_NAME = "Receivable" + LoginActivity.database;
            SQLiteDatabase db = this.getWritableDatabase();

            // Drop older table if existed
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

            // Create tables again
            onCreate(db);
        }

        /**
         * All CRUD(Create, Read, Update, Delete) Operations
         */

        // Adding new Receivable
        public void addReceivable(Receivable receivable) {
            TABLE_NAME = "Receivable" + LoginActivity.database;
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(KEY_FROM, receivable.getFrom()); // receivable from
            values.put(KEY_TOTAL, receivable.getTotal()); // receivable total
            values.put(KEY_DATE, receivable.getDate()); // receivable date
            values.put(KEY_ORIGINAL_DATE, receivable.getOriginalDate()); // receivable date

            // Inserting Row
            db.insert(TABLE_NAME, null, values);
            db.close(); // Closing database connection
        }

        // Getting single Receivable
        public Receivable getReceivable(int id) {
            TABLE_NAME = "Receivable" + LoginActivity.database;
            SQLiteDatabase db = this.getReadableDatabase();

            Receivable receivable = new Receivable();

            Cursor cursor = db.query(TABLE_NAME, new String[] { KEY_ID, KEY_FROM,
                            KEY_TOTAL, KEY_DATE, KEY_ORIGINAL_DATE }, KEY_ID + "=?",
                    new String[] { String.valueOf(id) }, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();

                receivable = new Receivable(Integer.parseInt(cursor.getString(0)), cursor.getString(1),
                        Integer.parseInt(cursor.getString(2)), cursor.getString(3), cursor.getString(4));
            }

            cursor.close();
            db.close();
            // return cash
            return receivable;
        }

        // Getting All Receivable
        public List<Receivable> getAllReceivable() {
            TABLE_NAME = "Receivable" + LoginActivity.database;
            List<Receivable> receivableList = new ArrayList<>();
            // Select All Query
            String selectQuery = "SELECT  * FROM " + TABLE_NAME;

            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    Receivable receivable = new Receivable();
                    receivable.setID(Integer.parseInt(cursor.getString(0)));
                    receivable.setFrom(cursor.getString(1));
                    receivable.setTotal(Integer.parseInt(cursor.getString(2)));
                    receivable.setDate(cursor.getString(3));
                    receivable.setOriginalDate(cursor.getString(4));
                    // Adding Receivable to list
                    receivableList.add(receivable);
                } while (cursor.moveToNext());
            }

            cursor.close();
            db.close();
            // return Receivable list
            return receivableList;
        }

        // Updating single Receivable
        public int updateReceivable(Receivable receivable) {
            TABLE_NAME = "Receivable" + LoginActivity.database;
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(KEY_FROM, receivable.getFrom());
            values.put(KEY_TOTAL, receivable.getTotal());
            values.put(KEY_DATE, receivable.getDate());
            values.put(KEY_ORIGINAL_DATE, receivable.getOriginalDate());

            int ret = db.update(TABLE_NAME, values, KEY_ID + " = ?",
                    new String[] { String.valueOf(receivable.getID()) });

            db.close();
            // updating row
            return ret;
        }

        // Deleting single Receivable
        public void deleteReceivable(Receivable receivable) {
            TABLE_NAME = "Receivable" + LoginActivity.database;
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(TABLE_NAME, KEY_ID + " = ?",
                    new String[] { String.valueOf(receivable.getID()) });
            db.close();
        }


        // Getting Receivable Count
        public int getReceivableCount() {
            TABLE_NAME = "Receivable" + LoginActivity.database;
            String countQuery = "SELECT  * FROM " + TABLE_NAME;
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(countQuery, null);

            int count = cursor.getCount();

            cursor.close();
            db.close();

            return count;
        }

    //checkif table exists
    public void isTableExists() {
        TABLE_NAME = "Receivable" + LoginActivity.database;

        SQLiteDatabase db = this.getWritableDatabase();

        String CREATE_CONTACTS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_FROM + " TEXT,"
                + KEY_TOTAL + " INTEGER," + KEY_DATE + " TEXT," + KEY_ORIGINAL_DATE + " TEXT" + ")";//AUTOINCREMENT

        db.execSQL(CREATE_CONTACTS_TABLE);

        db.close();
    }
}
