package com.stadiumse.stadiumstockexchange;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

public class AssetRecyclerAdapter extends RecyclerView.Adapter<AssetRecyclerAdapter.ViewHolder> {

    private int cardview_asset;
    private List<Asset> assetList;
    private Context context;
    private DatabaseHandler db;

    public AssetRecyclerAdapter(Context context, List<Asset> assetList, int cardview_asset) {
        this.assetList = assetList;
        this.cardview_asset = cardview_asset;
        this.context = context;
        db = new DatabaseHandler(context);
    }

    @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(cardview_asset, parent, false);
        return new ViewHolder(v);
    }

    @Override public void onBindViewHolder(ViewHolder holder, int position) {
        Asset asset = assetList.get(position);
        Stock stock = db.getStock(asset.getStockID());

        if (stock != null) {
            holder.textview_asset_shares_owned.setText(asset.getNumberShares());
            holder.textview_asset_price_change.setText(asset.getPriceChange());
            holder.textview_asset_total_value.setText(asset.getTotalValue());
            holder.textview_ticker_name.setText(stock.getStockTicker() +
                    " | " + stock.getStockName());
            holder.textview_asset_stock_id.setText(String.valueOf(stock.getStockID()));
            Picasso.with(context).load("https://res.cloudinary.com/bus-productions/" +
                    "image/upload/w_400,h_400,c_limit,c_fill/" + stock.getPictureUrl())
                    .into(holder.imageview_team_picture);
            holder.itemView.setTag(asset);
        }
    }

    @Override public int getItemCount() {
        return assetList.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textview_asset_shares_owned;
        TextView textview_asset_price_change;
        TextView textview_asset_total_value;
        TextView textview_ticker_name;
        TextView textview_asset_stock_id;
        ImageView imageview_team_picture;

        private Context context;
        private Activity activity;

        public ViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();

            textview_asset_shares_owned = (TextView) itemView.findViewById(R.id.textview_asset_shares_owned);
            textview_asset_price_change = (TextView) itemView.findViewById(R.id.textview_asset_change);
            textview_asset_total_value = (TextView) itemView.findViewById(R.id.textview_asset_total_value);
            textview_ticker_name = (TextView) itemView.findViewById(R.id.textview_ticker_name);
            textview_asset_stock_id = (TextView) itemView.findViewById(R.id.textview_asset_stock_id);
            imageview_team_picture = (ImageView) itemView.findViewById(R.id.imageview_team_picture);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(context, StockDetailActivity.class);
            intent.putExtra("STOCK_ID", textview_asset_stock_id.getText());
            intent.putExtra("STOCK_TICKER_NAME", textview_ticker_name.getText());
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation((Activity) context,
                    Pair.create((View) imageview_team_picture, "teamPicture"),
                    Pair.create((View) textview_ticker_name, "stockName"));
            context.startActivity(intent, options.toBundle());
        }
    }
}