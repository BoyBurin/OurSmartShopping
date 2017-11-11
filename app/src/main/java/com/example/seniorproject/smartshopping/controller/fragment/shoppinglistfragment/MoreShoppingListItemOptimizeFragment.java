package com.example.seniorproject.smartshopping.controller.fragment.shoppinglistfragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.seniorproject.smartshopping.R;

import com.example.seniorproject.smartshopping.model.dao.shoppinglist.ShoppingListMap;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class MoreShoppingListItemOptimizeFragment extends Fragment implements MoreShoppingListItemOptimizePriceFragment.BackgroundLoadingOptimizePrice,
        MoreShoppingListItemOptimizeTimeFragment.BackgroundLoadingOptimizeTime{

    /***********************************************************************************************
     ************************************* Variable class ********************************************
     ***********************************************************************************************/


    ShoppingListMap shoppingListMap;

    private Button optimizePrice;
    private Button optimizeTime;
    private RelativeLayout backgroundLoading;
    private LinearLayout linearBtn;

    private DatabaseReference mDatabaseRef;


    /***********************************************************************************************
     ************************************* Method class ********************************************
     ***********************************************************************************************/

    public MoreShoppingListItemOptimizeFragment() {
        super();
    }

    @SuppressWarnings("unused")
    public static MoreShoppingListItemOptimizeFragment newInstance(ShoppingListMap shoppingListMap) {
        MoreShoppingListItemOptimizeFragment fragment = new MoreShoppingListItemOptimizeFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_more_shopping_list_optimize, container, false);
        initInstances(rootView, savedInstanceState);
        return rootView;
    }

    private void init(Bundle savedInstanceState) {
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();

    }

    @SuppressWarnings("UnusedParameters")
    private void initInstances(View rootView, Bundle savedInstanceState) {
        backgroundLoading = (RelativeLayout) rootView.findViewById(R.id.backgroundLoading);
        linearBtn = (LinearLayout) rootView.findViewById(R.id.linearBtn);

        optimizePrice = (Button) rootView.findViewById(R.id.price);
        optimizeTime = (Button) rootView.findViewById(R.id.time);

        optimizePrice.setOnClickListener(optimizePriceListener);
        optimizeTime.setOnClickListener(optimizeTimeListener);

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

    private void loadingOptimize(){
        linearBtn.setVisibility(View.GONE);
        backgroundLoading.setVisibility(View.VISIBLE);
    }

    /***********************************************************************************************
     ************************************* Listener variables ********************************************
     ***********************************************************************************************/


    final View.OnClickListener optimizePriceListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            loadingOptimize();

            MoreShoppingListItemOptimizePriceFragment moreShoppingListItemOptimizePriceFragment =
                    MoreShoppingListItemOptimizePriceFragment.newInstance(shoppingListMap);

            getChildFragmentManager().beginTransaction()
                    .replace(R.id.containerMoreShoppingListOptimize, moreShoppingListItemOptimizePriceFragment)
                    .commit();

        }
    };

    final View.OnClickListener optimizeTimeListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            loadingOptimize();

            MoreShoppingListItemOptimizeTimeFragment moreShoppingListItemOptimizeTimeFragment =
                    MoreShoppingListItemOptimizeTimeFragment.newInstance(shoppingListMap);

            getChildFragmentManager().beginTransaction()
                    .replace(R.id.containerMoreShoppingListOptimize, moreShoppingListItemOptimizeTimeFragment)
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
    public void closeBackgroubdLoadingPrice() {
        linearBtn.setVisibility(View.VISIBLE);
        backgroundLoading.setVisibility(View.GONE);
    }

    @Override
    public void closeBackgroubdLoadingTime() {
        linearBtn.setVisibility(View.VISIBLE);
        backgroundLoading.setVisibility(View.GONE);
    }
}
