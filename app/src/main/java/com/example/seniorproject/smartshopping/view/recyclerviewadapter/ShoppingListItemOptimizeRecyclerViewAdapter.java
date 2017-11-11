package com.example.seniorproject.smartshopping.view.recyclerviewadapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.seniorproject.smartshopping.R;
import com.example.seniorproject.smartshopping.model.daorecyclerview.shoppinglistitemoptimize.BaseItemOptimize;
import com.example.seniorproject.smartshopping.model.daorecyclerview.shoppinglistitemoptimize.ItemOptimizeRecyclerView;
import com.example.seniorproject.smartshopping.model.daorecyclerview.shoppinglistitemoptimize.ItemOptimizeSavePriceRecyclerView;
import com.example.seniorproject.smartshopping.model.daorecyclerview.shoppinglistitemoptimize.ItemOptimizeStoreNameRecyclerView;
import com.example.seniorproject.smartshopping.model.daorecyclerview.shoppinglistitemoptimize.ItemOptimizeTotalPriceRecyclerView;
import com.example.seniorproject.smartshopping.view.customviewgroup.CustomViewGroupShoppingListItemOptimize;
import com.example.seniorproject.smartshopping.view.viewholder.shoppinglistitemptimize.ShoppingListItemOptimizeSavePriceViewHolder;
import com.example.seniorproject.smartshopping.view.viewholder.shoppinglistitemptimize.ShoppingListItemOptimizeTotalPriceViewHolder;
import com.example.seniorproject.smartshopping.view.viewholder.shoppinglistitemptimize.ShoppingListItemOptimizeViewHolder;
import com.example.seniorproject.smartshopping.view.viewholder.shoppinglistitemptimize.ShoppingListItemStoreNameViewHolder;

import java.util.ArrayList;

/**
 * Created by boyburin on 11/9/2017 AD.
 */

public class ShoppingListItemOptimizeRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<BaseItemOptimize> itemOptimizes;
    private Context context;

    /***********************************************************************************************
     ************************************* Method ********************************************
     ***********************************************************************************************/

    public ShoppingListItemOptimizeRecyclerViewAdapter(Context context){
        this.context = context;
        itemOptimizes = new ArrayList<>();
    }

    public void setPurchaseItems(ArrayList<BaseItemOptimize> itemOptimizes){
        this.itemOptimizes = itemOptimizes;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        RecyclerView.ViewHolder viewHolder;

        if(viewType == BaseItemOptimize.STORE_NAME){
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.custom_view_group_shopping_list_item_optimize_store, parent, false);
            viewHolder = new ShoppingListItemStoreNameViewHolder(view);
        }
        else if(viewType == BaseItemOptimize.TOTAL_PRICE){
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.custom_view_group_shopping_list_item_optimize_total_price, parent, false);
            viewHolder = new ShoppingListItemOptimizeTotalPriceViewHolder(view);
        }
        else if(viewType == BaseItemOptimize.SAVE_PRICE){
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.custom_view_group_shopping_list_item_optimize_save_price, parent, false);
            viewHolder = new ShoppingListItemOptimizeSavePriceViewHolder(view);
        }
        else{
            view = new CustomViewGroupShoppingListItemOptimize(context);
            view.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            viewHolder = new ShoppingListItemOptimizeViewHolder(view);
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        BaseItemOptimize item = itemOptimizes.get(position);

        if(holder instanceof ShoppingListItemStoreNameViewHolder){
            setStoreNameViewHolder(holder, item);
        }
        else if(holder instanceof ShoppingListItemOptimizeTotalPriceViewHolder){
            setShoppingListItemOptimizeTotalPriceViewHolder(holder, item);
        }
        else if(holder instanceof ShoppingListItemOptimizeSavePriceViewHolder){
            setShoppingListItemOptimizeSavePriceViewHolder(holder, item);
        }
        else if(holder instanceof ShoppingListItemOptimizeViewHolder){
            setItemOptimizeViewHolder(holder, item);
        }

    }

    @Override
    public int getItemCount() {
        return itemOptimizes.size();
    }

    @Override
    public int getItemViewType(int position) {
        return itemOptimizes.get(position).getType();
    }


    private void setStoreNameViewHolder(RecyclerView.ViewHolder holder, BaseItemOptimize item){
        ShoppingListItemStoreNameViewHolder myHolder = (ShoppingListItemStoreNameViewHolder) holder;
        ItemOptimizeStoreNameRecyclerView myItem = (ItemOptimizeStoreNameRecyclerView) item;

        myHolder.tvStoreName.setText(myItem.getStoreName());
    }


    private void setItemOptimizeViewHolder(RecyclerView.ViewHolder holder, BaseItemOptimize item){
        ShoppingListItemOptimizeViewHolder myHolder = (ShoppingListItemOptimizeViewHolder) holder;
        ItemOptimizeRecyclerView myItem  = (ItemOptimizeRecyclerView) item;

        ((CustomViewGroupShoppingListItemOptimize)myHolder.itemView).setName(myItem.getProductCrowd().getName());
        ((CustomViewGroupShoppingListItemOptimize)myHolder.itemView).setPrice(myItem.getProductCrowd().getPrice());

    }

    private void setShoppingListItemOptimizeTotalPriceViewHolder(RecyclerView.ViewHolder holder, BaseItemOptimize item){
        ShoppingListItemOptimizeTotalPriceViewHolder myHolder = (ShoppingListItemOptimizeTotalPriceViewHolder) holder;
        ItemOptimizeTotalPriceRecyclerView myItem = (ItemOptimizeTotalPriceRecyclerView) item;

        myHolder.tvTotalPrice.setText(myItem.getTotalPrice() + "");
    }

    private void setShoppingListItemOptimizeSavePriceViewHolder(RecyclerView.ViewHolder holder, BaseItemOptimize item){
        ShoppingListItemOptimizeSavePriceViewHolder myHolder = (ShoppingListItemOptimizeSavePriceViewHolder) holder;
        ItemOptimizeSavePriceRecyclerView myItem = (ItemOptimizeSavePriceRecyclerView) item;

        myHolder.tvSavePrice.setText(myItem.getSavePrice() + "");
    }

}
