package com.prototype.isbi.isbiprototype1.databaseHandlers;

/**
 * Created by MRuto on 12/16/2016.
 */

public class Cash {

    //private variables
    int _id, _actual, _bank, _mpesa;

    // Empty constructor
    public Cash(){

    }
    // constructor
    public Cash(int id, int actual, int bank, int mpesa){
        this._id = id;
        this._actual = actual;
        this._bank = bank;
        this._mpesa = mpesa;
    }

    // constructor
    public Cash(int actual, int bank, int mpesa){
        this._actual = actual;
        this._bank = bank;
        this._mpesa = mpesa;
    }
    // getting ID
    public int getID(){ return this._id; }

    // setting id
    public void setID(int id){ this._id = id; }

    // getting actual
    public int getActual(){ return this._actual; }

    // setting actual
    public void setActual(int actual){
        this._actual = actual;
    }

    // getting bank
    public int getBank(){
        return this._bank;
    }

    // setting bank
    public void setBank(int bank){ this._bank = bank;}

    // getting mpesa
    public int getMpesa(){
        return this._mpesa;
    }

    // setting mpesa
    public void setMpesa(int mpesa){ this._mpesa = mpesa; }
}
