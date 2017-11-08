package com.example.seniorproject.smartshopping.view.recyclerviewadapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.example.seniorproject.smartshopping.model.dao.group.GroupWatingListener;
import com.example.seniorproject.smartshopping.view.customviewgroup.CustomViewGroupGroupWating;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;


/**
 * Created by boyburin on 10/31/2017 AD.
 */

public class GroupWatingAdapter extends RecyclerView.Adapter<GroupWatingAdapter.ViewHolder>  {
    private ArrayList<GroupWatingListener> groups;
    private StorageReference storageReference;

    public GroupWatingAdapter(){
        groups = new ArrayList<>();
    }

    public void setGroup(ArrayList<GroupWatingListener> groups){
        this.groups = groups;
    }
    @Override
    public GroupWatingAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        /*View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_view_group_group_wating, parent, false);*/

        CustomViewGroupGroupWating v = new CustomViewGroupGroupWating(parent.getContext());
        v.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        GroupWatingAdapter.ViewHolder vh = new GroupWatingAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(GroupWatingAdapter.ViewHolder holder, int position) {
        GroupWatingListener group = groups.get(position);
        //holder.tvName.setText(group.getGroupWating().getName());
        ((CustomViewGroupGroupWating)holder.itemView).setNameText(group.getGroupWating().getName());
        ((CustomViewGroupGroupWating)holder.itemView).setImageUrl(group.getGroupWating().getPhotoUrl());
        ((CustomViewGroupGroupWating)holder.itemView).setImgBtnDelete(group.getDeleteListener());
    }

    @Override
    public int getItemCount() {
        return groups.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        /*public TextView tvName;
        public ImageView imgGroup;
        public ImageButton imgBtnDelete;*/

        public ViewHolder(View itemView) {
            super(itemView);

            /*tvName = (TextView) itemView.findViewById(R.id.tvName);
            imgGroup = (ImageView) itemView.findViewById(R.id.imgGroup);
            imgBtnDelete = (ImageButton) itemView.findViewById(R.id.imgBtnDelete);*/

        }
    }
}
