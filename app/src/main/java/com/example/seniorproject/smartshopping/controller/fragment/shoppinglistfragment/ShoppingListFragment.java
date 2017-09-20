package com.example.seniorproject.smartshopping.controller.fragment.shoppinglistfragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

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

    private DatabaseReference mMessagesDatabaseReference;
    private DatabaseReference shoopingListRef;



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
        mMessagesDatabaseReference = FirebaseDatabase.getInstance().getReference();
        shoopingListRef = mMessagesDatabaseReference.child("shoppinglistingroup")
        .child(GroupManager.getInstance().getCurrentGroup().getId());
        shoopingListRef.addChildEventListener(modifyShoppingListListener);
        shoppingListAdapter = new ShoppingListAdapter(lastPositionInteger);
        shoppingListManager = ShoppingListManager.getInstance();


    }

    @SuppressWarnings("UnusedParameters")
    private void initInstances(View rootView, Bundle savedInstanceState) {
        // Init 'View' instance(s) with rootView.findViewById here
        mMessagesDatabaseReference = FirebaseDatabase.getInstance().getReference();
        fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        listView = (ListView) rootView.findViewById(R.id.listViewActiveLists);

        fab.setOnClickListener(addShoppingListListener);
        shoppingListAdapter.setShoppingLists(shoppingListManager.getShoppingLists());
        listView.setAdapter(shoppingListAdapter);

        listView.setOnItemClickListener(moreShoppingListItemListener);



        //refreshData();
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        shoopingListRef.removeEventListener(modifyShoppingListListener);
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

    final View.OnClickListener addShoppingListListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            ShoopingListFloatingButton shoopingListFloatingButton =
                    (ShoopingListFloatingButton) getActivity();
            shoopingListFloatingButton.setShoopingListFloatingButtonFloationgButton();
        }
    };

    final ChildEventListener modifyShoppingListListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            String shoppingListID = dataSnapshot.getKey();
            mMessagesDatabaseReference.child("shoppinglist").child(shoppingListID)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            ShoppingListMap sMap = new ShoppingListMap();
                            sMap.setId(dataSnapshot.getKey());
                            sMap.setShoppingList(dataSnapshot.getValue(ShoppingList.class));
                            shoppingListManager.insertDaoAtTopPosition(sMap);

                            shoppingListAdapter.setShoppingLists(shoppingListManager.getShoppingLists());
                            shoppingListAdapter.notifyDataSetChanged();
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
