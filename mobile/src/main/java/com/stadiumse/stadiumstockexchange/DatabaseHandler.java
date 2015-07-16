package com.stadiumse.stadiumstockexchange;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "DatabaseHandler";

    // Contacts table name
    private static final String TABLE_STOCKS = "stocks";
    private static final String TABLE_SCHEDULE = "schedule";
    private static final String TABLE_DASHBOARD = "dashboard";
    private static final String TABLE_ASSETS = "assets";

    // Contacts Table Columns names
    private static final String KEY_STOCK_CURRENT_PRICE = "current_price";
    private static final String KEY_STOCK_DAILY_HIGH = "daily_high";
    private static final String KEY_STOCK_DAILY_LOW = "daily_low";
    private static final String KEY_STOCK_DAILY_OPEN = "daily_open";
    private static final String KEY_STOCK_ID = "id";
    private static final String KEY_STOCK_IPO_PRICE = "ipo_price";
    private static final String KEY_STOCK_NAME = "stock_name";
    private static final String KEY_STOCK_RECORD = "record";
    private static final String KEY_STOCK_PICTURE_URL = "picture_url";
    private static final String KEY_STOCK_SEASON_HIGH = "season_high";
    private static final String KEY_STOCK_SEASON_LOW = "season_low";
    private static final String KEY_STOCK_TICKER = "ticker";

    private static final String KEY_SCHEDULE_STOCKID = "id";
    private static final String KEY_SCHEDULE_OPPONENT = "opponent";
    private static final String KEY_SCHEDULE_RESULT = "result";

    private static final String KEY_DASHBOARD_USER_ID = "userID";
    private static final String KEY_DASHBOARD_CASH = "cachedCash";
    private static final String KEY_DASHBOARD_VALUE = "currentValue";
    private static final String KEY_DASHBOARD_STOCKCOUNT = "stockCount";
    private static final String KEY_DASHBOARD_FAVTICKER = "favTicker";
    private static final String KEY_DASHBOARD_FAVSTOCKNAME = "favStockName";
    private static final String KEY_DASHBOARD_FAVFANBASERANK = "favFanbaseRank";
    private static final String KEY_DASHBOARD_RIVALTICKER = "rivalTicker";
    private static final String KEY_DASHBOARD_RIVALSTOCKNAME = "rivalStockName";
    private static final String KEY_DASHBOARD_RIVALFANBASERANK = "rivalFanbaseRank";

    private static final String KEY_ASSETS_STOCK_ID = "stockID";
    private static final String KEY_ASSETS_PRICE_CHANGE = "priceChange";
    private static final String KEY_ASSETS_NUMBER_SHARES = "numberShares";
    private static final String KEY_ASSETS_TOTAL_VALUE = "totalValue";


    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_STOCKS_TABLE = "CREATE TABLE " + TABLE_STOCKS + "("
                + KEY_STOCK_CURRENT_PRICE + " TEXT," + KEY_STOCK_DAILY_HIGH + " TEXT,"
                + KEY_STOCK_DAILY_LOW + " TEXT," + KEY_STOCK_DAILY_OPEN + " TEXT,"
                + KEY_STOCK_ID + " REAL," + KEY_STOCK_IPO_PRICE + " TEXT,"
                + KEY_STOCK_NAME + " TEXT," + KEY_STOCK_RECORD + " TEXT,"
                + KEY_STOCK_PICTURE_URL + " TEXT," + KEY_STOCK_SEASON_HIGH + " TEXT,"
                + KEY_STOCK_SEASON_LOW + " TEXT," + KEY_STOCK_TICKER + " TEXT" + ")";
        db.execSQL(CREATE_STOCKS_TABLE);

        String CREATE_SCHEDULE_TABLE = "CREATE TABLE " + TABLE_SCHEDULE + "("
                + KEY_SCHEDULE_STOCKID + " TEXT," + KEY_SCHEDULE_OPPONENT + " TEXT,"
                + KEY_SCHEDULE_RESULT + " TEXT" + ")";
        db.execSQL(CREATE_SCHEDULE_TABLE);

        String CREATE_DASHBOARD_TABLE = "CREATE TABLE " + TABLE_DASHBOARD + "("
                + KEY_DASHBOARD_USER_ID + " REAL," + KEY_DASHBOARD_CASH + " TEXT,"
                + KEY_DASHBOARD_VALUE + " TEXT," + KEY_DASHBOARD_STOCKCOUNT + " TEXT,"
                + KEY_DASHBOARD_FAVTICKER + " TEXT," + KEY_DASHBOARD_FAVSTOCKNAME + " TEXT,"
                + KEY_DASHBOARD_FAVFANBASERANK + " TEXT," + KEY_DASHBOARD_RIVALTICKER + " TEXT,"
                + KEY_DASHBOARD_RIVALSTOCKNAME + " TEXT," + KEY_DASHBOARD_RIVALFANBASERANK + " TEXT"
                + ")";
        db.execSQL(CREATE_DASHBOARD_TABLE);

        String CREATE_ASSETS_TABLE = "CREATE TABLE " + TABLE_ASSETS + "("
                + KEY_ASSETS_STOCK_ID + " REAL," + KEY_ASSETS_NUMBER_SHARES + " TEXT,"
                + KEY_ASSETS_PRICE_CHANGE + " TEXT," + KEY_ASSETS_TOTAL_VALUE + " TEXT"
                + ")";
        db.execSQL(CREATE_ASSETS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STOCKS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SCHEDULE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DASHBOARD);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ASSETS);

        // Create tables again
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adding new stock
    void addStock(Stock stock) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_STOCK_CURRENT_PRICE, stock.getCurrentPrice());
        values.put(KEY_STOCK_DAILY_HIGH, stock.getDailyHigh());
        values.put(KEY_STOCK_DAILY_LOW, stock.getDailyLow());
        values.put(KEY_STOCK_DAILY_OPEN, stock.getDailyOpen());
        values.put(KEY_STOCK_ID, stock.getStockID());
        values.put(KEY_STOCK_IPO_PRICE, stock.getIpoPrice());
        values.put(KEY_STOCK_NAME, stock.getStockName());
        values.put(KEY_STOCK_PICTURE_URL, stock.getPictureUrl());
        values.put(KEY_STOCK_RECORD, stock.getStockRecord());
        values.put(KEY_STOCK_SEASON_HIGH, stock.getSeasonHigh());
        values.put(KEY_STOCK_SEASON_LOW, stock.getSeasonLow());
        values.put(KEY_STOCK_TICKER, stock.getStockTicker());

        // Inserting Row
        db.insert(TABLE_STOCKS, null, values);
        db.close(); // Closing database connection
    }

    //adding new schedule
    void addSchedule(Schedule schedule) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_SCHEDULE_STOCKID, schedule.getStockID());
        values.put(KEY_SCHEDULE_OPPONENT, schedule.getOpponent());
        values.put(KEY_SCHEDULE_RESULT, schedule.getResult());

        // Inserting Row
        db.insert(TABLE_SCHEDULE, null, values);
        db.close(); // Closing database connection
    }

    // Adding new stock
    void addDashboard(Dashboard dashboard) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_DASHBOARD_USER_ID, dashboard.getUserID());
        values.put(KEY_DASHBOARD_CASH, dashboard.getPortfolioCash());
        values.put(KEY_DASHBOARD_VALUE, dashboard.getPortfolioValue());
        values.put(KEY_DASHBOARD_STOCKCOUNT, dashboard.getStockCount());
        values.put(KEY_DASHBOARD_FAVTICKER, dashboard.getFavTicker());
        values.put(KEY_DASHBOARD_FAVSTOCKNAME, dashboard.getFavStockName());
        values.put(KEY_DASHBOARD_FAVFANBASERANK, dashboard.getFavFanbaseRank());
        values.put(KEY_DASHBOARD_RIVALTICKER, dashboard.getRivalTicker());
        values.put(KEY_DASHBOARD_RIVALSTOCKNAME, dashboard.getRivalStockName());
        values.put(KEY_DASHBOARD_RIVALFANBASERANK, dashboard.getRivalFanbaseRank());

        // Inserting Row
        db.insert(TABLE_DASHBOARD, null, values);
        db.close(); // Closing database connection
    }

    void addAsset(Asset asset) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ASSETS_STOCK_ID, asset.getStockID());
        values.put(KEY_ASSETS_NUMBER_SHARES, asset.getNumberShares());
        values.put(KEY_ASSETS_PRICE_CHANGE, asset.getPriceChange());
        values.put(KEY_ASSETS_TOTAL_VALUE, asset.getTotalValue());

        // Inserting Row
        db.insert(TABLE_ASSETS, null, values);
        db.close(); // Closing database connection
    }

    // Getting single stock
    Stock getStock(int stockID) {
        SQLiteDatabase db = this.getReadableDatabase();
        Stock stock = null;

        Cursor cursor = db.query(TABLE_STOCKS, new String[]{KEY_STOCK_CURRENT_PRICE,
                        KEY_STOCK_DAILY_HIGH, KEY_STOCK_DAILY_LOW, KEY_STOCK_DAILY_OPEN,
                        KEY_STOCK_ID, KEY_STOCK_IPO_PRICE, KEY_STOCK_NAME, KEY_STOCK_PICTURE_URL,
                        KEY_STOCK_RECORD, KEY_STOCK_SEASON_HIGH, KEY_STOCK_SEASON_LOW,
                        KEY_STOCK_TICKER}, KEY_STOCK_ID + "=" + stockID, null,
                null, null, null, null);
        if(cursor.moveToFirst()) {

            stock = new Stock(cursor.getString(0), cursor.getString(1), cursor.getString(2),
                    cursor.getString(3), cursor.getInt(4), cursor.getString(5), cursor.getString(6),
                    cursor.getString(7), cursor.getString(8), cursor.getString(9), cursor.getString(10),
                    cursor.getString(11));
        }
        cursor.close(); //closing cursor
        db.close(); //closing database connection
        // return contact
        return stock;
    }

    // Getting single schedule
    Schedule getSchedule(int stockID) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_SCHEDULE, new String[] { KEY_SCHEDULE_STOCKID,
                        KEY_SCHEDULE_OPPONENT, KEY_SCHEDULE_RESULT},
                        KEY_SCHEDULE_STOCKID + "=?", new String[] { String.valueOf(stockID) },
                        null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Schedule schedule = new Schedule(cursor.getInt(0), cursor.getString(1), cursor.getString(2));
        cursor.close();
        db.close();
        // return contact
        return schedule;
    }

    // Getting dashboard
    Dashboard getDashboard(int userID) {
        SQLiteDatabase db = this.getReadableDatabase();
        Dashboard dashboard = null;

        Cursor cursor = db.query(TABLE_DASHBOARD, new String[]{KEY_DASHBOARD_USER_ID, KEY_DASHBOARD_CASH,
                        KEY_DASHBOARD_VALUE, KEY_DASHBOARD_STOCKCOUNT, KEY_DASHBOARD_FAVTICKER,
                        KEY_DASHBOARD_FAVSTOCKNAME, KEY_DASHBOARD_FAVFANBASERANK, KEY_DASHBOARD_RIVALTICKER,
                        KEY_DASHBOARD_RIVALSTOCKNAME, KEY_DASHBOARD_RIVALFANBASERANK},
                KEY_DASHBOARD_USER_ID + "=" + userID, null,
                null, null, null, null);
        if(cursor.moveToFirst()) {

            dashboard = new Dashboard(cursor.getInt(0), cursor.getString(1), cursor.getString(2),
                    cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6),
                    cursor.getString(7), cursor.getString(8), cursor.getString(9));
        }
        cursor.close(); //closing cursor
        db.close(); //closing database connection
        // return contact
        return dashboard;
    }

    // Getting stock list
    public List<Stock> getStockList() {
        List<Stock> stockList = new ArrayList<Stock>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_STOCKS + " ORDER BY "
                + KEY_STOCK_CURRENT_PRICE + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Stock stock = new Stock();
                stock.setCurrentPrice(cursor.getString(0));
                stock.setDailyHigh(cursor.getString(1));
                stock.setDailyLow(cursor.getString(2));
                stock.setDailyOpen(cursor.getString(3));
                stock.setStockID(cursor.getInt(4));
                stock.setIpoPrice(cursor.getString(5));
                stock.setStockName(cursor.getString(6));
                stock.setPictureUrl(cursor.getString(7));
                stock.setStockRecord(cursor.getString(8));
                stock.setSeasonHigh(cursor.getString(9));
                stock.setSeasonLow(cursor.getString(10));
                stock.setStockTicker(cursor.getString(11));
                // Adding contact to list
                stockList.add(stock);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        // return contact list
        return stockList;
    }

    // Getting asset list
    public List<Asset> getAssetList() {
        List<Asset> assetList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_ASSETS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Asset asset = new Asset();
                asset.setStockID(cursor.getInt(0));
                asset.setNumberShares(cursor.getString(1));
                asset.setPriceChange(cursor.getString(2));
                asset.setTotalValue(cursor.getString(3));

                // Adding contact to list
                assetList.add(asset);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        // return contact list
        return assetList;
    }

    // Updating stock
    public int updateStock(Stock stock) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_STOCK_CURRENT_PRICE, stock.getCurrentPrice());
        values.put(KEY_STOCK_DAILY_HIGH, stock.getDailyHigh());
        values.put(KEY_STOCK_DAILY_LOW, stock.getDailyLow());
        values.put(KEY_STOCK_DAILY_OPEN, stock.getDailyOpen());
        values.put(KEY_STOCK_ID, stock.getStockID());
        values.put(KEY_STOCK_IPO_PRICE, stock.getIpoPrice());
        values.put(KEY_STOCK_NAME, stock.getStockName());
        values.put(KEY_STOCK_PICTURE_URL, stock.getPictureUrl());
        values.put(KEY_STOCK_RECORD, stock.getStockRecord());
        values.put(KEY_STOCK_SEASON_HIGH, stock.getSeasonHigh());
        values.put(KEY_STOCK_SEASON_LOW, stock.getSeasonLow());
        values.put(KEY_STOCK_TICKER, stock.getStockTicker());

        int result = db.update(TABLE_STOCKS, values, KEY_STOCK_ID + " = ?",
                new String[] { String.valueOf(stock.getStockID()) });
        db.close(); //closing database connection
        // updating row
        return result;
    }

    // Updating single schedule
    public int updateSchedule(Schedule schedule) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_SCHEDULE_STOCKID, schedule.getStockID());
        values.put(KEY_SCHEDULE_OPPONENT, schedule.getOpponent());
        values.put(KEY_SCHEDULE_RESULT, schedule.getResult());

        int result = db.update(TABLE_SCHEDULE, values, KEY_SCHEDULE_STOCKID + " = ?",
                new String[] { String.valueOf(schedule.getStockID()) });
        db.close();
        // updating row
        return result;
    }

    // Updating single contact
    public int updateDashboard(Dashboard dashboard) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_DASHBOARD_USER_ID, dashboard.getUserID());
        values.put(KEY_DASHBOARD_CASH, dashboard.getPortfolioCash());
        values.put(KEY_DASHBOARD_VALUE, dashboard.getPortfolioValue());
        values.put(KEY_DASHBOARD_STOCKCOUNT, dashboard.getStockCount());
        values.put(KEY_DASHBOARD_FAVTICKER, dashboard.getFavTicker());
        values.put(KEY_DASHBOARD_FAVSTOCKNAME, dashboard.getFavStockName());
        values.put(KEY_DASHBOARD_FAVFANBASERANK, dashboard.getFavFanbaseRank());
        values.put(KEY_DASHBOARD_RIVALTICKER, dashboard.getRivalTicker());
        values.put(KEY_DASHBOARD_RIVALSTOCKNAME, dashboard.getRivalStockName());
        values.put(KEY_DASHBOARD_RIVALFANBASERANK, dashboard.getRivalFanbaseRank());

        int result = db.update(TABLE_DASHBOARD, values, KEY_DASHBOARD_USER_ID + " = ?",
                new String[] { String.valueOf(dashboard.getUserID()) });
        db.close(); //closing database connection
        // updating row
        return result;
    }

    // Updating assets
    void deleteAssets() {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("DELETE FROM " + TABLE_ASSETS);
        db.close(); //closing database connection
    }
}