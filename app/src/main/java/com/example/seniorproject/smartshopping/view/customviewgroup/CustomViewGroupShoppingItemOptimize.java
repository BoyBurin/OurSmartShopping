package com.example.seniorproject.smartshopping.view.customviewgroup;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.widget.TextView;

import com.example.seniorproject.smartshopping.R;
import com.example.seniorproject.smartshopping.view.state.BundleSavedState;

public class CustomViewGroupShoppingItemOptimize extends BaseCustomViewGroup {

    private TextView name;
    private TextView price;
    private TextView place;

    public CustomViewGroupShoppingItemOptimize(Context context) {
        super(context);
        initInflate();
        initInstances();
    }

    public CustomViewGroupShoppingItemOptimize(Context context, AttributeSet attrs) {
        super(context, attrs);
        initInflate();
        initInstances();
        initWithAttrs(attrs, 0, 0);
    }

    public CustomViewGroupShoppingItemOptimize(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initInflate();
        initInstances();
        initWithAttrs(attrs, defStyleAttr, 0);
    }

    @TargetApi(21)
    public CustomViewGroupShoppingItemOptimize(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initInflate();
        initInstances();
        initWithAttrs(attrs, defStyleAttr, defStyleRes);
    }

    private void initInflate() {
        inflate(getContext(), R.layout.custom_view_group_shooping_list_item_optimize, this);
    }

    private void initInstances() {
        name = (TextView) findViewById(R.id.name);
        price = (TextView) findViewById(R.id.price);
        place = (TextView) findViewById(R.id.place);
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

    public void setName(String name) {
        this.name.setText(name);
    }

    public void setPrice(double price) {
        this.price.setText(price + "");
    }

    public void setPlace(String place) {
        this.place.setText(place);
    }
}
