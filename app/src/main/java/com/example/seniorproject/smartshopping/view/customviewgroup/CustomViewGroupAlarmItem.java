package com.example.seniorproject.smartshopping.view.customviewgroup;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.seniorproject.smartshopping.R;
import com.example.seniorproject.smartshopping.view.state.BundleSavedState;

public class CustomViewGroupAlarmItem extends BaseCustomViewGroup {

    private TextView tvDay;
    private TextView tvTime;
    private ImageButton imgBtnDelete;

    public CustomViewGroupAlarmItem(Context context) {
        super(context);
        initInflate();
        initInstances();
    }

    public CustomViewGroupAlarmItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        initInflate();
        initInstances();
        initWithAttrs(attrs, 0, 0);
    }

    public CustomViewGroupAlarmItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initInflate();
        initInstances();
        initWithAttrs(attrs, defStyleAttr, 0);
    }

    @TargetApi(21)
    public CustomViewGroupAlarmItem(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initInflate();
        initInstances();
        initWithAttrs(attrs, defStyleAttr, defStyleRes);
    }

    private void initInflate() {
        inflate(getContext(), R.layout.custom_view_group_alarm_item, this);
    }

    private void initInstances() {
        tvDay = (TextView) findViewById(R.id.tvDay);
        tvTime = (TextView) findViewById(R.id.tvTime);
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

    public void setDay(String tvDay) {
        this.tvDay.setText(tvDay);
    }

    public void setTime(int hour, int minute) {
        this.tvTime.setText("" + hour + ":" + minute);
    }

    public void setDeleteListener(OnClickListener onClickListener){
        this.imgBtnDelete.setOnClickListener(onClickListener);
    }

}
