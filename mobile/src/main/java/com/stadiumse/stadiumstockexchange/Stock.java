package com.stadiumse.stadiumstockexchange;

public class Stock {

    String _currentPrice;
    String _dailyHigh;
    String _dailyLow;
    String _dailyOpen;
    int _stockID;
    String _ipoPrice;
    String _stockName;
    String _pictureUrl;
    String _stockRecord;
    String _seasonHigh;
    String _seasonLow;
    String _stockTicker;

    // Empty constructor
    public Stock(){

    }
    // constructor
    public Stock(String currentPrice, String dailyHigh, String dailyLow, String dailyOpen,
            int stockID, String ipoPrice, String stockName, String stockRecord, String pictureUrl,
            String seasonHigh, String seasonLow, String stockTicker){
        this._currentPrice = currentPrice;
        this._dailyHigh = dailyHigh;
        this._dailyLow = dailyLow;
        this._dailyOpen = dailyOpen;
        this._stockID = stockID;
        this._ipoPrice = ipoPrice;
        this._stockName = stockName;
        this._pictureUrl = pictureUrl;
        this._stockRecord = stockRecord;
        this._seasonHigh = seasonHigh;
        this._seasonLow = seasonLow;
        this._stockTicker = stockTicker;

    }

    // getting
    public String getCurrentPrice(){
        return this._currentPrice;
    }

    // setting
    public void setCurrentPrice(String currentPrice){
        this._currentPrice = currentPrice;
    }

    // getting
    public String getDailyHigh(){
        return this._dailyHigh;
    }

    // setting
    public void setDailyHigh(String dailyHigh){
        this._dailyHigh = dailyHigh;
    }

    // getting
    public String getDailyLow(){
        return this._dailyHigh;
    }

    // setting
    public void setDailyLow(String dailyHigh){
        this._dailyHigh = dailyHigh;
    }

    // getting
    public String getDailyOpen(){
        return this._dailyOpen;
    }

    // setting
    public void setDailyOpen(String dailyOpen){
        this._dailyOpen = dailyOpen;
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
    public String getIpoPrice(){
        return this._ipoPrice;
    }

    // setting
    public void setIpoPrice(String ipoPrice){
        this._ipoPrice = ipoPrice;
    }

    // getting
    public String getStockName(){
        return this._stockName;
    }

    // setting
    public void setStockName(String stockName){
        this._stockName = stockName;
    }

    // getting
    public String getPictureUrl(){
        return this._pictureUrl;
    }

    // setting
    public void setPictureUrl(String pictureUrl){
        this._pictureUrl = pictureUrl;
    }

    // getting
    public String getStockRecord(){
        return this._stockRecord;
    }

    // setting
    public void setStockRecord(String stockRecord){
        this._stockRecord = stockRecord;
    }

    // getting
    public String getSeasonHigh(){
        return this._seasonHigh;
    }

    // setting
    public void setSeasonHigh(String seasonHigh){
        this._seasonHigh = seasonHigh;
    }

    // getting
    public String getSeasonLow(){
        return this._seasonLow;
    }

    // setting
    public void setSeasonLow(String seasonLow){
        this._seasonLow = seasonLow;
    }

    // getting
    public String getStockTicker(){
        return this._stockTicker;
    }

    // setting
    public void setStockTicker(String stockTicker){
        this._stockTicker = stockTicker;
    }
}
