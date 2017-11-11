package com.example.seniorproject.smartshopping.controller.fragment.settingfragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.seniorproject.smartshopping.R;
import com.example.seniorproject.smartshopping.model.dao.user.User;
import com.example.seniorproject.smartshopping.view.customviewgroup.ItemView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class SettingFragment extends Fragment {

    /***********************************************************************************************
     ************************************* Variable class ********************************************
     ***********************************************************************************************/
    public interface SignOutListener{
        void signOut();
    }

    public interface GroupSetting{
        void goToGroupSetting();
    }


    private Button logout;
    private Button btnGroups;
    private ItemView profile;

    private FirebaseFirestore db;
    private DocumentReference dUser;

    private User user;


    /***********************************************************************************************
     ************************************* Method class ********************************************
     ***********************************************************************************************/

    public SettingFragment() {
        super();
    }

    @SuppressWarnings("unused")
    public static SettingFragment newInstance() {
        SettingFragment fragment = new SettingFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_main_setting, container, false);
        initInstances(rootView, savedInstanceState);
        return rootView;
    }

    private void init(Bundle savedInstanceState) {

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser != null) {
            String id = firebaseUser.getUid();
            db = FirebaseFirestore.getInstance();
            dUser = db.collection("users").document(id);

        }

    }

    @SuppressWarnings("UnusedParameters")
    private void initInstances(View rootView, Bundle savedInstanceState) {
        logout = (Button)rootView.findViewById(R.id.btnLogout);
        btnGroups = (Button) rootView.findViewById(R.id.btnGroups);
        profile = (ItemView) rootView.findViewById(R.id.profile);

        logout.setOnClickListener(logoutListener);
        btnGroups.setOnClickListener(groupSettingListener);


        dUser.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        user = document.toObject(User.class);
                        profile.setImageUrl(user.getPhotoUrl());
                        profile.setNameText(user.getName().toString());
                    } else {
                        Log.d("TAG", "No such document");
                    }
                } else {
                    Log.d("TAG", "get failed with ", task.getException());
                }
            }
        });
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

    final View.OnClickListener logoutListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if(user != null){
                FirebaseAuth.getInstance().signOut();

                SignOutListener signOutListener = (SignOutListener) getActivity();
                signOutListener.signOut();

            }
        }
    };

    final View.OnClickListener groupSettingListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            GroupSetting groupSetting = (GroupSetting) getActivity();
            groupSetting.goToGroupSetting();
        }
    };


    /***********************************************************************************************
     ************************************* Inner class ********************************************
     ***********************************************************************************************/

}
