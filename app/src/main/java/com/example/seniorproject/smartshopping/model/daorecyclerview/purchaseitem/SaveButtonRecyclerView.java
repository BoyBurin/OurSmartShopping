package com.example.seniorproject.smartshopping.model.daorecyclerview.purchaseitem;

import android.view.View;

/**
 * Created by boyburin on 11/10/2017 AD.
 */

public class SaveButtonRecyclerView extends BasePurchaseItem {

    private View.OnClickListener saveListener;

    public SaveButtonRecyclerView() {
        super(BasePurchaseItem.SAVE_BUTTON);
    }

    public View.OnClickListener getSaveListener() {
        return saveListener;
    }

    public void setSaveListener(View.OnClickListener saveListener) {
        this.saveListener = saveListener;
    }
}
