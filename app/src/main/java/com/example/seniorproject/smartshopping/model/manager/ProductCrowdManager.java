package com.example.seniorproject.smartshopping.model.manager;

import android.content.Context;

import com.example.seniorproject.smartshopping.model.dao.ProductCrowd;

import java.util.ArrayList;

/**
 * Created by boyburin on 9/25/2017 AD.
 */

public class ProductCrowdManager {
    /******************************************************************************************
     * ****************************** Variable *********************************************
     *******************************************************************************************/

    private ArrayList<ProductCrowd> productCrowds;

    /******************************************************************************************
     * ****************************** Methods *********************************************
     *******************************************************************************************/

    public ProductCrowdManager() {
        productCrowds = new ArrayList<ProductCrowd>();
        mContext = Contextor.getInstance().getContext();
    }

    private Context mContext;

    public void addProductCrowd(ProductCrowd productCrowd){
        productCrowds.add(productCrowd);
    }

    public ArrayList<ProductCrowd> getProductCrowds(){
        return productCrowds;
    }
}
