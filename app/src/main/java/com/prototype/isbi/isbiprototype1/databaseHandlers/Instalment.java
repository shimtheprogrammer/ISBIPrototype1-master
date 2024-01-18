package com.prototype.isbi.isbiprototype1.databaseHandlers;

/**
 * Created by MRuto on 2/15/2017.
 */

public class Instalment {

    int _id, _total, _contractualSavings, _foreignKey;
    String _date, _payment, _number;

    public Instalment() {

    }

    //constructor
    public Instalment(int id, int total, String payment, String date, String number, int contractualSavings, int foreignKey){
        this._id = id;
        this._total = total;
        this._payment = payment;
        this._date = date;
        this._number = number;
        this._contractualSavings = contractualSavings;
        this._foreignKey = foreignKey;
    }

    //constructor
    public Instalment(int total, String payment, String date, String number, int contractualSavings, int foreignKey){
        this._total = total;
        this._payment = payment;
        this._date = date;
        this._number = number;
        this._contractualSavings = contractualSavings;
        this._foreignKey = foreignKey;
    }

    //getting id
    public int getId(){ return this._id; }

    //setting id
    public void setId(int id) { this._id = id; }

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
    public String getNumber() { return this._number; }

    //setting date
    public void setNumber( String number) { this._number = number; }

    // getting emial
    public int getContractualSavings(){ return this._contractualSavings; }

    // setting amil
    public void setContractualSavings(int contractualSavings){ this._contractualSavings = contractualSavings; }

    //setting foreignKey
    public int gettForeignKey() { return this._foreignKey; }

    //getting foreign key
    public void setForeignKay( int foreignKey ) { this._foreignKey = foreignKey; }

}
