package com.example.seniorproject.smartshopping.view.customviewgroup;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.seniorproject.smartshopping.R;
import com.example.seniorproject.smartshopping.view.state.BundleSavedState;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * Created by nuuneoi on 11/16/2014.
 */
public class ShoppingListView extends BaseCustomViewGroup {

    TextView tvName;
    TextView tvDescription;
    ImageView ivImg;

    StorageReference storageReference;

    public ShoppingListView(Context context) {
        super(context);
        initInflate();
        initInstances();
    }

    public ShoppingListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initInflate();
        initInstances();
        initWithAttrs(attrs, 0, 0);
    }

    public ShoppingListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initInflate();
        initInstances();
        initWithAttrs(attrs, defStyleAttr, 0);
    }

    @TargetApi(21)
    public ShoppingListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initInflate();
        initInstances();
        initWithAttrs(attrs, defStyleAttr, defStyleRes);
    }

    private void initInflate() {
        inflate(getContext(), R.layout.shopping_list_view, this);
    }

    private void initInstances() {

        tvName = (TextView) findViewById(R.id.tvName);
        tvDescription= (TextView) findViewById(R.id.tvDescription);
        ivImg = (ImageView) findViewById(R.id.ivImg);
        // findViewById here
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

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec); // width is pixel
        height = height / 3;
        int newHeightMeasureSpec = MeasureSpec.makeMeasureSpec(
                height,
                MeasureSpec.EXACTLY
        );
        super.onMeasure(widthMeasureSpec, newHeightMeasureSpec);

        setMeasuredDimension(width, height);

    }

    public void setNameText(String text) {
        this.tvName.setText(text);
    }

    public void setDescriptionText(String text) {
        this.tvDescription.setText(text);
    }

    public void setImageUrl(String url) {
        storageReference = FirebaseStorage.getInstance().getReference();
        Glide.with(getContext())
                .load(url)
                .placeholder(R.drawable.icon_smartshopping_app) //default pic
                .centerCrop()
                //.error(Drawable pic)  picture has problem
                //.transform() Cool !!!
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(ivImg);
    }
}
