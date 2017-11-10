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
import com.example.seniorproject.smartshopping.model.dao.iteminventory.ItemInventory;
import com.example.seniorproject.smartshopping.model.dao.itemocr.ItemOCR;
import com.example.seniorproject.smartshopping.model.dao.itemocr.PurchaseItemWithAction;
import com.example.seniorproject.smartshopping.model.dao.user.UserInGroup;
import com.example.seniorproject.smartshopping.model.daorecyclerview.group.BaseGroup;
import com.example.seniorproject.smartshopping.model.daorecyclerview.group.GroupDetailRecyclerView;
import com.example.seniorproject.smartshopping.model.daorecyclerview.group.GroupMemberRecyclerView;
import com.example.seniorproject.smartshopping.model.daorecyclerview.group.PendingGroupMemberRecyclerView;
import com.example.seniorproject.smartshopping.model.daorecyclerview.group.TittleNameRecyclerView;
import com.example.seniorproject.smartshopping.model.daorecyclerview.purchaseitem.AddButtonRecyclerView;
import com.example.seniorproject.smartshopping.model.daorecyclerview.purchaseitem.BasePurchaseItem;
import com.example.seniorproject.smartshopping.model.daorecyclerview.purchaseitem.PurchaseItemRecyclerView;
import com.example.seniorproject.smartshopping.model.daorecyclerview.purchaseitem.SaveButtonRecyclerView;
import com.example.seniorproject.smartshopping.model.daorecyclerview.purchaseitem.StoreNameRecyclerView;
import com.example.seniorproject.smartshopping.model.daorecyclerview.purchaseitem.TotalPriceRecyclerView;
import com.example.seniorproject.smartshopping.view.customviewgroup.CustomViewGroupPurchaseItem;
import com.example.seniorproject.smartshopping.view.customviewgroup.CustomViewGroupMember;
import com.example.seniorproject.smartshopping.view.transformation.CircleTransform;
import com.example.seniorproject.smartshopping.view.viewholder.group.GroupDetailViewHolder;
import com.example.seniorproject.smartshopping.view.viewholder.group.GroupMemberViewHolder;
import com.example.seniorproject.smartshopping.view.viewholder.group.PendingGroupMemberViewHolder;
import com.example.seniorproject.smartshopping.view.viewholder.group.TittleNameViewHolder;
import com.example.seniorproject.smartshopping.view.viewholder.purchaseitem.AddButtonViewHolder;
import com.example.seniorproject.smartshopping.view.viewholder.purchaseitem.PurchaseItemViewHolder;
import com.example.seniorproject.smartshopping.view.viewholder.purchaseitem.SaveButtonViewHolder;
import com.example.seniorproject.smartshopping.view.viewholder.purchaseitem.StoreNameViewHolder;
import com.example.seniorproject.smartshopping.view.viewholder.purchaseitem.TotalPriceViewHolder;

import java.util.ArrayList;

/**
 * Created by boyburin on 11/9/2017 AD.
 */

public class PurchaseItemRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<BasePurchaseItem> purchaseItems;
    private Context context;

    /***********************************************************************************************
     ************************************* Method ********************************************
     ***********************************************************************************************/

    public PurchaseItemRecyclerViewAdapter(Context context){
        this.context = context;
        purchaseItems = new ArrayList<>();
    }

    public void setPurchaseItems(ArrayList<BasePurchaseItem> purchaseItems){
        this.purchaseItems = purchaseItems;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        RecyclerView.ViewHolder viewHolder;

        if(viewType == BasePurchaseItem.STORE_NAME){
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.custom_view_group_purchase_item_store_name, parent, false);
            viewHolder = new StoreNameViewHolder(view);
        }
        else if(viewType == BasePurchaseItem.TOTAL_PRICE){
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.custom_view_group_purchase_item_total_price, parent, false);
            viewHolder = new TotalPriceViewHolder(view);
        }
        else if(viewType == BasePurchaseItem.SAVE_BUTTON){
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.custom_view_group_purchase_item_save_button, parent, false);
            viewHolder = new SaveButtonViewHolder(view);
        }
        else if(viewType == BasePurchaseItem.PURCHASE_ITEM){
            view = new CustomViewGroupPurchaseItem(context);
            view.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            viewHolder = new PurchaseItemViewHolder(view);
        }
        else{
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.custom_view_group_purchase_item_add_button, parent, false);
            viewHolder = new AddButtonViewHolder(view);
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        BasePurchaseItem item = purchaseItems.get(position);

        if(holder instanceof StoreNameViewHolder){
            setStoreNameViewHolder(holder, item);
        }
        else if(holder instanceof TotalPriceViewHolder){
            setTotalPriceViewHolder(holder, item);
        }
        else if(holder instanceof SaveButtonViewHolder){
            setSaveButtonViewHolder(holder, item);
        }
        else if(holder instanceof PurchaseItemViewHolder){
            setPurchaseItemViewHolder(holder, item);
        }
        else if(holder instanceof AddButtonViewHolder){
            setAddButtonViewHolder(holder, item);
        }

    }

    @Override
    public int getItemCount() {
        return purchaseItems.size();
    }

    @Override
    public int getItemViewType(int position) {
        return purchaseItems.get(position).getType();
    }

    private void setStoreNameViewHolder(RecyclerView.ViewHolder holder, BasePurchaseItem item){
        StoreNameViewHolder myHolder = (StoreNameViewHolder) holder;
        StoreNameRecyclerView myItem = (StoreNameRecyclerView) item;
        String storeName = myItem.getStoreName();

        myHolder.tvStoreName.setText(storeName);
    }

    private void setTotalPriceViewHolder(RecyclerView.ViewHolder holder, BasePurchaseItem item){
        TotalPriceViewHolder myHolder = (TotalPriceViewHolder) holder;
        TotalPriceRecyclerView myItem = (TotalPriceRecyclerView) item;

        myHolder.tvTotalPrice.setText(myItem.getTotalPrice());
    }

    private void setSaveButtonViewHolder(RecyclerView.ViewHolder holder, BasePurchaseItem item){
        SaveButtonViewHolder myHolder = (SaveButtonViewHolder) holder;
        SaveButtonRecyclerView myItem = (SaveButtonRecyclerView) item;

        myHolder.btnSave.setOnClickListener(myItem.getSaveListener());
    }

    private void setPurchaseItemViewHolder(RecyclerView.ViewHolder holder, BasePurchaseItem item){
        PurchaseItemViewHolder myHolder = (PurchaseItemViewHolder) holder;
        PurchaseItemRecyclerView myItem  = (PurchaseItemRecyclerView) item;
        PurchaseItemWithAction purchaseItemWithAction = myItem.getPurchaseItemWithAction();
        ItemInventory purchaseItem = purchaseItemWithAction.getItemOCR().getItemInventoryMap().getItemInventory();

        ((CustomViewGroupPurchaseItem)myHolder.itemView).setName(purchaseItem.getName());
        ((CustomViewGroupPurchaseItem)myHolder.itemView).setAmount(purchaseItemWithAction.getItemOCR().getAmount());
        ((CustomViewGroupPurchaseItem)myHolder.itemView).setPrice(purchaseItemWithAction.getItemOCR().getPrice());
        ((CustomViewGroupPurchaseItem)myHolder.itemView).setImgBtnDelete(purchaseItemWithAction.getDelete());
        ((CustomViewGroupPurchaseItem)myHolder.itemView).setImage(purchaseItem.getPhotoUrl());
    }

    private void setAddButtonViewHolder(RecyclerView.ViewHolder holder, BasePurchaseItem item){
        AddButtonViewHolder myHolder = (AddButtonViewHolder) holder;
        AddButtonRecyclerView myItem = (AddButtonRecyclerView) item;

        myHolder.btnAdd.setOnClickListener(myItem.getAddListener());
    }
}
