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
import com.example.seniorproject.smartshopping.view.transformation.CircleTransform;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class CustomViewGroupUser extends BaseCustomViewGroup {

    private ImageView imgUser;
    private TextView tvUsername;


    public CustomViewGroupUser(Context context) {
        super(context);
        initInflate();
        initInstances();
    }

    public CustomViewGroupUser(Context context, AttributeSet attrs) {
        super(context, attrs);
        initInflate();
        initInstances();
        initWithAttrs(attrs, 0, 0);
    }

    public CustomViewGroupUser(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initInflate();
        initInstances();
        initWithAttrs(attrs, defStyleAttr, 0);
    }

    @TargetApi(21)
    public CustomViewGroupUser(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initInflate();
        initInstances();
        initWithAttrs(attrs, defStyleAttr, defStyleRes);
    }

    private void initInflate() {
        inflate(getContext(), R.layout.custom_view_group_user, this);
    }

    private void initInstances() {
        tvUsername = (TextView) findViewById(R.id.tvUsername);
        imgUser = (ImageView) findViewById(R.id.imgUser);
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


    public void setImageUrl(String url) {
        Glide.with(getContext())
                .load(url)
                .thumbnail(Glide.with(getContext()).load(R.drawable.loading)
                //.placeholder(R.drawable.loading) //default pic
                .centerCrop())
                //.error(Drawable pic)  picture has problem
                .transform(new CircleTransform(getContext())) //Cool !!!
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgUser);

    }

    public void setNameText(String text) {
        this.tvUsername.setText(text);
    }

}
