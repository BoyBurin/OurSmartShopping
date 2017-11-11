package com.example.seniorproject.smartshopping.model.daorecyclerview.shoppinglistitemoptimize;

import com.example.seniorproject.smartshopping.model.dao.productstore.ProductCrowd;

/**
 * Created by boyburin on 11/11/2017 AD.
 */

public class ItemOptimizeRecyclerView extends BaseItemOptimize {

    private ProductCrowd productCrowd;

    public ItemOptimizeRecyclerView() {
        super(BaseItemOptimize.ITEM_OPTIMIZE);
    }

    public ProductCrowd getProductCrowd() {
        return productCrowd;
    }

    public void setProductCrowd(ProductCrowd productCrowd) {
        this.productCrowd = productCrowd;
    }
}
