package com.prototype.isbi.isbiprototype1.databaseHandlers;

import android.content.Context;
import android.content.ContentValues;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;
import android.util.Log;

import com.prototype.isbi.isbiprototype1.LoginActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MRuto on 1/9/2017.
 */

public class GoodsSalesHandler extends SQLiteOpenHelper{

    //satic variables
    //database version
    public static final int DATABASE_VERSION = 1;

    //database name
    public static final String DATABASE_NAME = "ISBI";

    //table name
    public static String TABLE_NAME = "GoodsSales";

    //table columns name
    public static final String KEY_ID = "id";
    public static final String KEY_PRODUCT = "product";
    public static final String KEY_NUMBER = "number";
    private static final String KEY_UNIT = "units";
    public static final String KEY_COST = "cost";
    public static final String KEY_TOTAL = "total";
    public static final String KEY_DATE = "date";
    public static final String KEY_FOREIGN_KEY = "foreignKey";
    public static final String KEY_P_COST = "pcost";

    public GoodsSalesHandler(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //creat table
    @Override
    public void onCreate(SQLiteDatabase db){
        TABLE_NAME = "GoodsSales" + LoginActivity.database;
        //create table
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_PRODUCT + " TEXT," + KEY_NUMBER + " INTEGER," + KEY_UNIT + " TEXT," + KEY_COST + " REAL,"
                + KEY_TOTAL + " INTEGER," + KEY_DATE + " TEXT," + KEY_FOREIGN_KEY + " INTEGER,"
                + KEY_P_COST + " REAL" +")";

        db.execSQL(CREATE_TABLE);

        //db.close();
    }

    //upgrade database
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newversion){
        TABLE_NAME = "GoodsSales" + LoginActivity.database;

        //drop table
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME );

        //create table
        onCreate(db);
    }

    public void dropTable(){
        TABLE_NAME = "GoodsSales" + LoginActivity.database;
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
    public void addSales(GoodsSales goodsSales){
        TABLE_NAME = "GoodsSales" + LoginActivity.database;
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PRODUCT, goodsSales.getProduct());
        values.put(KEY_NUMBER, goodsSales.getNumber());
        values.put(KEY_UNIT, goodsSales.getUnit());
        values.put(KEY_COST, goodsSales.getCost());
        values.put(KEY_TOTAL, goodsSales.getTotal());
        values.put(KEY_DATE, goodsSales.getDate());
        values.put(KEY_FOREIGN_KEY, goodsSales.gettForeignKey());
        values.put(KEY_P_COST, goodsSales.getPCost());

        //insert row
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    //getting single entry
    public GoodsSales getSale(int id){
        TABLE_NAME = "GoodsSales" + LoginActivity.database;
        SQLiteDatabase db = this.getReadableDatabase();

        GoodsSales sale = new GoodsSales();

        Cursor cursor = db.query(TABLE_NAME, new String[] { KEY_ID, KEY_PRODUCT, KEY_NUMBER, KEY_UNIT,
                KEY_COST, KEY_TOTAL, KEY_DATE, KEY_FOREIGN_KEY, KEY_P_COST }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);

        if(cursor != null) {
            cursor.moveToFirst();

            sale = new GoodsSales(Integer.parseInt(cursor.getString(0)), cursor.getString(1),
                    Integer.parseInt(cursor.getString(2)), cursor.getString(3),
                    Double.parseDouble(cursor.getString(4)), Integer.parseInt(cursor.getString(5)),
                    cursor.getString(6), Integer.parseInt(cursor.getString(7)), Double.parseDouble(cursor.getString(8)));
        }

        cursor.close();
        db.close();
        return sale;
    }

    //get all entries
    public List<GoodsSales> getAllSales() {
        TABLE_NAME = "GoodsSales" + LoginActivity.database;
        List<GoodsSales> goodsSalesList = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        //loop through all rows and add to list
        if(cursor.moveToFirst()){
            do{
                GoodsSales goodsSales = new GoodsSales();
                goodsSales.setId(Integer.parseInt(cursor.getString(0)));
                goodsSales.setProduct(cursor.getString(1));
                goodsSales.setNumber(Integer.parseInt(cursor.getString(2)));
                goodsSales.setUnit(cursor.getString(3));
                goodsSales.setCost(Double.parseDouble(cursor.getString(4)));
                goodsSales.setTotal(Integer.parseInt(cursor.getString(5)));
                goodsSales.setDate(cursor.getString(6));
                goodsSales.setForeignKay(Integer.parseInt(cursor.getString(7)));
                goodsSales.setPCost(Double.parseDouble(cursor.getString(8)));

                //add entry to list
                goodsSalesList.add(goodsSales);
            }while(cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return goodsSalesList;
    }

    //get using foreign key
    public List<GoodsSales> getByForeignKey(int foreignKey){
        TABLE_NAME = "GoodsSales" + LoginActivity.database;
        List<GoodsSales> goodsSalesList = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_NAME + " WHERE " + KEY_FOREIGN_KEY + " = " + foreignKey;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        //loop through all rows and add to list
        if(cursor.moveToFirst()){
            do{
                GoodsSales goodsSales = new GoodsSales();
                goodsSales.setId(Integer.parseInt(cursor.getString(0)));
                goodsSales.setProduct(cursor.getString(1));
                goodsSales.setNumber(Integer.parseInt(cursor.getString(2)));
                goodsSales.setUnit(cursor.getString(3));
                goodsSales.setCost(Double.parseDouble(cursor.getString(4)));
                goodsSales.setTotal(Integer.parseInt(cursor.getString(5)));
                goodsSales.setDate(cursor.getString(6));
                goodsSales.setForeignKay(Integer.parseInt(cursor.getString(7)));
                goodsSales.setPCost(Double.parseDouble(cursor.getString(8)));

                //add entry to list
                goodsSalesList.add(goodsSales);
            }while(cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return goodsSalesList;
    }

    //updating single entry
    public int updateSales(GoodsSales goodsSales){
        TABLE_NAME = "GoodsSales" + LoginActivity.database;
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PRODUCT, goodsSales.getProduct());
        values.put(KEY_NUMBER, goodsSales.getNumber());
        values.put(KEY_UNIT, goodsSales.getUnit());
        values.put(KEY_COST, goodsSales.getCost());
        values.put(KEY_TOTAL, goodsSales.getTotal());
        values.put(KEY_DATE, goodsSales.getDate());
        values.put(KEY_FOREIGN_KEY, goodsSales.gettForeignKey());
        values.put(KEY_P_COST, goodsSales.getPCost());

        //updating row
        int success = db.update(TABLE_NAME, values, KEY_ID + " = ?",
                new String[] { String.valueOf(goodsSales.getId()) });

        db.close();

        return success;
    }

    //delete single entry
    public void deleteSales(GoodsSales goodsSales){
        TABLE_NAME = "GoodsSales" + LoginActivity.database;
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_NAME, KEY_ID + " = ?",
                new String [] { String.valueOf(goodsSales.getId())});
        db.close();
    }

    //delete by foreig key
    public void deleteByForeignKey(int foreignKey){
        TABLE_NAME = "GoodsSales" + LoginActivity.database;
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
        TABLE_NAME = "GoodsSales" + LoginActivity.database;
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
        TABLE_NAME = "GoodsSales" + LoginActivity.database;
        SQLiteDatabase db = this.getWritableDatabase();

        String CREATE_CONTACTS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_PRODUCT + " TEXT," + KEY_NUMBER + " INTEGER," + KEY_UNIT + " TEXT," + KEY_COST + " REAL,"
                + KEY_TOTAL + " INTEGER," + KEY_DATE + " TEXT," + KEY_FOREIGN_KEY + " INTEGER," +
                KEY_P_COST + " REAL" +")";
        db.execSQL(CREATE_CONTACTS_TABLE);

        db.close();
    }
}
