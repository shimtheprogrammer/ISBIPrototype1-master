package com.prototype.isbi.isbiprototype1.databaseHandlers;

/**
 * Created by MRuto on 2/15/2017.
 */

public class Loans {

    //private variables
    int _id, _total, _instalments, _instalmentsNo, _contractualSavings;
    String _name, _payment, _startdate, _endDate, _status, _contractualSavingsDate;

    // Empty constructor
    public Loans(){

    }
    // constructor
    public Loans(int id, String name, int total, String payment, String startdate, String enddate, int instalments,
                 int instalmentsNo, int contractualSavings, String contractualSavingsDate, String status){
        this._id = id;
        this._name = name;
        this._total = total;
        this._payment = payment;
        this._startdate = startdate;
        this._endDate = enddate;
        this._instalments = instalments;
        this._instalmentsNo = instalmentsNo;
        this._contractualSavings = contractualSavings;
        this._contractualSavingsDate = contractualSavingsDate;
        this._status = status;

    }

    // constructor
    public Loans(String name, int total, String payment, String startdate, String enddate, int instalments,
                 int instalmentsNo, int contractualSavings, String contractualSavingsDate,String status){
        this._name = name;
        this._total = total;
        this._payment = payment;
        this._startdate = startdate;
        this._endDate = enddate;
        this._instalments = instalments;
        this._instalmentsNo = instalmentsNo;
        this._contractualSavings = contractualSavings;
        this._contractualSavingsDate = contractualSavingsDate;
        this._status = status;
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
    public void setName(String name){ this._name = name; }

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
    public String getStartDate(){
        return this._startdate;
    }

    // setting idpp
    public void setStartDate(String date){
        this._startdate = date;
    }

    // getting IDpp
    public String getEndDate(){
        return this._endDate;
    }

    // setting idpp
    public void setEndDate(String info){
        this._endDate = info;
    }

    // getting emial
    public int getInstalments(){ return this._instalments; }

    // setting amil
    public void setInstalments(int instalments){
        this._instalments = instalments;
    }

    // getting emial
    public int getInstalmentsNo(){ return this._instalmentsNo; }

    // setting amil
    public void setInstalmentsNo(int instalmentNo){
        this._instalmentsNo = instalmentNo;
    }

    // getting emial
    public int getContractualSavings(){ return this._contractualSavings; }

    // setting amil
    public void setContractualSavings(int contractualSavings){ this._contractualSavings = contractualSavings; }

    // getting emial
    public String getContractualSavingsDate(){ return this._contractualSavingsDate; }

    // setting amil
    public void setContractualSavingsDate(String contractualSavingsDate){ this._contractualSavingsDate = contractualSavingsDate; }

    // getting name
    public String getStatus(){
        return this._status;
    }

    // setting name
    public void setStatus(String status){ this._status = status; }

}
