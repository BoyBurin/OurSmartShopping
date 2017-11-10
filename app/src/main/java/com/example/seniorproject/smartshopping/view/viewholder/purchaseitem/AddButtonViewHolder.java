package com.example.seniorproject.smartshopping.view.viewholder.purchaseitem;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;

import com.example.seniorproject.smartshopping.R;

/**
 * Created by boyburin on 11/10/2017 AD.
 */

public class AddButtonViewHolder extends RecyclerView.ViewHolder {

    public ImageButton btnAdd;

    public AddButtonViewHolder(View itemView) {
        super(itemView);

        btnAdd = (ImageButton) itemView.findViewById(R.id.btnAdd);
    }
}
