package com.prototype.isbi.isbiprototype1.databaseHandlers;

/**
 * Created by MRuto on 1/18/2017.
 */

public class Delivery {

    int _id, _total;
    String _allocation, _paymentMethode, _date, _foreignKey;

    public Delivery(){

    }

    public Delivery(int id, String allocation, int total, String paymentMethode, String date, String foreignKey){
        this._id = id;
        this._allocation = allocation;
        this._paymentMethode = paymentMethode;
        this._total = total;
        this._date = date;
        this._foreignKey = foreignKey;
    }

    public Delivery(String allocation, int total, String paymentMethode, String date, String foreignKey){
        this._allocation = allocation;
        this._paymentMethode = paymentMethode;
        this._total = total;
        this._date = date;
        this._foreignKey = foreignKey;
    }

    // getting ID
    public int getID(){ return this._id; }

    // setting id
    public void setID(int id){ this._id = id; }

    // getting allocation
    public String getAllocation(){ return this._allocation; }

    // setting allocation
    public void setAllocation(String allocation){
        this._allocation = allocation;
    }

    // getting total
    public int getTotal(){ return this._total; }

    // setting total
    public void setTotal(int total){ this._total = total; }

    // getting paymentMethode
    public String getPaymentMethode(){ return this._paymentMethode; }

    // setting paymentMethode
    public void setPaymentMethode(String paymentMethode){
        this._paymentMethode = paymentMethode;
    }

    // getting date
    public String getDate(){ return this._date; }

    // setting paymentMethode
    public void setDate(String date){
        this._date = date;
    }

    // getting foreignKey
    public String getForeignKey(){ return this._foreignKey; }

    // setting foreignKey
    public void setForeignKey(String foreignKey){ this._foreignKey = foreignKey; }
}
