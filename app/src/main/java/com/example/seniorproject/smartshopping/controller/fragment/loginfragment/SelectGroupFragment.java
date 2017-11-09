package com.example.seniorproject.smartshopping.controller.fragment.loginfragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.example.seniorproject.smartshopping.R;
import com.example.seniorproject.smartshopping.model.dao.group.Group;
import com.example.seniorproject.smartshopping.model.dao.group.GroupList;
import com.example.seniorproject.smartshopping.model.dao.group.GroupMap;
import com.example.seniorproject.smartshopping.model.manager.group.GroupManager;
import com.example.seniorproject.smartshopping.view.recyclerviewadapter.SelectedGroupRecyclerViewAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class SelectGroupFragment extends Fragment implements SelectedGroupRecyclerViewAdapter.OnItemClickListener{


    /***********************************************************************************************
     ************************************* Variable class ********************************************
     ***********************************************************************************************/
    public interface SaveCurrentGroupListener{
        void saveCurrentGroup();
    }

    public interface VisibleSelectGroupFragmentListener{
        void visibleSelectGroup(boolean visible);
    }

    private GroupManager gm;
    private RecyclerView recyclerView;
    private SelectedGroupRecyclerViewAdapter selectedGroupRecyclerViewAdapter;

    private FirebaseFirestore db;
    private CollectionReference cGroup;




    /***********************************************************************************************
     ************************************* Method class ********************************************
     ***********************************************************************************************/

    public SelectGroupFragment() {
        super();
    }

    @SuppressWarnings("unused")
    public static SelectGroupFragment newInstance() {
        SelectGroupFragment fragment = new SelectGroupFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(savedInstanceState);

        db = FirebaseFirestore.getInstance();
        cGroup = db.collection("groups");

        if (savedInstanceState != null)
            onRestoreInstanceState(savedInstanceState);

        VisibleSelectGroupFragmentListener visibleSelectGroupFragmentListener =
                (VisibleSelectGroupFragmentListener) getActivity();

        visibleSelectGroupFragmentListener.visibleSelectGroup(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_login_select_group, container, false);
        initInstances(rootView, savedInstanceState);
        return rootView;
    }

    private void init(Bundle savedInstanceState) {
        gm = GroupManager.getInstance();
        selectedGroupRecyclerViewAdapter = new SelectedGroupRecyclerViewAdapter(getContext());

    }

    @SuppressWarnings("UnusedParameters")
    private void initInstances(View rootView, Bundle savedInstanceState) {
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        selectedGroupRecyclerViewAdapter.setGroups(gm.getGroups());
        selectedGroupRecyclerViewAdapter.setItemClickListener(this);
        recyclerView.setAdapter(selectedGroupRecyclerViewAdapter);
        selectedGroupRecyclerViewAdapter.notifyDataSetChanged();


    }

    @Override
    public void onDestroy() {
        super.onDestroy();

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

    /*final AdapterView.OnItemClickListener getCurrentGroupListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

            String groupId = gm.getGroup(position).getId();
            cGroup.document(groupId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if(documentSnapshot.exists()){
                        Group group = documentSnapshot.toObject(Group.class);
                        String groupId = documentSnapshot.getId();
                        GroupMap currentGroup = new GroupMap(groupId, group);

                        gm.setCurrentGroup(currentGroup);

                        SaveCurrentGroupListener saveCurrentGroupListener =
                                (SaveCurrentGroupListener) getActivity();

                        saveCurrentGroupListener.saveCurrentGroup();

                        Toast.makeText(getContext(), "Welcome to " + group.getName(), Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(getContext(), "Please select group again", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    };*/


    /***********************************************************************************************
     ************************************* Inner class ********************************************
     ***********************************************************************************************/


    /***********************************************************************************************
     ************************************* Implementation ********************************************
     ***********************************************************************************************/
    @Override
    public void onItemClick(GroupList group) {
        String groupId = group.getId();
        cGroup.document(groupId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    Group group = documentSnapshot.toObject(Group.class);
                    String groupId = documentSnapshot.getId();
                    GroupMap currentGroup = new GroupMap(groupId, group);

                    gm.setCurrentGroup(currentGroup);

                    SaveCurrentGroupListener saveCurrentGroupListener =
                            (SaveCurrentGroupListener) getActivity();

                    saveCurrentGroupListener.saveCurrentGroup();

                    Toast.makeText(getContext(), "Welcome to " + group.getName(), Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getContext(), "Please select group again", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
