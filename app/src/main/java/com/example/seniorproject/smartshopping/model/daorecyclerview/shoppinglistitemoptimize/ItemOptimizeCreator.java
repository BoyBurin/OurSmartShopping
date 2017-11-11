package com.example.seniorproject.smartshopping.model.daorecyclerview.shoppinglistitemoptimize;

import com.example.seniorproject.smartshopping.model.dao.productstore.ProductCrowd;
import com.example.seniorproject.smartshopping.view.viewholder.shoppinglistitemptimize.ShoppingListItemOptimizeTotalPriceViewHolder;

import java.util.ArrayList;

/**
 * Created by boyburin on 11/11/2017 AD.
 */

public class ItemOptimizeCreator {

    public ItemOptimizeTotalPriceRecyclerView createTotalPrice(double totalPrice){
        ItemOptimizeTotalPriceRecyclerView itemOptimizeTotalPriceRecyclerView = new ItemOptimizeTotalPriceRecyclerView();
        itemOptimizeTotalPriceRecyclerView.setTotalPrice(totalPrice);

        return itemOptimizeTotalPriceRecyclerView;
    }

    public ItemOptimizeSavePriceRecyclerView createSavePrice(long savePrice){
        ItemOptimizeSavePriceRecyclerView itemOptimizeSavePriceRecyclerView = new ItemOptimizeSavePriceRecyclerView();
        itemOptimizeSavePriceRecyclerView.setSavePrice(savePrice);

        return itemOptimizeSavePriceRecyclerView;
    }

    public ArrayList<BaseItemOptimize> createItemOptimize(ArrayList<ProductCrowd> productCrowds){
        ArrayList<BaseItemOptimize> baseItemOptimizes = new ArrayList<>();
        ArrayList<String> stores = getStores(productCrowds);

        for(String store : stores){
            ItemOptimizeStoreNameRecyclerView itemInventoryTypeNameRecyclerView = new ItemOptimizeStoreNameRecyclerView();
            itemInventoryTypeNameRecyclerView.setStoreName(store);
            baseItemOptimizes.add(itemInventoryTypeNameRecyclerView);

            for(ProductCrowd item : productCrowds){

                if(item.getStore().equals(store)) {
                    ItemOptimizeRecyclerView itemOptimizeRecyclerView = new ItemOptimizeRecyclerView();
                    itemOptimizeRecyclerView.setProductCrowd(item);
                    baseItemOptimizes.add(itemOptimizeRecyclerView);
                }
            }
        }

        return baseItemOptimizes;

    }


    private ArrayList<String> getStores(ArrayList<ProductCrowd> productCrowds){
        ArrayList<String> types = new ArrayList<>();

        for(ProductCrowd item : productCrowds){
            String itemType = item.getStore();
            if(!types.contains(itemType)){
                types.add(itemType);
            }
        }
        return types;
    }
}
