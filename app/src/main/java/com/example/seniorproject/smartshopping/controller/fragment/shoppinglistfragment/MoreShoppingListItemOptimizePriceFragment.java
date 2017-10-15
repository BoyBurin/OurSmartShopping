package com.example.seniorproject.smartshopping.controller.fragment.shoppinglistfragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.seniorproject.smartshopping.R;
import com.example.seniorproject.smartshopping.model.dao.ItemInventory;
import com.example.seniorproject.smartshopping.model.dao.ItemInventoryMap;
import com.example.seniorproject.smartshopping.model.dao.ItemShoppingList;
import com.example.seniorproject.smartshopping.model.dao.ProductCrowd;
import com.example.seniorproject.smartshopping.model.dao.ShoppingListMap;
import com.example.seniorproject.smartshopping.model.datatype.MutableInteger;
import com.example.seniorproject.smartshopping.model.manager.ItemShoppingListManager;
import com.example.seniorproject.smartshopping.model.manager.ProductCrowdManager;
import com.example.seniorproject.smartshopping.model.util.GenerateSubSet;
import com.example.seniorproject.smartshopping.view.adapter.ItemOptimizeAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class MoreShoppingListItemOptimizePriceFragment extends Fragment implements
        MoreShoppingListItemOptimizeFragment.OptimizePriceListener {

    /***********************************************************************************************
     ************************************* Variable class ********************************************
     ***********************************************************************************************/


    ShoppingListMap shoppingListMap;
    private ItemShoppingListManager itemShoppingListManager;
    private MutableInteger lastPositionInteger;
    private ItemOptimizeAdapter itemOptimizeAdapter;
    private ProductCrowdManager productCrowdManager;

    private ArrayList<String> stores;
    private ArrayList<Double> retailPrice;

    private TextView tvTotalPrice;
    private TextView tvSavePrice;
    private ListView listView;

    private DatabaseReference mDatabaseRef;

    private ArrayList<ProductCrowd> productCrowdArrayList;


    /***********************************************************************************************
     ************************************* Method class ********************************************
     ***********************************************************************************************/

    public MoreShoppingListItemOptimizePriceFragment() {
        super();
    }

    @SuppressWarnings("unused")
    public static MoreShoppingListItemOptimizePriceFragment newInstance(ShoppingListMap shoppingListMap) {
        MoreShoppingListItemOptimizePriceFragment fragment = new MoreShoppingListItemOptimizePriceFragment();
        Bundle args = new Bundle();
        args.putParcelable("shoppingListMap", shoppingListMap);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        shoppingListMap = getArguments().getParcelable("shoppingListMap");

        init(savedInstanceState);

        if (savedInstanceState != null)
            onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_more_shopping_list_optimize_price, container, false);
        initInstances(rootView, savedInstanceState);
        return rootView;
    }

    private void init(Bundle savedInstanceState) {
        productCrowdManager = new ProductCrowdManager();
        lastPositionInteger = new MutableInteger(-1);
        itemOptimizeAdapter = new ItemOptimizeAdapter(lastPositionInteger);
        itemShoppingListManager = new ItemShoppingListManager();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        productCrowdArrayList = new ArrayList<ProductCrowd>();
        mDatabaseRef.child("iteminshoppinglist").child(shoppingListMap.getId())
                .addChildEventListener(updateItemShoppingListListener);
        mDatabaseRef.child("storelist")
                .addValueEventListener(getStoreListener);

        stores = new ArrayList<String>();

        retailPrice = new ArrayList<Double>();

    }

    @SuppressWarnings("UnusedParameters")
    private void initInstances(View rootView, Bundle savedInstanceState) {
        tvTotalPrice = (TextView) rootView.findViewById(R.id.tvTotalPrice);
        tvSavePrice = (TextView) rootView.findViewById(R.id.tvSavePrice);

        tvTotalPrice.setVisibility(View.GONE);
        tvSavePrice.setVisibility(View.GONE);

        listView = (ListView) rootView.findViewById(R.id.listViewOptimizePrice);
        listView.setAdapter(itemOptimizeAdapter);
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

    final ChildEventListener updateItemShoppingListListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            final String itemInventoryID = dataSnapshot.getKey();
            final Long amount = dataSnapshot.getValue(Long.class);

            mDatabaseRef.child("iteminventory").child(itemInventoryID)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            ItemInventory itemInventory = dataSnapshot.getValue(ItemInventory.class);
                            final String itemName = itemInventory.getName();
                            ItemInventoryMap itemInventoryMap = new ItemInventoryMap(itemInventoryID, itemInventory);
                            final ItemShoppingList itemShoppingList = new ItemShoppingList(amount, itemInventoryMap);


                            itemShoppingListManager.addItemShoppingList(itemShoppingList);

                            mDatabaseRef.child("productlist").child(itemShoppingList.getItemInventoryMap().getItemInventory().getBarcodeId())
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            retailPrice.add(dataSnapshot.child("retailprice").getValue(Double.class));
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });

                            mDatabaseRef.child("storelist")
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            for (DataSnapshot data : dataSnapshot.getChildren()) {
                                                String storeKey = data.getKey();
                                                final String storeName = data.child("name").getValue(String.class);

                                                Log.d("tag: ", storeKey + "   " + storeName);

                                                final String itemName = itemShoppingList.getItemInventoryMap().getItemInventory().getName();
                                                final String itemBarcode = itemShoppingList.getItemInventoryMap().getItemInventory().getBarcodeId();
                                                Log.d("tag: ", itemName + "   " + itemBarcode);

                                                mDatabaseRef.child("productinstore").child(storeKey)
                                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                                for (DataSnapshot data : dataSnapshot.getChildren()) {
                                                                    String key = "" + data.child("barcode").getValue(Long.class);
                                                                    double price = data.child("price").getValue(Double.class);
                                                                    if (!itemBarcode.equals(key)) {
                                                                        continue;
                                                                    }
                                                                    ProductCrowd productCrowd = new ProductCrowd(itemName, price, storeName);
                                                                    productCrowdManager.addProductCrowd(productCrowd);

                                                                }

                                                            }

                                                            @Override
                                                            public void onCancelled(DatabaseError databaseError) {
                                                                Log.d("tag: ", "Finish");

                                                            }
                                                        });

                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {

            String itemInventoryID = dataSnapshot.getKey();
            int index = itemShoppingListManager.getIndexByKey(itemInventoryID);

            for (int i = 0; i < productCrowdManager.getProductCrowds().size(); i++) {
                ProductCrowd productCrowd = productCrowdManager.getProductCrowds().get(i);

                String nameItem = itemShoppingListManager.getItemShoppingLists()
                        .get(index).getItemInventoryMap().getItemInventory().getName();

                if (productCrowd.getName().equals(nameItem)) {
                    productCrowdManager.getProductCrowds().remove(i);
                }
            }

            itemShoppingListManager.getItemShoppingLists().remove(index);


        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    final ValueEventListener getStoreListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            for (DataSnapshot data : dataSnapshot.getChildren()) {
                stores.add(data.child("name").getValue(String.class));
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };


    /***********************************************************************************************
     ************************************* Implementation ********************************************
     ***********************************************************************************************/


    @Override
    public void startOptimizePrice() {
        Log.d("Tag", "Hello Price");

        ProductCrowdManager optimizeProductCrowd = new ProductCrowdManager();
        ArrayList<ProductCrowdManager> allStore = new ArrayList<ProductCrowdManager>();
        double[] prices = new double[stores.size()];

        for (int i = 0; i < stores.size(); i++) {
            prices[i] = 0;
            ProductCrowdManager productStore = new ProductCrowdManager();
            for (ProductCrowd p : productCrowdManager.getProductCrowds()) {
                if (p.getStore().equals(stores.get(i))) {
                    productStore.addProductCrowd(p);
                    prices[i] += p.getPrice();
                }
            }
            allStore.add(productStore);

        }

        ArrayList<ProductCrowd> optimize = new ArrayList<ProductCrowd>();
        ArrayList<ArrayList<ProductCrowd>> allSubsets = new ArrayList<ArrayList<ProductCrowd>>();

        for (ProductCrowdManager productCrowdManager : allStore) {

            for (ArrayList<ProductCrowd> product : GenerateSubSet.powerSet(productCrowdManager.getProductCrowds())) {

                allSubsets.add(product);
            }
        }
        for (int i = 0; i < allSubsets.size(); i++) {
            if (allSubsets.get(i).size() == 0) allSubsets.remove(i);

        }

        Log.d("Size set", allSubsets.size() + "");

        ArrayList<ItemShoppingList> list = itemShoppingListManager.getItemShoppingLists();

        double ratio = Double.MAX_VALUE;
        double minRatio = Double.MAX_VALUE;
        int index = -1;
        while (optimize.size() != list.size()) {
            for (int i = 0; i < allSubsets.size(); i++) {
                ArrayList<ProductCrowd> current = allSubsets.get(i);
                double cost = 0;

                for (int j = 0; j < current.size(); j++) {
                    cost += current.get(j).getPrice();
                }

                Log.d("Cost: ", "" + cost);
                int subtract = current.size();
                Log.d("s: ", "" + subtract);
                for (int j = 0; j < current.size(); j++) {
                    for (int k = 0; k < optimize.size(); k++) {
                        if (current.get(j).getName().equals(optimize.get(k).getName())) subtract--;
                        //System.out.println("s: " + subtract);
                    }
                }
                Log.d("s: ", "" + subtract);

                if (subtract == 0) continue;
                ratio = cost / subtract;

                if (minRatio > ratio) {
                    minRatio = ratio;
                    index = i;
                }


            }

            System.out.println("-------------index: " + index);

            for (int i = 0; i < allSubsets.get(index).size(); i++) {
                ProductCrowd value = allSubsets.get(index).get(i);
                if (optimize.contains(value)) continue;
                optimize.add(value);
            }
            minRatio = Double.MAX_VALUE;
            index = -1;
        }

        double totalPrice = 0;
        for (ProductCrowd productCrowd : optimize) {

            totalPrice += productCrowd.getPrice();
            Log.d("Hello: ", productCrowd.getName());
        }

        tvTotalPrice.setVisibility(View.VISIBLE);
        tvTotalPrice.setText("ราคารวม:  " + totalPrice + "    บาท");

        double retail = 0;
        for(int i = 0 ; i < retailPrice.size(); i++){
            retail += retailPrice.get(i);
        }

        Log.d("Retail: ", retail + "");
        long percent = Math.round(((retail - totalPrice) / retail) * 100);
        tvSavePrice.setVisibility(View.VISIBLE);
        tvSavePrice.setText("ประหยัด:  " + percent + "    %");

        itemOptimizeAdapter.setItemShoppingLists(optimize);
        itemOptimizeAdapter.notifyDataSetChanged();

    }


    /***********************************************************************************************
     ************************************* Inner class ********************************************
     ***********************************************************************************************/

}
