package com.example.seniorproject.smartshopping.controller.fragment.inventoryfragment;

/**
 * Created by thamm on 9/9/2560.
 */
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.example.seniorproject.smartshopping.R;
import com.example.seniorproject.smartshopping.model.dao.iteminventory.ItemInventory;
import com.example.seniorproject.smartshopping.model.dao.iteminventory.ItemInventoryMap;
import com.example.seniorproject.smartshopping.model.daorecyclerview.iteminventory.BaseItemInventory;
import com.example.seniorproject.smartshopping.model.daorecyclerview.iteminventory.ItemInventoryCreator;
import com.example.seniorproject.smartshopping.model.datatype.MutableInteger;
import com.example.seniorproject.smartshopping.model.manager.group.GroupManager;
import com.example.seniorproject.smartshopping.model.manager.iteminventory.ItemInventoryManager;
import com.example.seniorproject.smartshopping.view.adapter.iteminventory.ItemInventoryAdapter;
import com.example.seniorproject.smartshopping.view.recyclerviewadapter.ItemInventoryRecyclerViewAdapter;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class InventoryFragment extends Fragment implements ItemInventoryRecyclerViewAdapter.OnItemClickListener{

    /***********************************************************************************************
     ************************************* Variable class ********************************************
     ***********************************************************************************************/
    public interface MoreItemInventoryListener{
        void goToMoreItemInventory(ItemInventoryMap itemInventoryMap);
    }

    public interface ItemInventoryFloatingButton{
        void setItemInventoryFloationgButton();
    }

    private FloatingActionButton fab;


    private RecyclerView recyclerView;
    private ItemInventoryRecyclerViewAdapter itemInventoryRecyclerViewAdapter;

    private ArrayList<BaseItemInventory> baseItemInventories;
    private ArrayList<String> types;
    private ItemInventoryCreator itemInventoryCreator;
    private ItemInventoryManager itemInventoryManager;

    private FirebaseFirestore db;
    private CollectionReference cItems;
    private ListenerRegistration cItemsListener;



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
        itemInventoryManager = new ItemInventoryManager();
        baseItemInventories = new ArrayList<>();
        itemInventoryCreator = new ItemInventoryCreator();
        types = itemInventoryCreator.getTypes(itemInventoryManager.getItemInventoryMaps());
        itemInventoryRecyclerViewAdapter = new ItemInventoryRecyclerViewAdapter(getContext());

        db = FirebaseFirestore.getInstance();
        cItems = db.collection("groups")
                .document(GroupManager.getInstance().getCurrentGroup().getId())
                .collection("items");

        cItemsListener = cItems.addSnapshotListener(itemListener);

    }

    @SuppressWarnings("UnusedParameters")
    private void initInstances(View rootView, Bundle savedInstanceState) {

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);

        GridLayoutManager manager = new GridLayoutManager(getContext(), 2);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if(baseItemInventories.get(position).getType() == BaseItemInventory.ITEM_INVENTORY){
                    return 1;
                }
                else return 2;
            }
        });

        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(itemInventoryRecyclerViewAdapter);

        fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.setOnClickListener(addItemListener);





    }


    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(cItemsListener != null){
            cItemsListener.remove();
            cItemsListener = null;
        }
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

    public void setBaseItemInventories(){

        types = itemInventoryCreator.updateTypes(types, itemInventoryManager.getItemInventoryMaps());
        baseItemInventories = itemInventoryCreator.createItemInventories(itemInventoryManager.getItemInventoryMaps(), types);

        itemInventoryRecyclerViewAdapter.setItemInventories(baseItemInventories);
        itemInventoryRecyclerViewAdapter.setItemClickListener(this);
        itemInventoryRecyclerViewAdapter.notifyDataSetChanged();

        Log.d("size", "" + baseItemInventories.size());
    }

    /***********************************************************************************************
     ************************************* Listener variables ********************************************
     ***********************************************************************************************/


    final EventListener<QuerySnapshot> itemListener = new EventListener<QuerySnapshot>() {
        @Override
        public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
            if (e != null) {
                Log.w("TAG", "listen:error", e);
                return;
            }


            for (DocumentChange dc : documentSnapshots.getDocumentChanges()) {
                switch (dc.getType()) {
                    case ADDED:
                        DocumentSnapshot documentSnapshot = dc.getDocument();
                        ItemInventory item = documentSnapshot.toObject(ItemInventory.class);
                        String id = documentSnapshot.getId();

                        //Add
                        ItemInventoryMap itemMap = new ItemInventoryMap(id, item);
                        itemInventoryManager.addItemInventory(itemMap);
                        setBaseItemInventories();

                        //Toast.makeText(getContext(), "Added " + item.getName(), Toast.LENGTH_SHORT).show();

                        break;

                    case MODIFIED:
                        documentSnapshot = dc.getDocument();
                        ItemInventory update = documentSnapshot.toObject(ItemInventory.class);
                        id = documentSnapshot.getId();

                        int index = itemInventoryManager.getIndexByKey(id);
                        itemMap = itemInventoryManager.getItemInventory(index);

                        //Update
                        itemMap.setItemInventory(update);
                        itemInventoryManager.sortItem();
                        setBaseItemInventories();

                        Toast.makeText(getContext(), "Update " + update.getName(), Toast.LENGTH_SHORT).show();
                        break;

                    case REMOVED:
                        documentSnapshot = dc.getDocument();
                        item = documentSnapshot.toObject(ItemInventory.class);
                        id = documentSnapshot.getId();

                        index = itemInventoryManager.getIndexByKey(id);
                        itemInventoryManager.removeItemInventory(index);
                        setBaseItemInventories();

                        Toast.makeText(getContext(), "Remove " + item.getName(), Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }
    };

    final View.OnClickListener addItemListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            ItemInventoryFloatingButton itemInventoryFloatingButton = (ItemInventoryFloatingButton)
                    getActivity();
            itemInventoryFloatingButton.setItemInventoryFloationgButton();
        }
    };




    /***********************************************************************************************
     ************************************* Inner class ********************************************
     ***********************************************************************************************/


    /***********************************************************************************************
     ************************************* Implementation ********************************************
     ***********************************************************************************************/

    @Override
    public void onItemClick(ItemInventoryMap itemInventory) {
        MoreItemInventoryListener moreItemInventoryListener =
                (MoreItemInventoryListener) getActivity();
        moreItemInventoryListener.goToMoreItemInventory(itemInventory);
    }
}
