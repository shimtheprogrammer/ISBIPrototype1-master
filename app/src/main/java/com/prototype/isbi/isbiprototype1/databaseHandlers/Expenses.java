package com.prototype.isbi.isbiprototype1.databaseHandlers;

/**
 * Created by MRuto on 1/25/2017.
 */

public class Expenses {

    //private variables
    int _id, _total;
    String _name, _payment, _date, _info, _type;

    // Empty constructor
    public Expenses(){

    }
    // constructor
    public Expenses(int id, String type, String name, int total, String payment, String info, String date){
        this._id = id;
        this._type = type;
        this._name = name;
        this._total = total;
        this._payment = payment;
        this._info = info;
        this._date = date;
    }

    // constructor
    public Expenses(String type, String name, int total, String payment, String info, String date){
        this._type = type;
        this._name = name;
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


    // getting name
    public String getName(){
        return this._name;
    }

    // setting name
    public void setName(String name){
        this._name = name;
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
