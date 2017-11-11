package com.example.seniorproject.smartshopping.controller.fragment.shoppinglistfragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.seniorproject.smartshopping.R;
import com.example.seniorproject.smartshopping.model.dao.shoppinglist.ItemShoppingList;
import com.example.seniorproject.smartshopping.model.dao.productstore.ProductCrowd;
import com.example.seniorproject.smartshopping.model.dao.shoppinglist.ShoppingListMap;
import com.example.seniorproject.smartshopping.model.daorecyclerview.shoppinglistitemoptimize.BaseItemOptimize;
import com.example.seniorproject.smartshopping.model.daorecyclerview.shoppinglistitemoptimize.ItemOptimizeCreator;
import com.example.seniorproject.smartshopping.model.manager.group.GroupManager;
import com.example.seniorproject.smartshopping.model.manager.shoppinglist.ItemShoppingListManager;
import com.example.seniorproject.smartshopping.model.manager.itemocr.ProductCrowdManager;
import com.example.seniorproject.smartshopping.view.recyclerviewadapter.ShoppingListItemOptimizeRecyclerViewAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class MoreShoppingListItemOptimizeTimeFragment extends Fragment  {

    /***********************************************************************************************
     ************************************* Interface ********************************************
     ***********************************************************************************************/

    interface BackgroundLoadingOptimizeTime{
        public void closeBackgroubdLoadingTime();
    }

    /***********************************************************************************************
     ************************************* Variable class ********************************************
     ***********************************************************************************************/


    ShoppingListMap shoppingListMap;
    private ItemShoppingListManager itemShoppingListManager;
    private ProductCrowdManager productCrowdManager;
    private ShoppingListItemOptimizeRecyclerViewAdapter shoppingListItemOptimizeRecyclerViewAdapter;
    private ItemOptimizeCreator itemOptimizeCreator;

    private ArrayList<String> stores;
    private ArrayList<Double> retailPrice;

    private RecyclerView recyclerView;

    private FirebaseFirestore db;
    private CollectionReference cItemsShoppingList;
    private CollectionReference cProductList;


    /***********************************************************************************************
     ************************************* Method class ********************************************
     ***********************************************************************************************/

    public MoreShoppingListItemOptimizeTimeFragment() {
        super();
    }

    @SuppressWarnings("unused")
    public static MoreShoppingListItemOptimizeTimeFragment newInstance(ShoppingListMap shoppingListMap) {
        MoreShoppingListItemOptimizeTimeFragment fragment = new MoreShoppingListItemOptimizeTimeFragment();
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
        shoppingListItemOptimizeRecyclerViewAdapter = new ShoppingListItemOptimizeRecyclerViewAdapter(getContext());
        itemOptimizeCreator = new ItemOptimizeCreator();

        db = FirebaseFirestore.getInstance();
        cProductList = db.collection("productlist");
        cItemsShoppingList = db.collection("groups").document(GroupManager.getInstance().getCurrentGroup().getId())
                .collection("shoppinglists").document(shoppingListMap.getId())
                .collection("items");


        setStores();

    }

    @SuppressWarnings("UnusedParameters")
    private void initInstances(View rootView, Bundle savedInstanceState) {
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);

        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(shoppingListItemOptimizeRecyclerViewAdapter);

        startOptimizeTime();

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

    private void setStores(){
        stores = new ArrayList<String>();
        stores.add("Big C Bangpakok");
        stores.add("Max Value Pracha Uthit");
        stores.add("Tesco Lotus Bangpakok");
        stores.add("Tesco Lotus ตลาดโลตัสประชาอุทิศ");
    }

    private void setInterface(ArrayList<ProductCrowd> productCrowds, double totalPrice, long savePrice){
        ArrayList<BaseItemOptimize> baseItemOptimizes = new ArrayList<>();

        baseItemOptimizes.add(itemOptimizeCreator.createTotalPrice(totalPrice));
        baseItemOptimizes.add(itemOptimizeCreator.createSavePrice(savePrice));
        baseItemOptimizes.addAll(itemOptimizeCreator.createItemOptimize(productCrowds));

        shoppingListItemOptimizeRecyclerViewAdapter.setPurchaseItems(baseItemOptimizes);
        shoppingListItemOptimizeRecyclerViewAdapter.notifyDataSetChanged();
    }

    final void doTransaction(){
        db.runTransaction(new Transaction.Function<Map<String, Object>>() {
            @Override
            public Map<String, Object> apply(Transaction transaction) throws FirebaseFirestoreException {

                Map<String, Object> data = bathReadProductListData(transaction);
                batchWriteProductListData(transaction);
                return data;
            }
        }).addOnSuccessListener(new OnSuccessListener<Map<String, Object>>() {
            @Override
            public void onSuccess(Map<String, Object> data) {
                Log.d("TAG", "Transaction success!");

                ArrayList<ProductCrowd> productCrowds = (ArrayList<ProductCrowd>) data.get("productStore");
                ArrayList<Double> retailPrices = (ArrayList<Double>) data.get("retailPrice");

                for(ProductCrowd productStore : productCrowds){
                    productCrowdManager.addProductCrowd(productStore);
                }

                for(Double retail : retailPrices){
                    retailPrice.add(retail);
                }

                optimizeTime();
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("TAG", "Transaction failure.", e);
                    }
                });
    }

    private Map<String, Object> bathReadProductListData(Transaction transaction) throws FirebaseFirestoreException {

        ArrayList<ProductCrowd> productStores = new ArrayList<ProductCrowd>();

        for(String store : stores) {

            CollectionReference cProductStore = db.collection("stores").document(store)
                    .collection("products");

            for(ItemShoppingList itemShoppingList : itemShoppingListManager.getItemShoppingLists()) {
                String barcodeId = itemShoppingList.getBarcodeId();
                ProductCrowd productStore = transaction.get(cProductStore.document(barcodeId)).toObject(ProductCrowd.class);
                productStores.add(productStore);
            }
        }

        ArrayList<Double> retailPrices = new ArrayList<Double>();

        for(ItemShoppingList itemShoppingList : itemShoppingListManager.getItemShoppingLists()){
            String barcodeId = itemShoppingList.getBarcodeId();
            double retail = transaction.get(db.collection("productlist").document(barcodeId)).getDouble("retailprice");
            retailPrices.add(retail);
        }

        Map<String, Object> data = new HashMap<>();
        data.put("productStore", productStores);
        data.put("retailPrice", retailPrices);

        return data;
    }

    private void batchWriteProductListData(Transaction transaction){
        for(String store : stores) {

            CollectionReference cProductStore = db.collection("stores").document(store)
                    .collection("products");

            for(ItemShoppingList itemShoppingList : itemShoppingListManager.getItemShoppingLists()) {
                String barcodeId = itemShoppingList.getBarcodeId();
                Map<String, Object> update = new HashMap<>();
                update.put("name", itemShoppingList.getName());
                transaction.update(cProductStore.document(barcodeId), update);
            }
        }


        for(ItemShoppingList itemShoppingList : itemShoppingListManager.getItemShoppingLists()){
            String barcodeId = itemShoppingList.getBarcodeId();
            Map<String, Object> update = new HashMap<>();
            update.put("barcodeId", barcodeId);
            transaction.update(db.collection("productlist").document(barcodeId), update);
        }
    }

    /***********************************************************************************************
     ************************************* Listener variables ********************************************
     ***********************************************************************************************/


    final OnCompleteListener<QuerySnapshot> getItemShoppingList = new OnCompleteListener<QuerySnapshot>() {
        @Override
        public void onComplete(@NonNull Task<QuerySnapshot> task) {
            if(task.isSuccessful()){
                for(DocumentSnapshot documentSnapshot : task.getResult()){
                    itemShoppingListManager.addItemShoppingList(documentSnapshot.toObject(ItemShoppingList.class));
                }
                doTransaction();

            }
            else{
                Log.d("TAG", "Error to get Shopping List Item");
            }
        }
    };

    /***********************************************************************************************
     ************************************* Implementation ********************************************
     ***********************************************************************************************/


    public void startOptimizeTime() {
        itemShoppingListManager = new ItemShoppingListManager();
        productCrowdManager = new ProductCrowdManager();
        retailPrice = new ArrayList<Double>();


        cItemsShoppingList.get().addOnCompleteListener(getItemShoppingList);
    }


    public void optimizeTime() {

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

        double priceMin = Double.MAX_VALUE;
        int index = -1;

        for (int i = 0; i < stores.size(); i++) {
            if (priceMin > prices[i]) {
                priceMin = prices[i];
                index = i;
            }
        }

        //tvTotalPrice.setVisibility(View.VISIBLE);
        //tvTotalPrice.setText("ราคารวม:  " + priceMin + "    บาท");


        for(ProductCrowd productCrowd : allStore.get(index).getProductCrowds()) {
            optimizeProductCrowd.addProductCrowd(productCrowd);
        }


        double retail = 0;
        for(int i = 0 ; i < retailPrice.size(); i++){
            retail += retailPrice.get(i);
        }

        long percent = Math.round(((retail - priceMin) / retail) * 100);
        //tvSavePrice.setVisibility(View.VISIBLE);
        //tvSavePrice.setText("ประหยัด:  " + percent + "    %");

        BackgroundLoadingOptimizeTime backgroundLoading = (BackgroundLoadingOptimizeTime) getParentFragment();

        backgroundLoading.closeBackgroubdLoadingTime();
        setInterface(optimizeProductCrowd.getProductCrowds(), priceMin, percent);

        for (String s : stores) {
            Log.d("tag: ", s);
        }
    }


    /***********************************************************************************************
     ************************************* Inner class ********************************************
     ***********************************************************************************************/

}
