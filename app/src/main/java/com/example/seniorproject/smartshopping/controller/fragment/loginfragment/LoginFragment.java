package com.example.seniorproject.smartshopping.controller.fragment.loginfragment;

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
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.seniorproject.smartshopping.R;
import com.example.seniorproject.smartshopping.model.dao.Group;
import com.example.seniorproject.smartshopping.model.dao.GroupMap;
import com.example.seniorproject.smartshopping.model.dao.User;
import com.example.seniorproject.smartshopping.model.manager.Contextor;
import com.example.seniorproject.smartshopping.model.manager.GroupManager;
import com.example.seniorproject.smartshopping.view.customviewgroup.CustomViewGroupEditText;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class LoginFragment extends Fragment {

    /***********************************************************************************************
     ************************************* Variable class ********************************************
     ***********************************************************************************************/

    public interface LoginFragmentListener{
        void goToMain();
    }

    public interface CraateAccountFragmentListener{
        void goToCreateAccount();
    }

    private CustomViewGroupEditText customGroupUserName;
    private CustomViewGroupEditText customGroupPassword;
    private Button btnLogin;
    private Button btnCreateUser;

    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mMessagesDatabaseReference;

    private String username = "BoyBurin";



    /***********************************************************************************************
     ************************************* Method class ********************************************
     ***********************************************************************************************/

    public LoginFragment() {
        super();
    }

    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);
        initInstances(rootView);
        return rootView;
    }

    private void init(Bundle savedInstanceState) {
        // Init Fragment level's variable(s) here
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mMessagesDatabaseReference = mFirebaseDatabase.getReference();


    }

    private void initInstances(View rootView) {
        customGroupUserName = (CustomViewGroupEditText) rootView.findViewById(R.id.customGroupUserName);
        customGroupPassword = (CustomViewGroupEditText) rootView.findViewById(R.id.customGroupPassword);

        customGroupUserName.setTextView("User");
        customGroupUserName.setHintEditText("Username");
        customGroupUserName.setEditTextInputTypeToText();

        customGroupPassword.setEditTextInputTypeToPassword();
        customGroupPassword.setTextView("Pass");
        customGroupPassword.setHintEditText("Password");

        btnLogin = (Button)rootView.findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(loginOnClickListener);


        btnCreateUser = (Button)rootView.findViewById(R.id.btnCreateAccount);
        btnCreateUser.setOnClickListener(loginOnClickListener);

    }

    @Override
    public void onStart() {

        super.onStart();
        //updateUI(currentUser);
        mAuth.addAuthStateListener(mAuthListener);

    }

    @Override
    public void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(mAuthListener);
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
    private void onRestoreInstanceState(Bundle savedInstanceState) {
        // Restore Instance State here
    }


    /***********************************************************************************************
     ************************************* Listener variables ********************************************
     ***********************************************************************************************/


    final View.OnClickListener loginOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(view == btnLogin){
                FirebaseUser user = mAuth.getCurrentUser();
                if(user == null){
                    String email = customGroupUserName.getTextFromEditText().toString();
                    String password = customGroupPassword.getTextFromEditText().toString();

                    if(email == null || password == null || email.equals("") || password.equals("")){
                        return;
                    }

                    //customGroupUserName.setTextToEditText("");
                    customGroupPassword.setTextToEditText("");

                    mAuth.signInWithEmailAndPassword(email, password).
                            addOnCompleteListener(loginOnCompleteListener);
                }
            }

            if(view == btnCreateUser){
                FirebaseUser user = mAuth.getCurrentUser();
                if(user == null){
                    CraateAccountFragmentListener craateAccountFragmentListener =
                            (CraateAccountFragmentListener) getActivity();
                    craateAccountFragmentListener.goToCreateAccount();

                }
            }
        }
    };


    final OnCompleteListener<AuthResult> loginOnCompleteListener =  new OnCompleteListener<AuthResult>() {
        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
            if (task.isSuccessful()) {
                // Sign in success, update UI with the signed-in user's information
                Toast.makeText(Contextor.getInstance().getContext(), "signInWithEmail:success", Toast.LENGTH_SHORT).show();
            } else {
                // If sign in fails, display a message to the user.
                Toast.makeText(Contextor.getInstance().getContext(), "signInWithEmail:failure", Toast.LENGTH_SHORT).show();
            }
        }
    };

    final OnCompleteListener<AuthResult> createdOnCompleteListener =  new OnCompleteListener<AuthResult>() {
        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
            if (task.isSuccessful()) {
                // Sign in success, update UI with the signed-in user's information
                Toast.makeText(Contextor.getInstance().getContext(), "createWithEmail:success", Toast.LENGTH_SHORT).show();
            } else {
                // If sign in fails, display a message to the user.
                Toast.makeText(Contextor.getInstance().getContext(), "createWithEmail:failure", Toast.LENGTH_SHORT).show();
            }
        }
    };


    final FirebaseAuth.AuthStateListener mAuthListener = new FirebaseAuth.AuthStateListener() {
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
                // User is signed in
                Log.d("TAG", "onAuthStateChanged:signed_in:" + user.getDisplayName());

                String userID = mAuth.getCurrentUser().getUid();

                mMessagesDatabaseReference.child("groupinuser").child(userID)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for(DataSnapshot data : dataSnapshot.getChildren()){
                                    mMessagesDatabaseReference.child("groups")
                                            .child(data.getKey().toString())
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            GroupManager gm = GroupManager.getInstance();
                                            String groupID = dataSnapshot.getKey();
                                            Group group = dataSnapshot.getValue(Group.class);
                                            gm.addGroup(new GroupMap(groupID, group));
                                            gm.setCurrentGroup(gm.getGroup(0));
                                            Log.d("Group Name: ", gm.getCurrentGroup().getId());

                                            // Go to Main
                                            LoginFragmentListener loginFragmentListener = (LoginFragmentListener) getActivity();
                                            loginFragmentListener.goToMain();

                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });


                // Authenticated successfully with authData


            } else {
                // User is signed out
                Log.d("TAG", "onAuthStateChanged:signed_out");
            }
        }
    };



    /***********************************************************************************************
     ************************************* Inner class ********************************************
     ***********************************************************************************************/

}
