package com.stadiumse.stadiumstockexchange;

import android.app.Activity;
import android.app.Fragment;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
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

public class DashboardFragment extends Fragment {

    private Activity a;
    private DatabaseHandler db;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private AssetRecyclerAdapter assetRecyclerAdapter;

    public DashboardFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        //initialize activity variable as attached activity
        a = activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //initialize database handler
        db = new DatabaseHandler(a);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_dashboard, container, false);

        List<Asset> assetList = db.getAssetList();
        recyclerView = (RecyclerView) rootView.findViewById(R.id.container_assets);
        LinearLayoutManager llm = new LinearLayoutManager(a);
        recyclerView.setLayoutManager(llm);

        assetRecyclerAdapter = new AssetRecyclerAdapter(a, assetList, R.layout.cardview_asset);
        recyclerView.setAdapter(assetRecyclerAdapter);

        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeview_assets);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //TODO: get userID from SharedPreferences and fix location of server request within app
                new FetchDashboardTask().execute("1094");
            }
        });

        return rootView;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public interface OnFragmentInteractionListener {
    }

    private class FetchDashboardTask extends AsyncTask<String, Void, Void> {

        private final String LOG_TAG = FetchDashboardTask.class.getSimpleName();

        @Override
        protected Void doInBackground(String... params) {

            //If there's no userID, there's nothing to look up.  Verify size of params.
            if (params.length == 0) {
                return null;
            }

            //Must be declared here, so they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String dashboardJSONStr = null;

            try {
                //Constructs the URL for the call.
                final String BASE_URL = "http://stadium-demo.herokuapp.com/mbl_dashboard.json?";
                final String SECRET_PARAM = "secret";
                final String USERID_PARAM = "user[id]";

                Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                        .appendQueryParameter(SECRET_PARAM, "alak-_-sjdf!j234897alskf!$$")
                        .appendQueryParameter(USERID_PARAM, params[0])
                        .build();

                URL url = new URL(builtUri.toString());

                Log.v(LOG_TAG, "Built URI " + builtUri.toString());

                // Create the request to Stadium server, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuilder buffer = new StringBuilder();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a lot easier
                    buffer.append(line).append("\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }

                dashboardJSONStr = buffer.toString();

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
                getDashboardDataFromJson(dashboardJSONStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }
            // This will only happen if there was an error getting or parsing the forecast.
            return null;
        }

        private void getDashboardDataFromJson(String dashboardJSONStr)
                throws JSONException {

            db.deleteAssets();

            // These are the names of the JSON objects that need to be extracted.
            final String USER_OBJECT = "user";
            final String PORTFOLIO = "portfolio";
            final String STOCK = "stock";
            final String ASSETS = "assets";

            int userID;
            int stockID;

            String stockCount;
            String favFanbaseRank;
            String rivalFanbaseRank;
            String numberShares;
            String portfolioCash;
            String portfolioValue;
            String totalValue;
            String priceChange;
            String favTicker;
            String favStockName;
            String rivalTicker;
            String rivalStockName;

            NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();

            //TODO: get userID from SharedPreferences
            userID = 1094;

            JSONObject dashboardJson = new JSONObject(dashboardJSONStr);
            JSONObject userObject = dashboardJson.getJSONObject(USER_OBJECT);
            JSONObject portfolioObject = userObject.getJSONObject(PORTFOLIO);
            JSONObject dashboardObject = userObject.getJSONObject(STOCK);

            portfolioCash = currencyFormat.format(portfolioObject.getDouble("cachedCash"));
            portfolioValue = currencyFormat.format(portfolioObject.getDouble("currentValue"));
            stockCount = String.valueOf(dashboardObject.getInt("stockCount"));
            favTicker = dashboardObject.getString("favTicker");
            favStockName = dashboardObject.getString("favStockName");
            favFanbaseRank = String.valueOf(dashboardObject.getInt("favFanbaseRank"));
            rivalTicker = dashboardObject.getString("rivalTicker");
            rivalStockName = dashboardObject.getString("rivalStockName");
            rivalFanbaseRank = String.valueOf(dashboardObject.getInt("rivalFanbaseRank"));

            Dashboard dashboard = new Dashboard(userID, portfolioCash, portfolioValue,stockCount,
                    favTicker, favStockName, favFanbaseRank, rivalTicker, rivalStockName, rivalFanbaseRank);
            //TODO: get userID from SharedPreferences
            if (db.getDashboard(1094) != null) {
                db.updateDashboard(dashboard);
            } else {
                db.addDashboard(dashboard);
            }

            JSONArray assetArray = userObject.getJSONArray(ASSETS);
            for (int i = 0; i < assetArray.length(); i++) {
                JSONObject assetObject = assetArray.getJSONObject(i);

                priceChange = currencyFormat.format(assetObject.getDouble("currentPrice") -
                        assetObject.getDouble("executionPrice"));
                totalValue = currencyFormat.format((assetObject.getDouble("currentPrice") -
                        assetObject.getDouble("executionPrice")) * assetObject.getInt("numberShares"));
                numberShares = String.valueOf(assetObject.getInt("numberShares"));
                JSONObject stockObject = assetObject.getJSONObject("stock");
                stockID = stockObject.getInt("id");

                Asset asset = new Asset(stockID, numberShares, priceChange, totalValue);
                db.addAsset(asset);
            }
        }

        @Override
        protected void onPostExecute(Void voids) {
            super.onPostExecute(voids);

            assetRecyclerAdapter.notifyDataSetChanged();

            if (swipeRefreshLayout.isRefreshing())
                swipeRefreshLayout.setRefreshing(false);


        }
    }
}
