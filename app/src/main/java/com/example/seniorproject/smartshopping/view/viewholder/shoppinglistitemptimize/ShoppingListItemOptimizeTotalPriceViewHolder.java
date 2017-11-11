package com.example.seniorproject.smartshopping.view.viewholder.shoppinglistitemptimize;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.seniorproject.smartshopping.R;

/**
 * Created by boyburin on 11/11/2017 AD.
 */

public class ShoppingListItemOptimizeTotalPriceViewHolder extends RecyclerView.ViewHolder {

    public TextView tvTotalPrice;

    public ShoppingListItemOptimizeTotalPriceViewHolder(View itemView) {
        super(itemView);

        tvTotalPrice = (TextView) itemView.findViewById(R.id.tvTotalPrice);
    }
}
