package com.prototype.isbi.isbiprototype1.databaseHandlers;

/**
 * Created by MRuto on 12/16/2016.
 */

public class Inventory {

    //private variables
    int _id, _number, _total, _sprice;
    double _cost;
    String _product, _unit;

    // Empty constructor
    public Inventory(){

    }
    // constructor
    public Inventory(int id, String product, int number, String unit, double cost, int total, int sprice){
        this._id = id;
        this._product = product;
        this._number = number;
        this._unit = unit;
        this._cost = cost;
        this._total = total;
        this._sprice = sprice;
    }

    // constructor
    public Inventory(String product, int number, String unit, double cost, int total, int sprice){
        this._product = product;
        this._number = number;
        this._unit = unit;
        this._cost = cost;
        this._total = total;
        this._sprice = sprice;
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
    public String getProduct(){
        return this._product;
    }

    // setting id
    public void setProduct(String product){
        this._product = product;
    }

    // getting number
    public int getNumber(){
        return this._number;
    }

    // setting number
    public void setNumber(int number){
        this._number = number;
    }

    // getting units
    public String getUnit(){
        return this._unit;
    }

    // setting units
    public void setUnit(String unit){ this._unit = unit; }

    // getting cost
    public double getCost(){
        return this._cost;
    }

    // setting cost
    public void setCost(double cost){ this._cost = cost;}

    // getting total
    public int getTotal(){
        return this._total;
    }

    // setting total
    public void setTotal(int total){ this._total = total; }

    // getting cost
    public int getSPrice(){
        return this._sprice;
    }

    // setting cost
    public void setSPrice(int sprice){ this._sprice = sprice;}
}
