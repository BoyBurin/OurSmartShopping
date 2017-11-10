package com.example.seniorproject.smartshopping.view.customviewgroup;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.seniorproject.smartshopping.R;
import com.example.seniorproject.smartshopping.view.state.BundleSavedState;

public class CustomViewGroupShoppingListItemAdd extends BaseCustomViewGroup {

    private TextView tvAddedItemName;
    private EditText tvAddedAmount;

    public CustomViewGroupShoppingListItemAdd(Context context) {
        super(context);
        initInflate();
        initInstances();
    }

    public CustomViewGroupShoppingListItemAdd(Context context, AttributeSet attrs) {
        super(context, attrs);
        initInflate();
        initInstances();
        initWithAttrs(attrs, 0, 0);
    }

    public CustomViewGroupShoppingListItemAdd(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initInflate();
        initInstances();
        initWithAttrs(attrs, defStyleAttr, 0);
    }

    @TargetApi(21)
    public CustomViewGroupShoppingListItemAdd(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initInflate();
        initInstances();
        initWithAttrs(attrs, defStyleAttr, defStyleRes);
    }

    private void initInflate() {
        inflate(getContext(), R.layout.custom_view_group_shopping_list_item_add, this);
    }

    private void initInstances() {
        tvAddedItemName = (TextView) findViewById(R.id.tvAddedItemName);
        tvAddedAmount = (EditText) findViewById(R.id.tvAddedAmount);
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

    public void setItemName(String name) {
        this.tvAddedItemName.setText(name);
    }

    public long getAmount(){
        return Long.parseLong(tvAddedAmount.getText().toString());
    }

    public void clearAll(){
        this.tvAddedAmount.setText("1");
        this.tvAddedItemName.setText("");
    }


    public boolean isAmountEmpty(){
        if(tvAddedAmount.getText() == null || tvAddedAmount.getText().toString().equals("") ){
            return true;
        }
        else if(Long.parseLong(tvAddedAmount.getText().toString()) < 0) {
            return true;
        }
            return false;
    }


}
