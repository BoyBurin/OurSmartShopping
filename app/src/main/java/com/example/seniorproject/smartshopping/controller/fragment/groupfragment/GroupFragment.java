package com.example.seniorproject.smartshopping.controller.fragment.groupfragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.example.seniorproject.smartshopping.R;
import com.example.seniorproject.smartshopping.model.dao.group.GroupMap;
import com.example.seniorproject.smartshopping.model.dao.group.PendingGroupMember;
import com.example.seniorproject.smartshopping.model.dao.group.PendingGroupMemberWithAction;
import com.example.seniorproject.smartshopping.model.dao.user.User;
import com.example.seniorproject.smartshopping.model.dao.user.UserInGroup;
import com.example.seniorproject.smartshopping.model.dao.user.UserInGroupMap;
import com.example.seniorproject.smartshopping.model.dao.user.UserMap;
import com.example.seniorproject.smartshopping.model.daorecyclerview.group.BaseGroup;
import com.example.seniorproject.smartshopping.model.daorecyclerview.group.GroupCreator;
import com.example.seniorproject.smartshopping.model.datatype.MutableInteger;
import com.example.seniorproject.smartshopping.model.manager.group.GroupManager;
import com.example.seniorproject.smartshopping.model.manager.group.PendingGroupMemberManager;
import com.example.seniorproject.smartshopping.model.manager.user.UserInGroupManager;
import com.example.seniorproject.smartshopping.model.manager.user.UserManager;
import com.example.seniorproject.smartshopping.view.adapter.user.UserAdapter;
import com.example.seniorproject.smartshopping.view.recyclerviewadapter.GroupRecyclerViewAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

import java.util.ArrayList;


public class GroupFragment extends Fragment {

    /***********************************************************************************************
     ************************************* Variable class ********************************************
     ***********************************************************************************************/

    private UserInGroupManager userInGroupManager;
    private PendingGroupMemberManager pendingGroupMemberManager;
    private GroupMap currentGroup;
    private GroupRecyclerViewAdapter groupRecyclerViewAdapter;
    private RecyclerView recyclerView;
    private GroupCreator groupCreator;

    private FirebaseFirestore db;
    private CollectionReference cUser;
    private CollectionReference cPendingGroupMember;
    private ListenerRegistration cUserListener;
    private ListenerRegistration cPendingGroupMemberListener;


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

        groupRecyclerViewAdapter = new GroupRecyclerViewAdapter(getContext());
        userInGroupManager = new UserInGroupManager();
        pendingGroupMemberManager = new PendingGroupMemberManager();
        currentGroup = GroupManager.getInstance().getCurrentGroup();
        groupCreator = new GroupCreator();

        db = FirebaseFirestore.getInstance();

        cUser = db.collection("groups").document(GroupManager.getInstance().getCurrentGroup().getId())
                .collection("users");

        cPendingGroupMember = db.collection("groups").document(GroupManager.getInstance().getCurrentGroup().getId())
                .collection("pendingmember");

        cUserListener = cUser.addSnapshotListener(userListener);
        cPendingGroupMemberListener = cPendingGroupMember.addSnapshotListener(pendingGroupMemberListener);


    }

    @SuppressWarnings("UnusedParameters")
    private void initInstances(View rootView, Bundle savedInstanceState) {
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);

        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(groupRecyclerViewAdapter);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (cUserListener != null) {
            cUserListener.remove();
            cUserListener = null;
        }

        if(cPendingGroupMemberListener != null){
            cPendingGroupMemberListener.remove();
            cPendingGroupMemberListener = null;
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

    public void setGroupInterface(){
        ArrayList<BaseGroup> baseGroups = new ArrayList<>();

        baseGroups.add(groupCreator.createGroupDetail(currentGroup));
        baseGroups.add(groupCreator.createTittleName("Member"));
        baseGroups.addAll(groupCreator.createGroupMember(userInGroupManager.getUsers()));
        baseGroups.add(groupCreator.createTittleName("Pending Member"));
        baseGroups.addAll(groupCreator.createPendingGroupMember(pendingGroupMemberManager.getGroups()));

        groupRecyclerViewAdapter.setItemGroups(baseGroups);
        groupRecyclerViewAdapter.notifyDataSetChanged();



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
                        setGroupInterface();


                        break;
                    case MODIFIED:
                        break;
                    case REMOVED:
                        documentSnapshot = dc.getDocument();
                        user = documentSnapshot.toObject(UserInGroup.class);
                        userMap = new UserInGroupMap(documentSnapshot.getId(), user);
                        userInGroupManager.removeUser(documentSnapshot.getId());
                        setGroupInterface();

                        break;
                }
            }
        }
    };

    final EventListener<QuerySnapshot> pendingGroupMemberListener = new EventListener<QuerySnapshot>() {
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
                        final PendingGroupMember pendingGroupMember = documentSnapshot.toObject(PendingGroupMember.class);

                        View.OnClickListener accept = new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                cPendingGroupMember.document(documentSnapshot.getId()).delete()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {

                                                cUser.document(documentSnapshot.getId())
                                                        .set(pendingGroupMember)
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                pendingGroupMemberManager.removePendingGroupMember(documentSnapshot.getId());
                                                                setGroupInterface();
                                                                Toast.makeText(getContext(), "Accept" + pendingGroupMember.getName(), Toast.LENGTH_SHORT).show();
                                                                Log.d("TAG", "DocumentSnapshot successfully written!");
                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Toast.makeText(getContext(), "Error to decline", Toast.LENGTH_SHORT).show();
                                                                Log.w("TAG", "Error writing document", e);
                                                            }
                                                        });
                                                Log.d("TAG", "DocumentSnapshot successfully deleted!");
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

                        View.OnClickListener decline = new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                cPendingGroupMember.document(documentSnapshot.getId()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                        pendingGroupMemberManager.removePendingGroupMember(documentSnapshot.getId());
                                        setGroupInterface();
                                        Toast.makeText(getContext(), "Decline" + pendingGroupMember.getName(), Toast.LENGTH_SHORT).show();
                                        Log.d("TAG", "DocumentSnapshot successfully deleted!");
                                    }
                                })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(getContext(), "Error to decline", Toast.LENGTH_SHORT).show();
                                                Log.w("TAG", "Error deleting document", e);
                                            }
                                        });
                            }
                        };

                        PendingGroupMemberWithAction pendingGroupMemberWithAction =
                                new PendingGroupMemberWithAction(accept, decline, pendingGroupMember, documentSnapshot.getId());


                        pendingGroupMemberManager.addGroup(pendingGroupMemberWithAction);
                        setGroupInterface();

                        break;
                    case MODIFIED:
                        break;
                    case REMOVED:

                        break;
                }
            }
        }
    };



    /***********************************************************************************************
     ************************************* Inner class ********************************************
     ***********************************************************************************************/

}
