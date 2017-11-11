package com.example.seniorproject.smartshopping.controller.fragment.purchaseitem;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.seniorproject.smartshopping.R;
import com.example.seniorproject.smartshopping.model.dao.iteminventory.ItemInventory;
import com.example.seniorproject.smartshopping.model.dao.itemocr.ItemOCR;
import com.example.seniorproject.smartshopping.model.dao.itemocr.PurchaseItemWithAction;
import com.example.seniorproject.smartshopping.model.daorecyclerview.purchaseitem.BasePurchaseItem;
import com.example.seniorproject.smartshopping.model.daorecyclerview.purchaseitem.PurchaseItemCreator;
import com.example.seniorproject.smartshopping.model.manager.itemocr.PurchaseItemWithActionManager;
import com.example.seniorproject.smartshopping.view.recyclerviewadapter.PurchaseItemRecyclerViewAdapter;

import java.util.ArrayList;


public class PurchaseItemManuallyFragment extends Fragment implements PurchaseItemAddFragment.AddPurchaseItemInterface{

    /***********************************************************************************************
     ************************************* Variable class ********************************************
     ***********************************************************************************************/


    private Spinner spinnerStore;
    private RecyclerView recyclerView;

    private PurchaseItemWithActionManager purchaseItemWithActionManager;
    private PurchaseItemCreator purchaseItemCreator;
    private PurchaseItemRecyclerViewAdapter purchaseItemRecyclerViewAdapter;

    private ArrayAdapter<String> adapter;
    private String currentStore;


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

        currentStore = "Big C Bangpakok";
        String[] stores = {"Big C Bangpakok", "Max Value Pracha Uthit", "Tesco Lotus Bangpakok", "Tesco Lotus ตลาดโลตัสประชาอุทิศ"};
        adapter = new ArrayAdapter<String>(getActivity(),  android.R.layout.simple_spinner_item, stores);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

    }

    @SuppressWarnings("UnusedParameters")
    private void initInstances(View rootView, Bundle savedInstanceState) {

        /*spinnerStore = (Spinner) rootView.findViewById(R.id.spinnerStore);
        spinnerStore.setAdapter(adapter);*/
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
                    .hide(PurchaseItemManuallyFragment.this)
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
