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

public class LoansHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "ISBI";

    // Contacts table name
    private static String TABLE_NAME = "Loans";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_TOTAL = "total";
    private static final String KEY_PAYMENT = "payment";
    private static final String KEY_START_DATE = "startDate";
    private static final String KEY_END_DATE = "endDate";
    private static final String KEY_INSTALMENTS = "instalments";
    private static final String KEY_INSTALMENTS_N0 = "instalmentsNo";
    private static final String KEY_SAVINGS = "savings";
    private static final String KEY_SAVINGS_DATE = "savingsDate";
    private static final String KEY_STATUS = "status";

    public LoansHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        TABLE_NAME = "Loans" + LoginActivity.database;
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_NAME + " TEXT," + KEY_TOTAL + " INTEGER,"
                + KEY_PAYMENT + " TEXT," + KEY_START_DATE + " TEXT," + KEY_END_DATE + " TEXT,"
                + KEY_INSTALMENTS + " INTEGER," + KEY_INSTALMENTS_N0 + " INTEGER," + KEY_SAVINGS + " INTEGER,"
                + KEY_SAVINGS_DATE + " TEXT," + KEY_STATUS + " TEXT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);

        //db.close();
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        TABLE_NAME = "Loans" + LoginActivity.database;
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        // Create tables again
        onCreate(db);
    }

    public void dropTable() {
        TABLE_NAME = "Loans" + LoginActivity.database;
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
    public void addLoan(Loans loans) {
        TABLE_NAME = "Loans" + LoginActivity.database;
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, loans.getName()); // Phone
        values.put(KEY_TOTAL, loans.getTotal()); // ID/PP
        values.put(KEY_PAYMENT, loans.getPayment()); // Pin
        values.put(KEY_START_DATE, loans.getStartDate()); // ID/PP
        values.put(KEY_END_DATE, loans.getEndDate()); // ID/PP
        values.put(KEY_INSTALMENTS, loans.getInstalments()); // ID/PP
        values.put(KEY_INSTALMENTS_N0, loans.getInstalmentsNo()); // ID/PP
        values.put(KEY_SAVINGS, loans.getContractualSavings()); // ID/PP
        values.put(KEY_SAVINGS_DATE, loans.getContractualSavingsDate()); // ID/PP
        values.put(KEY_STATUS, loans.getStatus()); // ID/PP

        // Inserting Row
        db.insert(TABLE_NAME, null, values);
        db.close(); // Closing database connection
    }

    // Getting single person
    public Loans getLoans(int id) {
        TABLE_NAME = "Loans" + LoginActivity.database;
        SQLiteDatabase db = this.getReadableDatabase();

        Loans loans = new Loans();

        Cursor cursor = db.query(TABLE_NAME, new String[] { KEY_ID,
                KEY_NAME, KEY_TOTAL, KEY_PAYMENT, KEY_START_DATE, KEY_END_DATE, KEY_INSTALMENTS,
                KEY_INSTALMENTS_N0, KEY_SAVINGS, KEY_SAVINGS_DATE, KEY_STATUS}, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();

            loans = new Loans(Integer.parseInt(cursor.getString(0)),
                    cursor.getString(1), Integer.parseInt(cursor.getString(2)),
                    cursor.getString(3), cursor.getString(4), cursor.getString(5),
                    Integer.parseInt(cursor.getString(6)), Integer.parseInt(cursor.getString(7)),
                    Integer.parseInt(cursor.getString(8)), cursor.getString(9), cursor.getString(10));
        }

        cursor.close();
        db.close();
        // return person
        return loans;
    }

    // Getting All PersonData
    public List<Loans> getAllLoans() {
        TABLE_NAME = "Loans" + LoginActivity.database;
        List<Loans> loansList = new ArrayList<Loans>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Loans loans = new Loans();
                loans.setID(Integer.parseInt(cursor.getString(0)));
                loans.setName(cursor.getString(1));
                loans.setTotal(Integer.parseInt(cursor.getString(2)));
                loans.setPayment(cursor.getString(3));
                loans.setStartDate(cursor.getString(4));
                loans.setEndDate(cursor.getString(5));
                loans.setInstalments(Integer.parseInt(cursor.getString(6)));
                loans.setInstalmentsNo(Integer.parseInt(cursor.getString(7)));
                loans.setContractualSavings(Integer.parseInt(cursor.getString(8)));
                loans.setContractualSavingsDate(cursor.getString(9));
                loans.setStatus(cursor.getString(10));
                // Adding person to list
                loansList.add(loans);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        // return person list
        return loansList;
    }

    // Updating single person
    public int updateLoan(Loans loans) {
        TABLE_NAME = "Loans" + LoginActivity.database;
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, loans.getName()); // Phone
        values.put(KEY_TOTAL, loans.getTotal()); // ID/PP
        values.put(KEY_PAYMENT, loans.getPayment()); // Pin
        values.put(KEY_START_DATE, loans.getStartDate()); // ID/PP
        values.put(KEY_END_DATE, loans.getEndDate()); // ID/PP
        values.put(KEY_INSTALMENTS, loans.getInstalments()); // ID/PP
        values.put(KEY_INSTALMENTS_N0, loans.getInstalmentsNo()); // ID/PP
        values.put(KEY_SAVINGS, loans.getContractualSavings()); // ID/PP
        values.put(KEY_SAVINGS_DATE, loans.getContractualSavingsDate()); // ID/PP
        values.put(KEY_STATUS, loans.getStatus()); // ID/PP

        int ret = db.update(TABLE_NAME, values, KEY_ID + " = ?",
                new String[] { String.valueOf(loans.getID()) });

        db.close();
        // updating row
        return ret;
    }

    // Deleting single person
    public void deleteLoan(Loans loans) {
        TABLE_NAME = "Loans" + LoginActivity.database;
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, KEY_ID + " = ?",
                new String[] { String.valueOf(loans.getID()) });
        db.close();
    }


    // Getting person Count
    public int getLoansCount() {
        TABLE_NAME = "Loans" + LoginActivity.database;
        String countQuery = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int ret = cursor.getCount();

        cursor.close();
        db.close();

        return ret;
    }

    public void isTableExists() {
        TABLE_NAME = "Loans" + LoginActivity.database;
        SQLiteDatabase db = this.getWritableDatabase();

        String CREATE_CONTACTS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_NAME + " TEXT," + KEY_TOTAL + " INTEGER,"
                + KEY_PAYMENT + " TEXT," + KEY_START_DATE + " TEXT," + KEY_END_DATE + " TEXT,"
                + KEY_INSTALMENTS + " INTEGER," + KEY_INSTALMENTS_N0 + " INTEGER," + KEY_SAVINGS + " INTEGER,"
                + KEY_SAVINGS_DATE + " TEXT," + KEY_STATUS + " TEXT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);

        db.close();
    }

}
