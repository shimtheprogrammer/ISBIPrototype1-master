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
 * Created by MRuto on 2/7/2017.
 */

public class ReUpHandler extends SQLiteOpenHelper{

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "ISBI";

    // Contacts table name
    private static String TABLE_NAME = "ReUp";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_INFO = "info";
    private static final String KEY_TOTAL = "total";
    private static final String KEY_PAYMENT = "payment";
    private static final String KEY_DATE = "date";
    public static final String KEY_FOREIGN_KEY = "foreignKey";

    public ReUpHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        TABLE_NAME = "ReUp" + LoginActivity.database;
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_INFO + " TEXT,"
                + KEY_TOTAL + " INTEGER," + KEY_PAYMENT + " TEXT," + KEY_DATE + " TEXT,"
                + KEY_FOREIGN_KEY + " INTEGER" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);

        //db.close();
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        TABLE_NAME = "ReUp" + LoginActivity.database;
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        // Create tables again
        onCreate(db);
    }

    public void dropTable() {
        TABLE_NAME = "ReUp" + LoginActivity.database;
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
    public void addReUp(ReUp reUp) {
        TABLE_NAME = "ReUp" + LoginActivity.database;
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_INFO, reUp.getInfo()); // ID/PP
        values.put(KEY_TOTAL, reUp.getTotal()); // ID/PP
        values.put(KEY_PAYMENT, reUp.getPayment()); // Pin
        values.put(KEY_DATE, reUp.getDate()); // Pin
        values.put(KEY_FOREIGN_KEY, reUp.gettForeignKey()); // Pin

        // Inserting Row
        db.insert(TABLE_NAME, null, values);
        db.close(); // Closing database connection
    }

    // Getting single person
    public ReUp getReUp(int id) {
        TABLE_NAME = "ReUp" + LoginActivity.database;
        SQLiteDatabase db = this.getReadableDatabase();

        ReUp reUp = new ReUp();

        Cursor cursor = db.query(TABLE_NAME, new String[] { KEY_ID,
                        KEY_INFO, KEY_TOTAL, KEY_PAYMENT, KEY_DATE, KEY_FOREIGN_KEY}, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();

            reUp = new ReUp(Integer.parseInt(cursor.getString(0)),
                    cursor.getString(1), Integer.parseInt(cursor.getString(2)),
                    cursor.getString(3), cursor.getString(4)
                    ,Integer.parseInt(cursor.getString(5)));
        }

        cursor.close();
        db.close();
        // return person
        return reUp;
    }

    //get using foreign key
    public List<ReUp> getByForeignKey(int foreignKey){
        TABLE_NAME = "ReUp" + LoginActivity.database;
        List<ReUp> reUpList = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_NAME + " WHERE " + KEY_FOREIGN_KEY + " = " + foreignKey;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        //loop through all rows and add to list
        if(cursor.moveToFirst()){
            do{
                ReUp reUp = new ReUp();
                reUp.setID(Integer.parseInt(cursor.getString(0)));
                reUp.setInfo(cursor.getString(1));
                reUp.setTotal(Integer.parseInt(cursor.getString(2)));
                reUp.setPayment(cursor.getString(3));
                reUp.setDate(cursor.getString(4));
                reUp.setForeignKay(Integer.parseInt(cursor.getString(5)));
                //add entry to list
                reUpList.add(reUp);
            }while(cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return reUpList;
    }

    // Getting All PersonData
    public List<ReUp> getAllReUp() {
        TABLE_NAME = "ReUp" + LoginActivity.database;
        List<ReUp> reUpList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ReUp reUp = new ReUp();
                reUp.setID(Integer.parseInt(cursor.getString(0)));
                reUp.setInfo(cursor.getString(1));
                reUp.setTotal(Integer.parseInt(cursor.getString(2)));
                reUp.setPayment(cursor.getString(3));
                reUp.setDate(cursor.getString(4));
                reUp.setForeignKay(Integer.parseInt(cursor.getString(5)));
                //add entry to list
                reUpList.add(reUp);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        // return person list
        return reUpList;
    }

    // Updating single person
    public int updateReUp(ReUp reUp) {
        TABLE_NAME = "ReUp" + LoginActivity.database;
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_INFO, reUp.getInfo());
        values.put(KEY_TOTAL, reUp.getTotal());
        values.put(KEY_PAYMENT, reUp.getPayment());
        values.put(KEY_DATE, reUp.getDate());
        values.put(KEY_FOREIGN_KEY, reUp.gettForeignKey()); // Pin

        int ret = db.update(TABLE_NAME, values, KEY_ID + " = ?",
                new String[] { String.valueOf(reUp.getID()) });

        db.close();
        // updating row
        return ret;
    }

    // Deleting single person
    public void deleteReUp(ReUp reUp) {
        TABLE_NAME = "ReUp" + LoginActivity.database;
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, KEY_ID + " = ?",
                new String[] { String.valueOf(reUp.getID()) });
        db.close();
    }

    // Getting person Count
    public int getReUpCount() {
        TABLE_NAME = "ReUp" + LoginActivity.database;
        String countQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int ret = cursor.getCount();

        cursor.close();
        db.close();

        return ret;
    }

    public void isTableExists() {
        TABLE_NAME = "ReUp" + LoginActivity.database;
        SQLiteDatabase db = this.getWritableDatabase();

        String CREATE_CONTACTS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_INFO + " TEXT,"
                + KEY_TOTAL + " INTEGER," + KEY_PAYMENT + " TEXT," + KEY_DATE + " TEXT,"
                + KEY_FOREIGN_KEY + " INTEGER" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);

        db.close();
    }

}
