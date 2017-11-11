package com.example.seniorproject.smartshopping.view.viewholder.shoppinglistitemptimize;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.seniorproject.smartshopping.R;

/**
 * Created by boyburin on 11/11/2017 AD.
 */

public class ShoppingListItemOptimizeSavePriceViewHolder extends RecyclerView.ViewHolder {

    public TextView tvSavePrice;

    public ShoppingListItemOptimizeSavePriceViewHolder(View itemView) {
        super(itemView);

        tvSavePrice = (TextView) itemView.findViewById(R.id.tvSavePrice);
    }
}
