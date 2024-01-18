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
 * Created by MRuto on 1/10/2017.
 */

public class GoodsPurchasedHandler extends SQLiteOpenHelper{

    private static final String TAG = "Goods Purchased DB Handler:";

    //satic variables
    //database version
    public static final int DATABASE_VERSION = 1;

    //database name
    public static final String DATABASE_NAME = "ISBI";

    //table name
    public static String TABLE_NAME = "GoodsPurchase";

    //table columns name
    public static final String KEY_ID = "id";
    public static final String KEY_PRODUCT = "product";
    public static final String KEY_NUMBER = "number";
    private static final String KEY_UNIT = "units";
    public static final String KEY_COST = "cost";
    public static final String KEY_TOTAL = "total";
    public static final String KEY_DATE = "date";
    public static final String KEY_FOREIGN_KEY = "foreignKey";

    public GoodsPurchasedHandler(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //creat table
    @Override
    public void onCreate(SQLiteDatabase db){
        TABLE_NAME = "GoodsPurchase" + LoginActivity.database;
        //create table
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_PRODUCT + " TEXT," + KEY_NUMBER + " INTEGER," + KEY_UNIT + " TEXT," + KEY_COST + " REAL,"
                + KEY_TOTAL + " INTEGER," + KEY_DATE + " TEXT," + KEY_FOREIGN_KEY + " INTEGER" + ")";

        db.execSQL(CREATE_TABLE);

        //db.close();
    }

    //upgrade database
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newversion){
        TABLE_NAME = "GoodsPurchase" + LoginActivity.database;

        //drop table
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME );

        //create table
        onCreate(db);
    }

    public void dropTable(){
        TABLE_NAME = "GoodsPurchase" + LoginActivity.database;
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
    public void addPurchases(GoodsPurchased goodsPurchase){
        TABLE_NAME = "GoodsPurchase" + LoginActivity.database;
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PRODUCT, goodsPurchase.getProduct());
        values.put(KEY_NUMBER, goodsPurchase.getNumber());
        values.put(KEY_UNIT, goodsPurchase.getUnit());
        values.put(KEY_COST, goodsPurchase.getCost());
        values.put(KEY_TOTAL, goodsPurchase.getTotal());
        values.put(KEY_DATE, goodsPurchase.getDate());
        values.put(KEY_FOREIGN_KEY, goodsPurchase.gettForeignKey());

        //insert row
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    //getting single entry
    public GoodsPurchased getPurchase(int id){
        TABLE_NAME = "GoodsPurchase" + LoginActivity.database;
        SQLiteDatabase db = this.getReadableDatabase();
        GoodsPurchased purchase = new GoodsPurchased();

        Cursor cursor = db.query(TABLE_NAME, new String[] { KEY_ID, KEY_PRODUCT, KEY_NUMBER, KEY_UNIT,
                        KEY_COST, KEY_TOTAL, KEY_DATE, KEY_FOREIGN_KEY }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);

        if(cursor != null) {
            cursor.moveToFirst();


            purchase = new GoodsPurchased(Integer.parseInt(cursor.getString(0)), cursor.getString(1),
                    Integer.parseInt(cursor.getString(2)), cursor.getString(3),
                    Double.parseDouble(cursor.getString(4)), Integer.parseInt(cursor.getString(5)),
                    cursor.getString(6), Integer.parseInt(cursor.getString(7)));
        }

        cursor.close();
        db.close();

        return purchase;
    }

    //get all entries
    public List<GoodsPurchased> getAllPurchase() {
        TABLE_NAME = "GoodsPurchase" + LoginActivity.database;
        List<GoodsPurchased> goodsPurchaseList = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        //loop through all rows and add to list
        if(cursor.moveToFirst()){
            do{
                GoodsPurchased goodsPurchased = new GoodsPurchased();
                goodsPurchased.setId(Integer.parseInt(cursor.getString(0)));
                goodsPurchased.setProduct(cursor.getString(1));
                goodsPurchased.setNumber(Integer.parseInt(cursor.getString(2)));
                goodsPurchased.setUnit(cursor.getString(3));
                goodsPurchased.setCost(Double.parseDouble(cursor.getString(4)));
                goodsPurchased.setTotal(Integer.parseInt(cursor.getString(5)));
                goodsPurchased.setDate(cursor.getString(6));
                goodsPurchased.setForeignKay(Integer.parseInt(cursor.getString(7)));

                //add entry to list
                goodsPurchaseList.add(goodsPurchased);
            }while(cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return goodsPurchaseList;
    }

    //get using foreign key
    public List<GoodsPurchased> getByForeignKey(int foreignKey){
        TABLE_NAME = "GoodsPurchase" + LoginActivity.database;
        List<GoodsPurchased> goodsPurchaseList = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_NAME + " WHERE " + KEY_FOREIGN_KEY + " = " + foreignKey;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        //loop through all rows and add to list
        if(cursor.moveToFirst()){
            do{
                GoodsPurchased goodsPurchased = new GoodsPurchased();
                goodsPurchased.setId(Integer.parseInt(cursor.getString(0)));
                goodsPurchased.setProduct(cursor.getString(1));
                goodsPurchased.setNumber(Integer.parseInt(cursor.getString(2)));
                goodsPurchased.setUnit(cursor.getString(3));
                goodsPurchased.setCost(Double.parseDouble(cursor.getString(4)));
                goodsPurchased.setTotal(Integer.parseInt(cursor.getString(5)));
                goodsPurchased.setDate(cursor.getString(6));
                goodsPurchased.setForeignKay(Integer.parseInt(cursor.getString(7)));

                //add entry to list
                goodsPurchaseList.add(goodsPurchased);
            }while(cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return goodsPurchaseList;
    }

    //updating single entry
    public int updatePurchase(GoodsPurchased goodsPurchased){
        TABLE_NAME = "GoodsPurchase" + LoginActivity.database;
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PRODUCT, goodsPurchased.getProduct());
        values.put(KEY_NUMBER, goodsPurchased.getNumber());
        values.put(KEY_UNIT, goodsPurchased.getUnit());
        values.put(KEY_COST, goodsPurchased.getCost());
        values.put(KEY_TOTAL, goodsPurchased.getTotal());
        values.put(KEY_DATE, goodsPurchased.getDate());
        values.put(KEY_FOREIGN_KEY, goodsPurchased.gettForeignKey());

        //updating row
        int success = db.update(TABLE_NAME, values, KEY_ID + " = ?",
                new String[] { String.valueOf(goodsPurchased.getId()) });

        db.close();

        return success;
    }

    //delete single entry
    public void deletePurchase(GoodsPurchased goodsPurchased){
        TABLE_NAME = "GoodsPurchase" + LoginActivity.database;
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_NAME, KEY_ID + " = ?",
                new String [] { String.valueOf(goodsPurchased.getId())});
        db.close();
    }

    //delete by foreig key
    public void deleteByForeignKey(int foreignKey){
        TABLE_NAME = "GoodsPurchase" + LoginActivity.database;
        SQLiteDatabase db = this.getWritableDatabase();

        String deleteQuery = "DELETE FROM " + TABLE_NAME + " WHERE " + KEY_FOREIGN_KEY + " = " + "'" + foreignKey + "'";

        db.execSQL(deleteQuery);

        String displ = "deleting from foreign key; " + foreignKey;
        // Writing Contacts to log
        Log.d("Delete: ", displ);

        db.close();
    }

    // get row count
    public int getRowCount(){
        TABLE_NAME = "GoodsPurchase" + LoginActivity.database;
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
        TABLE_NAME = "GoodsPurchase" + LoginActivity.database;
        SQLiteDatabase db = this.getReadableDatabase();

        String CREATE_CONTACTS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_PRODUCT + " TEXT," + KEY_NUMBER + " INTEGER," + KEY_UNIT + " TEXT," + KEY_COST + " REAL,"
                + KEY_TOTAL + " INTEGER," + KEY_DATE + " TEXT," + KEY_FOREIGN_KEY + " INTEGER" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);

        db.close();
    }

}
