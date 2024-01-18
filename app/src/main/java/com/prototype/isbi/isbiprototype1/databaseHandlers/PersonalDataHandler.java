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
 * Created by MRuto on 12/16/2016.
 */

public class PersonalDataHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "ISBI";

    // Contacts table name
    private static String TABLE_NAME = "PersonalDetails";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_MD_SALARY = "mdsalary";
    private static final String KEY_PIN = "pin";
    private static final String KEY_B_NAME = "bname";
    private static final String KEY_LOCATION = "location";

    public PersonalDataHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        TABLE_NAME = "PersonalDetails"  + LoginActivity.database;
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_EMAIL + " TEXT," + KEY_MD_SALARY + " INTEGER," + KEY_PIN + " INTEGER,"
                + KEY_B_NAME + " TEXT," + KEY_LOCATION + " TEXT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);

        //db.close();
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        TABLE_NAME = "PersonalDetails"  + LoginActivity.database;
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        // Create tables again
        onCreate(db);
    }

    public void dropTable() {
        TABLE_NAME = "PersonalDetails"  + LoginActivity.database;
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
    public void addPerson(PersonalData personalData) {
        TABLE_NAME = "PersonalDetails"  + LoginActivity.database;
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, personalData.getName()); // Name
        values.put(KEY_EMAIL, personalData.getEmail()); // Phone
        values.put(KEY_MD_SALARY, personalData.getMDSalary()); // ID/PP
        values.put(KEY_PIN, personalData.getPin()); // Pin
        values.put(KEY_B_NAME, personalData.getBName()); // Pin
        values.put(KEY_LOCATION, personalData.getLocation()); // Pin


        // Inserting Row
        db.insert(TABLE_NAME, null, values);
        db.close(); // Closing database connection
    }

    // Getting single person
    public PersonalData getPerson(int id) {
        TABLE_NAME = "PersonalDetails"  + LoginActivity.database;
        SQLiteDatabase db = this.getReadableDatabase();

        PersonalData personalData = new PersonalData();

        Cursor cursor = db.query(TABLE_NAME, new String[] { KEY_ID,
                        KEY_NAME, KEY_EMAIL, KEY_MD_SALARY, KEY_PIN, KEY_B_NAME, KEY_LOCATION }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();

            personalData = new PersonalData(Integer.parseInt(cursor.getString(0)),
                    cursor.getString(1), cursor.getString(2), Integer.parseInt(cursor.getString(3)),
                    Integer.parseInt(cursor.getString(4)), cursor.getString(5), cursor.getString(6));
        }

        cursor.close();
        db.close();
        // return person
        return personalData;
    }

    // Getting All PersonData
    public List<PersonalData> getAllPeople() {
        TABLE_NAME = "PersonalDetails"  + LoginActivity.database;
        List<PersonalData> personList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                PersonalData personalData = new PersonalData();
                personalData.setID(Integer.parseInt(cursor.getString(0)));
                personalData.setName(cursor.getString(1));
                personalData.setEmail(cursor.getString(2));
                personalData.setMDSalary(Integer.parseInt(cursor.getString(3)));
                personalData.setPin(Integer.parseInt(cursor.getString(4)));
                personalData.setBName(cursor.getString(5));
                personalData.setLocation(cursor.getString(6));

                // Adding person to list
                personList.add(personalData);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        // return person list
        return personList;
    }

    // Updating single person
    public int updatePerson(PersonalData personalData) {
        TABLE_NAME = "PersonalDetails"  + LoginActivity.database;
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, personalData.getName());
        values.put(KEY_EMAIL, personalData.getEmail());
        values.put(KEY_MD_SALARY, personalData.getMDSalary());
        values.put(KEY_PIN, personalData.getPin());
        values.put(KEY_B_NAME, personalData.getBName()); // Pin
        values.put(KEY_LOCATION, personalData.getLocation()); // Pin

        int ret = db.update(TABLE_NAME, values, KEY_ID + " = ?",
                new String[] { String.valueOf(personalData.getID()) });

        db.close();
        // updating row
        return ret;
    }

    // Deleting single person
    public void deletePerson(PersonalData personalData) {
        TABLE_NAME = "PersonalDetails"  + LoginActivity.database;
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, KEY_ID + " = ?",
                new String[] { String.valueOf(personalData.getID()) });
        db.close();
    }


    // Getting person Count
    public int getPersonCount() {
        TABLE_NAME = "PersonalDetails"  + LoginActivity.database;
        String countQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int sum = cursor.getCount();

        cursor.close();
        db.close();

        return sum;
    }

    public void isTableExists() {
        TABLE_NAME = "PersonalDetails"  + LoginActivity.database;
        SQLiteDatabase db = this.getWritableDatabase();

        String CREATE_CONTACTS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_EMAIL + " TEXT," + KEY_MD_SALARY + " INTEGER," + KEY_PIN + " INTEGER,"
                + KEY_B_NAME + " TEXT," + KEY_LOCATION + " TEXT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);

        db.close();
    }

}
