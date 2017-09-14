package com.example.seniorproject.smartshopping.controller.fragment.dialogfragment;

import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.seniorproject.smartshopping.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class DialogAddItemInventoryFragment extends Fragment {

    /***********************************************************************************************
     ************************************* Variable class ********************************************
     ***********************************************************************************************/

    DatabaseReference mDatabaseRef;

    private ImageView imgItem;
    private TextView tvName;
    private EditText edtAmount;
    private EditText edtSoft;
    private EditText edtHard;
    private EditText edtListDescribe;
    private  Button btnScanBarcode;
    private Button btnUploadImage;
    private Button btnCancel;
    private Button btnAdd;



    /***********************************************************************************************
     ************************************* Method class ********************************************
     ***********************************************************************************************/

    public DialogAddItemInventoryFragment() {
        super();
    }

    @SuppressWarnings("unused")
    public static DialogAddItemInventoryFragment newInstance() {
        DialogAddItemInventoryFragment fragment = new DialogAddItemInventoryFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(savedInstanceState);

        if (savedInstanceState != null)
            onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_dialog_add_item_inventory, container, false);
        initInstances(rootView, savedInstanceState);
        return rootView;
    }

    private void init(Bundle savedInstanceState) {
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();

    }

    @SuppressWarnings("UnusedParameters")
    private void initInstances(View rootView, Bundle savedInstanceState) {
        imgItem = (ImageView) rootView.findViewById(R.id.imgItem);
        tvName = (TextView) rootView.findViewById(R.id.tvName);
        edtAmount = (EditText) rootView.findViewById(R.id.edtAmount);
        edtSoft = (EditText) rootView.findViewById(R.id.edtSoft);
        edtHard = (EditText) rootView.findViewById(R.id.edtHard);
        edtListDescribe = (EditText) rootView.findViewById(R.id.edtListDescribe);
        btnScanBarcode = (Button) rootView.findViewById(R.id.btnScanBarcode);
        btnUploadImage = (Button) rootView.findViewById(R.id.btnUploadImage);
        btnCancel = (Button) rootView.findViewById(R.id.btnCancel);
        btnAdd = (Button) rootView.findViewById(R.id.btnAdd);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    /*
     * Save Instance State Here
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save Instance State here
    }

    /*
     * Restore Instance State Here
     */
    @SuppressWarnings("UnusedParameters")
    private void onRestoreInstanceState(Bundle savedInstanceState) {
        // Restore Instance State here
    }

    /***********************************************************************************************
     ************************************* Listener variables ********************************************
     ***********************************************************************************************/


    /***********************************************************************************************
     ************************************* Inner class ********************************************
     ***********************************************************************************************/

}
