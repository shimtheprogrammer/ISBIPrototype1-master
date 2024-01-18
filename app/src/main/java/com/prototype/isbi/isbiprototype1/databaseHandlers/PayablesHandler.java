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

public class PayablesHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "ISBI";

    // Contacts table name
    private static String TABLE_NAME = "Payable";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_TO = "supplier";
    private static final String KEY_TOTAL = "total";
    private static final String KEY_DATE = "date";
    private static final String KEY_ORIGINAL_DATE = "originaldate";

    public PayablesHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        TABLE_NAME = "Payable" + LoginActivity.database;
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_TO + " TEXT,"
                + KEY_TOTAL + " INTEGER," + KEY_DATE + " TEXT," + KEY_ORIGINAL_DATE + " TEXT" + ")";//AUTOINCREMENT
        db.execSQL(CREATE_CONTACTS_TABLE);

        //db.close();
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        TABLE_NAME = "Payable" + LoginActivity.database;
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        // Create tables again
        onCreate(db);
    }

    public void dropTable() {
        TABLE_NAME = "Payable" + LoginActivity.database;
        SQLiteDatabase db = this.getWritableDatabase();

        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        // Create tables again
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adding new payable
    public void addPayable(Payable payable) {
        TABLE_NAME = "Payable" + LoginActivity.database;
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TO, payable.getTo()); // to
        values.put(KEY_TOTAL, payable.getTotal()); // total
        values.put(KEY_DATE, payable.getDate()); // date
        values.put(KEY_ORIGINAL_DATE, payable.getOriginalDate()); // date

        // Inserting Row
        db.insert(TABLE_NAME, null, values);
        db.close(); // Closing database connection
    }

    // Getting single payable
    public Payable getPayable(int id) {
        TABLE_NAME = "Payable" + LoginActivity.database;
        SQLiteDatabase db = this.getReadableDatabase();

        Payable payable = new Payable();

        Cursor cursor = db.query(TABLE_NAME, new String[] { KEY_ID, KEY_TO, KEY_TOTAL, KEY_DATE, KEY_ORIGINAL_DATE}, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();

            payable = new Payable(Integer.parseInt(cursor.getString(0)), cursor.getString(1),
                    Integer.parseInt(cursor.getString(2)), cursor.getString(3), cursor.getString(4));
        }

        cursor.close();
        db.close();
        // return
        return payable;
    }

    // Getting All Payable
    public List<Payable> getAllPayable() {
        TABLE_NAME = "Payable" + LoginActivity.database;
        List<Payable> payableList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Payable payable = new Payable();
                payable.setID(Integer.parseInt(cursor.getString(0)));
                payable.setTo(cursor.getString(1));
                payable.setTotal(Integer.parseInt(cursor.getString(2)));
                payable.setDate(cursor.getString(3));
                payable.setOriginalDate(cursor.getString(4));
                // Adding payable to list
                payableList.add(payable);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        // return payable list
        return payableList;
    }

    // Updating single Payable
    public int updatePayable(Payable payable) {
        TABLE_NAME = "Payable" + LoginActivity.database;
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TO, payable.getTo());
        values.put(KEY_TOTAL, payable.getTotal());
        values.put(KEY_DATE, payable.getDate());
        values.put(KEY_ORIGINAL_DATE, payable.getOriginalDate());

        int ret = db.update(TABLE_NAME, values, KEY_ID + " = ?",
                new String[] { String.valueOf(payable.getID()) });

        db.close();
        // updating row
        return ret;
    }

    // Deleting single payable
    public void deletepayable(Payable payable) {
        TABLE_NAME = "Payable" + LoginActivity.database;
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, KEY_ID + " = ?",
                new String[] { String.valueOf(payable.getID()) });
        db.close();
    }


    // Getting payable Count
    public int getPayableyCount() {
        TABLE_NAME = "Payable" + LoginActivity.database;
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
        TABLE_NAME = "Payable" + LoginActivity.database;
        SQLiteDatabase db = this.getWritableDatabase();

        String CREATE_CONTACTS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_TO + " TEXT,"
                + KEY_TOTAL + " INTEGER," + KEY_DATE + " TEXT," + KEY_ORIGINAL_DATE + " TEXT" + ")";//AUTOINCREMENT
        db.execSQL(CREATE_CONTACTS_TABLE);

        db.close();
    }

}
