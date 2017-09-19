package com.example.seniorproject.smartshopping.controller.fragment.mainfragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.seniorproject.smartshopping.R;
import com.example.seniorproject.smartshopping.model.dao.ItemInventory;
import com.example.seniorproject.smartshopping.model.dao.ItemInventoryMap;
import com.example.seniorproject.smartshopping.model.dao.ShoppingListMap;
import com.example.seniorproject.smartshopping.model.datatype.MutableInteger;
import com.example.seniorproject.smartshopping.model.manager.GroupManager;
import com.example.seniorproject.smartshopping.model.manager.ItemInventoryManager;
import com.example.seniorproject.smartshopping.model.manager.ItemShoppingListManager;
import com.example.seniorproject.smartshopping.view.adapter.ItemInventoryAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;


public class MoreShoppingListItemSelectorFragment extends Fragment {

    /***********************************************************************************************
     ************************************* Variable class ********************************************
     ***********************************************************************************************/
    public interface AddShoppingListItemListener{
        public void setName(String name);
        public long getAmount();
        public void setButton(View.OnClickListener onClick);
    }

    public interface FinishAddShoppingListItemListener{
        public void finishAdded();
    }

    private ShoppingListMap shoppingListMap;


    private GridView gridView;
    private DatabaseReference mDatabaseRef;
    private ItemInventoryAdapter itemInventoryAdapter;
    private MutableInteger lastPositionInteger;
    private ItemInventoryMap currentItemInventoryMap;

    private ImageButton addedButton;





    /***********************************************************************************************
     ************************************* Method class ********************************************
     ***********************************************************************************************/

    public MoreShoppingListItemSelectorFragment() {
        super();
    }

    @SuppressWarnings("unused")
    public static MoreShoppingListItemSelectorFragment newInstance(ShoppingListMap shoppingListMap) {
        MoreShoppingListItemSelectorFragment fragment = new MoreShoppingListItemSelectorFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_main_more_shopping_list_item_selector, container, false);
        initInstances(rootView, savedInstanceState);
        return rootView;
    }

    private void init(Bundle savedInstanceState) {
        lastPositionInteger = new MutableInteger(-1);
        itemInventoryAdapter = new ItemInventoryAdapter(lastPositionInteger);
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();

    }

    @SuppressWarnings("UnusedParameters")
    private void initInstances(View rootView, Bundle savedInstanceState) {
        gridView = (GridView) rootView.findViewById(R.id.gridView);
        itemInventoryAdapter.setItemInventories(ItemInventoryManager.getInstance().getItemInventories());
        gridView.setAdapter(itemInventoryAdapter);
        itemInventoryAdapter.notifyDataSetChanged();

        gridView.setOnItemClickListener(clickItemListener);

        ((AddShoppingListItemListener) getParentFragment()).setButton(savedShoppingListItem);

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

    final AdapterView.OnItemClickListener clickItemListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            AddShoppingListItemListener addShoppingListItemListener =
                    (AddShoppingListItemListener)getParentFragment();
            currentItemInventoryMap = ItemInventoryManager.getInstance().getItemInventory(position);
            addShoppingListItemListener.setName(currentItemInventoryMap.getItemInventory().getName());
        }
    };

    final View.OnClickListener savedShoppingListItem = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            AddShoppingListItemListener addShoppingListItemListener =
                    (AddShoppingListItemListener)getParentFragment();

            if(currentItemInventoryMap != null){
                if(addShoppingListItemListener.getAmount() != 0){
                    String itemInventoryID = currentItemInventoryMap.getId();
                    long amount = addShoppingListItemListener.getAmount();

                    mDatabaseRef.child("iteminshoppinglist").child(shoppingListMap.getId())
                            .child(itemInventoryID).setValue(amount).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            FinishAddShoppingListItemListener finishAddShoppingListItemListener =
                                    (FinishAddShoppingListItemListener) getParentFragment();

                            finishAddShoppingListItemListener.finishAdded();
                            String itemName = currentItemInventoryMap.getItemInventory().getName();
                            Toast.makeText(getContext(), "Added " +itemName +" Item into Shopping List Success", Toast.LENGTH_SHORT)
                                    .show();
                        }
                    });
                }
            }
        }
    };


    /***********************************************************************************************
     ************************************* Inner class ********************************************
     ***********************************************************************************************/

}
