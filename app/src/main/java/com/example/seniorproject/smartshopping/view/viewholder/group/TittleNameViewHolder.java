package com.example.seniorproject.smartshopping.view.viewholder.group;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.seniorproject.smartshopping.R;

/**
 * Created by boyburin on 11/9/2017 AD.
 */

public class TittleNameViewHolder extends RecyclerView.ViewHolder {

    public TextView tvTittleName;

    public TittleNameViewHolder(View itemView) {
        super(itemView);

        tvTittleName = (TextView) itemView.findViewById(R.id.tvTittleName);
    }
}
