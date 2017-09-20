package com.example.seniorproject.smartshopping.controller.fragment.shoppinglistfragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.example.seniorproject.smartshopping.R;
import com.example.seniorproject.smartshopping.model.dao.ItemInventory;
import com.example.seniorproject.smartshopping.model.dao.ItemInventoryMap;
import com.example.seniorproject.smartshopping.model.dao.ItemShoppingList;
import com.example.seniorproject.smartshopping.model.dao.ShoppingListMap;
import com.example.seniorproject.smartshopping.model.datatype.MutableInteger;
import com.example.seniorproject.smartshopping.model.manager.ItemShoppingListManager;
import com.example.seniorproject.smartshopping.view.adapter.ItemShoppingListAdapter;
import com.example.seniorproject.smartshopping.view.customviewgroup.CustomViewGroupShoppingListItemAdd;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class MoreShoppingListItemUpdateFragment extends Fragment implements
        MoreShoppingListItemSelectorFragment.AddShoppingListItemListener,
        MoreShoppingListItemSelectorFragment.FinishAddShoppingListItemListener {

    /***********************************************************************************************
     ************************************* Variable class ********************************************
     ***********************************************************************************************/

    ShoppingListMap shoppingListMap;

    private ListView listView;
    private ItemShoppingListAdapter itemShoppingListAdapter;
    private MutableInteger lastPositionInteger;
    private FloatingActionButton fab;
    private DatabaseReference mDatabaseRef;

    private ItemShoppingListManager itemShoppingListManager;
    private CustomViewGroupShoppingListItemAdd customViewGroupShoppingListItemAdd;

    private ArrayList<View.OnClickListener> deleteListener;




    /***********************************************************************************************
     ************************************* Method class ********************************************
     ***********************************************************************************************/

    public MoreShoppingListItemUpdateFragment() {
        super();
    }

    @SuppressWarnings("unused")
    public static MoreShoppingListItemUpdateFragment newInstance(ShoppingListMap shoppingListMap) {
        MoreShoppingListItemUpdateFragment fragment = new MoreShoppingListItemUpdateFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_main_more_shopping_list_item_update, container, false);
        initInstances(rootView, savedInstanceState);
        return rootView;
    }

    private void init(Bundle savedInstanceState) {
        lastPositionInteger = new MutableInteger(-1);
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        itemShoppingListAdapter = new ItemShoppingListAdapter(lastPositionInteger);
        itemShoppingListManager = new ItemShoppingListManager();
        deleteListener = new ArrayList<View.OnClickListener>();
        mDatabaseRef.child("iteminshoppinglist").child(shoppingListMap.getId())
                .addChildEventListener(updateItemShoppingListListener);

    }

    @SuppressWarnings("UnusedParameters")
    private void initInstances(View rootView, Bundle savedInstanceState) {
        fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.setOnClickListener(addedShoppingListItemListener);

        listView = (ListView) rootView.findViewById(R.id.shoppingListItem);
        itemShoppingListAdapter.setItemShoppingLists(itemShoppingListManager.getItemShoppingLists(),
                deleteListener);
        listView.setAdapter(itemShoppingListAdapter);

        customViewGroupShoppingListItemAdd = (CustomViewGroupShoppingListItemAdd) rootView
                .findViewById(R.id.addedItemBar);

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
                            ItemShoppingList itemShoppingList = new ItemShoppingList(amount, itemInventoryMap);

                            View.OnClickListener onClickDeleteListener = new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    mDatabaseRef.child("iteminshoppinglist").child(shoppingListMap.getId())
                                            .child(itemInventoryID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(getContext(), "Delete " + itemName+  " Success", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            };

                            deleteListener.add(onClickDeleteListener);
                            itemShoppingListManager.addItemShoppingList(itemShoppingList);
                            itemShoppingListAdapter.setItemShoppingLists(itemShoppingListManager.getItemShoppingLists(),
                                    deleteListener);
                            itemShoppingListAdapter.notifyDataSetChanged();
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

            itemShoppingListManager.getItemShoppingLists().remove(index);
            deleteListener.remove(index);

            itemShoppingListAdapter.setItemShoppingLists(itemShoppingListManager.getItemShoppingLists(), deleteListener);
            itemShoppingListAdapter.notifyDataSetChanged();

        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    final View.OnClickListener addedShoppingListItemListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            MoreShoppingListItemSelectorFragment moreShoppingListItemSelectorFragment =
                    MoreShoppingListItemSelectorFragment.newInstance(shoppingListMap);

            getChildFragmentManager().beginTransaction()
                    .add(R.id.containerMoreShoppingListItem, moreShoppingListItemSelectorFragment,
                            "moreShoppingListItemSelectorFragment")
                    .commit();

            customViewGroupShoppingListItemAdd.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
        }
    };

    /***********************************************************************************************
     ************************************* Implementation ********************************************
     ***********************************************************************************************/



    @Override
    public void setName(String name) {
        customViewGroupShoppingListItemAdd.setItemName(name);
    }

    @Override
    public long getAmount() {
        return customViewGroupShoppingListItemAdd.getAmount();
    }

    @Override
    public void setButton(View.OnClickListener onClick) {
        customViewGroupShoppingListItemAdd.setAddedButton(onClick);
    }

    @Override
    public void finishAdded() {
        MoreShoppingListItemSelectorFragment moreShoppingListItemSelectorFragment =
                (MoreShoppingListItemSelectorFragment)
                        getChildFragmentManager().findFragmentByTag("moreShoppingListItemSelectorFragment");

        getChildFragmentManager().beginTransaction()
                .remove(moreShoppingListItemSelectorFragment)
                .commit();

        customViewGroupShoppingListItemAdd.setVisibility(View.GONE);
        customViewGroupShoppingListItemAdd.clearAll();
        listView.setVisibility(View.VISIBLE);

    }


    /***********************************************************************************************
     ************************************* Inner class ********************************************
     ***********************************************************************************************/

}
