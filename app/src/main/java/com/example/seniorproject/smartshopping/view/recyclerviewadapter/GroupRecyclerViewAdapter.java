package com.example.seniorproject.smartshopping.view.recyclerviewadapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.seniorproject.smartshopping.R;
import com.example.seniorproject.smartshopping.model.dao.group.Group;
import com.example.seniorproject.smartshopping.model.dao.group.PendingGroupMember;
import com.example.seniorproject.smartshopping.model.dao.user.UserInGroup;
import com.example.seniorproject.smartshopping.model.daorecyclerview.group.BaseGroup;
import com.example.seniorproject.smartshopping.model.daorecyclerview.group.GroupDetailRecyclerView;
import com.example.seniorproject.smartshopping.model.daorecyclerview.group.GroupMemberRecyclerView;
import com.example.seniorproject.smartshopping.model.daorecyclerview.group.PendingGroupMemberRecyclerView;
import com.example.seniorproject.smartshopping.model.daorecyclerview.group.TittleNameRecyclerView;
import com.example.seniorproject.smartshopping.view.customviewgroup.CustomViewGroupMember;
import com.example.seniorproject.smartshopping.view.viewholder.group.GroupDetailViewHolder;
import com.example.seniorproject.smartshopping.view.viewholder.group.GroupMemberViewHolder;
import com.example.seniorproject.smartshopping.view.viewholder.group.PendingGroupMemberViewHolder;
import com.example.seniorproject.smartshopping.view.viewholder.group.TittleNameViewHolder;

import java.util.ArrayList;

/**
 * Created by boyburin on 11/9/2017 AD.
 */

public class GroupRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<BaseGroup> itemGroups;
    private Context context;

    /***********************************************************************************************
     ************************************* Method ********************************************
     ***********************************************************************************************/

    public GroupRecyclerViewAdapter(Context context){
        this.context = context;
        itemGroups = new ArrayList<>();
    }

    public void setItemGroups(ArrayList<BaseGroup> itemGroups){
        this.itemGroups = itemGroups;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        RecyclerView.ViewHolder viewHolder;

        if(viewType == BaseGroup.GROUP_DETAIL){
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.custom_view_group_group_detail, parent, false);
            viewHolder = new GroupDetailViewHolder(view);
        }
        else if(viewType == BaseGroup.TITTLE_NAME){
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.custom_view_group_group_tiitle_name, parent, false);
            viewHolder = new TittleNameViewHolder(view);
        }
        else if(viewType == BaseGroup.PENDING_GROUP_MEMBER){
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.custom_view_group_group_pending_member, parent, false);
            viewHolder = new PendingGroupMemberViewHolder(view);
        }
        else{
            view = new CustomViewGroupMember(context);
            view.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            viewHolder = new GroupMemberViewHolder(view);
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        BaseGroup item = itemGroups.get(position);

        if(holder instanceof GroupDetailViewHolder){
            setGroupDetailViewHolder(holder, item);
        }
        else if(holder instanceof TittleNameViewHolder){
            setTittleNameViewHolder(holder, item);
        }
        else if(holder instanceof PendingGroupMemberViewHolder){
            setPendingGroupMemberViewHolder(holder, item);
        }
        else if(holder instanceof GroupMemberViewHolder){
            setGroupMemberViewHolder(holder, item);
        }

    }

    @Override
    public int getItemCount() {
        return itemGroups.size();
    }

    @Override
    public int getItemViewType(int position) {
        return itemGroups.get(position).getType();
    }

    private void setGroupDetailViewHolder(RecyclerView.ViewHolder holder, BaseGroup item){
        GroupDetailViewHolder myHolder = (GroupDetailViewHolder) holder;
        GroupDetailRecyclerView myItem = (GroupDetailRecyclerView) item;
        Group group = myItem.getGroup().getGroup();

        setImageUrl(group.getPhotoUrl(), myHolder.imgViewGroup);
        myHolder.tvGroupName.setText(group.getName());
        myHolder.tvQuote.setText(group.getQuote());
    }

    private void setTittleNameViewHolder(RecyclerView.ViewHolder holder, BaseGroup item){
        TittleNameViewHolder myHolder = (TittleNameViewHolder) holder;
        TittleNameRecyclerView myItem = (TittleNameRecyclerView) item;

        myHolder.tvTittleName.setText(myItem.getTittleName());
    }

    private void setPendingGroupMemberViewHolder(RecyclerView.ViewHolder holder, BaseGroup item){
        PendingGroupMemberViewHolder myHolder = (PendingGroupMemberViewHolder) holder;
        PendingGroupMemberRecyclerView myItem = (PendingGroupMemberRecyclerView) item;
        PendingGroupMember pendingGroupMember = myItem.getPendingGroupMember().getPendingGroupMember();

        myHolder.btnAccept.setOnClickListener(myItem.getPendingGroupMember().getAccept());
        myHolder.btnDecline.setOnClickListener(myItem.getPendingGroupMember().getDecline());
        myHolder.tvMemberName.setText(pendingGroupMember.getName());
        setImageUrl(pendingGroupMember.getPhotoUrl(), myHolder.imgViewMember);
    }

    private void setGroupMemberViewHolder(RecyclerView.ViewHolder holder, BaseGroup item){
        GroupMemberViewHolder myHolder = (GroupMemberViewHolder) holder;
        GroupMemberRecyclerView myItem  = (GroupMemberRecyclerView) item;
        UserInGroup member = myItem.getMember().getUser();

        ((CustomViewGroupMember)myHolder.itemView).setNameText(member.getName());
        ((CustomViewGroupMember)myHolder.itemView).setImageUrl(member.getPhotoUrl());
    }

    private void setImageUrl(String url, ImageView imageView) {
        Glide.with(context)
                .load(url)
                .placeholder(R.drawable.loading) //default pic
                .centerCrop()
                //.error(Drawable pic)  picture has problem
                //.transform(new CircleTransform(context)) //Cool !!!
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);

    }
}
