package com.stadiumse.stadiumstockexchange;

import android.app.Activity;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.NumberFormat;
import java.util.List;

public class StocksFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    private Activity a;
    private DatabaseHandler db;

    public StocksFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = new DatabaseHandler(a);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_stocks, container, false);

        //Get the stocksList from the server and store in database
        new FetchStocksTask().execute();

        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view_stocks);
        LinearLayoutManager llm = new LinearLayoutManager(a);
        recyclerView.setLayoutManager(llm);

        List<Stock> stocksList = db.getStockList();
        StockListRecyclerAdapter adapter = new StockListRecyclerAdapter(stocksList, R.layout.list_item_stock);
        recyclerView.setAdapter(adapter);

        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }

        a = activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public class FetchStocksTask extends AsyncTask<Void, Void, List<Stock>> {

        final String STOCK_ARRAY = "stocks";
        //final String STOCK_SCHEDULE = "mbl_scheduled_games";
        final String LOG_TAG = FetchStocksTask.class.getSimpleName();

        @Override
        protected List<Stock> doInBackground(Void...params) {

            //Must be declared here, so they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String stocksJSONStr = null;

            try {
                //Constructs the URL for the call.
                final String BASE_URL = "http://stadium-demo.herokuapp.com/mbl_stocks_index.json?" +
                        "secret=alak-_-sjdf!j234897alskf!$$";

                Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                        .build();

                URL url = new URL(builtUri.toString());

                // Create the request to Stadium server, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }

                reader = new BufferedReader(new InputStreamReader(inputStream));

                stocksJSONStr = reader.readLine();

                if (stocksJSONStr.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }

            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the data, there's no point in attempting
                // to parse it.
                return null;

                //Close the stream.
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }
            try {
                //
                return getStocksDataFromJson(stocksJSONStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }
            // This will only happen if there was an error getting or parsing the forecast.
            return null;
        }

        private List<Stock> getStocksDataFromJson(String stocksJSONStr)
                throws JSONException {

            // These are the names of the JSON objects that need to be extracted.
            String currentPrice;
            String dailyHigh;
            String dailyLow;
            String dailyOpen;
            int stockID;
            String ipoPrice;
            String stockName;
            String seasonHigh;
            String seasonLow;
            String stockRecord;
            String pictureUrl;
            String stockTicker;

            //String opponent;
            //String result;

            NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();

            JSONObject forecastJson = new JSONObject(stocksJSONStr);
            JSONArray stocksArray = forecastJson.getJSONArray(STOCK_ARRAY);

            for (int i = 0; i < stocksArray.length(); i++) {

                // Get the JSON object representing the stock
                JSONObject stockObj = stocksArray.getJSONObject(i);
                currentPrice = currencyFormat.format(stockObj.getDouble("current_price"));
                dailyHigh = currencyFormat.format(stockObj.getDouble("daily_high"));
                dailyLow = currencyFormat.format(stockObj.getDouble("daily_low"));
                dailyOpen = currencyFormat.format(stockObj.getDouble("daily_open"));
                stockID = stockObj.getInt("id");
                ipoPrice = currencyFormat.format(stockObj.getDouble("ipo_price"));
                stockName = stockObj.getString("stock_name");
                pictureUrl = stockObj.getString("picture_url");
                stockRecord = stockObj.getString("record");
                seasonHigh = currencyFormat.format(stockObj.getDouble("season_high"));
                seasonLow = currencyFormat.format(stockObj.getDouble("season_low"));
                stockTicker = stockObj.getString("ticker");

                Stock stock = new Stock(currentPrice, dailyHigh, dailyLow, dailyOpen, stockID,
                        ipoPrice, stockName,pictureUrl, stockRecord, seasonHigh, seasonLow,
                        stockTicker);
                if (db.getStock(stockID) != null) {
                    db.updateStock(stock);
                } else {
                    db.addStock(stock);
                }


                /*JSONArray scheduleArray = stockObj.getJSONArray(STOCK_SCHEDULE);
                for (int n = 0; n < scheduleArray.length(); i++) {
                    JSONObject scheduleObj = scheduleArray.getJSONObject(i);
                    opponent = scheduleObj.getString("opponent");
                    result = scheduleObj.getString("result");

                    Schedule schedule = new Schedule(stockID, opponent, result);
                    if (db.getSchedule(stockID) != null) {
                        db.updateSchedule(schedule);
                    } else {
                        db.addSchedule(schedule);
                    }

                }*/
            }

            return db.getStockList();
        }

        @Override
        protected void onPostExecute(List<Stock> stocksList) {
            super.onPostExecute(stocksList);

        }
    }


}
