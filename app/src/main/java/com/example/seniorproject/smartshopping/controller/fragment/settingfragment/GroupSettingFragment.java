package com.example.seniorproject.smartshopping.controller.fragment.settingfragment;

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
import android.widget.Toast;

import com.example.seniorproject.smartshopping.R;
import com.example.seniorproject.smartshopping.model.dao.Group;
import com.example.seniorproject.smartshopping.model.dao.GroupList;
import com.example.seniorproject.smartshopping.model.dao.GroupMap;
import com.example.seniorproject.smartshopping.model.datatype.MutableInteger;
import com.example.seniorproject.smartshopping.model.manager.GroupManager;
import com.example.seniorproject.smartshopping.model.manager.ItemInventoryManager;
import com.example.seniorproject.smartshopping.model.manager.ShoppingListManager;
import com.example.seniorproject.smartshopping.superuser.ProductList;
import com.example.seniorproject.smartshopping.view.adapter.GroupSettingAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class GroupSettingFragment extends Fragment {

    /***********************************************************************************************
     ************************************* Variable class ********************************************
     ***********************************************************************************************/

    public interface ChangeGroup{
        void changeGroup();
    }

    private GridView gridView;
    private GroupSettingAdapter groupSettingAdapter;
    private MutableInteger lastPositionInteger;


    private FirebaseFirestore db;
    private CollectionReference cGroupUser;
    private CollectionReference cGroups;
    private ListenerRegistration cGroupUserListener;
    private FirebaseUser user;




    /***********************************************************************************************
     ************************************* Method class ********************************************
     ***********************************************************************************************/

    public GroupSettingFragment() {
        super();
    }

    @SuppressWarnings("unused")
    public static GroupSettingFragment newInstance() {
        GroupSettingFragment fragment = new GroupSettingFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_group_setting, container, false);
        initInstances(rootView, savedInstanceState);
        return rootView;
    }

    private void init(Bundle savedInstanceState) {

        lastPositionInteger = new MutableInteger(-1);
        groupSettingAdapter = new GroupSettingAdapter(lastPositionInteger);

        db = FirebaseFirestore.getInstance();

        cGroups = db.collection("groups");

        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null) {
            cGroupUser = db.collection("users").document(user.getUid()).collection("groups");
        }
        else{
            Toast.makeText(getContext(), "Cannot find User", Toast.LENGTH_SHORT).show();
        }

        cGroupUserListener = cGroupUser.addSnapshotListener(groupListener);


    }

    @SuppressWarnings("UnusedParameters")
    private void initInstances(View rootView, Bundle savedInstanceState) {

        gridView = (GridView) rootView.findViewById(R.id.gridView);
        gridView.setAdapter(groupSettingAdapter);

        gridView.setOnItemClickListener(changeGroupListener);



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

        if(cGroupUserListener != null){
            cGroupUserListener.remove();
            cGroupUserListener = null;
        }
    }

    /***********************************************************************************************
     ************************************* Listener variables ********************************************
     ***********************************************************************************************/

    private final EventListener<QuerySnapshot> groupListener = new EventListener<QuerySnapshot>() {
        @Override
        public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
            if (e != null) {
                Log.w("TAG", "listen:error", e);
                return;
            }



            for (DocumentChange dc : documentSnapshots.getDocumentChanges()) {
                switch (dc.getType()) {
                    case ADDED:

                        GroupList newGroup = dc.getDocument().toObject(GroupList.class);
                        if(!GroupManager.getInstance().isContain(newGroup)){
                            GroupManager.getInstance().addGroup(newGroup);
                        }
                        groupSettingAdapter.setGroups(GroupManager.getInstance().getGroups());
                        groupSettingAdapter.notifyDataSetChanged();
                        Log.d("TAG", "Added " );
                        break;
                    case MODIFIED:
                        Log.d("TAG", "Modified Group: " + dc.getDocument().getData());
                        break;
                    case REMOVED:
                        Log.d("TAG", "Removed Group: ");
                        break;
                }
            }
        }
    };

    private final AdapterView.OnItemClickListener changeGroupListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

            final GroupManager gm = GroupManager.getInstance();
            if(position < gm.getSize()){
                GroupList currentGroup = gm.getGroup(position);

                cGroups.document(currentGroup.getId()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()){
                            Group group = documentSnapshot.toObject(Group.class);
                            String groupId = documentSnapshot.getId();
                            GroupMap currentGroup = new GroupMap(groupId, group);

                            ItemInventoryManager.getInstance().reset();
                            ShoppingListManager.getInstance().reset();
                            gm.setCurrentGroup(currentGroup);

                            ChangeGroup changeGroup =
                                    (ChangeGroup) getActivity();

                            changeGroup.changeGroup();

                            Toast.makeText(getContext(), "Welcome to " + group.getName(), Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(getContext(), "Please select group again", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }
    };


    /***********************************************************************************************
     ************************************* Inner class ********************************************
     ***********************************************************************************************/























    /*public void getGroupTransaction(){

        db.runTransaction(new Transaction.Function<ArrayList<Group>>() {
            @Override
            public ArrayList<Group> apply(Transaction transaction) throws FirebaseFirestoreException {

                ArrayList<Group> groups = new ArrayList<>();
                GroupManager gm = GroupManager.getInstance();
                ArrayList<GroupList> groupLists = new ArrayList<GroupList>();

                for(GroupList groupList : gm.getGroups()){
                    Group data = transaction.get(cGroup.document(groupList.getId())).toObject(Group.class);
                    groups.add(data);
                }

                for(GroupList groupList : gm.getGroups()){
                    Map<String, Object> update = new HashMap<>();
                    update.put("name", groupList.getName());
                    transaction.update(cGroup.document(groupList.getId()), update);
                }

                // Success
                return groups;
            }
        }).addOnSuccessListener(new OnSuccessListener<ArrayList<Group>>() {
            @Override
            public void onSuccess(ArrayList<Group> data) {
                groups = data;

                groupSettingAdapter.setGroups(groups);
                groupSettingAdapter.notifyDataSetChanged();
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("TAG", "Transaction failure.", e);
                    }
                });
    }*/
}
