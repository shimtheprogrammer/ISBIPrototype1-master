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
 * Created by MRuto on 1/31/2017.
 */

public class OwnerHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "ISBI";

    // Contacts table name
    private static String TABLE_NAME = "owner";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_TYPE = "type";
    private static final String KEY_TOTAL = "total";
    private static final String KEY_PAYMENT = "payment";
    private static final String KEY_INFO = "info";
    private static final String KEY_DATE = "date";

    public OwnerHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        TABLE_NAME = "owner" + LoginActivity.database;
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_TYPE + " TEXT,"
                + KEY_TOTAL + " INTEGER," + KEY_PAYMENT + " TEXT," + KEY_INFO + " TEXT,"
                + KEY_DATE + " TEXT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);

        //db.close();
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        TABLE_NAME = "owner" + LoginActivity.database;
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        // Create tables again
        onCreate(db);
    }

    public void dropTable() {
        TABLE_NAME = "owner" + LoginActivity.database;
        SQLiteDatabase db = this.getWritableDatabase();

        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        // Create tables again
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adding new person
    public void addOwner(Owner expenses) {
        TABLE_NAME = "owner" + LoginActivity.database;
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TYPE, expenses.getType()); // Name
        values.put(KEY_TOTAL, expenses.getTotal()); // ID/PP
        values.put(KEY_PAYMENT, expenses.getPayment()); // Pin
        values.put(KEY_INFO, expenses.getInfo()); // ID/PP
        values.put(KEY_DATE, expenses.getDate()); // Pin

        // Inserting Row
        db.insert(TABLE_NAME, null, values);
        db.close(); // Closing database connection
    }

    // Getting single person
    public Owner getOwner(int id) {
        TABLE_NAME = "owner" + LoginActivity.database;
        SQLiteDatabase db = this.getReadableDatabase();

        Owner expenses = new Owner();

        Cursor cursor = db.query(TABLE_NAME, new String[] { KEY_ID,
                        KEY_TYPE, KEY_TOTAL, KEY_PAYMENT, KEY_INFO, KEY_DATE }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();

            expenses = new Owner(Integer.parseInt(cursor.getString(0)),
                    cursor.getString(1), Integer.parseInt(cursor.getString(2)),
                    cursor.getString(3), cursor.getString(5), cursor.getString(5));
        }

        cursor.close();
        db.close();
        // return person
        return expenses;
    }

    // Getting All PersonData
    public List<Owner> getAllOwner() {
        TABLE_NAME = "owner" + LoginActivity.database;
        List<Owner> expensesList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Owner expenses = new Owner();
                expenses.setID(Integer.parseInt(cursor.getString(0)));
                expenses.setType(cursor.getString(1));
                expenses.setTotal(Integer.parseInt(cursor.getString(2)));
                expenses.setPayment(cursor.getString(3));
                expenses.setInfo(cursor.getString(4));
                expenses.setDate(cursor.getString(5));
                // Adding person to list
                expensesList.add(expenses);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        // return person list
        return expensesList;
    }

    // Updating single person
    public int updateOwner(Owner expenses) {
        TABLE_NAME = "owner" + LoginActivity.database;
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TYPE, expenses.getType());
        values.put(KEY_TOTAL, expenses.getTotal());
        values.put(KEY_PAYMENT, expenses.getPayment());
        values.put(KEY_INFO, expenses.getInfo());
        values.put(KEY_DATE, expenses.getDate());

        int ret = db.update(TABLE_NAME, values, KEY_ID + " = ?",
                new String[] { String.valueOf(expenses.getID()) });

        db.close();
        // updating row
        return ret;
    }

    // Deleting single person
    public void deleteOwner(Owner expenses) {
        TABLE_NAME = "owner" + LoginActivity.database;
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, KEY_ID + " = ?",
                new String[] { String.valueOf(expenses.getID()) });
        db.close();
    }


    // Getting person Count
    public int getOwnerCount() {
        TABLE_NAME = "owner" + LoginActivity.database;
        String countQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();

        cursor.close();
        db.close();

        return count;
    }

    public void isTableExists() {
        TABLE_NAME = "owner" + LoginActivity.database;
        SQLiteDatabase db = this.getWritableDatabase();

        String CREATE_CONTACTS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_TYPE + " TEXT,"
                + KEY_TOTAL + " INTEGER," + KEY_PAYMENT + " TEXT," + KEY_INFO + " TEXT,"
                + KEY_DATE + " TEXT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);

        db.close();
    }

}
