package com.prototype.isbi.isbiprototype1.databaseHandlers;

/**
 * Created by MRuto on 12/18/2016.
 */

public class Payable {

    //private variables
    int _id, _total;
    String _to, _date, _originaldate;

    // Empty constructor
    public Payable() {

    }

    // constructor
    public Payable(int id, String to, int total, String date, String originaldate) {
        this._id = id;
        this._to = to;
        this._total = total;
        this._date = date;
        this._originaldate = originaldate;
    }

    // constructor
    public Payable(String to, int total, String date, String originaldate) {
        this._to = to;
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

    // getting to
    public String getTo() {
        return this._to;
    }

    // setting to
    public void setTo(String to) {
        this._to = to;
    }

    // getting total
    public int getTotal() {
        return this._total;
    }

    // setting total
    public void setTotal(int total) { this._total = total; }

    //getting date
    public String getDate() { return this._date; }

    //setting date
    public void setDate(String date) { this._date = date; }

    //getting date
    public String getOriginalDate() { return this._originaldate; }

    //setting date
    public void setOriginalDate(String date) { this._originaldate = date; }

}