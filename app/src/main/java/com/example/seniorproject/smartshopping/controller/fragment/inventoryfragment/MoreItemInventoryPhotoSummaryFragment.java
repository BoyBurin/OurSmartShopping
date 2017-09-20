package com.example.seniorproject.smartshopping.controller.fragment.inventoryfragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.seniorproject.smartshopping.R;
import com.example.seniorproject.smartshopping.model.dao.ItemInventory;
import com.example.seniorproject.smartshopping.model.dao.ItemInventoryMap;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class MoreItemInventoryPhotoSummaryFragment extends Fragment {

    /***********************************************************************************************
     ************************************* Variable class ********************************************
     ***********************************************************************************************/

    private ItemInventoryMap itemInventoryMap;

    private TextView tvName;
    private TextView tvAmount;
    private TextView tvComment;
    private ImageView ivImg;
    private TextView tvUnit;

    private DatabaseReference mDatabaseRef;



    /***********************************************************************************************
     ************************************* Method class ********************************************
     ***********************************************************************************************/

    public MoreItemInventoryPhotoSummaryFragment() {
        super();
    }

    @SuppressWarnings("unused")
    public static MoreItemInventoryPhotoSummaryFragment newInstance(ItemInventoryMap itemInventoryMap) {
        MoreItemInventoryPhotoSummaryFragment fragment = new MoreItemInventoryPhotoSummaryFragment();
        Bundle args = new Bundle();
        args.putParcelable("itemInventoryMap", itemInventoryMap);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        itemInventoryMap = getArguments().getParcelable("itemInventoryMap");
        init(savedInstanceState);

        if (savedInstanceState != null)
            onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main_more_item_inventory_photo_summary, container, false);
        initInstances(rootView, savedInstanceState);
        return rootView;
    }

    private void init(Bundle savedInstanceState) {
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        mDatabaseRef.child("iteminventory").child(itemInventoryMap.getId())
                .addValueEventListener(updateItemInventory);

    }

    @SuppressWarnings("UnusedParameters")
    private void initInstances(View rootView, Bundle savedInstanceState) {
        tvName = (TextView) rootView.findViewById(R.id.tvName);
        tvAmount = (TextView) rootView.findViewById(R.id.tvAmount);
        tvComment = (TextView) rootView.findViewById(R.id.tvComment);
        tvUnit = (TextView) rootView.findViewById(R.id.tvUnit);
        ivImg = (ImageView) rootView.findViewById(R.id.ivImg);

        setValueOfItem();
    }

    private void setValueOfItem(){
        ItemInventory itemInventory = itemInventoryMap.getItemInventory();
        tvName.setText(itemInventory.getName().toString());
        tvAmount.setText(""+ itemInventory.getAmount());
        tvComment.setText(itemInventory.getComment());
        tvUnit.setText(itemInventory.getUnit());

        Glide.with(getContext())
                .load(itemInventory.getPhotoUrl())
                .placeholder(R.drawable.bg_small) //default pic
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(ivImg);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mDatabaseRef.child("iteminventory").child(itemInventoryMap.getId())
                .removeEventListener(updateItemInventory);
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

    final ValueEventListener updateItemInventory = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            ItemInventory itemInventory = dataSnapshot.getValue(ItemInventory.class);


            tvAmount.setText(itemInventory.getAmount() + "");
            tvComment.setText(itemInventory.getComment());

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };


    /***********************************************************************************************
     ************************************* Inner class ********************************************
     ***********************************************************************************************/

}
