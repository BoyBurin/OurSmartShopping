package com.example.seniorproject.smartshopping.view.customviewgroup;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.seniorproject.smartshopping.R;
import com.example.seniorproject.smartshopping.view.state.BundleSavedState;

public class CustomViewGroupPurchaseItem extends BaseCustomViewGroup {

    private ImageView imgViewPurchaseItem;
    private TextView tvName;
    private TextView tvAmount;
    private TextView tvPrice;
    private ImageButton imgBtnDelete;

    public CustomViewGroupPurchaseItem(Context context) {
        super(context);
        initInflate();
        initInstances();
    }

    public CustomViewGroupPurchaseItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        initInflate();
        initInstances();
        initWithAttrs(attrs, 0, 0);
    }

    public CustomViewGroupPurchaseItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initInflate();
        initInstances();
        initWithAttrs(attrs, defStyleAttr, 0);
    }

    @TargetApi(21)
    public CustomViewGroupPurchaseItem(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initInflate();
        initInstances();
        initWithAttrs(attrs, defStyleAttr, defStyleRes);
    }

    private void initInflate() {
        inflate(getContext(), R.layout.custom_view_group_purchase_item, this);
    }

    private void initInstances() {
        imgViewPurchaseItem = (ImageView) findViewById(R.id.imgViewPurchaseItem);
        tvName = (TextView) findViewById(R.id.tvName);
        tvAmount = (TextView) findViewById(R.id.tvAmount);
        tvPrice = (TextView) findViewById(R.id.tvPrice);
        imgBtnDelete = (ImageButton) findViewById(R.id.imgBtnDelete);
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

    public void setName(String name){
        tvName.setText(name);
    }

    public void setAmount(long amount){
        tvAmount.setText(amount + "");
    }


    public void setPrice(double price){
        tvPrice.setText(price + "");
    }

    public void setImage(String url){
        Glide.with(getContext())
                .load(url)
                .thumbnail(Glide.with(getContext()).load(R.drawable.loading)
                        //.placeholder(R.drawable.loading) //default pic
                        .centerCrop())
                //.error(Drawable pic)  picture has problem
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgViewPurchaseItem);
    }

    public void setImgBtnDelete(OnClickListener deleteListener){
        imgBtnDelete.setOnClickListener(deleteListener);
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

}
