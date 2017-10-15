package com.example.seniorproject.smartshopping.controller.fragment.loginfragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.seniorproject.smartshopping.R;
import com.example.seniorproject.smartshopping.model.dao.Group;
import com.example.seniorproject.smartshopping.model.dao.GroupMap;
import com.example.seniorproject.smartshopping.model.dao.User;
import com.example.seniorproject.smartshopping.model.dao.UserMap;
import com.example.seniorproject.smartshopping.model.datatype.MutableInteger;
import com.example.seniorproject.smartshopping.model.manager.GroupManager;
import com.example.seniorproject.smartshopping.model.manager.UserManager;
import com.example.seniorproject.smartshopping.view.adapter.GroupAdapter;
import com.example.seniorproject.smartshopping.view.adapter.UserAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class SelectGroupFragment extends Fragment {

    /***********************************************************************************************
     ************************************* Variable class ********************************************
     ***********************************************************************************************/
    public interface SaveCurrentGroupListener{
        void saveCurrentGroup();
    }

    public interface VisibleSelectGroupFragmentListener{
        void visibleSelectGroup(boolean visible);
    }

    private ListView listView;
    private GroupManager gm;
    private GroupAdapter groupAdapter;
    private MutableInteger lastPositionInteger;




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
        lastPositionInteger = new MutableInteger(-1);
        groupAdapter = new GroupAdapter(lastPositionInteger);
        gm = GroupManager.getInstance();

    }

    @SuppressWarnings("UnusedParameters")
    private void initInstances(View rootView, Bundle savedInstanceState) {
        listView = (ListView) rootView.findViewById(R.id.listViewGroup);
        groupAdapter.setGroups(gm.getGroups());
        listView.setAdapter(groupAdapter);
        Log.d("tag: ", gm.getGroup(0).getGroup().getName());

        listView.setOnItemClickListener(getCurrentGroupListener);
        groupAdapter.notifyDataSetChanged();
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

    final AdapterView.OnItemClickListener getCurrentGroupListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

            GroupMap currentGroup = gm.getGroup(position);
            gm.setCurrentGroup(currentGroup);
            SaveCurrentGroupListener saveCurrentGroupListener =
                    (SaveCurrentGroupListener) getActivity();

            saveCurrentGroupListener.saveCurrentGroup();
        }
    };


    /***********************************************************************************************
     ************************************* Inner class ********************************************
     ***********************************************************************************************/

}
