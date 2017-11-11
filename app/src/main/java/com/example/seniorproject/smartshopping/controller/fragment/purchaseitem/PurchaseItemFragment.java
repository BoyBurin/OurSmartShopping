package com.example.seniorproject.smartshopping.controller.fragment.purchaseitem;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.seniorproject.smartshopping.R;
import com.example.seniorproject.smartshopping.controller.activity.MoreBuyItemManuallyActivity;
import com.example.seniorproject.smartshopping.controller.activity.OCRActivity;


public class PurchaseItemFragment extends Fragment {

    /***********************************************************************************************
     ************************************* Variable class ********************************************
     ***********************************************************************************************/

    private Button btnOCR;
    private Button btnManually;



    /***********************************************************************************************
     ************************************* Method class ********************************************
     ***********************************************************************************************/

    public PurchaseItemFragment() {
        super();
    }

    @SuppressWarnings("unused")
    public static PurchaseItemFragment newInstance() {
        PurchaseItemFragment fragment = new PurchaseItemFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_main_purchase_item, container, false);
        initInstances(rootView, savedInstanceState);
        return rootView;
    }

    private void init(Bundle savedInstanceState) {
        // Init Fragment level's variable(s) here

    }

    @SuppressWarnings("UnusedParameters")
    private void initInstances(View rootView, Bundle savedInstanceState) {

        btnOCR = (Button) rootView.findViewById(R.id.btnOCR);
        btnManually = (Button) rootView.findViewById(R.id.btnManually);

        btnManually.setOnClickListener(manuallyListener);
        btnOCR.setOnClickListener(ocrListener);
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

    private View.OnClickListener manuallyListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(getActivity(), MoreBuyItemManuallyActivity.class);
            getActivity().startActivity(intent);
        }
    };

    private View.OnClickListener ocrListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(getActivity(), OCRActivity.class);
            getActivity().startActivity(intent);
        }
    };


    /***********************************************************************************************
     ************************************* Inner class ********************************************
     ***********************************************************************************************/

}
