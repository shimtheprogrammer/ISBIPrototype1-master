package com.prototype.isbi.isbiprototype1.databaseHandlers;

import android.content.ContentValues;
import android.content.Context;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import android.util.Log;

import com.prototype.isbi.isbiprototype1.LoginActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MRuto on 12/16/2016.
 */

public class CashHandler extends SQLiteOpenHelper {
    private static final String TAG = "CashHandler";

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "ISBI";

    // Contacts table name
    private static String TABLE_NAME = "Cash";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_ACTUAL = "actual";
    private static final String KEY_BANK = "bank";
    private static final String KEY_MPESA = "mpesa";

    public CashHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        TABLE_NAME = "Cash"  + LoginActivity.database;

        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_ACTUAL + " INTEGER,"
                + KEY_BANK + " INTEGER," + KEY_MPESA + " INTEGER" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);

        Log.d(TAG, TABLE_NAME + " created in db " + DATABASE_NAME);
        //db.close();
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        TABLE_NAME = "Cash"  + LoginActivity.database;
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        // Create tables again
        onCreate(db);

        db.close();
    }

    public void dropTable() {
        TABLE_NAME = "Cash"  + LoginActivity.database;
        SQLiteDatabase db = this.getWritableDatabase();

        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        // Create tables again
        onCreate(db);

        db.close();
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adding new row
    public void addCash(Cash cash) {
        TABLE_NAME = "Cash"  + LoginActivity.database;
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ACTUAL, cash.getActual()); // actual
        values.put(KEY_BANK, cash.getBank()); // bank
        values.put(KEY_MPESA, cash.getMpesa()); // mpesa

        Log.d(TAG, "Before executind add entry ");
        // Inserting Row
        db.insert(TABLE_NAME, null, values);

        Log.d(TAG, "After executind add entry ");
        db.close(); // Closing database connection
    }

    // Getting single row
    public Cash getCash(int id) {
        TABLE_NAME = "Cash"  + LoginActivity.database;
        SQLiteDatabase db = this.getReadableDatabase();

        Cash cash = new Cash();

        Cursor cursor = db.query(TABLE_NAME, new String[] { KEY_ID,
                        KEY_ACTUAL, KEY_BANK, KEY_MPESA }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();

            cash = new Cash(Integer.parseInt(cursor.getString(0)), Integer.parseInt(cursor.getString(1)),
                    Integer.parseInt(cursor.getString(2)), Integer.parseInt(cursor.getString(3)));
        }

        cursor.close();
        db.close();
        // return cash
        return cash;
    }

    // Getting All cash
    public List<Cash> getAllCash() {
        Log.d(TAG, "Getting All Cash from " + TABLE_NAME);
        TABLE_NAME = "Cash"  + LoginActivity.database;
        Log.d(TAG, "Now Called " + TABLE_NAME);
//        Log.d(TAG, "refrencing from " + LoginActivity.database + " table is " + TABLE_NAME);
        List<Cash> cashList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Cash cash = new Cash();
                cash.setID(Integer.parseInt(cursor.getString(0)));
                cash.setActual(Integer.parseInt(cursor.getString(1)));
                cash.setBank(Integer.parseInt(cursor.getString(2)));
                cash.setMpesa(Integer.parseInt(cursor.getString(3)));
                // Adding person to list
                cashList.add(cash);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        // return person list
        return cashList;
    }

    // Updating single cash
    public int updateCash(Cash cash) {
        TABLE_NAME = "Cash"  + LoginActivity.database;
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ACTUAL, cash.getActual());
        values.put(KEY_BANK, cash.getBank());
        values.put(KEY_MPESA, cash.getMpesa());

        int res = db.update(TABLE_NAME, values, KEY_ID + " = ?",
                new String[] { String.valueOf(cash.getID()) });

        db.close();
        // updating row
        return res;
    }

    // Updating for adding to total
    public void updateSingleCash(String acc, int amount) {
        TABLE_NAME = "Cash"  + LoginActivity.database;
        SQLiteDatabase db = this.getWritableDatabase();

        int id = 1;

        Cursor cursor = db.query(TABLE_NAME, new String[] { KEY_ID,
                        KEY_ACTUAL, KEY_BANK, KEY_MPESA }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();

            if (acc.matches("Cash")) {
                int original = Integer.parseInt(cursor.getString(1));

                String updateQuery = "UPDATE " + TABLE_NAME + " SET "
                        + KEY_ACTUAL + " = " + "'" + (original + amount) + "'"
                        + " WHERE " + KEY_ID + " = " + "'" + id + "'";

                // Writing Contacts to log
                Log.i(TAG, "Updating Cash: " + updateQuery);

                db.execSQL(updateQuery);
            } else if (acc.matches("Bank Transfer")) {
                int original = Integer.parseInt(cursor.getString(2));

                String updateQuery = "UPDATE " + TABLE_NAME + " SET "
                        + KEY_BANK + " = " + "'" + (original + amount) + "'"
                        + " WHERE " + KEY_ID + " = " + "'" + id + "'";

                // Writing Contacts to log
                Log.i(TAG, "Updating Bank Transfer: " + updateQuery);

                db.execSQL(updateQuery);
            } else if (acc.matches("MPESA")) {
                int original = Integer.parseInt(cursor.getString(3));

                String updateQuery = "UPDATE " + TABLE_NAME + " SET "
                        + KEY_MPESA + " = " + "'" + (original + amount) + "'"
                        + " WHERE " + KEY_ID + " = " + "'" + id + "'";

                // Writing Contacts to log
                Log.i(TAG, "Updating Mpesa: " + updateQuery);

                db.execSQL(updateQuery);
            }

            cursor.close();
            db.close();
        }

    }

    // Updating for subtracting from total
    public void updateSingleCashPurchase(String acc, int amount) {
        TABLE_NAME = "Cash"  + LoginActivity.database;
        SQLiteDatabase db = this.getWritableDatabase();

        int id = 1;

        Cursor cursor = db.query(TABLE_NAME, new String[] { KEY_ID,
                        KEY_ACTUAL, KEY_BANK, KEY_MPESA }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();

            if (acc.matches("Cash")) {
                int original = Integer.parseInt(cursor.getString(1));

                String updateQuery = "UPDATE " + TABLE_NAME + " SET "
                        + KEY_ACTUAL + " = " + "'" + (original - amount) + "'"
                        + " WHERE " + KEY_ID + " = " + "'" + id + "'";

                // Writing Contacts to log
                Log.i(TAG, "Removed Actual: " + updateQuery);

                db.execSQL(updateQuery);
            } else if (acc.matches("Bank Transfer")) {
                int original = Integer.parseInt(cursor.getString(2));

                String updateQuery = "UPDATE " + TABLE_NAME + " SET "
                        + KEY_BANK + " = " + "'" + (original - amount) + "'"
                        + " WHERE " + KEY_ID + " = " + "'" + id + "'";

                // Writing Contacts to log
                Log.i(TAG, "Removed Bank Transfer: " + updateQuery);

                db.execSQL(updateQuery);
            } else if (acc.matches("MPESA")) {
                int original = Integer.parseInt(cursor.getString(3));

                String updateQuery = "UPDATE " + TABLE_NAME + " SET "
                        + KEY_MPESA + " = " + "'" + (original - amount) + "'"
                        + " WHERE " + KEY_ID + " = " + "'" + id + "'";

                // Writing Contacts to log
                Log.i(TAG, "Removed Mpesa: " + updateQuery);

                db.execSQL(updateQuery);
            }
        }

        cursor.close();
        db.close();
    }

    // Deleting single cash
    public void deleteCash(Cash cash) {
        TABLE_NAME = "Cash"  + LoginActivity.database;
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, KEY_ID + " = ?",
                new String[] { String.valueOf(cash.getID()) });
        db.close();
    }


    // Getting row Count
    public int getCashCount() {
        TABLE_NAME = "Cash"  + LoginActivity.database;
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
        TABLE_NAME = "Cash"  + LoginActivity.database;
        SQLiteDatabase db = this.getWritableDatabase();

        String CREATE_CONTACTS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_ACTUAL + " INTEGER,"
                + KEY_BANK + " INTEGER," + KEY_MPESA + " INTEGER" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);

        db.close();
    }
}
