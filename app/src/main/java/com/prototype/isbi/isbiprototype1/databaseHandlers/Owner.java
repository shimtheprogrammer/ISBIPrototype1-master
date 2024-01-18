package com.prototype.isbi.isbiprototype1.databaseHandlers;

/**
 * Created by MRuto on 1/31/2017.
 */

public class Owner {

    //private variables
    int _id, _total;
    String _payment, _date, _info, _type;

    // Empty constructor
    public Owner(){

    }
    // constructor
    public Owner(int id, String type, int total, String payment, String info, String date){
        this._id = id;
        this._type = type;
        this._total = total;
        this._payment = payment;
        this._info = info;
        this._date = date;
    }

    // constructor
    public Owner(String type, int total, String payment, String info, String date){
        this._type = type;
        this._total = total;
        this._payment = payment;
        this._info = info;
        this._date = date;
    }
    // getting ID
    public int getID(){
        return this._id;
    }

    // setting id
    public void setID(int id){
        this._id = id;
    }

    // getting name
    public String getType(){
        return this._type;
    }

    // setting name
    public void setType(String type){
        this._type = type;
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

}
