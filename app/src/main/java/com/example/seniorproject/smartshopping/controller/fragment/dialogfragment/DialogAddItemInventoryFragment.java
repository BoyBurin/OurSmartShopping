package com.example.seniorproject.smartshopping.controller.fragment.dialogfragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
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
import com.example.seniorproject.smartshopping.model.manager.GroupManager;
import com.example.seniorproject.smartshopping.superuser.ProductList;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;


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

    private FirebaseFirestore db;
    private CollectionReference cProductList;
    private CollectionReference cItems;

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

        db = FirebaseFirestore.getInstance();
        cProductList = db.collection("productlist");
        cItems = db.collection("groups").document(GroupManager.getInstance().getCurrentGroup().getId())
                .collection("items");

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

        btnAdd.setOnClickListener(addItemInventory);

        btnCancel.setOnClickListener(cancelDialogListener);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        String contents = data.getStringExtra("SCAN_RESULT");
        barcodeId = contents;
        cProductList.whereEqualTo("barcodeId", barcodeId).get().addOnCompleteListener(getProductList);
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


    final OnCompleteListener<QuerySnapshot> getProductList = new OnCompleteListener<QuerySnapshot>() {
        @Override
        public void onComplete(@NonNull Task<QuerySnapshot> task) {

            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult().getDocuments().get(0);
                if (document != null) {
                    ProductList productList = document.toObject(ProductList.class);
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

                } else {
                    Toast.makeText(getContext(), "Item is not found", Toast.LENGTH_SHORT).show();
                    Log.d("TAG", "No such document");
                    closeDialog();
                }
            } else {
                Log.d("TAG", "get failed with ", task.getException());
                closeDialog();
            }
        }
    };


    final View.OnClickListener cancelDialogListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            closeDialog();
        }
    };

    final View.OnClickListener addBarcodeListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            BarcodeListener barcodeListener = (BarcodeListener) getActivity();
            barcodeListener.scanBarcode();
        }
    };



    final View.OnClickListener addItemInventory = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            if(tvName.getText().toString().equals("") || tvName.getText().toString() == null ||
                    edtAmount.getText().toString().equals("") || edtAmount.getText().toString() == null ||
                    edtHard.getText().toString().equals("") || edtHard.getText().toString() == null ||
                    edtSoft.getText().toString().equals("") || edtSoft.getText().toString() == null){

                Toast.makeText(getContext(), "Please complete information", Toast.LENGTH_SHORT).show();
                closeDialog();
                return;
            }

            String name = tvName.getText().toString();
            long amount = Long.parseLong(edtAmount.getText().toString());
            String comment = edtListDescribe.getText().toString();
            long hard = Long.parseLong(edtHard.getText().toString());
            long soft = Long.parseLong(edtSoft.getText().toString());

            ItemInventory item = new ItemInventory(name, amount, comment,
                    photoUrl, unit, barcodeId, hard, soft);

            cItems.add(item);
            Toast.makeText(getActivity(), "Add Item Success", Toast.LENGTH_SHORT).show();

            closeDialog();

        }
    };


    /***********************************************************************************************
     ************************************* Inner class ********************************************
     ***********************************************************************************************/

}
