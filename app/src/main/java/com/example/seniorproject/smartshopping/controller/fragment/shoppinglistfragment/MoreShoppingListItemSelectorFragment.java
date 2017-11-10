package com.example.seniorproject.smartshopping.controller.fragment.shoppinglistfragment;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.seniorproject.smartshopping.R;
import com.example.seniorproject.smartshopping.model.dao.iteminventory.ItemInventory;
import com.example.seniorproject.smartshopping.model.dao.iteminventory.ItemInventoryMap;
import com.example.seniorproject.smartshopping.model.dao.shoppinglist.ItemShoppingList;
import com.example.seniorproject.smartshopping.model.dao.shoppinglist.ShoppingListMap;
import com.example.seniorproject.smartshopping.model.daorecyclerview.addpurchaseitem.AddPurchaseItemCreator;
import com.example.seniorproject.smartshopping.model.daorecyclerview.addpurchaseitem.BaseAddPurchaseItem;
import com.example.seniorproject.smartshopping.model.datatype.MutableInteger;
import com.example.seniorproject.smartshopping.model.manager.group.GroupManager;
import com.example.seniorproject.smartshopping.model.manager.iteminventory.ItemInventoryManager;
import com.example.seniorproject.smartshopping.model.manager.shoppinglist.ItemShoppingListManager;
import com.example.seniorproject.smartshopping.view.adapter.iteminventory.ItemInventoryAdapter;
import com.example.seniorproject.smartshopping.view.customviewgroup.CustomViewGroupShoppingListItemAdd;
import com.example.seniorproject.smartshopping.view.recyclerviewadapter.AddPurchaseItemRecyclerViewAdapter;
import com.example.seniorproject.smartshopping.view.recyclerviewadapter.ItemInventoryRecyclerViewAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import java.util.HashMap;
import java.util.Map;


public class MoreShoppingListItemSelectorFragment extends Fragment implements AddPurchaseItemRecyclerViewAdapter.OnItemClickListener {

    /***********************************************************************************************
     ************************************* Variable class ********************************************
     ***********************************************************************************************/

    public interface FinishAddShoppingListItemListener {
        public void finishAdded();
    }

    private ShoppingListMap shoppingListMap;
    private ItemShoppingListManager itemShoppingListManager;

    private RecyclerView recyclerView;
    private CustomViewGroupShoppingListItemAdd customViewGroupShoppingListItemAdd;

    private AddPurchaseItemRecyclerViewAdapter addItemAdapter;
    private AddPurchaseItemCreator addItemCreator;
    private ItemInventoryMap currentItemInventoryMap;
    private ItemInventoryManager itemInventoryManager;
    private ArrayList<BaseAddPurchaseItem> baseAddItems;

    private FirebaseFirestore db;
    private CollectionReference cItemShoppingList;
    private CollectionReference cItems;
    private ListenerRegistration cItemsListener;

    private ImageButton addedButton;


    /***********************************************************************************************
     ************************************* Method class ********************************************
     ***********************************************************************************************/

    public MoreShoppingListItemSelectorFragment() {
        super();
    }

    @SuppressWarnings("unused")
    public static MoreShoppingListItemSelectorFragment newInstance(ShoppingListMap shoppingListMap, ArrayList<ItemShoppingList> itemShoppingLists) {
        MoreShoppingListItemSelectorFragment fragment = new MoreShoppingListItemSelectorFragment();
        Bundle args = new Bundle();
        args.putParcelable("shoppingListMap", shoppingListMap);
        args.putParcelableArrayList("itemShoppingLists", itemShoppingLists);
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

        ArrayList<Parcelable> item = getArguments().getParcelableArrayList("itemShoppingLists");
        itemShoppingListManager = new ItemShoppingListManager();

        for (int i = 0; i < item.size(); i++) {
            ItemShoppingList itemShoppingList = (ItemShoppingList) item.get(i);
            itemShoppingListManager.addItemShoppingList(itemShoppingList);
        }

        itemInventoryManager = new ItemInventoryManager();
        addItemAdapter = new AddPurchaseItemRecyclerViewAdapter(getContext());
        addItemCreator = new AddPurchaseItemCreator();
        baseAddItems = new ArrayList<>();

        String groupId = GroupManager.getInstance().getCurrentGroup().getId();
        String shoppingListId = shoppingListMap.getId();

        // Connect to Database
        db = FirebaseFirestore.getInstance();
        cItemShoppingList = db.collection("groups").document(groupId)
                .collection("shoppinglists").document(shoppingListId)
                .collection("items");

        cItems = db.collection("groups")
                .document(GroupManager.getInstance().getCurrentGroup().getId())
                .collection("items");

        cItemsListener = cItems.addSnapshotListener(itemListener);

    }

    @SuppressWarnings("UnusedParameters")
    private void initInstances(View rootView, Bundle savedInstanceState) {
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        customViewGroupShoppingListItemAdd = (CustomViewGroupShoppingListItemAdd) rootView.findViewById(R.id.customViewGroupShoppingListItemAdd);

        GridLayoutManager manager = new GridLayoutManager(getContext(), 2);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (baseAddItems.get(position).getType() == BaseAddPurchaseItem.ADD_PURCHASE_ITEM) {
                    return 1;
                } else return 2;
            }
        });

        addItemAdapter.setItemClickListener(this);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(addItemAdapter);


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

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (cItemsListener != null) {
            cItemsListener.remove();
            cItemsListener = null;
        }
    }

    public void setInterface() {
        baseAddItems = new ArrayList<>();

        baseAddItems.addAll(addItemCreator.createAddPurchaseItem(itemInventoryManager.getItemInventoryMaps()));
        baseAddItems.add(addItemCreator.createAddPurchaseItemButton(cancelListener, addListener));

        addItemAdapter.setAddPurchaseItems(baseAddItems);
        addItemAdapter.notifyDataSetChanged();
    }

    /***********************************************************************************************
     ************************************* Listener variables ********************************************
     ***********************************************************************************************/

    final View.OnClickListener cancelListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            FinishAddShoppingListItemListener finishAddShoppingListItemListener =
                    (FinishAddShoppingListItemListener) getParentFragment();

            finishAddShoppingListItemListener.finishAdded();
        }
    };

    final View.OnClickListener addListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {


            if (currentItemInventoryMap != null) {
                if (!customViewGroupShoppingListItemAdd.isAmountEmpty()) {
                    String itemBarcode = currentItemInventoryMap.getItemInventory().getBarcodeId();
                    String itemname = currentItemInventoryMap.getItemInventory().getName();
                    long amount = customViewGroupShoppingListItemAdd.getAmount();
                    Map<String, Object> newData = new HashMap<>();

                    newData.put("amount", amount);
                    newData.put("barcodeId", itemBarcode);
                    newData.put("name", itemname);

                    cItemShoppingList.document(itemBarcode).set(newData)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    FinishAddShoppingListItemListener finishAddShoppingListItemListener =
                                            (FinishAddShoppingListItemListener) getParentFragment();

                                    finishAddShoppingListItemListener.finishAdded();
                                    String itemName = currentItemInventoryMap.getItemInventory().getName();
                                    Toast.makeText(getContext(), "Added " + itemName + " Item into Shopping List Success", Toast.LENGTH_SHORT)
                                            .show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w("TAG", "Error adding document", e);
                                    Toast.makeText(getContext(), "Add Item in Shopping List Failed", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        }
    };


    final EventListener<QuerySnapshot> itemListener = new EventListener<QuerySnapshot>() {
        @Override
        public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
            if (e != null) {
                Log.w("TAG", "listen:error", e);
                return;
            }


            for (DocumentChange dc : documentSnapshots.getDocumentChanges()) {
                boolean isAdded = false;
                switch (dc.getType()) {
                    case ADDED:
                        DocumentSnapshot documentSnapshot = dc.getDocument();
                        ItemInventory item = documentSnapshot.toObject(ItemInventory.class);
                        String id = documentSnapshot.getId();
                        isAdded = false;
                        for (ItemShoppingList itemShoppingList : itemShoppingListManager.getItemShoppingLists()) {
                            if (itemShoppingList.getBarcodeId().equals(id)) {
                                isAdded = true;
                            }
                        }
                        if(isAdded){
                            setInterface();
                            continue;
                        }

                        //Add
                        ItemInventoryMap itemMap = new ItemInventoryMap(id, item);
                        itemInventoryManager.addItemInventory(itemMap);

                        setInterface();


                        //Toast.makeText(getContext(), "Added " + item.getName(), Toast.LENGTH_SHORT).show();

                        break;

                    case MODIFIED:
                        documentSnapshot = dc.getDocument();
                        ItemInventory update = documentSnapshot.toObject(ItemInventory.class);
                        id = documentSnapshot.getId();
                        isAdded = false;
                        for (ItemShoppingList itemShoppingList : itemShoppingListManager.getItemShoppingLists()) {
                            if (itemShoppingList.getBarcodeId().equals(id)) {
                                isAdded = true;
                            }
                        }
                        if(isAdded){
                            setInterface();
                            continue;
                        }

                        int index = itemInventoryManager.getIndexByKey(id);
                        itemMap = itemInventoryManager.getItemInventory(index);

                        //Update
                        itemMap.setItemInventory(update);
                        itemInventoryManager.sortItem();


                        setInterface();
                        Toast.makeText(getContext(), "Update " + update.getName(), Toast.LENGTH_SHORT).show();
                        break;

                    case REMOVED:
                        documentSnapshot = dc.getDocument();
                        item = documentSnapshot.toObject(ItemInventory.class);
                        id = documentSnapshot.getId();
                        isAdded = false;
                        for (ItemShoppingList itemShoppingList : itemShoppingListManager.getItemShoppingLists()) {
                            if (itemShoppingList.getBarcodeId().equals(id)) {
                                isAdded = true;
                            }
                        }
                        if(isAdded){
                            setInterface();
                            continue;
                        }

                        index = itemInventoryManager.getIndexByKey(id);
                        itemInventoryManager.removeItemInventory(index);

                        setInterface();

                        Toast.makeText(getContext(), "Remove " + item.getName(), Toast.LENGTH_SHORT).show();
                        break;
                }
            }
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
        currentItemInventoryMap = itemInventory;
        customViewGroupShoppingListItemAdd.setItemName(itemInventory.getItemInventory().getName());
    }


}
