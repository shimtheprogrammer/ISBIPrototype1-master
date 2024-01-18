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
 * Created by MRuto on 5/4/2017.
 */

public class MixHandler extends SQLiteOpenHelper {

    private static final String TAG = "MixHandler";

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "ISBI";

    // Contacts table name
    private static String TABLE_NAME = "Mix";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_FLAG = "flag";
    private static final String KEY_CREDIT = "credit";
    private static final String KEY_ACTUAL = "actual";
    private static final String KEY_BANK = "bank";
    private static final String KEY_MPESA = "mpesa";
    private static final String KEY_BANKFEE = "bankfee";
    private static final String KEY_MPESAFEE = "mpesafee";

    public MixHandler(Context context) {
        super(context, DATABASE_NAME , null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        TABLE_NAME = "Mix" + LoginActivity.database;
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_FLAG + " TEXT," +KEY_CREDIT + " INTEGER,"
                + KEY_ACTUAL + " INTEGER," + KEY_BANK + " INTEGER," + KEY_MPESA + " INTEGER,"
                + KEY_BANKFEE + " INTEGER," + KEY_MPESAFEE + " INTEGER" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);

        Log.d(TAG, TABLE_NAME + " created in db " + DATABASE_NAME);
        //db.close();
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        TABLE_NAME = "Mix" + LoginActivity.database;
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        // Create tables again
        onCreate(db);

        db.close();
    }

    public void dropTable() {
        TABLE_NAME = "Mix" + LoginActivity.database;
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
    public void addMix(Mix mix) {
        TABLE_NAME = "Mix" + LoginActivity.database;
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_FLAG, mix.getFlag());
        values.put(KEY_CREDIT, mix.getCredit());
        values.put(KEY_ACTUAL, mix.getCash()); // actual
        values.put(KEY_BANK, mix.getBank()); // bank
        values.put(KEY_MPESA, mix.getMpesa()); // mpesa
        values.put(KEY_BANKFEE, mix.getBankFee()); // bank
        values.put(KEY_MPESAFEE, mix.getMpesaFee()); // mpesa

        Log.d(TAG, "Before executind add entry ");
        // Inserting Row
        db.insert(TABLE_NAME, null, values);

        Log.d(TAG, "After executind add entry ");
        db.close(); // Closing database connection
    }

    // Getting single row
    public Mix getMix(int id) {
        TABLE_NAME = "Mix" + LoginActivity.database;
        SQLiteDatabase db = this.getReadableDatabase();

        Mix mix = new Mix();

        Cursor cursor = db.query(TABLE_NAME, new String[] { KEY_ID, KEY_FLAG, KEY_CREDIT,
                        KEY_ACTUAL, KEY_BANK, KEY_MPESA, KEY_BANKFEE, KEY_MPESAFEE }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();

            mix = new Mix(Integer.parseInt(cursor.getString(0)), cursor.getString(1), Integer.parseInt(cursor.getString(2)),
                    Integer.parseInt(cursor.getString(3)), Integer.parseInt(cursor.getString(4)), Integer.parseInt(cursor.getString(5)),
                    Integer.parseInt(cursor.getString(6)), Integer.parseInt(cursor.getString(7)));
        }

        cursor.close();
        db.close();
        // return cash
        return mix;
    }

    // Getting All cash
    public List<Mix> getAllMix() {
        TABLE_NAME = "Mix" + LoginActivity.database;
        List<Mix> mixList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Mix mix = new Mix();
                mix.setID(Integer.parseInt(cursor.getString(0)));
                mix.setFlag(cursor.getString(1));
                mix.setCredit(Integer.parseInt(cursor.getString(2)));
                mix.setCash(Integer.parseInt(cursor.getString(3)));
                mix.setBank(Integer.parseInt(cursor.getString(4)));
                mix.setMpesa(Integer.parseInt(cursor.getString(5)));
                mix.setBankFee(Integer.parseInt(cursor.getString(6)));
                mix.setMpesaFee(Integer.parseInt(cursor.getString(7)));
                // Adding person to list
                mixList.add(mix);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        // return person list
        return mixList;
    }

    // Updating single cash
    public int updateMix(Mix mix) {
        TABLE_NAME = "Mix" + LoginActivity.database;
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_FLAG, mix.getFlag());
        values.put(KEY_CREDIT, mix.getCredit());
        values.put(KEY_ACTUAL, mix.getCash()); // actual
        values.put(KEY_BANK, mix.getBank()); // bank
        values.put(KEY_MPESA, mix.getMpesa()); // mpesa
        values.put(KEY_BANKFEE, mix.getBankFee()); // bank
        values.put(KEY_MPESAFEE, mix.getMpesaFee()); // mpesa

        int res = db.update(TABLE_NAME, values, KEY_ID + " = ?",
                new String[] { String.valueOf(mix.getID()) });

        db.close();
        // updating row
        return res;
    }

    // Deleting single cash
    public void deleteMix(Mix mix) {
        TABLE_NAME = "Mix" + LoginActivity.database;
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, KEY_ID + " = ?",
                new String[] { String.valueOf(mix.getID()) });
        db.close();
    }


    // Getting row Count
    public int getMixCount() {
        TABLE_NAME = "Mix" + LoginActivity.database;
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
        TABLE_NAME = "Mix" + LoginActivity.database;
        SQLiteDatabase db = this.getWritableDatabase();

        String CREATE_CONTACTS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_FLAG + " TEXT," +KEY_CREDIT + " INTEGER,"
                + KEY_ACTUAL + " INTEGER," + KEY_BANK + " INTEGER," + KEY_MPESA + " INTEGER,"
                + KEY_BANKFEE + " INTEGER," + KEY_MPESAFEE + " INTEGER" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);

        db.close();
    }
}
