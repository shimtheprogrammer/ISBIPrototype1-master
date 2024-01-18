package com.prototype.isbi.isbiprototype1.databaseHandlers;

/**
 * Created by MRuto on 1/18/2017.
 */

public class PaidPayable {

    //private variables
    int _id, _total;
    String _to, _date, _originaldate, _payment;

    // Empty constructor
    public PaidPayable() {

    }

    // constructor
    public PaidPayable(int id, String to, int total, String date, String originaldate, String payment) {
        this._id = id;
        this._to = to;
        this._total = total;
        this._date = date;
        this._originaldate = originaldate;
        this._payment = payment;
    }

    // constructor
    public PaidPayable(String to, int total, String date, String originaldate, String payment) {
        this._to = to;
        this._total = total;
        this._date = date;
        this._originaldate = originaldate;
        this._payment = payment;
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

    //getting payment
    public String getPayment() { return this._payment; }

    //setting payment
    public void setPayment(String payment) { this._payment = payment; }



}
