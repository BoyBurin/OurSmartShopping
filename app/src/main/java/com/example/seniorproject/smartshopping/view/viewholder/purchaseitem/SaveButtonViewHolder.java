package com.example.seniorproject.smartshopping.view.viewholder.purchaseitem;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.example.seniorproject.smartshopping.R;

/**
 * Created by boyburin on 11/10/2017 AD.
 */

public class SaveButtonViewHolder extends RecyclerView.ViewHolder {

    public Button btnSave;

    public SaveButtonViewHolder(View itemView) {
        super(itemView);

        btnSave = (Button) itemView.findViewById(R.id.btnSave);
    }
}
