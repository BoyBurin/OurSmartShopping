package com.example.seniorproject.smartshopping.controller.fragment.purchaseitem;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.seniorproject.smartshopping.R;
import com.example.seniorproject.smartshopping.model.dao.iteminventory.ItemInventory;
import com.example.seniorproject.smartshopping.model.dao.itemocr.ItemOCR;
import com.example.seniorproject.smartshopping.model.dao.itemocr.PurchaseItemWithAction;
import com.example.seniorproject.smartshopping.model.dao.productstore.ProductCrowd;
import com.example.seniorproject.smartshopping.model.daorecyclerview.purchaseitem.BasePurchaseItem;
import com.example.seniorproject.smartshopping.model.daorecyclerview.purchaseitem.PurchaseItemCreator;
import com.example.seniorproject.smartshopping.model.manager.group.GroupManager;
import com.example.seniorproject.smartshopping.model.manager.itemocr.ItemOCRManager;
import com.example.seniorproject.smartshopping.model.manager.itemocr.PurchaseItemWithActionManager;
import com.example.seniorproject.smartshopping.view.recyclerviewadapter.PurchaseItemRecyclerViewAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class PurchaseItemOCRFragment extends Fragment implements PurchaseItemAddFragment.AddPurchaseItemInterface {

    /***********************************************************************************************
     ************************************* Variable class ********************************************
     ***********************************************************************************************/


    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    //private ItemOCRManager itemOCRManager;
    private PurchaseItemWithActionManager purchaseItemWithActionManager;
    private PurchaseItemCreator purchaseItemCreator;
    private String storeName;
    private PurchaseItemRecyclerViewAdapter purchaseItemRecyclerViewAdapter;

    private FirebaseFirestore db;

    /***********************************************************************************************
     ************************************* Method class ********************************************
     ***********************************************************************************************/

    public PurchaseItemOCRFragment() {
        super();
    }

    @SuppressWarnings("unused")
    public static PurchaseItemOCRFragment newInstance(ArrayList<ItemOCR> itemOCRs, String storeName) {
        PurchaseItemOCRFragment fragment = new PurchaseItemOCRFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList("itemOCRs", itemOCRs);
        args.putString("storeName", storeName);
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
        View rootView = inflater.inflate(R.layout.fragment_main_purchase_item_ocr, container, false);
        initInstances(rootView, savedInstanceState);
        return rootView;
    }

    private void init(Bundle savedInstanceState) {
        //itemOCRManager = new ItemOCRManager();
        purchaseItemWithActionManager = new PurchaseItemWithActionManager();

        ArrayList<Parcelable> item = getArguments().getParcelableArrayList("itemOCRs");
        for (int i = 0; i < item.size(); i++) {
            final ItemOCR purchasetem = (ItemOCR) item.get(i);
            View.OnClickListener deleteListener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String barcodeId = purchasetem.getItemInventoryMap().getItemInventory().getBarcodeId();
                    purchaseItemWithActionManager.removePurchaseItemWithAction(barcodeId);
                    setInterface();
                }
            };

            PurchaseItemWithAction purchaseItemWithAction = new PurchaseItemWithAction(purchasetem, deleteListener);
            purchaseItemWithActionManager.addPurchaseItemWithAction(purchaseItemWithAction);
        }

        storeName = getArguments().getString("storeName");
        purchaseItemCreator = new PurchaseItemCreator();
        purchaseItemRecyclerViewAdapter = new PurchaseItemRecyclerViewAdapter(getContext());

        db = FirebaseFirestore.getInstance();
    }

    @SuppressWarnings("UnusedParameters")
    private void initInstances(View rootView, Bundle savedInstanceState) {
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);

        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
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

    public void setInterface() {
        ArrayList<BasePurchaseItem> basePurchaseItems = new ArrayList<>();

        basePurchaseItems.add(purchaseItemCreator.createStoreName(storeName));

        double totalPrice = 0;
        for (PurchaseItemWithAction purchaseItemWithAction : purchaseItemWithActionManager.getIPurchaseItemWithActions()) {
            totalPrice += purchaseItemWithAction.getItemOCR().getPrice();
        }

        basePurchaseItems.add(purchaseItemCreator.createTotalPrice(totalPrice));
        basePurchaseItems.add(purchaseItemCreator.createSaveButton(saveInfoListener));
        basePurchaseItems.addAll(purchaseItemCreator.createPurchaseItems(purchaseItemWithActionManager.getIPurchaseItemWithActions()));
        basePurchaseItems.add(purchaseItemCreator.createAddButton(addPurchaseItem));

        purchaseItemRecyclerViewAdapter.setPurchaseItems(basePurchaseItems);
        purchaseItemRecyclerViewAdapter.notifyDataSetChanged();
    }

    private double getTotalRetailPrice(ArrayList<PurchaseItemWithAction> purchaseItemWithActions) {
        double totalRetailPrice = 0;
        for (PurchaseItemWithAction purchaseItemWithAction : purchaseItemWithActions) {
            ItemInventory itemInventory = purchaseItemWithAction.getItemOCR().getItemInventoryMap().getItemInventory();

            double retailPrice = itemInventory.getRetailPrice() * purchaseItemWithAction.getItemOCR().getAmount();
            totalRetailPrice += retailPrice;
        }

        return totalRetailPrice;
    }

    private double getTotalPrice(ArrayList<PurchaseItemWithAction> purchaseItemWithActions) {
        double totalPrice = 0;
        for (PurchaseItemWithAction purchaseItemWithAction : purchaseItemWithActions) {
            ItemOCR itemOCR = purchaseItemWithAction.getItemOCR();

            double price = itemOCR.getPrice();
            totalPrice += price;
        }

        return totalPrice;
    }

    /***********************************************************************************************
     ************************************* Listener variables ********************************************
     ***********************************************************************************************/

    View.OnClickListener addPurchaseItem = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            PurchaseItemAddFragment purchaseItemAddFragment = PurchaseItemAddFragment.newInstance(purchaseItemWithActionManager
                    .getIPurchaseItemWithActions());
            getActivity().getSupportFragmentManager().beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .add(((ViewGroup) getView().getParent()).getId(), purchaseItemAddFragment)
                    .hide(PurchaseItemOCRFragment.this)
                    .commit();
        }
    };

    View.OnClickListener saveInfoListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (purchaseItemWithActionManager.getSize() == 0) {
                Toast.makeText(getContext(), "Please select Item", Toast.LENGTH_SHORT).show();
                return;
            }
            recyclerView.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            db.runTransaction(new Transaction.Function<Void>() {
                @Override
                public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                    CollectionReference storeRef = db.collection("stores").document(storeName)
                            .collection("products");
                    CollectionReference itemRef = db.collection("groups").document(GroupManager.getInstance().getCurrentGroup().getId())
                            .collection("items");

                    long[] amountSet = new long[purchaseItemWithActionManager.getIPurchaseItemWithActions().size()];
                    double[] priceSet = new double[purchaseItemWithActionManager.getIPurchaseItemWithActions().size()];

                    for (int i = 0; i < purchaseItemWithActionManager.getIPurchaseItemWithActions().size(); i++) {
                        ItemOCR itemOCR = purchaseItemWithActionManager.getIPurchaseItemWithActions().get(i).getItemOCR();
                        priceSet[i] = itemOCR.getPrice();
                        amountSet[i] = itemOCR.getAmount();
                        priceSet[i] = priceSet[i] / amountSet[i];
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
                        update.put("store", storeName);
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

                    double totalRetailPrice = getTotalRetailPrice(purchaseItemWithActionManager.getIPurchaseItemWithActions());
                    double totalPrice = getTotalPrice(purchaseItemWithActionManager.getIPurchaseItemWithActions());

                    Calendar calendar = Calendar.getInstance();
                    int year = calendar.get(Calendar.YEAR);
                    int month = calendar.get(Calendar.MONTH) + 1;
                    int day = calendar.get(Calendar.DAY_OF_MONTH);

                    int hour = calendar.get(Calendar.HOUR_OF_DAY);
                    int minute = calendar.get(Calendar.MINUTE);
                    int second = calendar.get(Calendar.SECOND);

                    WriteBatch batch = db.batch();

                    HashMap<String, Object> data = new HashMap<String, Object>();
                    data.put("totalRetailPrice", totalRetailPrice);
                    data.put("totalPrice", totalPrice);
                    data.put("date", year + "/" + month + "/" + day);
                    data.put("time", hour + "." + minute + "." + second);
                    data.put("store", storeName);

                    batch.set(db.collection("groups").document(GroupManager.getInstance().getCurrentGroup().getId())
                            .collection("history").document(year + "" + month + "" + day + hour + "" + minute + "" + second), data);

                    for (PurchaseItemWithAction purchaseItemWithAction : purchaseItemWithActionManager.getIPurchaseItemWithActions()) {
                        long amount = purchaseItemWithAction.getItemOCR().getAmount();
                        double price = purchaseItemWithAction.getItemOCR().getPrice();
                        String name = purchaseItemWithAction.getItemOCR().getItemInventoryMap().getItemInventory().getName();

                        HashMap<String, Object> newData = new HashMap<String, Object>();
                        newData.put("amount", amount);
                        newData.put("price", price);
                        newData.put("name", name);

                        batch.set(db.collection("groups").document(GroupManager.getInstance().getCurrentGroup().getId())
                                .collection("history").document(year + "" + month + "" + day + hour + "" + minute + "" + second)
                                .collection("purchaseitems").document(name), newData);
                    }

                    batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(getContext(), "Save Successful", Toast.LENGTH_SHORT).show();
                            getActivity().finish();
                        }
                    });
                    //Toast.makeText(getContext(), "Save Successful", Toast.LENGTH_SHORT).show();
                    //getActivity().finish();
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
