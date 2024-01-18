package com.prototype.isbi.isbiprototype1.databaseHandlers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MRuto on 8/28/2017.
 */

public class AccountDetailsHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "ISBI";

    // Contacts table name
    private static final String TABLE_NAME = "AccountDetails";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PIN = "pin";

    public AccountDetailsHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_EMAIL + " TEXT," + KEY_PIN + " INTEGER" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);

        //db.close();
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        // Create tables again
        onCreate(db);
    }

    public void dropTable() {
        SQLiteDatabase db = this.getWritableDatabase();

        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        // Create tables again
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adding new account
    public void addAccount(AccountDetails accountDetails) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_EMAIL, accountDetails.getEmail()); // Phone
        values.put(KEY_PIN, accountDetails.getPin()); // Pin

        // Inserting Row
        db.insert(TABLE_NAME, null, values);
        db.close(); // Closing database connection
    }

    // Getting single account
    public AccountDetails getAccount(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        AccountDetails accountDetails = new AccountDetails();

        Cursor cursor = db.query(TABLE_NAME, new String[] { KEY_ID,
                        KEY_EMAIL, KEY_PIN }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();

            accountDetails = new AccountDetails(Integer.parseInt(cursor.getString(0)),
                    cursor.getString(1), Integer.parseInt(cursor.getString(2)));
        }

        cursor.close();
        db.close();
        // return person
        return accountDetails;
    }

    public AccountDetails getAccountByName(String name) {
        SQLiteDatabase db = this.getReadableDatabase();

        AccountDetails accountDetails = new AccountDetails();

        Cursor cursor = db.query(TABLE_NAME, new String[] { KEY_ID,
                        KEY_EMAIL, KEY_PIN }, KEY_EMAIL + "=?",
                new String[] { String.valueOf(name) }, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();

            accountDetails = new AccountDetails(Integer.parseInt(cursor.getString(0)),
                    cursor.getString(1), Integer.parseInt(cursor.getString(2)));
        }

        cursor.close();
        db.close();
        // return person
        return accountDetails;
    }

    // Getting All AccountDetails
    public List<AccountDetails> getAllAccounts() {
        List<AccountDetails> accountDetailsList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                AccountDetails accountDetails = new AccountDetails();
                accountDetails.setID(Integer.parseInt(cursor.getString(0)));
                accountDetails.setEmail(cursor.getString(1));
                accountDetails.setPin(Integer.parseInt(cursor.getString(2)));

                // Adding person to list
                accountDetailsList.add(accountDetails);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        // return person list
        return accountDetailsList;
    }

    // Updating single account
    public int updateAccount(AccountDetails accountDetails) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_EMAIL, accountDetails.getEmail());
        values.put(KEY_PIN, accountDetails.getPin());

        int ret = db.update(TABLE_NAME, values, KEY_ID + " = ?",
                new String[] { String.valueOf(accountDetails.getID()) });

        db.close();
        // updating row
        return ret;
    }

    // Deleting single account
    public void deleteAccount(AccountDetails accountDetails) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, KEY_ID + " = ?",
                new String[] { String.valueOf(accountDetails.getID()) });
        db.close();
    }


    // Getting account Count
    public int getAccountCount() {
        String countQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int sum = cursor.getCount();

        int count = cursor.getCount();

        cursor.close();
        db.close();

        return count;
    }

    public void isTableExists() {
        SQLiteDatabase db = this.getWritableDatabase();

        String CREATE_CONTACTS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_EMAIL + " TEXT," + KEY_PIN + " INTEGER" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);

        db.close();
    }

}
