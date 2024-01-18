package com.prototype.isbi.isbiprototype1.databaseHandlers;

/**
 * Created by MRuto on 2/7/2017.
 */

public class ReUp {

    //private variables
    int _id, _total, _foreignKey;
    String _payment, _date, _info;

    // Empty constructor
    public ReUp(){

    }
    // constructor
    public ReUp(int id, String info, int total, String payment, String date, int foreignKey){
        this._id = id;
        this._total = total;
        this._payment = payment;
        this._info = info;
        this._date = date;
        this._foreignKey = foreignKey;
    }

    // constructor
    public ReUp(String info, int total, String payment, String date, int foreignKey){
        this._total = total;
        this._payment = payment;
        this._info = info;
        this._date = date;
        this._foreignKey = foreignKey;
    }
    // getting ID
    public int getID(){
        return this._id;
    }

    // setting id
    public void setID(int id){
        this._id = id;
    }

    // getting emial
    public int getTotal(){ return this._total; }

    // setting amil
    public void setTotal(int total){
        this._total = total;
    }

    // getting IDpp
    public String getPayment(){
        return this._payment;
    }

    // setting idpp
    public void setPayment(String payment){
        this._payment = payment;
    }

    // getting IDpp
    public String getInfo(){
        return this._info;
    }

    // setting idpp
    public void setInfo(String info){
        this._info = info;
    }

    // getting IDpp
    public String getDate(){
        return this._date;
    }

    // setting idpp
    public void setDate(String date){
        this._date = date;
    }

    //setting foreignKey
    public int gettForeignKey() { return this._foreignKey; }

    //getting foreign key
    public void setForeignKay( int foreignKey ) { this._foreignKey = foreignKey; }

}
