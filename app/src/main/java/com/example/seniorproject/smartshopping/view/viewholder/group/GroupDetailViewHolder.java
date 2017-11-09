package com.example.seniorproject.smartshopping.view.viewholder.group;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.seniorproject.smartshopping.R;

/**
 * Created by boyburin on 11/9/2017 AD.
 */

public class GroupDetailViewHolder extends RecyclerView.ViewHolder {

    public ImageView imgViewGroup;
    public TextView tvGroupName;
    public TextView tvPendingAmount;
    public TextView tvQuote;

    public GroupDetailViewHolder(View itemView) {
        super(itemView);

        imgViewGroup = (ImageView) itemView.findViewById(R.id.imgViewGroup);
        tvGroupName = (TextView) itemView.findViewById(R.id.tvGroupName);
        tvPendingAmount = (TextView) itemView.findViewById(R.id.tvPendingAmount);
        tvQuote = (TextView) itemView.findViewById(R.id.tvQuote);
    }
}
