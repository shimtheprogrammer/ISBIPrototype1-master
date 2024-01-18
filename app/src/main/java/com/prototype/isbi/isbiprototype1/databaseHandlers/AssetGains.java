package com.prototype.isbi.isbiprototype1.databaseHandlers;

/**
 * Created by MRuto on 2/9/2017.
 */

public class AssetGains {

    int _id, _total, _foreignKey;
    String _customer, _date, _payment;

    public AssetGains() {

    }

    //constructor
    public AssetGains(int id, String customer, int total, String payment, String date, int foreignKey){
        this._id = id;
        this._customer = customer;
        this._total = total;
        this._payment = payment;
        this._date = date;
        this._foreignKey = foreignKey;
    }

    //constructor
    public AssetGains(String customer, int total, String payment, String date, int foreignKey){
        this._customer = customer;
        this._total = total;
        this._payment = payment;
        this._date = date;
        this._foreignKey = foreignKey;
    }

    //getting id
    public int getId(){ return this._id; }

    //setting id
    public void setId(int id) { this._id = id; }

    //getting customer
    public String getCustomer() { return this._customer; }

    //setting customer
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

    //setting foreignKey
    public int gettForeignKey() { return this._foreignKey; }

    //getting foreign key
    public void setForeignKay( int foreignKey ) { this._foreignKey = foreignKey; }

}
