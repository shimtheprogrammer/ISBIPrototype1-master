package com.prototype.isbi.isbiprototype1.databaseHandlers;

/**
 * Created by MRuto on 1/25/2017.
 */

public class ClosedPreOrder {

    int _id, _total, _foreignKey;
    String _supplier, _date, _payment, _originaldate;

    public ClosedPreOrder() {

    }

    //constructor
    public ClosedPreOrder(int id, String supplier, int total, String payment, String date, String originaldate, int foreignKey){
        this._id = id;
        this._supplier = supplier;
        this._total = total;
        this._payment = payment;
        this._date = date;
        this._originaldate = originaldate;
        this._foreignKey = foreignKey;
    }

    //constructor
    public ClosedPreOrder(String supplier, int total, String payment, String date, String originaldate, int foreignKey){
        this._supplier = supplier;
        this._total = total;
        this._payment = payment;
        this._date = date;
        this._originaldate = originaldate;
        this._foreignKey = foreignKey;
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
    public void setOriginalDate(String originaldate) { this._originaldate = originaldate; }

    //getting foreignKey
    public int getForeignKey(){ return this._foreignKey; }

    //setting foreignKey
    public void setForeignKey(int foreignKey) { this._foreignKey = foreignKey; }

}
