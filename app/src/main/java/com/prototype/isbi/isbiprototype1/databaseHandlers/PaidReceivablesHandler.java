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
 * Created by MRuto on 1/18/2017.
 */

public class PaidReceivablesHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "ISBI";

    // Contacts table name
    private static String TABLE_NAME = "PaidReceivable";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_FROM = "customer";
    private static final String KEY_TOTAL = "total";
    private static final String KEY_DATE = "date";
    private static final String KEY_ORIGINAL_DATE = "originaldate";
    private static final String KEY_PAYMENT = "payment";

    public PaidReceivablesHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        TABLE_NAME = "PaidReceivable" + LoginActivity.database;
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_FROM + " TEXT,"
                + KEY_TOTAL + " REAL," + KEY_DATE + " TEXT," + KEY_ORIGINAL_DATE + " TEXT,"
                + KEY_PAYMENT + " TEXT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);

        //db.close();
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        TABLE_NAME = "PaidReceivable" + LoginActivity.database;
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        // Create tables again
        onCreate(db);
    }

    public void dropTable() {
        TABLE_NAME = "PaidReceivable" + LoginActivity.database;
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
    public void addPaidReceivable(PaidReceivable paidReceivable) {
        TABLE_NAME = "PaidReceivable" + LoginActivity.database;
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_FROM, paidReceivable.getFrom()); // receivable from
        values.put(KEY_TOTAL, paidReceivable.getTotal()); // receivable total
        values.put(KEY_DATE, paidReceivable.getDate()); // receivable date
        values.put(KEY_ORIGINAL_DATE, paidReceivable.getOriginalDate()); // receivable date
        values.put(KEY_PAYMENT, paidReceivable.getPayment()); // receivable date

        // Inserting Row
        db.insert(TABLE_NAME, null, values);
        db.close(); // Closing database connection
    }

    // Getting single Receivable
    public PaidReceivable getPaidReceivable(int id) {
        TABLE_NAME = "PaidReceivable" + LoginActivity.database;
        SQLiteDatabase db = this.getReadableDatabase();

        PaidReceivable paidReceivable = new PaidReceivable();

        Cursor cursor = db.query(TABLE_NAME, new String[] { KEY_ID, KEY_FROM,
                        KEY_TOTAL, KEY_DATE, KEY_ORIGINAL_DATE, KEY_PAYMENT }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();

            paidReceivable = new PaidReceivable(Integer.parseInt(cursor.getString(0)), cursor.getString(1),
                    Integer.parseInt(cursor.getString(2)), cursor.getString(3),
                    cursor.getString(4), cursor.getString(5));
        }

        cursor.close();
        db.close();
        // return cash
        return paidReceivable;
    }

    // Getting All Receivable
    public List<PaidReceivable> getAllPaidReceivable() {
        TABLE_NAME = "PaidReceivable" + LoginActivity.database;
        List<PaidReceivable> paidReceivableList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                PaidReceivable paidReceivable = new PaidReceivable();
                paidReceivable.setID(Integer.parseInt(cursor.getString(0)));
                paidReceivable.setFrom(cursor.getString(1));
                paidReceivable.setTotal(Integer.parseInt(cursor.getString(2)));
                paidReceivable.setDate(cursor.getString(3));
                paidReceivable.setOriginalDate(cursor.getString(4));
                paidReceivable.setPayment(cursor.getString(5));
                // Adding Receivable to list
                paidReceivableList.add(paidReceivable);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        // return Receivable list
        return paidReceivableList;
    }

    // Updating single Receivable
    public int updatePaidReceivable(PaidReceivable paidReceivable) {
        TABLE_NAME = "PaidReceivable" + LoginActivity.database;
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_FROM, paidReceivable.getFrom());
        values.put(KEY_TOTAL, paidReceivable.getTotal());
        values.put(KEY_DATE, paidReceivable.getDate());
        values.put(KEY_ORIGINAL_DATE, paidReceivable.getOriginalDate());
        values.put(KEY_PAYMENT, paidReceivable.getPayment());

        int ret = db.update(TABLE_NAME, values, KEY_ID + " = ?",
                new String[] { String.valueOf(paidReceivable.getID()) });

        db.close();
        // updating row
        return ret;
    }

    // Deleting single Receivable
    public void deletePaidReceivable(PaidReceivable paidReceivable) {
        TABLE_NAME = "PaidReceivable" + LoginActivity.database;
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, KEY_ID + " = ?",
                new String[] { String.valueOf(paidReceivable.getID()) });
        db.close();
    }


    // Getting Receivable Count
    public int getPaidReceivableCount() {
        TABLE_NAME = "PaidReceivable" + LoginActivity.database;
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
        TABLE_NAME = "PaidReceivable" + LoginActivity.database;

        SQLiteDatabase db = this.getWritableDatabase();

        String CREATE_CONTACTS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_FROM + " TEXT,"
                + KEY_TOTAL + " INTEGER," + KEY_DATE + " TEXT," + KEY_ORIGINAL_DATE + " TEXT,"
                + KEY_PAYMENT + " TEXT"+ ")";

        db.execSQL(CREATE_CONTACTS_TABLE);

        db.close();
    }

}
