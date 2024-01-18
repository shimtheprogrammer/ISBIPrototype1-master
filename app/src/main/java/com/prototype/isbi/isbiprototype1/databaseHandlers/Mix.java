package com.prototype.isbi.isbiprototype1.databaseHandlers;

/**
 * Created by MRuto on 5/4/2017.
 */

public class Mix {

    int _id, _credit, _cash, _bank, _mpesa, _bankfee, _mpesafee;
    String _flag;

    public Mix(){

    }

    public Mix(String flag, int credit, int cash, int bank, int mpesa, int bankfee, int mpesafee){
        this._flag = flag;
        this._credit = credit;
        this._cash = cash;
        this._bank = bank;
        this._mpesa = mpesa;
        this._bankfee = bankfee;
        this._mpesafee = mpesafee;
    }

    public Mix(int id, String flag, int credit, int cash, int bank, int mpesa, int bankfee, int mpesafee){
        this._id = id;
        this._flag = flag;
        this._credit = credit;
        this._cash = cash;
        this._bank = bank;
        this._mpesa = mpesa;
        this._bankfee = bankfee;
        this._mpesafee = mpesafee;
    }

    // getting ID
    public int getID(){ return this._id; }

    // setting id
    public void setID(int id){ this._id = id; }

    // getting flag
    public String getFlag(){
        return this._flag;
    }

    // setting flag
    public void setFlag(String flag){
        this._flag = flag;
    }

    // getting credit
    public int getCredit(){ return this._credit; }

    // setting credit
    public void setCredit(int credit){ this._credit = credit; }

    // getting cash
    public int getCash(){
        return this._cash;
    }

    // setting cash
    public void setCash(int cash){ this._cash = cash; }

    // getting bank
    public int getBank(){
        return this._bank;
    }

    // setting bank
    public void setBank(int bank){ this._bank = bank; }

    // getting mpesa
    public int getMpesa(){
        return this._mpesa;
    }

    // setting mpesa
    public void setMpesa(int mpesa){ this._mpesa = mpesa; }

    // getting bank
    public int getBankFee(){
        return this._bankfee;
    }

    // setting bank
    public void setBankFee(int bankfee){ this._bankfee = bankfee; }

    // getting mpesa
    public int getMpesaFee(){ return this._mpesafee; }

    // setting mpesa
    public void setMpesaFee(int mpesafee){ this._mpesafee = mpesafee; }
}
