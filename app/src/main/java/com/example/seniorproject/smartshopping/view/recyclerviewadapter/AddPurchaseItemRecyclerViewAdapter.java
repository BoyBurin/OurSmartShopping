package com.example.seniorproject.smartshopping.view.recyclerviewadapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.seniorproject.smartshopping.R;
import com.example.seniorproject.smartshopping.model.dao.iteminventory.ItemInventory;
import com.example.seniorproject.smartshopping.model.dao.iteminventory.ItemInventoryMap;
import com.example.seniorproject.smartshopping.model.daorecyclerview.addpurchaseitem.AddPurchaseItemButtonRecyclerView;
import com.example.seniorproject.smartshopping.model.daorecyclerview.addpurchaseitem.AddPurchaseItemRecyclerView;
import com.example.seniorproject.smartshopping.model.daorecyclerview.addpurchaseitem.BaseAddPurchaseItem;
import com.example.seniorproject.smartshopping.model.daorecyclerview.iteminventory.BaseItemInventory;
import com.example.seniorproject.smartshopping.model.daorecyclerview.iteminventory.ItemInventoryRecyclerView;
import com.example.seniorproject.smartshopping.model.daorecyclerview.iteminventory.ItemInventoryTypeNameRecyclerView;
import com.example.seniorproject.smartshopping.view.customviewgroup.ItemView;
import com.example.seniorproject.smartshopping.view.viewholder.addpurchaseitem.AddPurchaseItemButtonViewHolder;
import com.example.seniorproject.smartshopping.view.viewholder.inventory.ItemInventoryTypeViewHolder;
import com.example.seniorproject.smartshopping.view.viewholder.inventory.ItemInventoryViewHolder;

import java.util.ArrayList;

/**
 * Created by boyburin on 11/8/2017 AD.
 */

public class AddPurchaseItemRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<BaseAddPurchaseItem> addPurchaseItems;
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

    public AddPurchaseItemRecyclerViewAdapter(Context context){
        this.context = context;
        addPurchaseItems = new ArrayList<>();
    }

    public void setAddPurchaseItems(ArrayList<BaseAddPurchaseItem> addPurchaseItems){
        this.addPurchaseItems = addPurchaseItems;
    }

    public void setItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        RecyclerView.ViewHolder viewHolder;
        if(BaseAddPurchaseItem.ADD_PURCHASE_ITEM == viewType){
            view = new ItemView(context);
            view.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

            viewHolder = new ItemInventoryViewHolder(view);
        }
        else{
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.custom_view_group_add_purchase_item_button, parent, false);

            viewHolder = new AddPurchaseItemButtonViewHolder(view);
        }

        return viewHolder;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        BaseAddPurchaseItem baseAddPurchaseItem = addPurchaseItems.get(position);
        if(holder instanceof ItemInventoryViewHolder){
            setItemInventoryViewHolder(holder,baseAddPurchaseItem);
        }

        else{
            setAddPurchaseItemButton(holder, baseAddPurchaseItem);

        }
    }

    @Override
    public int getItemCount() {
        return addPurchaseItems.size();
    }

    @Override
    public int getItemViewType(int position) {
        return addPurchaseItems.get(position).getType();
    }

    private void setItemInventoryViewHolder(RecyclerView.ViewHolder holder, BaseAddPurchaseItem item){
        final AddPurchaseItemRecyclerView myItem = (AddPurchaseItemRecyclerView)item;
        ItemInventory itemInventory = myItem.getItemInventoryMap().getItemInventory();

        final ItemInventoryViewHolder myHolder = (ItemInventoryViewHolder) holder;

        ((ItemView)(myHolder).itemView).setNameText(itemInventory.getName());
        ((ItemView)(myHolder).itemView).setImageUrl(itemInventory.getPhotoUrl());
        ((ItemView)(myHolder).itemView).setRemainder(itemInventory.getSoft(), itemInventory.getHard(), itemInventory.getAmount());
        ((ItemView)(myHolder).itemView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(myItem.getItemInventoryMap());
            }
        });
    }

    private void setAddPurchaseItemButton(RecyclerView.ViewHolder holder, BaseAddPurchaseItem item){
        AddPurchaseItemButtonRecyclerView myItem = (AddPurchaseItemButtonRecyclerView) item;

        AddPurchaseItemButtonViewHolder myHolder = (AddPurchaseItemButtonViewHolder) holder;

        myHolder.btnCancel.setOnClickListener(myItem.getBtnCancel());
        myHolder.btnAdd.setOnClickListener(myItem.getBtnAdd());
    }
}
