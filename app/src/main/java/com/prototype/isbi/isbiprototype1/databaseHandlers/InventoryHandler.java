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

public class InventoryHandler extends SQLiteOpenHelper {

    private static final String TAG = "Inventory DB Handler:";

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "ISBI";

    // Contacts table name
    private static String TABLE_NAME = "Inventory";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_PRODUCT = "product";
    private static final String KEY_NUMB = "number";
    private static final String KEY_UNIT = "units";
    private static final String KEY_COST = "cost";
    private static final String KEY_TOTAL = "total";
    private static final String KEY_SPRICE = "SallPrice";

    public InventoryHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        TABLE_NAME = "Inventory" + LoginActivity.database;
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_PRODUCT + " TEXT,"
                + KEY_NUMB + " INTEGER," + KEY_UNIT + " TEXT," + KEY_COST + " REAL,"
                + KEY_TOTAL + " INTEGER," + KEY_SPRICE + " INTEGER" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
        //db.close();

    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        TABLE_NAME = "Inventory" + LoginActivity.database;
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        // Create tables again
        onCreate(db);
    }

    public void dropTable() {
        TABLE_NAME = "Inventory" + LoginActivity.database;
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
    public void addInventory(Inventory inventory) {
        TABLE_NAME = "Inventory" + LoginActivity.database;
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PRODUCT, inventory.getProduct()); // product
        values.put(KEY_NUMB, inventory.getNumber()); // number
        values.put(KEY_UNIT, inventory.getUnit());
        values.put(KEY_COST, inventory.getCost()); // cost
        values.put(KEY_TOTAL, inventory.getTotal()); // total
        values.put(KEY_SPRICE, inventory.getSPrice());

        // Inserting Row
        db.insert(TABLE_NAME, null, values);
        db.close(); // Closing database connection
    }

    // Getting single Inventory
    public Inventory getInventory(int id) {
        TABLE_NAME = "Inventory" + LoginActivity.database;
        SQLiteDatabase db = this.getReadableDatabase();

        Inventory inventory = new Inventory();

        Cursor cursor = db.query(TABLE_NAME, new String[] { KEY_ID,
                        KEY_PRODUCT, KEY_NUMB, KEY_UNIT, KEY_COST, KEY_TOTAL, KEY_SPRICE }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();

            inventory = new Inventory(Integer.parseInt(cursor.getString(0)), cursor.getString(1),
                    Integer.parseInt(cursor.getString(2)), cursor.getString(3),
                    Double.parseDouble(cursor.getString(4)), Integer.parseInt(cursor.getString(5)),
                    Integer.parseInt(cursor.getString(6)));
        }

        cursor.close();
        db.close();
        // return cash
        return inventory;
    }

    // Getting single Inventory
    public Inventory getInventoryByProduct(String product) {
        TABLE_NAME = "Inventory" + LoginActivity.database;
        if(!(product.matches("--Delivery--"))){
            SQLiteDatabase db = this.getReadableDatabase();

            Inventory inventory = new Inventory();

            Cursor cursor = db.query(TABLE_NAME, new String[]{KEY_ID,
                            KEY_PRODUCT, KEY_NUMB, KEY_UNIT, KEY_COST, KEY_TOTAL, KEY_SPRICE}, KEY_PRODUCT + "=?",
                    new String[]{String.valueOf(product)}, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();

                inventory = new Inventory(Integer.parseInt(cursor.getString(0)), cursor.getString(1),
                        Integer.parseInt(cursor.getString(2)), cursor.getString(3),
                        Double.parseDouble(cursor.getString(4)), Integer.parseInt(cursor.getString(5)),
                        Integer.parseInt(cursor.getString(6)));
            }

            cursor.close();
            db.close();
            // return cash
            return inventory;
        }
        Inventory inventory = new Inventory(0, "", 0, "", 0, 0, 0);
        return inventory;
    }

    // Getting All Inventory
    public List<Inventory> getAllInventory() {
        TABLE_NAME = "Inventory" + LoginActivity.database;
        List<Inventory> inventoryList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Inventory inventory = new Inventory();
                inventory.setID(Integer.parseInt(cursor.getString(0)));
                inventory.setProduct(cursor.getString(1));
                inventory.setNumber(Integer.parseInt(cursor.getString(2)));
                inventory.setUnit(cursor.getString(3));
                inventory.setCost(Double.parseDouble(cursor.getString(4)));
                inventory.setTotal(Integer.parseInt(cursor.getString(5)));
                inventory.setSPrice(Integer.parseInt(cursor.getString(6)));
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
    public int updateInventory(Inventory inventory) {
        TABLE_NAME = "Inventory" + LoginActivity.database;
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PRODUCT, inventory.getProduct());
        values.put(KEY_NUMB, inventory.getNumber());
        values.put(KEY_UNIT, inventory.getUnit());
        values.put(KEY_COST, inventory.getCost()); // cost
        values.put(KEY_TOTAL, inventory.getTotal()); // total
        values.put(KEY_SPRICE, inventory.getSPrice());

        int success = db.update(TABLE_NAME, values, KEY_ID + " = ?", new String[] { String.valueOf(inventory.getID())});

        db.close();
        // updating row
        return success;
    }

    // Updating single Inventory removing
    public void updateNumberInventory(String product, int numb) {
        TABLE_NAME = "Inventory" + LoginActivity.database;
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query(TABLE_NAME, new String[]{KEY_ID,
                        KEY_PRODUCT, KEY_NUMB, KEY_UNIT, KEY_COST, KEY_TOTAL, KEY_SPRICE}, KEY_PRODUCT + "=?",
                new String[]{String.valueOf(product)}, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();

            int original = Integer.parseInt(cursor.getString(2));
            double cost = Double.parseDouble(cursor.getString(4));

            int total = (int) Math.round((original - numb) * cost);

            String updateQuery = "UPDATE " + TABLE_NAME + " SET "
                    + KEY_NUMB + " = " + "'" + (original - numb) + "', "
                    + KEY_TOTAL + " = " + "'" + total + "'"
                    + " WHERE " + KEY_PRODUCT + " = " + "'" + product + "'";

            // Writing Contacts to log
            Log.i(TAG, "Updating Number(removing): " + updateQuery);

            db.execSQL(updateQuery);
        }

        cursor.close();
        db.close();
    }

    // Updating single Inventory adding
    public void updateNumberInventoryPurchase(String product, int numb, int tot) {
        TABLE_NAME = "Inventory" + LoginActivity.database;
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query(TABLE_NAME, new String[] { KEY_ID,
                        KEY_PRODUCT, KEY_NUMB, KEY_UNIT, KEY_COST, KEY_TOTAL, KEY_SPRICE }, KEY_PRODUCT + "=?",
                new String[] { String.valueOf(product) }, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();

            double original = Double.parseDouble(cursor.getString(2));
            double total = Double.parseDouble(cursor.getString(5));

            double newNumb = (original + ((double) numb));
            double newTotal = (((double) tot) + total);

            double newCost = (newTotal / newNumb);

            String updateQuery = "UPDATE " + TABLE_NAME + " SET "
                    + KEY_NUMB + " = " + "'" + (int) Math.round(newNumb) + "', "
                    + KEY_COST + " = " + "'" + newCost + "', "
                    + KEY_TOTAL + " = " + "'" + (int) Math.round(newTotal) + "'"
                    + " WHERE " + KEY_PRODUCT + " = " + "'" + product + "'";

            // Writing Contacts to log
            Log.i(TAG, "Updating Number(adding): " + updateQuery);

            db.execSQL(updateQuery);
        }

        cursor.close();
        db.close();
    }

    // Updating single Inventory adding
    public void updateNumberInventoryDelivery(String product, int tot) {
        TABLE_NAME = "Inventory" + LoginActivity.database;
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query(TABLE_NAME, new String[] { KEY_ID,
                        KEY_PRODUCT, KEY_NUMB, KEY_UNIT, KEY_COST, KEY_TOTAL, KEY_SPRICE }, KEY_PRODUCT + "=?",
                new String[] { String.valueOf(product) }, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();

            double original = Double.parseDouble(cursor.getString(2));
            double total = Double.parseDouble(cursor.getString(5));

            double newTotal = ((double) tot) + total;

            double newCost = (newTotal / original);

            String updateQuery = "UPDATE " + TABLE_NAME + " SET "
                    + KEY_COST + " = " + "'" + newCost + "', "
                    + KEY_TOTAL + " = " + "'" + (int) Math.round(newTotal) + "'"
                    + " WHERE " + KEY_PRODUCT + " = " + "'" + product + "'";

            // Writing Contacts to log
            Log.i(TAG, "Updating Number(delivery): " + updateQuery);

            db.execSQL(updateQuery);
        }

        cursor.close();
        db.close();
    }

    // Updating single Inventory
    public int updateCostInventory(Inventory inventory) {
        TABLE_NAME = "Inventory" + LoginActivity.database;
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        //values.put(KEY_PRODUCT, inventory.getProduct());
        //values.put(KEY_NUMB, inventory.getNumber());
        values.put(KEY_COST, inventory.getCost());
        //values.put(KEY_TOTAL, inventory.getTotal());

        int success = db.update(TABLE_NAME, values, KEY_PRODUCT + " = ?", new String[] { String.valueOf(inventory.getProduct())});

        db.close();
        // updating row
        return success;
    }

    // Deleting single inventory
    public void deleteInventory(Inventory inventory) {
        TABLE_NAME = "Inventory" + LoginActivity.database;
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, KEY_ID + " = ?",
                new String[] { String.valueOf(inventory.getID()) });
        db.close();
    }

    // Getting inventory Count
    public int getInventoryCount() {
        TABLE_NAME = "Inventory" + LoginActivity.database;
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
        TABLE_NAME = "Inventory" + LoginActivity.database;
        SQLiteDatabase db = this.getWritableDatabase();

        String CREATE_CONTACTS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_PRODUCT + " TEXT,"
                + KEY_NUMB + " INTEGER," + KEY_UNIT + " TEXT," + KEY_COST + " REAL,"
                + KEY_TOTAL + " INTEGER," + KEY_SPRICE + " INTEGER" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);

        db.close();
    }
}
