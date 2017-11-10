package com.example.seniorproject.smartshopping.view.viewholder.purchaseitem;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.seniorproject.smartshopping.R;

/**
 * Created by boyburin on 11/10/2017 AD.
 */

public class TotalPriceViewHolder extends RecyclerView.ViewHolder {

    public TextView tvTotalPrice;

    public TotalPriceViewHolder(View itemView) {
        super(itemView);

        tvTotalPrice = (TextView) itemView.findViewById(R.id.tvTotalPrice);
    }
}
