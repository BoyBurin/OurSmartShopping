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
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.seniorproject.smartshopping.R;
import com.example.seniorproject.smartshopping.model.dao.iteminventory.ItemInventory;
import com.example.seniorproject.smartshopping.model.dao.iteminventory.ItemInventoryMap;
import com.example.seniorproject.smartshopping.model.manager.group.GroupManager;
import com.example.seniorproject.smartshopping.model.manager.iteminventory.ItemInventoryManager;
import com.example.seniorproject.smartshopping.superuser.ProductList;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;


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
    private ListenerRegistration cItemsListener;

    private ImageView imgItem;
    private TextView tvName;
    private EditText edtAmount;
    private EditText edtSoft;
    private EditText edtHard;
    private EditText edtListDescribe;
    private  Button btnScanBarcode;
    private Button btnCancel;
    private Button btnAdd;
    private Button btnAdd2;
    private Button btnCancel2;
    private RadioGroup rg;
    private TextView tvUnit;
    private EditText edtNumber;
    private LinearLayout buttonLayout1;
    private LinearLayout buttonLayout2;
    private RelativeLayout downloading;
    private ProgressBar progressBar;

    private ItemInventoryManager itemInventoryManager = new ItemInventoryManager();

    private String photoUrl;

    private String unit;
    private String barcodeId;
    private String type;



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
        itemInventoryManager = new ItemInventoryManager();
        photoUrl = "bf";
        barcodeId = "vdf";

        db = FirebaseFirestore.getInstance();
        cProductList = db.collection("productlist");
        cItems = db.collection("groups").document(GroupManager.getInstance().getCurrentGroup().getId())
                .collection("items");

        cItemsListener = cItems.addSnapshotListener(itemListener);

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
        tvUnit = (TextView) rootView.findViewById(R.id.tvUnit);
        rg = (RadioGroup) rootView.findViewById(R.id.rg);
        edtNumber = (EditText) rootView.findViewById(R.id.edtNumber);
        btnAdd2 = (Button) rootView.findViewById(R.id.btnAdd2);
        btnCancel2 = (Button) rootView.findViewById(R.id.btnCancel2);
        buttonLayout1 = (LinearLayout)rootView.findViewById(R.id.buttonLayout1);
        buttonLayout2 = (LinearLayout)rootView.findViewById(R.id.buttonLayout2);
        downloading = (RelativeLayout) rootView.findViewById(R.id.downloading);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);


        btnScanBarcode.setOnClickListener(addBarcodeListener);

        btnAdd.setOnClickListener(addItemInventory);
        btnAdd2.setOnClickListener(updateItemListener);

        btnCancel.setOnClickListener(cancelDialogListener);
        btnCancel2.setOnClickListener(cancelDialogListener);

        rg.check(R.id.rIncrease);
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

    @Override
    public void onDestroy() {
        super.onDestroy();

        if(cItemsListener != null){
            cItemsListener.remove();
            cItemsListener = null;
        }
    }

    private void downLoading() {
        progressBar.setVisibility(View.VISIBLE);
        downloading.setVisibility(View.GONE);
    }

    private void finishDownloading(){
        progressBar.setVisibility(View.GONE);
        downloading.setVisibility(View.VISIBLE);
    }

    /***********************************************************************************************
     ************************************* Listener variables ********************************************
     ***********************************************************************************************/

    final View.OnClickListener updateItemListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            downLoading();
            db.runTransaction(new Transaction.Function<Void>() {
                @Override
                public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                    DocumentSnapshot snapshot = transaction.get(cItems.document(barcodeId));
                    int mul;
                    if(rg.getCheckedRadioButtonId() == R.id.rIncrease){
                        mul = 1;
                    }
                    else{
                        mul = -1;
                    }

                    long updateAmount = Long.parseLong(edtNumber.getText().toString());
                    long newAmount = snapshot.getLong("amount") + (updateAmount * mul);

                    if(!(newAmount < 0)){
                        transaction.update(cItems.document(barcodeId), "amount", newAmount);
                    }

                    // Success
                    return null;
                }
            }).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d("TAG", "Transaction success!");
                    Toast.makeText(getContext(), "Update amount Successful", Toast.LENGTH_SHORT).show();
                    closeDialog();
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            finishDownloading();
                            Toast.makeText(getContext(), "Update amount Failed", Toast.LENGTH_SHORT).show();
                            Log.w("TAG", "Transaction failure.", e);
                        }
                    });
        }
    };


    final OnCompleteListener<QuerySnapshot> getProductList = new OnCompleteListener<QuerySnapshot>() {
        @Override
        public void onComplete(@NonNull Task<QuerySnapshot> task) {

            if (task.isSuccessful()) {
                if(task.getResult().getDocuments().size() == 0){
                    Toast.makeText(getContext(), "Item is not found", Toast.LENGTH_SHORT).show();
                    Log.d("TAG", "No such document");
                    closeDialog();
                    return;
                }
                DocumentSnapshot document = task.getResult().getDocuments().get(0);
                if (document != null) {
                    imgItem.setVisibility(View.VISIBLE);
                    tvName.setVisibility(View.VISIBLE);
                    buttonLayout1.setVisibility(View.VISIBLE);
                    buttonLayout2.setVisibility(View.VISIBLE);
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
                    type = productList.getType();

                    if(itemInventoryManager.isContain(barcodeId)){
                        rg.setVisibility(View.VISIBLE);
                        tvUnit.setVisibility(View.VISIBLE);
                        edtNumber.setVisibility(View.VISIBLE);
                        btnAdd2.setVisibility(View.VISIBLE);
                        btnCancel2.setVisibility(View.VISIBLE);
                        btnScanBarcode.setVisibility(View.GONE);

                        tvUnit.setText(productList.getUnit());
                    }else {
                        edtAmount.setVisibility(View.VISIBLE);
                        edtSoft.setVisibility(View.VISIBLE);
                        edtHard.setVisibility(View.VISIBLE);
                        edtListDescribe.setVisibility(View.VISIBLE);
                        btnCancel.setVisibility(View.VISIBLE);
                        btnAdd.setVisibility(View.VISIBLE);
                        btnScanBarcode.setVisibility(View.GONE);
                    }

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

            downLoading();

            if(tvName.getText().toString().equals("") || tvName.getText().toString() == null ||
                    edtAmount.getText().toString().equals("") || edtAmount.getText().toString() == null ||
                    edtHard.getText().toString().equals("") || edtHard.getText().toString() == null ||
                    edtSoft.getText().toString().equals("") || edtSoft.getText().toString() == null){

                Toast.makeText(getContext(), "Please complete all information", Toast.LENGTH_SHORT).show();
                finishDownloading();
                return;
            }

            String name = tvName.getText().toString();
            long amount = Long.parseLong(edtAmount.getText().toString());
            String comment = edtListDescribe.getText().toString();
            long hard = Long.parseLong(edtHard.getText().toString());
            long soft = Long.parseLong(edtSoft.getText().toString());

            if(soft <= hard){
                Toast.makeText(getContext(), "Number of soft have to be more than number of hard", Toast.LENGTH_SHORT).show();
                finishDownloading();
                return;
            }

            ItemInventory item = new ItemInventory(name, amount, comment,
                    photoUrl, unit, barcodeId, hard, soft, type);

            cItems.document(barcodeId).set(item).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(getActivity(), "Add Item Success", Toast.LENGTH_SHORT).show();
                        closeDialog();
                    }
                    else{
                        finishDownloading();
                        Log.d("TAG", "Cannot add Item");
                    }
                }
            });

        }
    };


    final EventListener<QuerySnapshot> itemListener = new EventListener<QuerySnapshot>() {
        @Override
        public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
            if (e != null) {
                Log.w("TAG", "listen:error", e);
                return;
            }


            for (DocumentChange dc : documentSnapshots.getDocumentChanges()) {
                switch (dc.getType()) {
                    case ADDED:
                        DocumentSnapshot documentSnapshot = dc.getDocument();
                        ItemInventory item = documentSnapshot.toObject(ItemInventory.class);
                        String id = documentSnapshot.getId();

                        //Add
                        ItemInventoryMap itemMap = new ItemInventoryMap(id, item);
                        itemInventoryManager.addItemInventory(itemMap);

                        //Toast.makeText(getContext(), "Added " + item.getName(), Toast.LENGTH_SHORT).show();

                        break;

                    case MODIFIED:
                        documentSnapshot = dc.getDocument();
                        ItemInventory update = documentSnapshot.toObject(ItemInventory.class);
                        id = documentSnapshot.getId();

                        int index = itemInventoryManager.getIndexByKey(id);
                        itemMap = itemInventoryManager.getItemInventory(index);

                        //Update
                        itemMap.setItemInventory(update);
                        itemInventoryManager.sortItem();

                        Toast.makeText(getContext(), "Update " + update.getName(), Toast.LENGTH_SHORT).show();
                        break;

                    case REMOVED:
                        documentSnapshot = dc.getDocument();
                        item = documentSnapshot.toObject(ItemInventory.class);
                        id = documentSnapshot.getId();

                        index = itemInventoryManager.getIndexByKey(id);
                        itemInventoryManager.removeItemInventory(index);

                        Toast.makeText(getContext(), "Remove " + item.getName(), Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }
    };


    /***********************************************************************************************
     ************************************* Inner class ********************************************
     ***********************************************************************************************/

}
