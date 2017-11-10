package com.example.seniorproject.smartshopping.view.customviewgroup;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.TextView;

import com.example.seniorproject.smartshopping.R;
import com.example.seniorproject.smartshopping.view.state.BundleSavedState;

public class CustomViewGroupAddPurchaseItemDetail extends BaseCustomViewGroup {

    private TextView tvItemName;
    private EditText tvAmount;
    private EditText tvPrice;

    public CustomViewGroupAddPurchaseItemDetail(Context context) {
        super(context);
        initInflate();
        initInstances();
    }

    public CustomViewGroupAddPurchaseItemDetail(Context context, AttributeSet attrs) {
        super(context, attrs);
        initInflate();
        initInstances();
        initWithAttrs(attrs, 0, 0);
    }

    public CustomViewGroupAddPurchaseItemDetail(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initInflate();
        initInstances();
        initWithAttrs(attrs, defStyleAttr, 0);
    }

    @TargetApi(21)
    public CustomViewGroupAddPurchaseItemDetail(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initInflate();
        initInstances();
        initWithAttrs(attrs, defStyleAttr, defStyleRes);
    }

    private void initInflate() {
        inflate(getContext(), R.layout.custom_view_group_add_purchase_item_detail, this);
    }

    private void initInstances() {

        tvItemName = (TextView) findViewById(R.id.tvItemName);
        tvAmount = (EditText) findViewById(R.id.tvAmount);
        tvPrice = (EditText) findViewById(R.id.tvPrice);
    }

    private void initWithAttrs(AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        /*
        TypedArray a = getContext().getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.StyleableName,
                defStyleAttr, defStyleRes);

        try {

        } finally {
            a.recycle();
        }
        */
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();

        BundleSavedState savedState = new BundleSavedState(superState);
        // Save Instance State(s) here to the 'savedState.getBundle()'
        // for example,
        // savedState.getBundle().putString("key", value);

        return savedState;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        BundleSavedState ss = (BundleSavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());

        Bundle bundle = ss.getBundle();
        // Restore State from bundle here
    }

    public void setItemName(String name){
        tvItemName.setText(name);
    }

    public String getItemName(){
        return tvItemName.getText().toString();
    }

    public void setPrice(double price){
        tvPrice.setText(""+price);
    }

    public double getPrice(){
        return Double.parseDouble(tvPrice.getText().toString());
    }

    public void setAmount(long amount){
        tvAmount.setText(""+amount);
    }

    public long getAmount(){
        return Long.parseLong(tvAmount.getText().toString());
    }

    public boolean isPriceEmpty(){
        if(tvPrice.getText() == null || Double.parseDouble(tvPrice.getText().toString()) < 0){
            return true;
        }
        return false;
    }

    public boolean isAmountEmpty(){
        if(tvAmount.getText() == null || Long.parseLong(tvAmount.getText().toString()) < 0){
            return true;
        }
        return false;
    }

}
