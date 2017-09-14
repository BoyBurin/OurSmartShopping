package com.example.seniorproject.smartshopping.controller.fragment.dialogfragment;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.seniorproject.smartshopping.R;
import com.example.seniorproject.smartshopping.model.dao.ItemInventory;
import com.example.seniorproject.smartshopping.model.dao.RemindItem;
import com.example.seniorproject.smartshopping.model.manager.GroupManager;
import com.example.seniorproject.smartshopping.superuser.ProductList;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class DialogAddItemInventoryFragment extends DialogFragment {

    /***********************************************************************************************
     ************************************* Variable class ********************************************
     ***********************************************************************************************/
    public interface BarcodeListener{
        public void scanBarcode();
    }

    public interface DeleteItemInventoryDialog{
        void deleteAddItemInventoryDialog();
    }

    DatabaseReference mDatabaseRef;

    private ImageView imgItem;
    private TextView tvName;
    private EditText edtAmount;
    private EditText edtSoft;
    private EditText edtHard;
    private EditText edtListDescribe;
    private  Button btnScanBarcode;
    private Button btnCancel;
    private Button btnAdd;


    private String photoUrl;

    private String unit;
    private String barcodeId;



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
        photoUrl = "bf";
        barcodeId = "vdf";
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
        btnCancel = (Button) rootView.findViewById(R.id.btnCancel);
        btnAdd = (Button) rootView.findViewById(R.id.btnAdd);

        btnScanBarcode.setOnClickListener(addBarcodeListener);

        btnAdd.setOnClickListener(addItemListener);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        String contents = data.getStringExtra("SCAN_RESULT");
        barcodeId = contents;
        mDatabaseRef.child("productlist").child(barcodeId).addListenerForSingleValueEvent(retriveProductListListener);
    }

    private void closeDialog(){
        DeleteItemInventoryDialog deleteItemInventoryDialog =
                (DeleteItemInventoryDialog) getActivity();
        deleteItemInventoryDialog.deleteAddItemInventoryDialog();
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

    final View.OnClickListener addBarcodeListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            BarcodeListener barcodeListener = (BarcodeListener) getActivity();
            barcodeListener.scanBarcode();
        }
    };

    final ValueEventListener retriveProductListListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            ProductList productList = dataSnapshot.getValue(ProductList.class);
            Glide.with(getContext())
                    .load(productList.getPhotoUrl())
                    .placeholder(R.drawable.bg_small) //default pic
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imgItem);

            tvName.setText(productList.getName());
            photoUrl = productList.getPhotoUrl().toString();
            unit = productList.getUnit();

            edtAmount.setVisibility(View.VISIBLE);
            edtSoft.setVisibility(View.VISIBLE);
            edtHard.setVisibility(View.VISIBLE);
            edtListDescribe.setVisibility(View.VISIBLE);
            btnCancel.setVisibility(View.VISIBLE);
            btnAdd.setVisibility(View.VISIBLE);




        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    final View.OnClickListener addItemListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(photoUrl == null || barcodeId == null) return;
            DatabaseReference itemInventoryRef = mDatabaseRef.child("iteminventory");
            final String itemInventoryID = itemInventoryRef.push().getKey();
            String name = tvName.getText().toString();
            long amount = Long.parseLong(edtAmount.getText().toString());
            String comment = edtListDescribe.getText().toString();

            RemindItem remindItem = new RemindItem();
            remindItem.setSoft(Long.parseLong(edtSoft.getText().toString()));
            remindItem.setSoft(Long.parseLong(edtHard.getText().toString()));

            ItemInventory itemInventory = new ItemInventory(name, amount, comment,
                    photoUrl, remindItem, unit, barcodeId);
            itemInventoryRef.child(itemInventoryID).setValue(itemInventory)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            DatabaseReference itemInGroup = mDatabaseRef.child("itemingroup");

                            itemInGroup.child(GroupManager.getInstance().getCurrentGroup().getId()).child(itemInventoryID)
                                    .setValue(true).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(getActivity(), "Add Item Success", Toast.LENGTH_SHORT).show();

                                    closeDialog();

                                }
                            });

                        }
                    });
        }
    };


    /***********************************************************************************************
     ************************************* Inner class ********************************************
     ***********************************************************************************************/

}
