package com.stadiumse.stadiumstockexchange;

public class Dashboard {

    int _userID;
    String _portfolioCash;
    String _portfolioValue;
    String _stockCount;
    String _favTicker;
    String _favStockName;
    String _favFanbaseRank;
    String _rivalTicker;
    String _rivalStockName;
    String _rivalFanbaseRank;

    // Empty constructor
    public Dashboard(){

    }
    // constructor
    public Dashboard(int userID, String portfolioCash, String portfolioValue, String stockCount,
                     String favTicker, String favStockName, String favFanbaseRank,
                     String rivalTicker, String rivalStockName, String rivalFanbaseRank) {
        this._userID = userID;
        this._portfolioCash = portfolioCash;
        this._portfolioValue = portfolioValue;
        this._stockCount = stockCount;
        this._favTicker = favTicker;
        this._favStockName = favStockName;
        this._favFanbaseRank = favFanbaseRank;
        this._rivalTicker = rivalTicker;
        this._rivalStockName = rivalStockName;
        this._rivalFanbaseRank = rivalFanbaseRank;
    }

    // getting
    public int getUserID(){
        return this._userID;
    }

    // setting
    public void setUserID(int userID){
        this._userID = userID;
    }

    // getting
    public String getPortfolioCash(){
        return this._portfolioCash;
    }

    // setting
    public void setPortfolioCash(String portfolioCash){
        this._portfolioCash = portfolioCash;
    }

    // getting
    public String getPortfolioValue(){
        return this._portfolioValue;
    }

    // setting
    public void setPortfolioValue(String portfolioValue){
        this._portfolioValue = portfolioValue;
    }

    // getting
    public String getStockCount(){
        return this._stockCount;
    }

    // setting
    public void setStockCount(String stockCount){
        this._stockCount = stockCount;
    }

    // getting
    public String getFavTicker(){
        return this._favTicker;
    }

    // setting
    public void setFavTicker(String favTicker){
        this._favTicker = favTicker;
    }

    // getting
    public String getFavStockName(){
        return this._favStockName;
    }

    // setting
    public void setFavStockName(String favStockName){
        this._favStockName = favStockName;
    }

    // getting
    public String getFavFanbaseRank(){
        return this._favFanbaseRank;
    }

    // setting
    public void setFavFanbaseRank(String favFanbaseRank){
        this._favFanbaseRank = favFanbaseRank;
    }

    // getting
    public String getRivalTicker(){
        return this._rivalTicker;
    }

    // setting
    public void setRivalTicker(String rivalTicker){
        this._rivalTicker = rivalTicker;
    }

    // getting
    public String getRivalStockName(){
        return this._rivalStockName;
    }

    // setting
    public void setRivalStockName(String rivalStockName){
        this._rivalStockName = rivalStockName;
    }

    // getting
    public String getRivalFanbaseRank(){
        return this._rivalFanbaseRank;
    }

    // setting
    public void setRivalFanbaseRank(String rivalFanbaseRank){
        this._rivalFanbaseRank = rivalFanbaseRank;
    }
}
