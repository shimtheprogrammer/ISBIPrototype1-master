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
 * Created by MRuto on 1/18/2017.
 */

public class DeliveryHandler extends SQLiteOpenHelper {

    //satic variables
    //database version
    public static final int DATABASE_VERSION = 1;

    //database name
    public static final String DATABASE_NAME = "ISBI";

    //table name
    public static String TABLE_NAME = "Delivery";

    //table columns name
    public static final String KEY_ID = "id";
    public static final String KEY_ALLOCATION = "allocation";
    public static final String KEY_PAYMENT = "payment";
    public static final String KEY_TOTAL = "total";
    public static final String KEY_DATE = "date";
    public static final String KEY_FOREIGN_KEY = "foreignkey";

    public DeliveryHandler(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //creat table
    @Override
    public void onCreate(SQLiteDatabase db){
        TABLE_NAME = "Delivery" + LoginActivity.database;
        //create table
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_ALLOCATION + " TEXT," + KEY_TOTAL + " INTEGER," + KEY_PAYMENT + " TEXT,"
                + KEY_DATE + " TEXT," + KEY_FOREIGN_KEY + " TEXT" + ")";

        db.execSQL(CREATE_TABLE);
        //db.close();
    }

    //upgrade database
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newversion){
        TABLE_NAME = "Delivery" + LoginActivity.database;

        //drop table
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME );

        //create table
        onCreate(db);
    }

    public void dropTable(){
        TABLE_NAME = "Delivery" + LoginActivity.database;
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
    public void addDelivery(Delivery delivery){
        TABLE_NAME = "Delivery" + LoginActivity.database;
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ALLOCATION, delivery.getAllocation());
        values.put(KEY_TOTAL, delivery.getTotal());
        values.put(KEY_PAYMENT, delivery.getPaymentMethode());
        values.put(KEY_DATE, delivery.getDate());
        values.put(KEY_FOREIGN_KEY, delivery.getForeignKey());


        //insert row
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    //getting single entry
    public Delivery getDelivery(int id){
        TABLE_NAME = "Delivery" + LoginActivity.database;
        SQLiteDatabase db = this.getReadableDatabase();

        Delivery delivery = new Delivery();

        Cursor cursor = db.query(TABLE_NAME, new String[] { KEY_ID, KEY_ALLOCATION,
                        KEY_TOTAL, KEY_PAYMENT, KEY_DATE, KEY_FOREIGN_KEY }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);

        if(cursor != null) {
            cursor.moveToFirst();


            delivery = new Delivery(Integer.parseInt(cursor.getString(0)),
                    cursor.getString(1), Integer.parseInt(cursor.getString(2)),
                    cursor.getString(3), cursor.getString(4), cursor.getString(5));
        }

        cursor.close();
        db.close();
        return delivery;
    }

    //get all entries
    public List<Delivery> getAllDelivery() {
        TABLE_NAME = "Delivery" + LoginActivity.database;
        List<Delivery> deliveryList = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        //loop through all rows and add to list
        if(cursor.moveToFirst()){
            do{
                Delivery delivery = new Delivery();
                delivery.setID(Integer.parseInt(cursor.getString(0)));
                delivery.setAllocation(cursor.getString(1));
                delivery.setTotal(Integer.parseInt(cursor.getString(2)));
                delivery.setPaymentMethode(cursor.getString(3));
                delivery.setDate(cursor.getString(4));
                delivery.setForeignKey(cursor.getString(5));

                //add entry to list
                deliveryList.add(delivery);
            }while(cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return deliveryList;
    }

    //updating single entry
    public int updateDelivery(Delivery delivery){
        TABLE_NAME = "Delivery" + LoginActivity.database;
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ALLOCATION, delivery.getAllocation());
        values.put(KEY_TOTAL, delivery.getTotal());
        values.put(KEY_PAYMENT, delivery.getPaymentMethode());
        values.put(KEY_DATE, delivery.getDate());
        values.put(KEY_FOREIGN_KEY, delivery.getForeignKey());

        //updating row
        int success = db.update(TABLE_NAME, values, KEY_ID + " = ?",
                new String[] { String.valueOf(delivery.getID()) });

        db.close();

        return success;
    }

    //delete single entry
    public void deleteDelivery(Delivery delivery){
        TABLE_NAME = "Delivery" + LoginActivity.database;
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_NAME, KEY_ID + " = ?",
                new String [] { String.valueOf(delivery.getID())});
        db.close();
    }

    // get row count
    public int getRowCount(){
        TABLE_NAME = "Delivery" + LoginActivity.database;
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
        TABLE_NAME = "Delivery" + LoginActivity.database;

        SQLiteDatabase db = this.getWritableDatabase();

        String CREATE_CONTACTS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_ALLOCATION + " TEXT," + KEY_TOTAL + " INTEGER," + KEY_PAYMENT + " TEXT,"
                + KEY_DATE + " TEXT," + KEY_FOREIGN_KEY + " TEXT" + ")";

        db.execSQL(CREATE_CONTACTS_TABLE);

        db.close();
    }

}
