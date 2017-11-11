package com.example.seniorproject.smartshopping.controller.fragment.purchaseitem;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.seniorproject.smartshopping.R;
import com.example.seniorproject.smartshopping.model.dao.iteminventory.ItemInventory;
import com.example.seniorproject.smartshopping.model.dao.itemocr.ItemOCR;
import com.example.seniorproject.smartshopping.model.dao.itemocr.PurchaseItemWithAction;
import com.example.seniorproject.smartshopping.model.daorecyclerview.purchaseitem.BasePurchaseItem;
import com.example.seniorproject.smartshopping.model.daorecyclerview.purchaseitem.PurchaseItemCreator;
import com.example.seniorproject.smartshopping.model.manager.itemocr.ItemOCRManager;
import com.example.seniorproject.smartshopping.model.manager.itemocr.PurchaseItemWithActionManager;
import com.example.seniorproject.smartshopping.view.recyclerviewadapter.PurchaseItemRecyclerViewAdapter;

import java.util.ArrayList;


public class PurchaseItemOCRFragment extends Fragment implements PurchaseItemAddFragment.AddPurchaseItemInterface {

    /***********************************************************************************************
     ************************************* Variable class ********************************************
     ***********************************************************************************************/


    private RecyclerView recyclerView;
    //private ItemOCRManager itemOCRManager;
    private PurchaseItemWithActionManager purchaseItemWithActionManager;
    private PurchaseItemCreator purchaseItemCreator;
    private String storeName;
    private PurchaseItemRecyclerViewAdapter purchaseItemRecyclerViewAdapter;

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
        for(int i = 0 ; i < item.size() ; i++){
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
    }

    @SuppressWarnings("UnusedParameters")
    private void initInstances(View rootView, Bundle savedInstanceState) {
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

    public void setInterface(){
        ArrayList<BasePurchaseItem> basePurchaseItems = new ArrayList<>();

        basePurchaseItems.add(purchaseItemCreator.createStoreName(storeName));

        double totalPrice = 0;
        for(PurchaseItemWithAction purchaseItemWithAction : purchaseItemWithActionManager.getIPurchaseItemWithActions()){
            totalPrice += purchaseItemWithAction.getItemOCR().getPrice();
        }

        basePurchaseItems.add(purchaseItemCreator.createTotalPrice(totalPrice));
        //basePurchaseItems.add(purchaseItemCreator.createSaveButton());
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
            PurchaseItemAddFragment purchaseItemAddFragment = PurchaseItemAddFragment.newInstance();
            getActivity().getSupportFragmentManager().beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .add(((ViewGroup)getView().getParent()).getId(), purchaseItemAddFragment)
                    .hide(PurchaseItemOCRFragment.this)
                    .commit();
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
