package com.example.seniorproject.smartshopping.controller.fragment.shoppinglistfragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.seniorproject.smartshopping.R;
import com.example.seniorproject.smartshopping.model.dao.shoppinglist.ItemShoppingList;
import com.example.seniorproject.smartshopping.model.dao.productstore.ProductCrowd;
import com.example.seniorproject.smartshopping.model.dao.shoppinglist.ShoppingListMap;
import com.example.seniorproject.smartshopping.model.datatype.MutableInteger;
import com.example.seniorproject.smartshopping.model.manager.group.GroupManager;
import com.example.seniorproject.smartshopping.model.manager.shoppinglist.ItemShoppingListManager;
import com.example.seniorproject.smartshopping.model.manager.itemocr.ProductCrowdManager;
import com.example.seniorproject.smartshopping.model.util.GenerateSubSet;
import com.example.seniorproject.smartshopping.view.adapter.shoppinglist.ItemOptimizeAdapter;
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

    private FirebaseFirestore db;
    private CollectionReference cItemsShoppingList;
    private CollectionReference cProductList;


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
        lastPositionInteger = new MutableInteger(-1);
        itemOptimizeAdapter = new ItemOptimizeAdapter(lastPositionInteger);

        db = FirebaseFirestore.getInstance();
        cProductList = db.collection("productlist");
        cItemsShoppingList = db.collection("groups").document(GroupManager.getInstance().getCurrentGroup().getId())
                .collection("shoppinglists").document(shoppingListMap.getId())
                .collection("items");


        setStores();

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

    private void setStores(){
        stores = new ArrayList<String>();
        stores.add("Big C Bangpakok");
        stores.add("Max Value Pracha Uthit");
        stores.add("Tesco Lotus Bangpakok");
        stores.add("Tesco Lotus ตลาดโลตัสประชาอุทิศ");
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

                optimizePrice();
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

    @Override
    public void startOptimizePrice() {

        itemShoppingListManager = new ItemShoppingListManager();
        productCrowdManager = new ProductCrowdManager();
        retailPrice = new ArrayList<Double>();
        itemOptimizeAdapter.setItemShoppingLists(productCrowdManager.getProductCrowds());
        itemOptimizeAdapter.notifyDataSetChanged();

        if(tvSavePrice != null || tvSavePrice != null){
            tvTotalPrice.setVisibility(View.GONE);
            tvSavePrice.setVisibility(View.GONE);
        }

        cItemsShoppingList.get().addOnCompleteListener(getItemShoppingList);
    }

    private void optimizePrice() {
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
