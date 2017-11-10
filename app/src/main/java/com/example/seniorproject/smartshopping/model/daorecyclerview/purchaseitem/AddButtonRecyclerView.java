package com.example.seniorproject.smartshopping.model.daorecyclerview.purchaseitem;

import android.view.View;

/**
 * Created by boyburin on 11/10/2017 AD.
 */

public class AddButtonRecyclerView extends BasePurchaseItem {

    private View.OnClickListener addListener;

    public AddButtonRecyclerView() {
        super(BasePurchaseItem.ADD_BUTTON);
    }

    public View.OnClickListener getAddListener() {
        return addListener;
    }

    public void setAddListener(View.OnClickListener addListener) {
        this.addListener = addListener;
    }
}
