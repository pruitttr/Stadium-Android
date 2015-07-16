package com.stadiumse.stadiumstockexchange;

public class Schedule {

    int _stockID;
    String _opponent;
    String _result;


    // Empty constructor
    public Schedule(){

    }
    // constructor
    public Schedule(int stockID, String opponent, String result){
        this._stockID = stockID;
        this._opponent = opponent;
        this._result = result;

    }

    // getting
    public int getStockID(){
        return this._stockID;
    }

    // setting
    public void setStockID(int stockID){
        this._stockID = stockID;
    }

    // getting
    public String getOpponent(){
        return this._opponent;
    }

    // setting
    public void setOpponent(String opponent){
        this._opponent = opponent;
    }

    // getting
    public String getResult(){
        return this._result;
    }

    // setting
    public void setResult(String result){
        this._result = result;
    }
}
