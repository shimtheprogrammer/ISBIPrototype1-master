package com.prototype.isbi.isbiprototype1.databaseHandlers;

/**
 * Created by MRuto on 1/25/2017.
 */

public class GoodsPreOrdered {

    int _id, _number, _foreignKey, _total;
    double _cost;
    String _product, _date, _unit;

    public GoodsPreOrdered() {

    }

    //constructor
    public GoodsPreOrdered(int id, String product, int number, String unit, double cost, int total, String date, int foreignKey){
        this._id = id;
        this._product = product;
        this._number = number;
        this._unit = unit;
        this._cost = cost;
        this._total = total;
        this._date = date;
        this._foreignKey = foreignKey;
    }

    //constructor
    public GoodsPreOrdered(String product, int number, String unit, double cost, int total, String date, int foreignKey){
        this._product = product;
        this._number = number;
        this._unit = unit;
        this._cost = cost;
        this._total = total;
        this._date = date;
        this._foreignKey = foreignKey;
    }

    //getting id
    public int getId(){ return this._id; }

    //setting id
    public void setId(int id) { this._id = id; }

    //getting product
    public String getProduct() { return this._product; }

    //setting product
    public void setProduct(String product) { this._product = product; }

    //getting number
    public int getNumber() { return this._number; }

    //setting number
    public void setNumber(int number) { this._number = number; }

    // getting units
    public String getUnit(){
        return this._unit;
    }

    // setting units
    public void setUnit(String unit){ this._unit = unit; }

    //getting cost
    public double getCost() { return this._cost; }

    //setting cost
    public void setCost(double cost) { this._cost = cost; }

    //getting total
    public int getTotal() { return this._total; }

    //setting total
    public void setTotal(int total) { this._total = total; }

    //getting date
    public String getDate() { return this._date; }

    //setting date
    public void setDate( String date) { this._date = date; }

    //setting foreignKey
    public int gettForeignKey() { return this._foreignKey; }

    //getting foreign key
    public void setForeignKay( int foreignKey ) { this._foreignKey = foreignKey; }

}
