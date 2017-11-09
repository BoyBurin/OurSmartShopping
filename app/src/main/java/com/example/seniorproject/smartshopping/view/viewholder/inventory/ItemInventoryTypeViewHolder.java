package com.example.seniorproject.smartshopping.view.viewholder.inventory;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.seniorproject.smartshopping.R;

/**
 * Created by boyburin on 11/8/2017 AD.
 */

public class ItemInventoryTypeViewHolder extends RecyclerView.ViewHolder{
    public TextView tvTypeName;

    public ItemInventoryTypeViewHolder(View itemView) {
        super(itemView);

        tvTypeName = (TextView) itemView.findViewById(R.id.tvTypeName);
    }
}
