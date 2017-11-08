package com.example.seniorproject.smartshopping.controller.fragment.inventoryfragment;

/**
 * Created by thamm on 9/9/2560.
 */
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
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
import com.example.seniorproject.smartshopping.model.datatype.MutableInteger;
import com.example.seniorproject.smartshopping.model.manager.group.GroupManager;
import com.example.seniorproject.smartshopping.model.manager.iteminventory.ItemInventoryManager;
import com.example.seniorproject.smartshopping.view.adapter.iteminventory.ItemInventoryAdapter;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;

public class InventoryFragment extends Fragment{
    /***********************************************************************************************
     ************************************* Variable class ********************************************
     ***********************************************************************************************/
    public interface MoreItemInventoryListener{
        void goToMoreItemInventory(ItemInventoryMap itemInventoryMap, int position);
    }

    public interface ItemInventoryFloatingButton{
        void setItemInventoryFloationgButton();
    }

    private FloatingActionButton fab;


    private GridView gridView;
    private ItemInventoryAdapter itemInventoryAdapter;
    private MutableInteger lastPositionInteger;

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
        lastPositionInteger = new MutableInteger(-1);
        itemInventoryAdapter = new ItemInventoryAdapter(lastPositionInteger);

        db = FirebaseFirestore.getInstance();
        cItems = db.collection("groups")
                .document(GroupManager.getInstance().getCurrentGroup().getId())
                .collection("items");

        cItemsListener = cItems.addSnapshotListener(itemListener);

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

            ItemInventoryManager gm = ItemInventoryManager.getInstance();

            for (DocumentChange dc : documentSnapshots.getDocumentChanges()) {
                switch (dc.getType()) {
                    case ADDED:
                        DocumentSnapshot documentSnapshot = dc.getDocument();
                        ItemInventory item = documentSnapshot.toObject(ItemInventory.class);
                        String id = documentSnapshot.getId();

                        //Add
                        ItemInventoryMap itemMap = new ItemInventoryMap(id, item);
                        gm.addItemInventory(itemMap);
                        itemInventoryAdapter.setItemInventories(gm.getItemInventories());
                        itemInventoryAdapter.notifyDataSetChanged();

                        Toast.makeText(getContext(), "Added " + item.getName(), Toast.LENGTH_SHORT).show();

                        break;

                    case MODIFIED:
                        documentSnapshot = dc.getDocument();
                        ItemInventory update = documentSnapshot.toObject(ItemInventory.class);
                        id = documentSnapshot.getId();

                        int index = gm.getIndexByKey(id);
                        itemMap = gm.getItemInventory(index);

                        //Update
                        itemMap.setItemInventory(update);
                        gm.sortItem();
                        itemInventoryAdapter.setItemInventories(gm.getItemInventories());
                        itemInventoryAdapter.notifyDataSetChanged();

                        Toast.makeText(getContext(), "Update " + update.getName(), Toast.LENGTH_SHORT).show();
                        break;

                    case REMOVED:
                        documentSnapshot = dc.getDocument();
                        item = documentSnapshot.toObject(ItemInventory.class);
                        id = documentSnapshot.getId();

                        index = gm.getIndexByKey(id);
                        gm.removeItemInventory(index);
                        itemInventoryAdapter.setItemInventories(gm.getItemInventories());
                        itemInventoryAdapter.notifyDataSetChanged();

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



    final AdapterView.OnItemClickListener moreItemInventoryListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            if(position < ItemInventoryManager.getInstance().getSize()) {

                ItemInventoryMap itemInventoryMap = ItemInventoryManager.getInstance().getItemInventory(position);
                MoreItemInventoryListener moreItemInventoryListener =
                        (MoreItemInventoryListener) getActivity();
                moreItemInventoryListener.goToMoreItemInventory(itemInventoryMap, position);
            }
        }
    };


    /***********************************************************************************************
     ************************************* Inner class ********************************************
     ***********************************************************************************************/
}
