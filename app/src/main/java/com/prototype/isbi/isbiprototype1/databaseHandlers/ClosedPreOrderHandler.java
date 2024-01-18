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
 * Created by MRuto on 1/25/2017.
 */

public class ClosedPreOrderHandler extends SQLiteOpenHelper {

    //satic variables
    //database version
    public static final int DATABASE_VERSION = 1;

    //database name
    public static final String DATABASE_NAME = "ISBI";

    //table name
    public static String TABLE_NAME = "ClosedPreOrder";

    //table columns name
    public static final String KEY_ID = "id";
    public static final String KEY_SUPPLIER = "supplier";
    public static final String KEY_TOTAL = "total";
    public static final String KEY_PAYMENT = "payment";
    public static final String KEY_DATE = "date";
    public static final String KEY_ORIGINAL_DATE = "originaldate";
    public static final String KEY_FOREIGN_KEY = "foreignKey";

    public ClosedPreOrderHandler(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //creat table
    @Override
    public void onCreate(SQLiteDatabase db){
        TABLE_NAME = "ClosedPreOrder" + LoginActivity.database;
        //create table
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_SUPPLIER + " TEXT," + KEY_TOTAL + " INTEGER," + KEY_PAYMENT + " TEXT,"
                + KEY_DATE + " TEXT," + KEY_ORIGINAL_DATE + " TEXT," + KEY_FOREIGN_KEY + " INTEGER" + ")";

        db.execSQL(CREATE_TABLE);
        //db.close();
    }

    //upgrade database
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newversion){
        TABLE_NAME = "ClosedPreOrder" + LoginActivity.database;

        //drop table
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME );

        //create table
        onCreate(db);
    }

    public void dropTable(){
        TABLE_NAME = "ClosedPreOrder" + LoginActivity.database;
        SQLiteDatabase db = this.getWritableDatabase();

        //drop table
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME );

        //create table
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    //add new entry
    public void addPreOrder(ClosedPreOrder purchase){
        TABLE_NAME = "ClosedPreOrder" + LoginActivity.database;
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_SUPPLIER, purchase.getSupplier());
        values.put(KEY_TOTAL, purchase.getTotal());
        values.put(KEY_PAYMENT, purchase.getPayment());
        values.put(KEY_DATE, purchase.getDate());
        values.put(KEY_ORIGINAL_DATE, purchase.getOriginalDate());
        values.put(KEY_FOREIGN_KEY, purchase.getForeignKey());

        //insert row
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    //getting single entry
    public ClosedPreOrder getPreOrder(int id){
        TABLE_NAME = "ClosedPreOrder" + LoginActivity.database;
        SQLiteDatabase db = this.getReadableDatabase();

        ClosedPreOrder purchase = new ClosedPreOrder();

        Cursor cursor = db.query(TABLE_NAME, new String[] { KEY_ID, KEY_SUPPLIER,
                        KEY_TOTAL, KEY_PAYMENT, KEY_DATE, KEY_ORIGINAL_DATE, KEY_FOREIGN_KEY }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);

        if(cursor != null) {
            cursor.moveToFirst();


            purchase = new ClosedPreOrder(Integer.parseInt(cursor.getString(0)),
                    cursor.getString(1), Integer.parseInt(cursor.getString(2)),
                    cursor.getString(3), cursor.getString(4), cursor.getString(5), Integer.parseInt(cursor.getString(6)));
        }

        cursor.close();
        db.close();
        return purchase;
    }

    //get all entries
    public List<ClosedPreOrder> getAllPreOrder() {
        TABLE_NAME = "ClosedPreOrder" + LoginActivity.database;
        List<ClosedPreOrder> purchaseList = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        //loop through all rows and add to list
        if(cursor.moveToFirst()){
            do{
                ClosedPreOrder purchase = new ClosedPreOrder();
                purchase.setId(Integer.parseInt(cursor.getString(0)));
                purchase.setSupplier(cursor.getString(1));
                purchase.setTotal(Integer.parseInt(cursor.getString(2)));
                purchase.setPayment(cursor.getString(3));
                purchase.setDate(cursor.getString(4));
                purchase.setOriginalDate(cursor.getString(5));
                purchase.setForeignKey(Integer.parseInt(cursor.getString(6)));

                //add entry to list
                purchaseList.add(purchase);
            }while(cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return purchaseList;
    }

    //updating single entry
    public int updatePreOrder(ClosedPreOrder purchase){
        TABLE_NAME = "ClosedPreOrder" + LoginActivity.database;
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_SUPPLIER, purchase.getSupplier());
        values.put(KEY_TOTAL, purchase.getTotal());
        values.put(KEY_PAYMENT, purchase.getPayment());
        values.put(KEY_DATE, purchase.getDate());
        values.put(KEY_ORIGINAL_DATE, purchase.getOriginalDate());
        values.put(KEY_FOREIGN_KEY, purchase.getForeignKey());

        //updating row
        int success = db.update(TABLE_NAME, values, KEY_ID + " = ?",
                new String[] { String.valueOf(purchase.getId()) });

        db.close();

        return success;
    }

    //delete single entry
    public void deletePreOrder(ClosedPreOrder purchase){
        TABLE_NAME = "ClosedPreOrder" + LoginActivity.database;
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_NAME, KEY_ID + " = ?",
                new String [] { String.valueOf(purchase.getId())});
        db.close();
    }

    // get row count
    public int getRowCount(){
        TABLE_NAME = "ClosedPreOrder" + LoginActivity.database;
        String countQuery = "SELECT * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(countQuery, null);

        int ret = cursor.getCount();

        cursor.close();
        db.close();

        return ret;
    }

    //checkif table exists
    public void isTableExists() {
        TABLE_NAME = "ClosedPreOrder" + LoginActivity.database;

        SQLiteDatabase db = this.getWritableDatabase();

        String CREATE_CONTACTS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_SUPPLIER + " TEXT," + KEY_TOTAL + " INTEGER," + KEY_PAYMENT + " TEXT,"
                + KEY_DATE + " TEXT," + KEY_ORIGINAL_DATE + " TEXT," + KEY_FOREIGN_KEY + " INTEGER" + ")";

        db.execSQL(CREATE_CONTACTS_TABLE);

        db.close();
    }

}
