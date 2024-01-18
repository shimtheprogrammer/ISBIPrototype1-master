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

import javax.microedition.khronos.opengles.GL;

/**
 * Created by MRuto on 2/1/2017.
 */

public class GlobalVariablesHandler extends SQLiteOpenHelper {

    private static final String TAG = "Global-V DB Handler:";

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "ISBI";

    // Contacts table name
    private static String TABLE_NAME = "GlobalVariable";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_VARIABLE = "variable";
    private static final String KEY_TOTAL = "total";

    public GlobalVariablesHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        TABLE_NAME = "GlobalVariable" + LoginActivity.database;
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_VARIABLE + " TEXT,"
                + KEY_TOTAL + " INTEGER" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
        //db.close();

    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        TABLE_NAME = "GlobalVariable" + LoginActivity.database;
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        // Create tables again
        onCreate(db);
    }

    public void dropTable() {
        TABLE_NAME = "GlobalVariable" + LoginActivity.database;
        SQLiteDatabase db = this.getWritableDatabase();

        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        // Create tables again
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adding new inventory
    public void addVariable(GlobalVariables inventory) {
        TABLE_NAME = "GlobalVariable" + LoginActivity.database;
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_VARIABLE, inventory.getVariable()); // product
        values.put(KEY_TOTAL, inventory.getTotal()); // total

        // Inserting Row
        db.insert(TABLE_NAME, null, values);
        db.close(); // Closing database connection
    }

    // Getting single Inventory
    public GlobalVariables getVariable(int id) {
        TABLE_NAME = "GlobalVariable" + LoginActivity.database;
        SQLiteDatabase db = this.getReadableDatabase();

        GlobalVariables inventory = new GlobalVariables();

        Cursor cursor = db.query(TABLE_NAME, new String[] { KEY_ID,
                        KEY_VARIABLE, KEY_TOTAL }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();

            inventory = new GlobalVariables(Integer.parseInt(cursor.getString(0)), cursor.getString(1),
                    Integer.parseInt(cursor.getString(2)));
        }

        cursor.close();
        db.close();
        // return cash
        return inventory;
    }

    // Getting single Inventory
    public GlobalVariables getVariableByName(String name) {
        TABLE_NAME = "GlobalVariable" + LoginActivity.database;
        SQLiteDatabase db = this.getReadableDatabase();

        GlobalVariables inventory = new GlobalVariables();

        Cursor cursor = db.query(TABLE_NAME, new String[] { KEY_ID,
                        KEY_VARIABLE, KEY_TOTAL }, KEY_VARIABLE + "=?",
                new String[] { String.valueOf(name) }, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();

            inventory = new GlobalVariables(Integer.parseInt(cursor.getString(0)), cursor.getString(1),
                    Integer.parseInt(cursor.getString(2)));
        } else {
            inventory = new GlobalVariables(0, "", 0);
        }

        cursor.close();
        db.close();
        // return cash
        return inventory;
    }

    // Getting All Inventory
    public List<GlobalVariables> getAllVariable() {
        TABLE_NAME = "GlobalVariable" + LoginActivity.database;
        List<GlobalVariables> inventoryList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                GlobalVariables inventory = new GlobalVariables();
                inventory.setID(Integer.parseInt(cursor.getString(0)));
                inventory.setVariable(cursor.getString(1));
                inventory.setTotal(Integer.parseInt(cursor.getString(2)));
                // Adding person to list
                inventoryList.add(inventory);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        // return person list
        return inventoryList;
    }

    // Updating single Inventory
    public int updateVariable(GlobalVariables inventory) {
        TABLE_NAME = "GlobalVariable" + LoginActivity.database;
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_VARIABLE, inventory.getVariable());
        values.put(KEY_TOTAL, inventory.getTotal());

        int success = db.update(TABLE_NAME, values, KEY_ID + " = ?", new String[] { String.valueOf(inventory.getID())});

        db.close();
        // updating row
        return success;
    }

    // Updating single Inventory removing
    public void updateAddVariable(String name, int numb) {
        TABLE_NAME = "GlobalVariable" + LoginActivity.database;
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query(TABLE_NAME, new String[]{KEY_ID,
                        KEY_VARIABLE, KEY_TOTAL}, KEY_VARIABLE + "=?",
                new String[]{String.valueOf(name)}, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();

            int original = Integer.parseInt(cursor.getString(2));

            int total = original + numb;

            String updateQuery = "UPDATE " + TABLE_NAME + " SET "
                    + KEY_TOTAL + " = " + "'" + total + "'"
                    + " WHERE " + KEY_VARIABLE + " = " + "'" + name + "'";

            // Writing Contacts to log
            Log.i(TAG, "adding to variable: " + updateQuery);

            db.execSQL(updateQuery);
        }

        cursor.close();
        db.close();
    }

    // Updating single Inventory adding
    public void updateSubVariable(String name, int tot) {
        TABLE_NAME = "GlobalVariable" + LoginActivity.database;
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query(TABLE_NAME, new String[] { KEY_ID,
                        KEY_VARIABLE, KEY_TOTAL }, KEY_VARIABLE + "=?",
                new String[] { String.valueOf(name) }, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();

            int total = Integer.parseInt(cursor.getString(2));

            int newTotal = (total - tot);

            String updateQuery = "UPDATE " + TABLE_NAME + " SET "
                    + KEY_TOTAL + " = " + "'" + (newTotal) + "'"
                    + " WHERE " + KEY_VARIABLE + " = " + "'" + name + "'";

            // Writing Contacts to log
            Log.i(TAG, "Subtracting from variable: " + updateQuery);

            db.execSQL(updateQuery);
        }

        cursor.close();
        db.close();
    }

    // Deleting single inventory
    public void deleteVariable(GlobalVariables inventory) {
        TABLE_NAME = "GlobalVariable" + LoginActivity.database;
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, KEY_ID + " = ?",
                new String[] { String.valueOf(inventory.getID()) });
        db.close();
    }


    // Getting inventory Count
    public int getVariableCount() {
        TABLE_NAME = "GlobalVariable" + LoginActivity.database;
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
        TABLE_NAME = "GlobalVariable" + LoginActivity.database;
        SQLiteDatabase db = this.getWritableDatabase();

        String CREATE_CONTACTS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_VARIABLE + " TEXT,"
                + KEY_TOTAL + " INTEGER" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);

        db.close();
    }
}
