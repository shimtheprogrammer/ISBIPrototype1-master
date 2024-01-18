package com.prototype.isbi.isbiprototype1.databaseHandlers;

/**
 * Created by MRuto on 8/28/2017.
 */

public class AccountDetails {

    //private variables
    int _id, _pin;
    String _email;

    // Empty constructor
    public AccountDetails(){

    }
    // constructor
    public AccountDetails(int id, String email, int pin){
        this._id = id;
        this._email = email;
        this._pin = pin;
    }

    // constructor
    public AccountDetails(String email, int pin){
        this._email = email;
        this._pin = pin;
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
    public String getEmail(){ return this._email; }

    // setting amil
    public void setEmail(String email){
        this._email = email;
    }

    // getting pin
    public int getPin(){
        return this._pin;
    }

    // setting pin
    public void setPin(int pin){
        this._pin = pin;
    }

}
