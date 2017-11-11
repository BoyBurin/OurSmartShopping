package com.example.seniorproject.smartshopping.model.dao.itemocr;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;

/**
 * Created by boyburin on 11/10/2017 AD.
 */

public class PurchaseItemWithAction implements Parcelable{

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


    protected PurchaseItemWithAction(Parcel in) {
        itemOCR = in.readParcelable(ItemOCR.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(itemOCR, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PurchaseItemWithAction> CREATOR = new Creator<PurchaseItemWithAction>() {
        @Override
        public PurchaseItemWithAction createFromParcel(Parcel in) {
            return new PurchaseItemWithAction(in);
        }

        @Override
        public PurchaseItemWithAction[] newArray(int size) {
            return new PurchaseItemWithAction[size];
        }
    };
}
