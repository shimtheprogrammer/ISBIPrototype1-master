package com.prototype.isbi.isbiprototype1.databaseHandlers;

/**
 * Created by MRuto on 2/1/2017.
 */

public class GlobalVariables {

    //private variables
    int _id, _total;
    String _variable;

    // Empty constructor
    public GlobalVariables(){

    }
    // constructor
    public GlobalVariables(int id, String variable, int total){
        this._id = id;
        this._variable = variable;
        this._total = total;
    }

    // constructor
    public GlobalVariables(String variable, int total){
        this._variable = variable;
        this._total = total;
    }
    // getting ID
    public int getID(){
        return this._id;
    }

    // setting id
    public void setID(int id){
        this._id = id;
    }

    // getting product
    public String getVariable(){
        return this._variable;
    }

    // setting id
    public void setVariable(String variable){
        this._variable = variable;
    }

    // getting total
    public int getTotal(){
        return this._total;
    }

    // setting total
    public void setTotal(int total){ this._total = total; }
}
