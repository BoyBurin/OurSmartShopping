package com.example.seniorproject.smartshopping.controller.fragment.shoppinglistfragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.example.seniorproject.smartshopping.R;

import com.example.seniorproject.smartshopping.model.dao.ShoppingListMap;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class MoreShoppingListItemOptimizeFragment extends Fragment {

    /***********************************************************************************************
     ************************************* Variable class ********************************************
     ***********************************************************************************************/
    interface OptimizePriceListener {
        void startOptimizePrice();
    }

    interface OptimizeTimeListener {
        void startOptimizeTime();
    }

    ShoppingListMap shoppingListMap;

    private Button optimizePrice;
    private Button optimizeTime;

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
        optimizePrice = (Button) rootView.findViewById(R.id.price);
        optimizeTime = (Button) rootView.findViewById(R.id.time);

        optimizePrice.setOnClickListener(optimizePriceListener);
        optimizeTime.setOnClickListener(optimizeTimeListener);

        MoreShoppingListItemOptimizePriceFragment moreShoppingListItemOptimizePriceFragment =
                MoreShoppingListItemOptimizePriceFragment.newInstance(shoppingListMap);

        MoreShoppingListItemOptimizeTimeFragment moreShoppingListItemOptimizeTimeFragment =
                MoreShoppingListItemOptimizeTimeFragment.newInstance(shoppingListMap);

        getChildFragmentManager().beginTransaction()
                .add(R.id.containerMoreShoppingListOptimize, moreShoppingListItemOptimizePriceFragment,
                        "moreShoppingListItemOptimizePriceFragment")
                .add(R.id.containerMoreShoppingListOptimize, moreShoppingListItemOptimizeTimeFragment,
                        "moreShoppingListItemOptimizeTimeFragment")
                .detach(moreShoppingListItemOptimizePriceFragment)
                .detach(moreShoppingListItemOptimizeTimeFragment)
                .commit();

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
            MoreShoppingListItemOptimizePriceFragment moreShoppingListItemOptimizePriceFragment =
                    (MoreShoppingListItemOptimizePriceFragment) getChildFragmentManager()
                            .findFragmentByTag("moreShoppingListItemOptimizePriceFragment");

            MoreShoppingListItemOptimizeTimeFragment moreShoppingListItemOptimizeTimeFragment =
                    (MoreShoppingListItemOptimizeTimeFragment) getChildFragmentManager()
                            .findFragmentByTag("moreShoppingListItemOptimizeTimeFragment");

            getChildFragmentManager().beginTransaction()
                    .detach(moreShoppingListItemOptimizeTimeFragment)
                    .attach(moreShoppingListItemOptimizePriceFragment)
                    .commit();

            moreShoppingListItemOptimizePriceFragment.startOptimizePrice();
        }
    };

    final View.OnClickListener optimizeTimeListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            MoreShoppingListItemOptimizePriceFragment moreShoppingListItemOptimizePriceFragment =
                    (MoreShoppingListItemOptimizePriceFragment) getChildFragmentManager()
                            .findFragmentByTag("moreShoppingListItemOptimizePriceFragment");

            MoreShoppingListItemOptimizeTimeFragment moreShoppingListItemOptimizeTimeFragment =
                    (MoreShoppingListItemOptimizeTimeFragment) getChildFragmentManager()
                            .findFragmentByTag("moreShoppingListItemOptimizeTimeFragment");

            getChildFragmentManager().beginTransaction()
                    .detach(moreShoppingListItemOptimizePriceFragment)
                    .attach(moreShoppingListItemOptimizeTimeFragment)
                    .commit();

            moreShoppingListItemOptimizeTimeFragment.startOptimizeTime();

        }
    };


    /***********************************************************************************************
     ************************************* Inner class ********************************************
     ***********************************************************************************************/

}
