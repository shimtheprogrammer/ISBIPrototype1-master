package com.prototype.isbi.isbiprototype1.databaseHandlers;

/**
 * Created by MRuto on 1/9/2017.
 */

public class Sales {

    int _id, _total;
    String _customer, _date, _payment;

    public Sales() {

    }

    //constructor
    public Sales(int id, String customer, int total, String payment, String date){
        this._id = id;
        this._customer = customer;
        this._total = total;
        this._payment = payment;
        this._date = date;
    }

    //constructor
    public Sales(String customer, int total, String payment, String date){
        this._customer = customer;
        this._total = total;
        this._payment = payment;
        this._date = date;
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
}
