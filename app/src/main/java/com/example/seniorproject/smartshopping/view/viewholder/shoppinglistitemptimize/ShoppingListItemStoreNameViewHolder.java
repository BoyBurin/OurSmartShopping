package com.example.seniorproject.smartshopping.view.viewholder.shoppinglistitemptimize;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.seniorproject.smartshopping.R;

/**
 * Created by boyburin on 11/11/2017 AD.
 */

public class ShoppingListItemStoreNameViewHolder extends RecyclerView.ViewHolder {

    public TextView tvStoreName;

    public ShoppingListItemStoreNameViewHolder(View itemView) {
        super(itemView);

        tvStoreName = (TextView) itemView.findViewById(R.id.tvStoreName);
    }
}
