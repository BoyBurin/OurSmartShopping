package com.example.seniorproject.smartshopping.view.customviewgroup;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.seniorproject.smartshopping.R;
import com.example.seniorproject.smartshopping.view.state.BundleSavedState;

public class CustomViewGroupEditText extends BaseCustomViewGroup {

    private TextView tvCustomViewGroupEditText;
    private EditText edTextCustomViewGroupEditText;


    public void setTextView(String text) {

        this.tvCustomViewGroupEditText.setText(text);
    }

    public void setHintEditText(String text){

        this.edTextCustomViewGroupEditText.setHint(text);
    }

    public String getTextFromEditText(){
        return edTextCustomViewGroupEditText.getText().toString();
    }

    public void setTextToEditText(String text){

        edTextCustomViewGroupEditText.setText(text);
    }

    public void setEditTextInputTypeToPassword(){
        edTextCustomViewGroupEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        edTextCustomViewGroupEditText.setImeOptions(EditorInfo.IME_ACTION_DONE);
    }

    public void setEditTextInputTypeToText(){
        edTextCustomViewGroupEditText.setInputType(InputType.TYPE_CLASS_TEXT);
        edTextCustomViewGroupEditText.setImeOptions(EditorInfo.IME_ACTION_NEXT);
    }

    public CustomViewGroupEditText(Context context) {
        super(context);
        initInflate();
        initInstances();
    }

    public CustomViewGroupEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        initInflate();
        initInstances();
        initWithAttrs(attrs, 0, 0);
    }

    public CustomViewGroupEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initInflate();
        initInstances();
        initWithAttrs(attrs, defStyleAttr, 0);
    }

    @TargetApi(21)
    public CustomViewGroupEditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initInflate();
        initInstances();
        initWithAttrs(attrs, defStyleAttr, defStyleRes);
    }

    private void initInflate() {

        inflate(getContext(), R.layout.custome_view_group_edit_text, this);
    }

    private void initInstances() {

        tvCustomViewGroupEditText = (TextView) findViewById(R.id.tvCustomViewGroupEditText);
        edTextCustomViewGroupEditText = (EditText) findViewById(R.id.edTextCustomViewGroupEditText);

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


}
