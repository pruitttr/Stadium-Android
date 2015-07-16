package com.stadiumse.stadiumstockexchange;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toolbar;

import com.squareup.picasso.Picasso;

public class StockDetailActivity extends Activity {

    private DatabaseHandler db;
    private Toolbar toolbar;
    private Stock stock;
    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_detail);

        toolbar = (Toolbar) findViewById(R.id.toolbar_stock_detail);
        db = new DatabaseHandler(this);
        stock = db.getStock(Integer.parseInt(getIntent().getStringExtra("STOCK_ID")));

        setActionBar(toolbar);
        if (getActionBar() != null) {
            actionBar = getActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

        if (getIntent().hasExtra("STOCK_ID")) {
            ImageView imageview_stock_picture = (ImageView) findViewById(R.id.imageview_team_picture);
            Picasso.with(this).load("https://res.cloudinary.com/bus-productions/" +
                    "image/upload/w_400,h_400,c_limit,c_fill/" + stock.getPictureUrl())
                    .into(imageview_stock_picture);
            getActionBar().setTitle(getIntent().getStringExtra("STOCK_TICKER_NAME"));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_stock_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finishAfterTransition();
                return true;
            //noinspection SimplifiableIfStatement
            case R.id.action_settings:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}