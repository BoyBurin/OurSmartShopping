package com.example.seniorproject.smartshopping.controller.fragment.purchaseitem;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.seniorproject.smartshopping.R;
import com.example.seniorproject.smartshopping.model.dao.iteminventory.ItemInventory;
import com.example.seniorproject.smartshopping.model.dao.itemocr.ItemOCR;
import com.example.seniorproject.smartshopping.model.dao.itemocr.PurchaseItemWithAction;
import com.example.seniorproject.smartshopping.model.dao.productstore.ProductCrowd;
import com.example.seniorproject.smartshopping.model.daorecyclerview.purchaseitem.BasePurchaseItem;
import com.example.seniorproject.smartshopping.model.daorecyclerview.purchaseitem.PurchaseItemCreator;
import com.example.seniorproject.smartshopping.model.manager.group.GroupManager;
import com.example.seniorproject.smartshopping.model.manager.itemocr.PurchaseItemWithActionManager;
import com.example.seniorproject.smartshopping.view.recyclerviewadapter.PurchaseItemRecyclerViewAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class PurchaseItemManuallyFragment extends Fragment implements PurchaseItemAddFragment.AddPurchaseItemInterface{

    /***********************************************************************************************
     ************************************* Variable class ********************************************
     ***********************************************************************************************/


    private Spinner spinnerStore;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;

    private PurchaseItemWithActionManager purchaseItemWithActionManager;
    private PurchaseItemCreator purchaseItemCreator;
    private PurchaseItemRecyclerViewAdapter purchaseItemRecyclerViewAdapter;

    private ArrayAdapter<String> adapter;
    private String currentStore;

    private FirebaseFirestore db;


    /***********************************************************************************************
     ************************************* Method class ********************************************
     ***********************************************************************************************/

    public PurchaseItemManuallyFragment() {
        super();
    }

    @SuppressWarnings("unused")
    public static PurchaseItemManuallyFragment newInstance() {
        PurchaseItemManuallyFragment fragment = new PurchaseItemManuallyFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_main_purchase_item_manually, container, false);
        initInstances(rootView, savedInstanceState);
        return rootView;
    }

    private void init(Bundle savedInstanceState) {
        purchaseItemWithActionManager = new PurchaseItemWithActionManager();
        purchaseItemCreator = new PurchaseItemCreator();
        purchaseItemRecyclerViewAdapter = new PurchaseItemRecyclerViewAdapter(getContext());

        currentStore = "บิ๊กซี พระราม 2";
        String[] stores = {"บิ๊กซี พระราม 2", "Max Value Pracha Uthit", "Tesco Lotus Bangpakok", "Tesco Lotus ตลาดโลตัสประชาอุทิศ"};
        adapter = new ArrayAdapter<String>(getActivity(),  android.R.layout.simple_spinner_item, stores);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        db = FirebaseFirestore.getInstance();

    }

    @SuppressWarnings("UnusedParameters")
    private void initInstances(View rootView, Bundle savedInstanceState) {

        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        spinnerStore = (Spinner) rootView.findViewById(R.id.spinner);
        spinnerStore.setAdapter(adapter);
        spinnerStore.setOnItemSelectedListener(storeSelectedLister);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(purchaseItemRecyclerViewAdapter);
        setInterface();
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

    private void setInterface(){
        ArrayList<BasePurchaseItem> basePurchaseItems = new ArrayList<>();

        //basePurchaseItems.add(purchaseItemCreator.createStoreName(storeName));

        double totalPrice = 0;
        for(PurchaseItemWithAction purchaseItemWithAction : purchaseItemWithActionManager.getIPurchaseItemWithActions()){
            totalPrice += purchaseItemWithAction.getItemOCR().getPrice();
        }

        basePurchaseItems.add(purchaseItemCreator.createTotalPrice(totalPrice));
        basePurchaseItems.add(purchaseItemCreator.createSaveButton(saveInfoListener));
        basePurchaseItems.addAll(purchaseItemCreator.createPurchaseItems(purchaseItemWithActionManager.getIPurchaseItemWithActions()));
        basePurchaseItems.add(purchaseItemCreator.createAddButton(addPurchaseItem));

        purchaseItemRecyclerViewAdapter.setPurchaseItems(basePurchaseItems);
        purchaseItemRecyclerViewAdapter.notifyDataSetChanged();
    }

    /***********************************************************************************************
     ************************************* Listener variables ********************************************
     ***********************************************************************************************/

    View.OnClickListener addPurchaseItem = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            PurchaseItemAddFragment purchaseItemAddFragment = PurchaseItemAddFragment.newInstance(purchaseItemWithActionManager.getIPurchaseItemWithActions());
            getActivity().getSupportFragmentManager().beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .add(((ViewGroup)getView().getParent()).getId(), purchaseItemAddFragment)
                    .hide(PurchaseItemManuallyFragment.this)
                    .commit();
        }
    };

    View.OnClickListener saveInfoListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(purchaseItemWithActionManager.getSize() == 0){
                Toast.makeText(getContext(), "Please select Item", Toast.LENGTH_SHORT).show();
                return;
            }
            recyclerView.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            db.runTransaction(new Transaction.Function<Void>() {
                @Override
                public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                    CollectionReference storeRef = db.collection("stores").document(currentStore)
                            .collection("products");
                    CollectionReference itemRef = db.collection("groups").document(GroupManager.getInstance().getCurrentGroup().getId())
                            .collection("items");

                    long[] amountSet = new long[purchaseItemWithActionManager.getIPurchaseItemWithActions().size()];
                    double[] priceSet = new double[purchaseItemWithActionManager.getIPurchaseItemWithActions().size()];

                    for (int i = 0; i < purchaseItemWithActionManager.getIPurchaseItemWithActions().size(); i++) {
                        ItemOCR itemOCR = purchaseItemWithActionManager.getIPurchaseItemWithActions().get(i).getItemOCR();
                        priceSet[i] = itemOCR.getPrice();
                        amountSet[i] = itemOCR.getAmount();
                        priceSet[i] = priceSet[i]/amountSet[i];
                    }

                    ArrayList<ProductCrowd> products = new ArrayList<ProductCrowd>();
                    for (int i = 0; i < purchaseItemWithActionManager.getIPurchaseItemWithActions().size(); i++) {
                        String itemInventoryID = purchaseItemWithActionManager.getIPurchaseItemWithActions().get(i)
                                .getItemOCR().getItemInventoryMap().getId();
                        ProductCrowd newData = transaction.get(storeRef.document(itemInventoryID)).toObject(ProductCrowd.class);
                        products.add(newData);
                    }

                    ArrayList<ItemInventory> itemInventories = new ArrayList<ItemInventory>();
                    double[] updateAmount = new double[purchaseItemWithActionManager.getIPurchaseItemWithActions().size()];
                    for (int i = 0; i < purchaseItemWithActionManager.getIPurchaseItemWithActions().size(); i++) {
                        String itemInventoryID = purchaseItemWithActionManager.getIPurchaseItemWithActions().get(i)
                                .getItemOCR().getItemInventoryMap().getId();
                        ItemInventory newData = transaction.get(itemRef.document(itemInventoryID)).toObject(ItemInventory.class);
                        itemInventories.add(newData);
                        updateAmount[i] = newData.getAmount() + amountSet[i];
                    }

                    for (int i = 0; i < purchaseItemWithActionManager.getIPurchaseItemWithActions().size(); i++) {
                        String itemInventoryID = purchaseItemWithActionManager.getIPurchaseItemWithActions().get(i)
                                .getItemOCR().getItemInventoryMap().getId();
                        String name = purchaseItemWithActionManager.getIPurchaseItemWithActions().get(i)
                                .getItemOCR().getItemInventoryMap().getItemInventory().getName();
                        Map<String, Object> update = new HashMap<>();
                        update.put("price", priceSet[i]);
                        update.put("name", name);
                        update.put("store", currentStore);
                        transaction.set(storeRef.document(itemInventoryID), update);
                    }

                    for (int i = 0; i < purchaseItemWithActionManager.getIPurchaseItemWithActions().size(); i++) {
                        String itemInventoryID = purchaseItemWithActionManager.getIPurchaseItemWithActions().get(i)
                                .getItemOCR().getItemInventoryMap().getId();
                        Map<String, Object> update = new HashMap<>();
                        update.put("amount", updateAmount[i]);
                        transaction.update(itemRef.document(itemInventoryID), update);
                    }

                    return null;
                }
            }).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d("TAG", "Transaction success!");
                    Toast.makeText(getContext(), "Save Successful", Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressBar.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                            Log.w("TAG", "Transaction failure.", e);
                        }
                    });
        }
    };


    final AdapterView.OnItemSelectedListener storeSelectedLister = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
            switch (position) {
                case 0:
                    currentStore = "บิ๊กซี พระราม 2";
                    break;
                case 1:
                    currentStore = "Max Value Pracha Uthit";
                    break;
                case 2:
                    currentStore = "Tesco Lotus Bangpakok";
                    break;
                case 3:
                    currentStore = "Tesco Lotus ตลาดโลตัสประชาอุทิศ";
                    break;
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };


    /***********************************************************************************************
     ************************************* Inner class ********************************************
     ***********************************************************************************************/



    /***********************************************************************************************
     ************************************* Implementation ********************************************
     ***********************************************************************************************/
    @Override
    public void addPurchaseItem(ItemOCR purchaseItem) {
        final ItemInventory itemInventory = purchaseItem.getItemInventoryMap().getItemInventory();
        View.OnClickListener deleteListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String barcodeId = itemInventory.getBarcodeId();
                purchaseItemWithActionManager.removePurchaseItemWithAction(barcodeId);
                setInterface();
            }
        };

        PurchaseItemWithAction purchaseItemWithAction = new PurchaseItemWithAction();

        purchaseItemWithAction.setItemOCR(purchaseItem);
        purchaseItemWithAction.setDelete(deleteListener);
        purchaseItemWithActionManager.addPurchaseItemWithAction(purchaseItemWithAction);

        setInterface();
    }

}
