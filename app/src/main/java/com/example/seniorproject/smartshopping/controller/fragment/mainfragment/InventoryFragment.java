package com.example.seniorproject.smartshopping.controller.fragment.mainfragment;

/**
 * Created by thamm on 9/9/2560.
 */
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.seniorproject.smartshopping.R;
import com.example.seniorproject.smartshopping.model.dao.ItemInventory;
import com.example.seniorproject.smartshopping.model.dao.ItemInventoryMap;
import com.example.seniorproject.smartshopping.model.datatype.MutableInteger;
import com.example.seniorproject.smartshopping.model.manager.GroupManager;
import com.example.seniorproject.smartshopping.model.manager.ItemInventoryManager;
import com.example.seniorproject.smartshopping.view.adapter.ItemInventoryAdapter;
import com.example.seniorproject.smartshopping.view.customviewgroup.ItemView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class InventoryFragment extends Fragment{
    /***********************************************************************************************
     ************************************* Variable class ********************************************
     ***********************************************************************************************/
    public interface MoreItemInventoryListener{
        void goToMoreItemInventory(int position);
    }

    private ImageButton btnAll;
    private ImageButton btnMeat;
    private ImageButton btnVegetandFruit;
    private ImageButton btnFood;
    private ImageButton btnSnack;
    private ImageButton btnbCleaningEquip;
    private FloatingActionButton fab;


    private GridView gridView;
    private ItemInventoryAdapter itemInventoryAdapter;
    private MutableInteger lastPositionInteger;

    private DatabaseReference mDatabaseRef;



    /***********************************************************************************************
     ************************************* Method class ********************************************
     ***********************************************************************************************/

    public InventoryFragment() {
        super();
    }

    @SuppressWarnings("unused")
    public static InventoryFragment newInstance() {
        InventoryFragment fragment = new InventoryFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_main_inventory, container, false);
        initInstances(rootView, savedInstanceState);
        return rootView;
    }

    private void init(Bundle savedInstanceState) {
        // Init Fragment level's variable(s) here
        lastPositionInteger = new MutableInteger(-1);
        itemInventoryAdapter = new ItemInventoryAdapter(lastPositionInteger);
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        mDatabaseRef.child("itemingroup")
                .child(GroupManager.getInstance().getCurrentGroup().getId())
                .addChildEventListener(itemInventoryListener);





    }

    @SuppressWarnings("UnusedParameters")
    private void initInstances(View rootView, Bundle savedInstanceState) {

        gridView = (GridView) rootView.findViewById(R.id.gridView);
        gridView.setAdapter(itemInventoryAdapter);

        fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.setOnClickListener(addItemListener);

        gridView.setOnItemClickListener(moreItemInventoryListener);




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

    final View.OnClickListener addItemListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            DatabaseReference itemInventoryRef = mDatabaseRef.child("iteminventory");
            final String itemInventoryID = itemInventoryRef.push().getKey();
            itemInventoryRef.child(itemInventoryID).child("name").setValue("")
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            DatabaseReference itemInGroup = mDatabaseRef.child("itemingroup");

                            itemInGroup.child(GroupManager.getInstance().getCurrentGroup().getId()).child(itemInventoryID)
                                    .setValue(true).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(getActivity(), "Add Item Success", Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                    });
        }
    };

    final ChildEventListener itemInventoryListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            final String itemInventoryID = dataSnapshot.getKey();
            mDatabaseRef.child("iteminventory").child(itemInventoryID)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            ItemInventory itemInventory = dataSnapshot.getValue(ItemInventory.class);
                            ItemInventoryMap itemInventoryMap = new ItemInventoryMap(itemInventoryID, itemInventory);
                            ItemInventoryManager.getInstance().addItemInventory(itemInventoryMap);
                            itemInventoryAdapter.setItemInventories(ItemInventoryManager.getInstance().getItemInventories());
                            itemInventoryAdapter.notifyDataSetChanged();
                            Log.d("Soft", ""+ itemInventory.getRemindItem().getSoft());
                            Log.d("Hard", ""+ itemInventory.getRemindItem().getHard());

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

        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    final AdapterView.OnItemClickListener moreItemInventoryListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            if(position < ItemInventoryManager.getInstance().getSize()) {
                MoreItemInventoryListener moreItemInventoryListener =
                        (MoreItemInventoryListener) getActivity();
                moreItemInventoryListener.goToMoreItemInventory(position);
            }
        }
    };


    /***********************************************************************************************
     ************************************* Inner class ********************************************
     ***********************************************************************************************/
}
