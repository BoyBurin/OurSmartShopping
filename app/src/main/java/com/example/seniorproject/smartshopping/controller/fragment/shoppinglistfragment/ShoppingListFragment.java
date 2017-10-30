package com.example.seniorproject.smartshopping.controller.fragment.shoppinglistfragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.seniorproject.smartshopping.R;
import com.example.seniorproject.smartshopping.model.dao.ShoppingList;
import com.example.seniorproject.smartshopping.model.dao.ShoppingListMap;
import com.example.seniorproject.smartshopping.model.datatype.MutableInteger;
import com.example.seniorproject.smartshopping.model.manager.GroupManager;
import com.example.seniorproject.smartshopping.model.manager.ShoppingListManager;
import com.example.seniorproject.smartshopping.view.adapter.ShoppingListAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;


public class ShoppingListFragment extends Fragment {

    /***********************************************************************************************
     ************************************* Variable class ********************************************
     ***********************************************************************************************/

    public interface ShoopingListFloatingButton{
         void setShoopingListFloatingButtonFloationgButton();
    }

    public interface MoreShoppingListItemListener{
        void goToMoreShoppingListItem(ShoppingListMap shoppingListMap, int position);
    }

    private ListView listView;
    private ShoppingListAdapter shoppingListAdapter;
    private ShoppingListManager shoppingListManager;
    private MutableInteger lastPositionInteger;
    private FloatingActionButton fab;

    private FirebaseFirestore db;
    private CollectionReference cShoppingLists;
    private ListenerRegistration cShoppingListsListener;



    /***********************************************************************************************
     ************************************* Method class ********************************************
     ***********************************************************************************************/

    public ShoppingListFragment() {
        super();
    }

    @SuppressWarnings("unused")
    public static ShoppingListFragment newInstance() {
        ShoppingListFragment fragment = new ShoppingListFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_main_shopping_list, container, false);
        initInstances(rootView, savedInstanceState);
        return rootView;
    }

    private void init(Bundle savedInstanceState) {
        // Init Fragment level's variable(s) here
        shoppingListManager = ShoppingListManager.getInstance();
        lastPositionInteger = new MutableInteger(-1);

        shoppingListAdapter = new ShoppingListAdapter(lastPositionInteger);
        shoppingListManager = ShoppingListManager.getInstance();

        db = FirebaseFirestore.getInstance();
        cShoppingLists = db.collection("groups").document(GroupManager.getInstance().getCurrentGroup().getId())
                .collection("shoppinglists");

        cShoppingListsListener = cShoppingLists.addSnapshotListener(shoppingListListener);



    }

    @SuppressWarnings("UnusedParameters")
    private void initInstances(View rootView, Bundle savedInstanceState) {
        // Init 'View' instance(s) with rootView.findViewById here
        fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        listView = (ListView) rootView.findViewById(R.id.listViewActiveLists);

        fab.setOnClickListener(addShoppingListListener);
        shoppingListAdapter.setShoppingLists(shoppingListManager.getShoppingLists());
        listView.setAdapter(shoppingListAdapter);

        listView.setOnItemClickListener(moreShoppingListItemListener);



        //refreshData();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if(cShoppingListsListener != null){
            cShoppingListsListener.remove();
            cShoppingListsListener = null;
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


    /*private void refreshData(){
        if(shoppingListManager.getSize() == 0) {
            reloadData();
        }
        else {
            // reloadDataNewer();
        }
    }

    private void reloadData(){
        if(user != null){
            mMessagesDatabaseReference.child("shoppinglist")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    ShoppingListManager sManager = ShoppingListManager.getInstance();
                    for(DataSnapshot data : dataSnapshot.getChildren()) {
                        ShoppingListMap sMap = new ShoppingListMap();
                        sMap.setId(data.getKey());
                        sMap.setShoppingList(data.getValue(ShoppingList.class));

                        Log.d("Key: ", sMap.getId());
                        //Log.d("Name: ", sMap.getShoppingList().getPhotoURL().toString());
                        sManager.insertDaoAtTopPosition(sMap);
                    }

                    shoppingListAdapter.setShoppingLists(sManager.getShoppingLists());
                    shoppingListAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }*/


    /***********************************************************************************************
     ************************************* Listener variables ********************************************
     ***********************************************************************************************/

    final View.OnClickListener addShoppingListListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            ShoopingListFloatingButton shoopingListFloatingButton =
                    (ShoopingListFloatingButton) getActivity();
            shoopingListFloatingButton.setShoopingListFloatingButtonFloationgButton();
        }
    };

    final EventListener<QuerySnapshot> shoppingListListener = new EventListener<QuerySnapshot>() {
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
                        ShoppingList newShoppingList = documentSnapshot.toObject(ShoppingList.class);
                        String id = documentSnapshot.getId();
                        ShoppingListMap shoppingList = new ShoppingListMap(id, newShoppingList);

                        shoppingListManager.insertDaoAtTopPosition(shoppingList);

                        shoppingListAdapter.setShoppingLists(shoppingListManager.getShoppingLists());
                        shoppingListAdapter.notifyDataSetChanged();

                        Toast.makeText(getContext(), "Added " + newShoppingList.getName(), Toast.LENGTH_SHORT).show();

                        break;

                    case MODIFIED:

                        documentSnapshot = dc.getDocument();
                        newShoppingList = documentSnapshot.toObject(ShoppingList.class);
                        id = documentSnapshot.getId();
                        shoppingList = new ShoppingListMap(id, newShoppingList);

                        shoppingListManager.deleteShoppingList(id);
                        shoppingListManager.insertDaoAtTopPosition(shoppingList);

                        shoppingListAdapter.setShoppingLists(shoppingListManager.getShoppingLists());
                        shoppingListAdapter.notifyDataSetChanged();

                        Toast.makeText(getContext(), "Update " + newShoppingList.getName(), Toast.LENGTH_SHORT).show();
                        break;

                    case REMOVED:

                        documentSnapshot = dc.getDocument();
                        newShoppingList = documentSnapshot.toObject(ShoppingList.class);
                        id = documentSnapshot.getId();

                        shoppingListManager.deleteShoppingList(id);

                        shoppingListAdapter.setShoppingLists(shoppingListManager.getShoppingLists());
                        shoppingListAdapter.notifyDataSetChanged();

                        Toast.makeText(getContext(), "Remove " + newShoppingList.getName(), Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }
    };


    final AdapterView.OnItemClickListener moreShoppingListItemListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if(position < ShoppingListManager.getInstance().getSize()) {
                MoreShoppingListItemListener moreShoppingListItemListener =
                        (MoreShoppingListItemListener) getActivity();
                ShoppingListMap shoppingListMap = shoppingListManager.getShoppingList(position);
                moreShoppingListItemListener.goToMoreShoppingListItem(shoppingListMap,position);
            }

        }
    };



    /***********************************************************************************************
     ************************************* Inner class ********************************************
     ***********************************************************************************************/

}
