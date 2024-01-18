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

public class PreSaleHandler extends SQLiteOpenHelper {

    //satic variables
    //database version
    public static final int DATABASE_VERSION = 1;

    //database name
    public static final String DATABASE_NAME = "ISBI";

    //table name
    public static String TABLE_NAME = "PreSale";

    //table columns name
    public static final String KEY_ID = "id";
    public static final String KEY_CUSTOMER = "product";
    public static final String KEY_TOTAL = "total";
    public static final String KEY_PAYMENT = "payment";
    public static final String KEY_DATE = "date";
    public static final String KEY_ORIGINAL_DATE = "originaldate";
    public static final String KEY_STATUS = "status";

    public PreSaleHandler(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //creat table
    @Override
    public void onCreate(SQLiteDatabase db){
        TABLE_NAME = "PreSale" + LoginActivity.database;
        //create table
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_CUSTOMER + " TEXT," + KEY_TOTAL + " INTEGER," + KEY_PAYMENT + " TEXT,"
                + KEY_DATE + " TEXT," + KEY_ORIGINAL_DATE + " TEXT,"  + KEY_STATUS + " TEXT" + ")";

        db.execSQL(CREATE_TABLE);
        db.close();
    }

    //upgrade database
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newversion){
        TABLE_NAME = "PreSale" + LoginActivity.database;

        //drop table
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME );

        //create table
        onCreate(db);
    }

    public void dropTable(){
        TABLE_NAME = "PreSale" + LoginActivity.database;
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
    public void addPreSale(PreSale sales){
        TABLE_NAME = "PreSale" + LoginActivity.database;
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_CUSTOMER, sales.getCustomer());
        values.put(KEY_TOTAL, sales.getTotal());
        values.put(KEY_PAYMENT, sales.getPayment());
        values.put(KEY_DATE, sales.getDate());
        values.put(KEY_ORIGINAL_DATE, sales.getOriginalDate());
        values.put(KEY_STATUS, sales.getStatus());

        //insert row
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    //getting single entry
    public PreSale getPreSale(int id){
        TABLE_NAME = "PreSale" + LoginActivity.database;
        SQLiteDatabase db = this.getReadableDatabase();

        PreSale sale = new PreSale();

        Cursor cursor = db.query(TABLE_NAME, new String[] { KEY_ID, KEY_CUSTOMER,
                        KEY_TOTAL, KEY_PAYMENT, KEY_DATE, KEY_ORIGINAL_DATE, KEY_STATUS }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);

        if(cursor != null) {
            cursor.moveToFirst();


            sale = new PreSale(Integer.parseInt(cursor.getString(0)), cursor.getString(1), Integer.parseInt(cursor.getString(2)),
                    cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6));
        }

        cursor.close();
        db.close();
        return sale;
    }

    //get all entries
    public List<PreSale> getAllPreSale() {
        TABLE_NAME = "PreSale" + LoginActivity.database;
        List<PreSale> salesList = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        //loop through all rows and add to list
        if(cursor.moveToFirst()){
            do{
                PreSale sales = new PreSale();
                sales.setId(Integer.parseInt(cursor.getString(0)));
                sales.setCustomer(cursor.getString(1));
                sales.setTotal(Integer.parseInt(cursor.getString(2)));
                sales.setPayment(cursor.getString(3));
                sales.setDate(cursor.getString(4));
                sales.setOriginalDate(cursor.getString(5));
                sales.setStatus(cursor.getString(6));

                //add entry to list
                salesList.add(sales);
            }while(cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return salesList;
    }

    //updating single entry
    public int updatePreSale(PreSale sales){
        TABLE_NAME = "PreSale" + LoginActivity.database;
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_CUSTOMER, sales.getCustomer());
        values.put(KEY_TOTAL, sales.getTotal());
        values.put(KEY_PAYMENT, sales.getPayment());
        values.put(KEY_DATE, sales.getDate());
        values.put(KEY_ORIGINAL_DATE, sales.getOriginalDate());
        values.put(KEY_STATUS, sales.getStatus());

        //updating row
        int success = db.update(TABLE_NAME, values, KEY_ID + " = ?",
                new String[] { String.valueOf(sales.getId()) });

        db.close();

        return success;
    }

    //delete single entry
    public void deletePreSale(PreSale sales){
        TABLE_NAME = "PreSale" + LoginActivity.database;
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_NAME, KEY_ID + " = ?",
                new String [] { String.valueOf(sales.getId())});
        db.close();
    }

    // get row count
    public int getRowCount(){
        TABLE_NAME = "PreSale" + LoginActivity.database;
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
        TABLE_NAME = "PreSale" + LoginActivity.database;

        SQLiteDatabase db = this.getWritableDatabase();

        String CREATE_CONTACTS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_CUSTOMER + " TEXT," + KEY_TOTAL + " INTEGER," + KEY_PAYMENT + " TEXT,"
                + KEY_DATE + " TEXT," + KEY_ORIGINAL_DATE + " TEXT,"  + KEY_STATUS + " TEXT" + ")";

        db.execSQL(CREATE_CONTACTS_TABLE);

        db.close();
    }
}
