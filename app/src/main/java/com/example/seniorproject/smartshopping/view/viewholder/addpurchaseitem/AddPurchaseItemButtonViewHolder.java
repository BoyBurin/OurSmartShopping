package com.example.seniorproject.smartshopping.view.viewholder.addpurchaseitem;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.example.seniorproject.smartshopping.R;

/**
 * Created by boyburin on 11/10/2017 AD.
 */

public class AddPurchaseItemButtonViewHolder extends RecyclerView.ViewHolder {

    public Button btnCancel;
    public Button btnAdd;

    public AddPurchaseItemButtonViewHolder(View itemView) {
        super(itemView);

        btnCancel = (Button) itemView.findViewById(R.id.btnCancel);
        btnAdd = (Button) itemView.findViewById(R.id.btnAdd);
    }
}
