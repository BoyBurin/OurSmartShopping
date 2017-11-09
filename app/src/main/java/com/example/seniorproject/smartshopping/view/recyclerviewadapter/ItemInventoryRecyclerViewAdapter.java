package com.example.seniorproject.smartshopping.view.recyclerviewadapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.seniorproject.smartshopping.R;
import com.example.seniorproject.smartshopping.model.dao.iteminventory.ItemInventory;
import com.example.seniorproject.smartshopping.model.dao.iteminventory.ItemInventoryMap;
import com.example.seniorproject.smartshopping.model.daorecyclerview.iteminventory.BaseItemInventory;
import com.example.seniorproject.smartshopping.model.daorecyclerview.iteminventory.ItemInventoryRecyclerView;
import com.example.seniorproject.smartshopping.model.daorecyclerview.iteminventory.ItemInventoryTypeNameRecyclerView;
import com.example.seniorproject.smartshopping.view.customviewgroup.ItemView;
import com.example.seniorproject.smartshopping.view.viewholder.inventory.ItemInventoryTypeViewHolder;
import com.example.seniorproject.smartshopping.view.viewholder.inventory.ItemInventoryViewHolder;

import java.util.ArrayList;

/**
 * Created by boyburin on 11/8/2017 AD.
 */

public class ItemInventoryRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<BaseItemInventory> itemInventories;
    private Context context;

    private OnItemClickListener listener;

    /***********************************************************************************************
     *************************************Interface ********************************************
     ***********************************************************************************************/

    public interface OnItemClickListener {
        void onItemClick(ItemInventoryMap itemInventory);
    }


    /***********************************************************************************************
     ************************************* Method ********************************************
     ***********************************************************************************************/

    public ItemInventoryRecyclerViewAdapter(Context context){
        this.context = context;
        itemInventories = new ArrayList<>();
    }

    public void setItemInventories(ArrayList<BaseItemInventory> itemInventories){
        this.itemInventories = itemInventories;
    }

    public void setItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        RecyclerView.ViewHolder viewHolder;
        if(BaseItemInventory.ITEM_INVENTORY == viewType){
            view = new ItemView(context);
            view.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

            viewHolder = new ItemInventoryViewHolder(view);
        }
        else{
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.custom_view_group_item_inventory_type, parent, false);

            viewHolder = new ItemInventoryTypeViewHolder(view);
        }

        return viewHolder;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if(holder instanceof ItemInventoryViewHolder){
            final ItemInventoryRecyclerView itemInventoryRecyclerView = (ItemInventoryRecyclerView)itemInventories.get(position);
            ItemInventory itemInventory = itemInventoryRecyclerView.getItemInventory().getItemInventory();

            ItemInventoryViewHolder itemInventoryViewHolder = (ItemInventoryViewHolder) holder;

            ((ItemView)(itemInventoryViewHolder).itemView).setNameText(itemInventory.getName());
            ((ItemView)(itemInventoryViewHolder).itemView).setImageUrl(itemInventory.getPhotoUrl());
            ((ItemView)(itemInventoryViewHolder).itemView).setRemainder(itemInventory.getSoft(), itemInventory.getHard(), itemInventory.getAmount());
            ((ItemView)(itemInventoryViewHolder).itemView).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(itemInventoryRecyclerView.getItemInventory());
                }
            });
        }

        else{
            ItemInventoryTypeNameRecyclerView itemInventoryTypeNameRecyclerView =
                    (ItemInventoryTypeNameRecyclerView)itemInventories.get(position);
            String typeName = itemInventoryTypeNameRecyclerView.getTypeName();

            ItemInventoryTypeViewHolder itemInventoryTypeViewHolder = (ItemInventoryTypeViewHolder) holder;
            itemInventoryTypeViewHolder.tvTypeName.setText(typeName);

        }
    }

    @Override
    public int getItemCount() {
        return itemInventories.size();
    }

    @Override
    public int getItemViewType(int position) {
        return itemInventories.get(position).getType();
    }
}
