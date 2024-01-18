package com.prototype.isbi.isbiprototype1.databaseHandlers;

/**
 * Created by MRuto on 1/25/2017.
 */

public class PreSale {

    int _id, _total;
    String _customer, _date, _payment, _originalDate, _status;

    public PreSale() {

    }

    //constructor
    public PreSale(int id, String customer, int total, String payment, String date, String originalDate, String status){
        this._id = id;
        this._customer = customer;
        this._total = total;
        this._payment = payment;
        this._date = date;
        this._originalDate = originalDate;
        this._status = status;
    }

    //constructor
    public PreSale(String customer, int total, String payment, String date, String originalDate, String status){
        this._customer = customer;
        this._total = total;
        this._payment = payment;
        this._date = date;
        this._originalDate = originalDate;
        this._status = status;
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

    //getting date
    public String getOriginalDate() { return this._originalDate; }

    //setting date
    public void setOriginalDate( String originalDatedate) { this._originalDate = originalDatedate; }

    //getting status
    public String getStatus() { return this._status; }

    //setting status
    public void setStatus(String status) { this._status = status; }
}
