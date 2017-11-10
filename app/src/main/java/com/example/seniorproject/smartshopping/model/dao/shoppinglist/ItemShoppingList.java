package com.example.seniorproject.smartshopping.model.dao.shoppinglist;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;

/**
 * Created by boyburin on 9/12/2017 AD.
 */

public class ItemShoppingList  implements Parcelable{
    /******************************************************************************************
     * ****************************** Variable *********************************************
     *******************************************************************************************/

    private long amount;
    private String name;
    private String barcodeId;
    private int status;
    private View.OnClickListener deleteListener;

    /******************************************************************************************
     * ****************************** Methods *********************************************
     *******************************************************************************************/

    public ItemShoppingList(){

    }

    public ItemShoppingList(long amount, String name, String barcodeId, int status){
        this.amount = amount;
        this.name = name;
        this.barcodeId = barcodeId;
        this.status = status;
    }


    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBarcodeId() {
        return barcodeId;
    }

    public void setBarcodeId(String barcodeId) {
        this.barcodeId = barcodeId;
    }

    public void setDeleteListener(View.OnClickListener deleteListener){
        this.deleteListener = deleteListener;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public View.OnClickListener getDeleteListener(){
        return deleteListener;
    }

    /******************************************************************************************
     * ****************************** Implementation *********************************************
     *******************************************************************************************/

    protected ItemShoppingList(Parcel in) {
        amount = in.readLong();
        name = in.readString();
        barcodeId = in.readString();
        status = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(amount);
        dest.writeString(name);
        dest.writeString(barcodeId);
        dest.writeInt(status);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ItemShoppingList> CREATOR = new Creator<ItemShoppingList>() {
        @Override
        public ItemShoppingList createFromParcel(Parcel in) {
            return new ItemShoppingList(in);
        }

        @Override
        public ItemShoppingList[] newArray(int size) {
            return new ItemShoppingList[size];
        }
    };
}
