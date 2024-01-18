package com.prototype.isbi.isbiprototype1.databaseHandlers;

/**
 * Created by MRuto on 12/16/2016.
 */

public class PersonalData {

    //private variables
    int _id, _md_salary, _pin;
    String _name, _email, _b_name, _location;

    // Empty constructor
    public PersonalData(){

    }
    // constructor
    public PersonalData(int id, String name, String email, int mdSalary, int pin, String bName, String location){
        this._id = id;
        this._name = name;
        this._email = email;
        this._md_salary = mdSalary;
        this._pin = pin;
        this._b_name = bName;
        this._location = location;
    }

    // constructor
    public PersonalData( String name, String email, int mdSalary, int pin, String bName, String location){
        this._name = name;
        this._email = email;
        this._md_salary = mdSalary;
        this._pin = pin;
        this._b_name = bName;
        this._location = location;
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
    public String getName(){
        return this._name;
    }

    // setting name
    public void setName(String name){
        this._name = name;
    }

    // getting emial
    public String getEmail(){ return this._email; }

    // setting amil
    public void setEmail(String email){
        this._email = email;
    }

    // getting IDpp
    public int getMDSalary(){
        return this._md_salary;
    }

    // setting idpp
    public void setMDSalary(int mdSalary){
        this._md_salary = mdSalary;
    }

    // getting pin
    public int getPin(){
        return this._pin;
    }

    // setting pin
    public void setPin(int pin){
        this._pin = pin;
    }

    // getting BName
    public String getBName(){ return this._b_name; }

    // setting BName
    public void setBName(String BName){
        this._b_name = BName;
    }

    // getting Location
    public String getLocation(){ return this._location; }

    // setting Location
    public void setLocation(String location){
        this._location = location;
    }

}
