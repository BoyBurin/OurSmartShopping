package com.example.seniorproject.smartshopping.model.manager.itemocr;

import android.content.Context;

import com.example.seniorproject.smartshopping.model.dao.itemocr.ItemOCR;
import com.example.seniorproject.smartshopping.model.dao.itemocr.PurchaseItemWithAction;
import com.example.seniorproject.smartshopping.model.manager.Contextor;

import java.util.ArrayList;

/**
 * Created by boyburin on 9/24/2017 AD.
 */

public class PurchaseItemWithActionManager {

    /******************************************************************************************
     * ****************************** Variable *********************************************
     *******************************************************************************************/

    private ArrayList<PurchaseItemWithAction> purchaseItemWithActions;

    /******************************************************************************************
     * ****************************** Methods *********************************************
     *******************************************************************************************/

    public PurchaseItemWithActionManager() {
        purchaseItemWithActions = new ArrayList<PurchaseItemWithAction>();
        mContext = Contextor.getInstance().getContext();
    }

    private Context mContext;

    public void addPurchaseItemWithAction(PurchaseItemWithAction purchaseItemWithAction){
        purchaseItemWithActions.add(purchaseItemWithAction);
    }

    public ArrayList<PurchaseItemWithAction> getIPurchaseItemWithActions(){
        return purchaseItemWithActions;
    }

    public int getSize(){
        return purchaseItemWithActions.size();
    }

    public void removePurchaseItemWithAction(String barcodeId){
        for(int i = 0 ; i < purchaseItemWithActions.size() ; i++){
            String myBarcodeId = purchaseItemWithActions.get(i).getItemOCR().getItemInventoryMap().getItemInventory().getBarcodeId();
            if(myBarcodeId.equals(barcodeId)){
                purchaseItemWithActions.remove(i);
                break;
            }
        }

    }
}
