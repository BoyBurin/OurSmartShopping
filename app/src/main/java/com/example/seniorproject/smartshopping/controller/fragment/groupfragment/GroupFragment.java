package com.example.seniorproject.smartshopping.controller.fragment.groupfragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.seniorproject.smartshopping.R;
import com.example.seniorproject.smartshopping.model.dao.user.User;
import com.example.seniorproject.smartshopping.model.dao.user.UserInGroup;
import com.example.seniorproject.smartshopping.model.dao.user.UserInGroupMap;
import com.example.seniorproject.smartshopping.model.dao.user.UserMap;
import com.example.seniorproject.smartshopping.model.datatype.MutableInteger;
import com.example.seniorproject.smartshopping.model.manager.group.GroupManager;
import com.example.seniorproject.smartshopping.model.manager.user.UserInGroupManager;
import com.example.seniorproject.smartshopping.model.manager.user.UserManager;
import com.example.seniorproject.smartshopping.view.adapter.user.UserAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;


public class GroupFragment extends Fragment {

    /***********************************************************************************************
     ************************************* Variable class ********************************************
     ***********************************************************************************************/

    private ListView listView;
    private UserInGroupManager userInGroupManager;
    private UserAdapter userAdapter;
    private MutableInteger lastPositionInteger;

    private DatabaseReference mDatabaseRef;
    private FirebaseFirestore db;
    private CollectionReference cUser;
    private ListenerRegistration cUserListener;



    /***********************************************************************************************
     ************************************* Method class ********************************************
     ***********************************************************************************************/

    public GroupFragment() {
        super();
    }

    @SuppressWarnings("unused")
    public static GroupFragment newInstance() {
        GroupFragment fragment = new GroupFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_main_group, container, false);
        initInstances(rootView, savedInstanceState);
        return rootView;
    }

    private void init(Bundle savedInstanceState) {
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        lastPositionInteger = new MutableInteger(-1);
        userAdapter = new UserAdapter(lastPositionInteger);
        userInGroupManager = new UserInGroupManager();

        db = FirebaseFirestore.getInstance();

        cUser = db.collection("groups").document(GroupManager.getInstance().getCurrentGroup().getId())
        .collection("users");
        cUserListener = cUser.addSnapshotListener(userListener);


    }

    @SuppressWarnings("UnusedParameters")
    private void initInstances(View rootView, Bundle savedInstanceState) {
        listView = (ListView) rootView.findViewById(R.id.listViewGroup);
        userAdapter.setUsers(userInGroupManager.getUsers());
        listView.setAdapter(userAdapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(cUserListener != null){
            cUserListener.remove();
            cUserListener = null;
        }

        /*mDatabaseRef.child("iteminventory")
                .removeEventListener(itemUpdateListener);*/
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

    final EventListener<QuerySnapshot> userListener = new EventListener<QuerySnapshot>() {
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
                        UserInGroup user = documentSnapshot.toObject(UserInGroup.class);
                        UserInGroupMap userMap = new UserInGroupMap(documentSnapshot.getId(), user);
                        userInGroupManager.addUser(userMap);
                        userAdapter.setUsers(userInGroupManager.getUsers());
                        userAdapter.notifyDataSetChanged();


                        break;
                    case MODIFIED:
                        break;
                    case REMOVED:

                        break;
                }
            }
        }
    };

    /*final ChildEventListener UserListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            final String userID = dataSnapshot.getKey();
            mDatabaseRef.child("users").child(userID)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            User user = dataSnapshot.getValue(User.class);
                            UserMap userMap = new UserMap(userID, user);
                            userManager.addUser(userMap);
                            userAdapter.setUsers(userManager.getUsers());
                            userAdapter.notifyDataSetChanged();
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
    };*/


    /***********************************************************************************************
     ************************************* Inner class ********************************************
     ***********************************************************************************************/

}
