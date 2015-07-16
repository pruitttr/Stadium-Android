package com.stadiumse.stadiumstockexchange;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class StockListRecyclerAdapter extends RecyclerView.Adapter<StockListRecyclerAdapter.ViewHolder> {

    private List<Stock> stocksList;
    private int list_item_stock;

    public StockListRecyclerAdapter(List<Stock> stocksList, int list_item_stock) {
        this.stocksList = stocksList;
        this.list_item_stock = list_item_stock;
    }

    @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(list_item_stock, parent, false);
        return new ViewHolder(v);
    }

    @Override public void onBindViewHolder(ViewHolder holder, int position) {
        Stock stock = stocksList.get(position);
        holder.textview_stock_ticker.setText(stock._stockTicker);
        holder.textview_stock_record.setText(stock._stockRecord);
        holder.textview_stock_name.setText(stock._stockName);
        holder.textview_stock_price.setText(String.valueOf(stock._currentPrice));
        //holder.textview_stock_dailychange.setText(String.valueOf(stock._currentPrice - stock._dailyOpen));

        holder.itemView.setTag(stock);
    }

    @Override public int getItemCount() {
        return stocksList.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textview_stock_ticker;
        TextView textview_stock_record;
        TextView textview_stock_name;
        TextView textview_stock_price;
        TextView textview_stock_dailychange;

        public ViewHolder(View itemView) {
            super(itemView);
            textview_stock_ticker = (TextView) itemView.findViewById(R.id.textview_stock_ticker);
            textview_stock_record = (TextView) itemView.findViewById(R.id.textview_stock_record);
            textview_stock_name = (TextView) itemView.findViewById(R.id.textview_stock_name);
            textview_stock_price = (TextView) itemView.findViewById(R.id.textview_stock_price);
            textview_stock_dailychange = (TextView) itemView.findViewById(R.id.textview_stock_dailychange);
        }

        @Override
        public void onClick(View view) {
            
        }
    }
}