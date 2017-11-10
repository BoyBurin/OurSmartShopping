package com.example.seniorproject.smartshopping.controller.fragment.shoppinglistfragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.example.seniorproject.smartshopping.model.dao.shoppinglist.ShoppingListMap;
import com.example.seniorproject.smartshopping.model.datatype.MutableInteger;
import com.example.seniorproject.smartshopping.model.manager.group.GroupManager;
import com.example.seniorproject.smartshopping.model.manager.iteminventory.ItemInventoryManager;
import com.example.seniorproject.smartshopping.view.adapter.iteminventory.ItemInventoryAdapter;
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

import java.util.HashMap;
import java.util.Map;


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
    private ItemInventoryAdapter itemInventoryAdapter;
    private MutableInteger lastPositionInteger;
    private ItemInventoryMap currentItemInventoryMap;
    private ItemInventoryManager itemInventoryManager;

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
        itemInventoryManager = new ItemInventoryManager();
        lastPositionInteger = new MutableInteger(-1);
        itemInventoryAdapter = new ItemInventoryAdapter(lastPositionInteger);

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
        gridView = (GridView) rootView.findViewById(R.id.gridView);
        itemInventoryAdapter.setItemInventories(itemInventoryManager.getItemInventories());
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

    @Override
    public void onDestroy() {
        super.onDestroy();

        if(cItemsListener != null){
            cItemsListener.remove();
            cItemsListener = null;
        }
    }

    /***********************************************************************************************
     ************************************* Listener variables ********************************************
     ***********************************************************************************************/

    final AdapterView.OnItemClickListener clickItemListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            AddShoppingListItemListener addShoppingListItemListener =
                    (AddShoppingListItemListener)getParentFragment();
            currentItemInventoryMap = itemInventoryManager.getItemInventory(position);
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
                    String itemBarcode = currentItemInventoryMap.getItemInventory().getBarcodeId();
                    String itemname = currentItemInventoryMap.getItemInventory().getName();
                    long amount = addShoppingListItemListener.getAmount();
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
                            Toast.makeText(getContext(), "Added " +itemName +" Item into Shopping List Success", Toast.LENGTH_SHORT)
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
                switch (dc.getType()) {
                    case ADDED:
                        DocumentSnapshot documentSnapshot = dc.getDocument();
                        ItemInventory item = documentSnapshot.toObject(ItemInventory.class);
                        String id = documentSnapshot.getId();

                        //Add
                        ItemInventoryMap itemMap = new ItemInventoryMap(id, item);
                        itemInventoryManager.addItemInventory(itemMap);

                        itemInventoryAdapter.setItemInventories(itemInventoryManager.getItemInventories());
                        itemInventoryAdapter.notifyDataSetChanged();

                        Toast.makeText(getContext(), "Added " + item.getName(), Toast.LENGTH_SHORT).show();

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

                        itemInventoryAdapter.setItemInventories(itemInventoryManager.getItemInventories());
                        itemInventoryAdapter.notifyDataSetChanged();

                        Toast.makeText(getContext(), "Update " + update.getName(), Toast.LENGTH_SHORT).show();
                        break;

                    case REMOVED:
                        documentSnapshot = dc.getDocument();
                        item = documentSnapshot.toObject(ItemInventory.class);
                        id = documentSnapshot.getId();

                        index = itemInventoryManager.getIndexByKey(id);
                        itemInventoryManager.removeItemInventory(index);

                        itemInventoryAdapter.setItemInventories(itemInventoryManager.getItemInventories());
                        itemInventoryAdapter.notifyDataSetChanged();

                        Toast.makeText(getContext(), "Remove " + item.getName(), Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }
    };


    /***********************************************************************************************
     ************************************* Inner class ********************************************
     ***********************************************************************************************/

}
