package com.prototype.isbi.isbiprototype1.databaseHandlers;

/**
 * Created by MRuto on 1/25/2017.
 */

public class PreOrder {

    int _id, _total;
    String _supplier, _date, _payment, _originaldate, _status;

    public PreOrder() {

    }

    //constructor
    public PreOrder(int id, String supplier, int total, String payment, String date, String originaldate, String status){
        this._id = id;
        this._supplier = supplier;
        this._total = total;
        this._payment = payment;
        this._date = date;
        this._originaldate = originaldate;
        this._status = status;
    }

    //constructor
    public PreOrder(String supplier, int total, String payment, String date, String originaldate, String status){
        this._supplier = supplier;
        this._total = total;
        this._payment = payment;
        this._date = date;
        this._originaldate = originaldate;
        this._status = status;
    }

    //getting id
    public int getId(){ return this._id; }

    //setting id
    public void setId(int id) { this._id = id; }

    //getting supplier
    public String getSupplier() { return this._supplier; }

    //setting supplier
    public void setSupplier(String supplier) { this._supplier = supplier; }

    //getting total
    public int getTotal() { return this._total; }

    //setting total
    public void setTotal(int total) { this._total = total; }

    //setting payment
    public String getPayment() { return this._payment; }

    //getting payment
    public void setPayment( String payment ) { this._payment = payment; }

    //getting date
    public String getDate() { return this._date; }

    //setting date
    public void setDate( String date) { this._date = date; }

    //getting date
    public String getOriginalDate() { return this._originaldate; }

    //setting date
    public void setOriginalDate(String date) { this._originaldate = date; }

    //getting status
    public String getStatus() { return this._status; }

    //setting status
    public void setStatus(String status) { this._status = status; }

}
