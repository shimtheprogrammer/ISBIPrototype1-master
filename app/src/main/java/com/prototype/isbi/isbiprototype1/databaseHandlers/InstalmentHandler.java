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
 * Created by MRuto on 2/15/2017.
 */

public class InstalmentHandler extends SQLiteOpenHelper {

    //satic variables
    //database version
    public static final int DATABASE_VERSION = 1;

    //database name
    public static final String DATABASE_NAME = "ISBI";

    //table name
    public static String TABLE_NAME = "Instalments";

    //table columns name
    public static final String KEY_ID = "id";
    public static final String KEY_TOTAL = "total";
    public static final String KEY_PAYMENT = "payment";
    public static final String KEY_DATE = "date";
    public static final String KEY_NUMBER = "number";
    public static final String KEY_SAVINGS = "savings";
    public static final String KEY_FOREIGN_KEY = "foreignKey";

    public InstalmentHandler(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //creat table
    @Override
    public void onCreate(SQLiteDatabase db){
        TABLE_NAME = "Instalments" + LoginActivity.database;
        //create table
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_TOTAL + " INTEGER," + KEY_PAYMENT + " TEXT,"
                + KEY_DATE + " TEXT," + KEY_NUMBER + " TEXT,"
                + KEY_SAVINGS + " INTEGER," + KEY_FOREIGN_KEY + " INTEGER" + ")";

        db.execSQL(CREATE_TABLE);
        db.close();
    }

    //upgrade database
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newversion){
        TABLE_NAME = "Instalments" + LoginActivity.database;

        //drop table
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME );

        //create table
        onCreate(db);
    }

    public void dropTable(){
        TABLE_NAME = "Instalments" + LoginActivity.database;
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
    public void addInstalments(Instalment instalment){
        TABLE_NAME = "Instalments" + LoginActivity.database;
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TOTAL, instalment.getTotal());
        values.put(KEY_PAYMENT, instalment.getPayment());
        values.put(KEY_DATE, instalment.getDate());
        values.put(KEY_NUMBER, instalment.getNumber());
        values.put(KEY_SAVINGS, instalment.getContractualSavings());
        values.put(KEY_FOREIGN_KEY, instalment.gettForeignKey());

        //insert row
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    //getting single entry
    public Instalment getInstalments(int id){
        TABLE_NAME = "Instalments" + LoginActivity.database;
        SQLiteDatabase db = this.getReadableDatabase();

        Instalment instalment = new Instalment();

        Cursor cursor = db.query(TABLE_NAME, new String[] { KEY_ID, KEY_TOTAL, KEY_PAYMENT,
                KEY_DATE, KEY_NUMBER, KEY_SAVINGS, KEY_FOREIGN_KEY }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);

        if(cursor != null) {
            cursor.moveToFirst();


            instalment = new Instalment(Integer.parseInt(cursor.getString(0)), Integer.parseInt(cursor.getString(1)),
                    cursor.getString(2), cursor.getString(3),  cursor.getString(4),
                    Integer.parseInt(cursor.getString(5)) ,Integer.parseInt(cursor.getString(6)));
        }

        cursor.close();
        db.close();
        return instalment;
    }

    //get all entries
    public List<Instalment> getAllInstalment() {
        TABLE_NAME = "Instalments" + LoginActivity.database;
        List<Instalment> instalments = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        //loop through all rows and add to list
        if(cursor.moveToFirst()){
            do{
                Instalment instalment = new Instalment();
                instalment.setId(Integer.parseInt(cursor.getString(0)));
                instalment.setTotal(Integer.parseInt(cursor.getString(1)));
                instalment.setPayment(cursor.getString(2));
                instalment.setDate(cursor.getString(3));
                instalment.setNumber(cursor.getString(4));
                instalment.setContractualSavings(Integer.parseInt(cursor.getString(5)));
                instalment.setForeignKay(Integer.parseInt(cursor.getString(6)));

                //add entry to list
                instalments.add(instalment);
            }while(cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return instalments;
    }

    //get using foreign key
    public List<Instalment> getByForeignKey(int foreignKey){
        TABLE_NAME = "Instalments" + LoginActivity.database;
        List<Instalment> instalmentList = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_NAME + " WHERE " + KEY_FOREIGN_KEY + " = " + foreignKey;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        //loop through all rows and add to list
        if(cursor.moveToFirst()){
            do{
                Instalment instalment = new Instalment();
                instalment.setId(Integer.parseInt(cursor.getString(0)));
                instalment.setTotal(Integer.parseInt(cursor.getString(1)));
                instalment.setPayment(cursor.getString(2));
                instalment.setDate(cursor.getString(3));
                instalment.setNumber(cursor.getString(4));
                instalment.setContractualSavings(Integer.parseInt(cursor.getString(5)));
                instalment.setForeignKay(Integer.parseInt(cursor.getString(6)));

                //add entry to list
                instalmentList.add(instalment);
            }while(cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return instalmentList;
    }

    //updating single entry
    public int updateInstalment(Instalment instalment){
        TABLE_NAME = "Instalments" + LoginActivity.database;
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TOTAL, instalment.getTotal());
        values.put(KEY_PAYMENT, instalment.getPayment());
        values.put(KEY_DATE, instalment.getDate());
        values.put(KEY_NUMBER, instalment.getNumber());
        values.put(KEY_SAVINGS, instalment.getContractualSavings());
        values.put(KEY_FOREIGN_KEY, instalment.gettForeignKey());

        //updating row
        int success = db.update(TABLE_NAME, values, KEY_ID + " = ?",
                new String[] { String.valueOf(instalment.getId()) });

        db.close();

        return success;
    }

    //delete single entry
    public void deleteInstalment(Instalment instalment){
        TABLE_NAME = "Instalments" + LoginActivity.database;
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_NAME, KEY_ID + " = ?",
                new String [] { String.valueOf(instalment.getId())});
        db.close();
    }

    // get row count
    public int getRowCount(){
        TABLE_NAME = "Instalments" + LoginActivity.database;
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
        TABLE_NAME = "Instalments" + LoginActivity.database;

        SQLiteDatabase db = this.getWritableDatabase();

        String CREATE_CONTACTS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_TOTAL + " INTEGER," + KEY_PAYMENT + " TEXT,"
                + KEY_DATE + " TEXT," + KEY_NUMBER + " TEXT,"
                +  KEY_SAVINGS + " INTEGER," + KEY_FOREIGN_KEY + " INTEGER" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);

        db.close();
    }

}
