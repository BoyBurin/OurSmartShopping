package com.example.seniorproject.smartshopping.controller.fragment.mainfragment;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.seniorproject.smartshopping.R;
import com.example.seniorproject.smartshopping.model.dao.ProductCrowd;
import com.example.seniorproject.smartshopping.superuser.ProductList;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class MoreShoppingListItemOptimizeFragment extends Fragment {

    /***********************************************************************************************
     ************************************* Variable class ********************************************
     ***********************************************************************************************/


    private Button optimizePrice;
    private Button optimizeTime;

    private DatabaseReference mDatabaseRef;

    private ArrayList<ProductCrowd> productCrowdArrayList;


    /***********************************************************************************************
     ************************************* Method class ********************************************
     ***********************************************************************************************/

    public MoreShoppingListItemOptimizeFragment() {
        super();
    }

    @SuppressWarnings("unused")
    public static MoreShoppingListItemOptimizeFragment newInstance() {
        MoreShoppingListItemOptimizeFragment fragment = new MoreShoppingListItemOptimizeFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_more_shopping_list_optimize, container, false);
        initInstances(rootView, savedInstanceState);
        return rootView;
    }

    private void init(Bundle savedInstanceState) {
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        productCrowdArrayList = new ArrayList<ProductCrowd>();

    }

    @SuppressWarnings("UnusedParameters")
    private void initInstances(View rootView, Bundle savedInstanceState) {
        optimizePrice = (Button) rootView.findViewById(R.id.price);
        optimizeTime = (Button) rootView.findViewById(R.id.time);

        //optimizePrice.setOnClickListener();
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

    final View.OnClickListener optimizePriceListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //mDatabaseRef.child("productcrowd").addListenerForSingleValueEvent();
        }
    };


    final ValueEventListener getProductListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            ProductCrowd productCrowd1 = new ProductCrowd();
            ProductCrowd productCrowd2 = new ProductCrowd();

            productCrowd1.setBarcode("8850188243308");
            productCrowd1.setName("โฟร์โมสต์รสช็อคโกแลต");
            productCrowd1.setPrice(35);
            productCrowd1.setStore("1");

            productCrowd2.setBarcode("8850425007830");
            productCrowd2.setName("ยูโร่เค้กใบเตย");
            productCrowd2.setPrice(36);
            productCrowd2.setStore("2");
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };


    /***********************************************************************************************
     ************************************* Inner class ********************************************
     ***********************************************************************************************/

}
