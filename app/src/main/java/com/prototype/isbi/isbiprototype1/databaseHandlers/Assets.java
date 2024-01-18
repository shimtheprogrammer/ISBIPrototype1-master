package com.prototype.isbi.isbiprototype1.databaseHandlers;

/**
 * Created by MRuto on 2/7/2017.
 */

public class Assets {

    //private variables
    int _id, _total, _usefull, _scrap;
    String _payment, _date, _info, _type;

    // Empty constructor
    public Assets(){

    }
    // constructor
    public Assets(int id, String type, String info, int total, String payment, String date, int usefull, int scrap){
        this._id = id;
        this._type = type;
        this._total = total;
        this._payment = payment;
        this._info = info;
        this._date = date;
        this._usefull = usefull;
        this._scrap = scrap;
    }

    // constructor
    public Assets(String type, String info, int total, String payment, String date, int usefull, int scrap){
        this._type = type;
        this._total = total;
        this._payment = payment;
        this._info = info;
        this._date = date;
        this._usefull = usefull;
        this._scrap = scrap;
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

    // getting usefull
    public int getUsefull(){ return this._usefull; }

    // setting usefull
    public void setUsefull(int usefull){
        this._usefull = usefull;
    }

    // getting scrap
    public int getScrsp(){ return this._scrap; }

    // setting scrap
    public void setScrap(int scrap){
        this._scrap = scrap;
    }

}
