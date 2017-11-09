package com.example.seniorproject.smartshopping.view.viewholder.group;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.seniorproject.smartshopping.R;

/**
 * Created by boyburin on 11/9/2017 AD.
 */

public class PendingGroupMemberViewHolder extends RecyclerView.ViewHolder {

    public ImageView imgViewMember;
    public TextView tvMemberName;
    public Button btnAccept;
    public Button btnDecline;

    public PendingGroupMemberViewHolder(View itemView) {
        super(itemView);

        imgViewMember = (ImageView) itemView.findViewById(R.id.imgViewMember);
        tvMemberName = (TextView) itemView.findViewById(R.id.tvMemberName);
        btnAccept = (Button) itemView.findViewById(R.id.btnAccept);
        btnDecline = (Button) itemView.findViewById(R.id.btnDecline);
    }
}
