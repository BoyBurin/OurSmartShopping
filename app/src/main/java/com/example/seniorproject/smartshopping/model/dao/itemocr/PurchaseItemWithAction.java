package com.example.seniorproject.smartshopping.model.dao.itemocr;

import android.view.View;

/**
 * Created by boyburin on 11/10/2017 AD.
 */

public class PurchaseItemWithAction {

    private ItemOCR itemOCR;
    private View.OnClickListener delete;

    public PurchaseItemWithAction(){}

    public PurchaseItemWithAction(ItemOCR itemOCR, View.OnClickListener delete){
        this.itemOCR = itemOCR;
        this.delete = delete;
    }

    public ItemOCR getItemOCR() {
        return itemOCR;
    }

    public void setItemOCR(ItemOCR itemOCR) {
        this.itemOCR = itemOCR;
    }

    public View.OnClickListener getDelete() {
        return delete;
    }

    public void setDelete(View.OnClickListener edit) {
        this.delete = edit;
    }
}
