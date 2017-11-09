package com.example.seniorproject.smartshopping.controller.fragment.shoppinglistfragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.example.seniorproject.smartshopping.R;
import com.example.seniorproject.smartshopping.model.dao.iteminventory.ItemInventory;
import com.example.seniorproject.smartshopping.model.dao.shoppinglist.ItemShoppingList;
import com.example.seniorproject.smartshopping.model.dao.shoppinglist.ShoppingListMap;
import com.example.seniorproject.smartshopping.model.datatype.MutableInteger;
import com.example.seniorproject.smartshopping.model.manager.group.GroupManager;
import com.example.seniorproject.smartshopping.model.manager.shoppinglist.ItemShoppingListManager;
import com.example.seniorproject.smartshopping.view.adapter.shoppinglist.ItemShoppingListAdapter;
import com.example.seniorproject.smartshopping.view.customviewgroup.CustomViewGroupShoppingListItemAdd;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;

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

    private FirebaseFirestore db;
    private CollectionReference cItemsShoppingList;
    private ListenerRegistration cItemsShoppingListListener;
    private ArrayList<ListenerRegistration> dItemInventoryListeners;

    private ItemShoppingListManager itemShoppingListManager;
    private CustomViewGroupShoppingListItemAdd customViewGroupShoppingListItemAdd;

    //private ArrayList<View.OnClickListener> deleteListener;




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
        itemShoppingListAdapter = new ItemShoppingListAdapter(lastPositionInteger);
        itemShoppingListManager = new ItemShoppingListManager();
        dItemInventoryListeners = new ArrayList<>();
        //deleteListener = new ArrayList<View.OnClickListener>();
        db = FirebaseFirestore.getInstance();
        cItemsShoppingList = db.collection("groups").document(GroupManager.getInstance().getCurrentGroup().getId())
                .collection("shoppinglists").document(shoppingListMap.getId())
                .collection("items");

        cItemsShoppingListListener = cItemsShoppingList.addSnapshotListener(itemShoppingListListener);

    }

    @SuppressWarnings("UnusedParameters")
    private void initInstances(View rootView, Bundle savedInstanceState) {
        fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.setOnClickListener(addedShoppingListItemListener);

        listView = (ListView) rootView.findViewById(R.id.shoppingListItem);
        listView.setAdapter(itemShoppingListAdapter);

        customViewGroupShoppingListItemAdd = (CustomViewGroupShoppingListItemAdd) rootView
                .findViewById(R.id.addedItemBar);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(cItemsShoppingListListener != null){
            cItemsShoppingListListener.remove();
            cItemsShoppingListListener = null;
        }

        for(ListenerRegistration dItemInventoryListener : dItemInventoryListeners){
            if(dItemInventoryListener != null){
                dItemInventoryListener.remove();
            }
            dItemInventoryListeners = null;
        }
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

    public int getStatus(long soft, long hard, long amount){
        int status = -1;

        if(amount > soft){
            status = 0;
        } else if(amount < hard){
            status = 2;
        } else{
            status = 1;
        }

        return status;
    }

    /***********************************************************************************************
     ************************************* Listener variables ********************************************
     ***********************************************************************************************/

    final EventListener<QuerySnapshot> itemShoppingListListener = new EventListener<QuerySnapshot>() {
        @Override
        public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
            if (e != null) {
                Log.w("TAG", "listen:error", e);
                return;
            }


            for (DocumentChange dc : documentSnapshots.getDocumentChanges()) {
                switch (dc.getType()) {
                    case ADDED:
                        final DocumentSnapshot documentSnapshot = dc.getDocument();
                        final ItemShoppingList newItemShoppingList = documentSnapshot.toObject(ItemShoppingList.class);

                        View.OnClickListener onClickDeleteListener = new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                cItemsShoppingList.document(documentSnapshot.getId()).delete()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Log.d("TAG", "DocumentSnapshot successfully deleted!");
                                                Toast.makeText(getContext(), "Delete " + newItemShoppingList.getName()+
                                                        " Success", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w("TAG", "Error deleting document", e);
                                            }
                                        });
                            }
                        };

                        DocumentReference dIteminventory = db.collection("groups").document(GroupManager.getInstance().getCurrentGroup().getId())
                                .collection("items").document(newItemShoppingList.getBarcodeId());
                        ListenerRegistration dItemInventoryListener = dIteminventory.addSnapshotListener(itemInventoryListener);

                        dItemInventoryListeners.add(dItemInventoryListener);
                        newItemShoppingList.setDeleteListener(onClickDeleteListener);

                        itemShoppingListManager.addItemShoppingList(newItemShoppingList);
                        itemShoppingListAdapter.setItemShoppingLists(itemShoppingListManager.getItemShoppingLists());
                        itemShoppingListAdapter.notifyDataSetChanged();

                        Toast.makeText(getContext(), "Added " + newItemShoppingList.getName(), Toast.LENGTH_SHORT).show();

                        break;

                    case MODIFIED:

                        /*DocumentSnapshot documentSnapshotModified = dc.getDocument();
                        ItemShoppingList newItemShoppingListModified = documentSnapshotModified.toObject(ItemShoppingList.class);
                        String barcodeId = newItemShoppingListModified.getBarcodeId();
                        int status = newItemShoppingListModified.getStatus();

                        ItemShoppingList itemShoppingList = itemShoppingListManager.getItemShoppingListByBarcode(barcodeId);

                        itemShoppingList.setStatus(status);
                        itemShoppingListAdapter.setItemShoppingLists(itemShoppingListManager.getItemShoppingLists());
                        itemShoppingListAdapter.notifyDataSetChanged();*/


                        Toast.makeText(getContext(), "Update Status Successful" , Toast.LENGTH_SHORT).show();
                        break;

                    case REMOVED:

                        DocumentSnapshot documentSnapshotRemove = dc.getDocument();
                        ItemShoppingList newItemShoppingListRemove = documentSnapshotRemove.toObject(ItemShoppingList.class);

                        itemShoppingListManager.removeItemShoppingList(newItemShoppingListRemove.getBarcodeId());
                        itemShoppingListManager.sortItem();
                        itemShoppingListAdapter.setItemShoppingLists(itemShoppingListManager.getItemShoppingLists());
                        itemShoppingListAdapter.notifyDataSetChanged();


                        Toast.makeText(getContext(), "Remove " + newItemShoppingListRemove.getName() , Toast.LENGTH_SHORT).show();
                        break;
                }
            }
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

    final EventListener<DocumentSnapshot> itemInventoryListener = new EventListener<DocumentSnapshot>() {
        @Override
        public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
            if (e != null) {
                Log.w("TAG", "Listen failed.", e);
                return;
            }

            if (documentSnapshot != null && documentSnapshot.exists()) {
                ItemInventory itemInventory = documentSnapshot.toObject(ItemInventory.class);
                long soft = itemInventory.getSoft();
                long hard = itemInventory.getHard();
                long amount = itemInventory.getAmount();

                ItemShoppingList itemShoppingList = itemShoppingListManager.getItemShoppingListByBarcode(itemInventory.getBarcodeId());
                itemShoppingList.setStatus(getStatus(soft, hard, amount));

                itemShoppingListManager.sortItem();
                itemShoppingListAdapter.setItemShoppingLists(itemShoppingListManager.getItemShoppingLists());
                itemShoppingListAdapter.notifyDataSetChanged();

            } else {
                Log.d("TAG", "Current data: null");
            }
        }
    };

    /***********************************************************************************************
     ************************************* Implementation ********************************************
     ***********************************************************************************************/


    // AddShoppingListItemListener *******************************************************************

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


    // FinishAddShoppingListItemListener ********************************************************************

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
