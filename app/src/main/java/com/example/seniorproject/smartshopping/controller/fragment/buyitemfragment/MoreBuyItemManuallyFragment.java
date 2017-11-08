package com.example.seniorproject.smartshopping.controller.fragment.buyitemfragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.seniorproject.smartshopping.R;


public class MoreBuyItemManuallyFragment extends Fragment {

    /***********************************************************************************************
     ************************************* Variable class ********************************************
     ***********************************************************************************************/


    private Spinner spinnerStore;
    private TextView tvTotalPrice;
    private Button btnSaveItem;
    private ListView listView;

    private ArrayAdapter<String> adapter;
    private String currentStore;


    /***********************************************************************************************
     ************************************* Method class ********************************************
     ***********************************************************************************************/

    public MoreBuyItemManuallyFragment() {
        super();
    }

    @SuppressWarnings("unused")
    public static MoreBuyItemManuallyFragment newInstance() {
        MoreBuyItemManuallyFragment fragment = new MoreBuyItemManuallyFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_main_more_buy_item_add_manually, container, false);
        initInstances(rootView, savedInstanceState);
        return rootView;
    }

    private void init(Bundle savedInstanceState) {

        currentStore = "Big C Bangpakok";
        String[] stores = {"Big C Bangpakok", "Max Value Pracha Uthit", "Tesco Lotus Bangpakok", "Tesco Lotus ตลาดโลตัสประชาอุทิศ"};
        adapter = new ArrayAdapter<String>(getActivity(),  android.R.layout.simple_spinner_item, stores);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

    }

    @SuppressWarnings("UnusedParameters")
    private void initInstances(View rootView, Bundle savedInstanceState) {

        spinnerStore = (Spinner) rootView.findViewById(R.id.spinnerStore);
        tvTotalPrice = (TextView) rootView.findViewById(R.id.tvTotalPrice);
        listView = (ListView) rootView.findViewById(R.id.listView);
        btnSaveItem = (Button) rootView.findViewById(R.id.btnSaveItem);

        spinnerStore.setAdapter(adapter);
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


    /***********************************************************************************************
     ************************************* Inner class ********************************************
     ***********************************************************************************************/

}
