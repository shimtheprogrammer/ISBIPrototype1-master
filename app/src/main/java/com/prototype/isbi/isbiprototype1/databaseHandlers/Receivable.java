package com.prototype.isbi.isbiprototype1.databaseHandlers;

/**
 * Created by MRuto on 12/18/2016.
 */

public class Receivable {

    //private variables
    int _id, _total;
    String _from, _date, _originaldate;

    // Empty constructor
    public Receivable() {

    }

    // constructor
    public Receivable(int id, String from, int total, String date, String originaldate) {
        this._id = id;
        this._from = from;
        this._total = total;
        this._date = date;
        this._originaldate = originaldate;
    }

    // constructor
    public Receivable(String from, int total, String date, String originaldate) {
        this._from = from;
        this._total = total;
        this._date = date;
        this._originaldate = originaldate;
    }

    // getting ID
    public int getID() {
        return this._id;
    }

    // setting id
    public void setID(int id) {
        this._id = id;
    }

    // getting from
    public String getFrom() {
        return this._from;
    }

    // setting from
    public void setFrom(String from) { this._from = from; }

    // getting total
    public int getTotal() {
        return this._total;
    }

    // setting total
    public void setTotal(int total) { this._total = total; }

    //getting date
    public String getDate() { return this._date; }

    // setting date
    public void setDate(String date) { this._date = date; }

    //getting date
    public String getOriginalDate() { return this._originaldate; }

    //setting date
    public void setOriginalDate(String date) { this._originaldate = date; }

}
