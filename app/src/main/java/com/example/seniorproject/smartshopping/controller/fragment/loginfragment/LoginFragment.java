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
import android.widget.Toast;

import com.example.seniorproject.smartshopping.R;
import com.example.seniorproject.smartshopping.view.customviewgroup.CustomViewGroupEditText;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginFragment extends Fragment {

    /***********************************************************************************************
     ************************************* Variable class ********************************************
     ***********************************************************************************************/

    private CustomViewGroupEditText customGroupUserName;
    private CustomViewGroupEditText customGroupPassword;
    private Button btnLogin;
    private Button btnCreateUser;

    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mMessagesDatabaseReference;


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

                    //Toast.makeText(getActivity(), email + " : " + password, Toast.LENGTH_SHORT).show();

                    mAuth.signInWithEmailAndPassword(email, password).
                            addOnCompleteListener(getActivity(), loginOnCompleteListener);
                    customGroupUserName.setTextToEditText("");
                    customGroupPassword.setTextToEditText("");
                } else{
                    String email = user.getEmail();
                    Toast.makeText(getActivity(), email, Toast.LENGTH_SHORT).show();
                    mAuth.getInstance().signOut();
                }
            }

            if(view == btnCreateUser){
                FirebaseUser user = mAuth.getCurrentUser();
                if(user == null){
                    String email = "boyzaburin@hotmail.com";
                    String password = "123456";
                    String username = "BoyBurin";

                    mAuth.createUserWithEmailAndPassword(email, password);

                    mAuth.signInWithEmailAndPassword(email, password).
                            addOnCompleteListener(getActivity(), loginOnCompleteListener);

                    String id = mAuth.getCurrentUser().getUid();
                    mMessagesDatabaseReference.child("users").child(id).child("username").setValue(username);
                    Toast.makeText(getActivity(), "Create Account Success", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getActivity(), user.getEmail(), Toast.LENGTH_SHORT).show();
                }
            }
        }
    };


    final OnCompleteListener<AuthResult> loginOnCompleteListener =  new OnCompleteListener<AuthResult>() {
        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
            if (task.isSuccessful()) {
                // Sign in success, update UI with the signed-in user's information
                Toast.makeText(getActivity(), "signInWithEmail:success", Toast.LENGTH_SHORT).show();
                FirebaseUser user = mAuth.getCurrentUser();
            } else {
                // If sign in fails, display a message to the user.
                Toast.makeText(getActivity(), "signInWithEmail:failure", Toast.LENGTH_SHORT).show();
            }
        }
    };


    /***********************************************************************************************
     ************************************* Inner class ********************************************
     ***********************************************************************************************/

}
