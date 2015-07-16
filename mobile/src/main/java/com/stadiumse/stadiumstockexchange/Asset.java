package com.stadiumse.stadiumstockexchange;

public class Asset {

    int _stockID;
    String _numberShares;
    String _priceChange;
    String _totalValue;

    // Empty constructor
    public Asset(){

    }
    // constructor
    public Asset(int stockID, String numberShares, String priceChange, String totalValue) {
        this._stockID = stockID;
        this._numberShares = numberShares;
        this._priceChange = priceChange;
        this._totalValue = totalValue;
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
    public String getNumberShares(){
        return this._numberShares;
    }

    // setting
    public void setNumberShares(String numberShares){
        this._numberShares = numberShares;
    }

    // getting
    public String getPriceChange(){
        return this._priceChange;
    }

    // setting
    public void setPriceChange(String priceChange){
        this._priceChange = priceChange;
    }

    // getting
    public String getTotalValue(){
        return this._totalValue;
    }

    // setting
    public void setTotalValue(String totalValue){
        this._totalValue = totalValue;
    }

}
