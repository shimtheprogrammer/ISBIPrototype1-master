package com.prototype.isbi.isbiprototype1.databaseHandlers;

/**
 * Created by MRuto on 1/31/2017.
 */

public class ClosedPreSale {

    int _id, _total, _foreignKey;
    String _customer, _date, _payment, _originaldate;

    public ClosedPreSale() {

    }

    //constructor
    public ClosedPreSale(int id, String customer, int total, String payment, String date, String originaldate, int foreignKey){
        this._id = id;
        this._customer = customer;
        this._total = total;
        this._payment = payment;
        this._date = date;
        this._originaldate = originaldate;
        this._foreignKey = foreignKey;
    }

    //constructor
    public ClosedPreSale(String customer, int total, String payment, String date, String originaldate, int foreignKey){
        this._customer = customer;
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
    public String getCustomer() { return this._customer; }

    //setting supplier
    public void setCustomer(String customer) { this._customer = customer; }

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
