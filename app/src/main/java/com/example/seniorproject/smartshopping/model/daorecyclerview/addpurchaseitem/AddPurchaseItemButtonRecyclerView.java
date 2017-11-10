package com.example.seniorproject.smartshopping.model.daorecyclerview.addpurchaseitem;

import android.view.View;

/**
 * Created by boyburin on 11/10/2017 AD.
 */

public class AddPurchaseItemButtonRecyclerView extends BaseAddPurchaseItem {

    private View.OnClickListener btnCancel;
    private View.OnClickListener btnAdd;

    public AddPurchaseItemButtonRecyclerView() {
        super(BaseAddPurchaseItem.ADD_PURCHASE_ITEM_BUTTON);
    }

    public View.OnClickListener getBtnCancel() {
        return btnCancel;
    }

    public void setBtnCancel(View.OnClickListener btnCancel) {
        this.btnCancel = btnCancel;
    }

    public View.OnClickListener getBtnAdd() {
        return btnAdd;
    }

    public void setBtnAdd(View.OnClickListener btnAdd) {
        this.btnAdd = btnAdd;
    }
}
