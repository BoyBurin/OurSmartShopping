package com.example.seniorproject.smartshopping.controller.fragment.loginfragment;

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
import com.example.seniorproject.smartshopping.model.dao.Group;
import com.example.seniorproject.smartshopping.model.dao.User;
import com.example.seniorproject.smartshopping.view.customviewgroup.CustomViewGroupEditText;
import com.example.seniorproject.smartshopping.view.customviewgroup.CustomViewGroupUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class CreateAccountFragment extends Fragment {

    /***********************************************************************************************
     ************************************* Variable class ********************************************
     ***********************************************************************************************/
    public interface SaveInfoListener{
        void saveInfo();
    }

    private CustomViewGroupEditText customEmail;
    private CustomViewGroupEditText customUsername;
    private CustomViewGroupEditText customPassword;
    private CustomViewGroupEditText customCreateGroup;

    private Button btnCreateAccount;

    private DatabaseReference mDatabaseRef;
    private FirebaseAuth mAuth;

    private String userID;
    private String groupID;



    /***********************************************************************************************
     ************************************* Method class ********************************************
     ***********************************************************************************************/

    public CreateAccountFragment() {
        super();
    }

    @SuppressWarnings("unused")
    public static CreateAccountFragment newInstance() {
        CreateAccountFragment fragment = new CreateAccountFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_login_create_account, container, false);
        initInstances(rootView, savedInstanceState);
        return rootView;
    }

    private void init(Bundle savedInstanceState) {
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

    }

    @SuppressWarnings("UnusedParameters")
    private void initInstances(View rootView, Bundle savedInstanceState) {
        customEmail = (CustomViewGroupEditText) rootView.findViewById(R.id.customEmail);
        customUsername = (CustomViewGroupEditText) rootView.findViewById(R.id.customUsername);
        customPassword = (CustomViewGroupEditText) rootView.findViewById(R.id.customPassword);
        customCreateGroup = (CustomViewGroupEditText) rootView.findViewById(R.id.customCreateGroup);
        btnCreateAccount = (Button) rootView.findViewById(R.id.btnCreateAccount);

        customEmail.setTextView("Email");
        customUsername.setTextView("Username");
        customPassword.setTextView("Password");
        customCreateGroup.setTextView("Group");
        btnCreateAccount.setOnClickListener(saveInfo);
    }

    private boolean emptyFill(String name, String password, String group){
        return name.equals("") || password.equals("") || group.equals("");
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

    final View.OnClickListener saveInfo = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            final String email = customEmail.getTextFromEditText();
            final String password = customPassword.getTextFromEditText();
            final String groupName = customCreateGroup.getTextFromEditText();
            final String username = customUsername.getTextFromEditText();

            if(emptyFill(email, password, groupName)){
                Toast.makeText(getContext(), "Please complete your information", Toast.LENGTH_SHORT);
                return;
            }


            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        User user = new User(username, email, "");
                        userID = mAuth.getCurrentUser().getUid();
                        mDatabaseRef.child("users").child(userID).setValue(user)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        groupID = mDatabaseRef.child("groups").push().getKey();
                                        Log.d("tag: ", groupID);
                                        Group group = new Group();
                                        group.setName(groupName);
                                        mDatabaseRef.child("groups").child(groupID).setValue(group)
                                                .addOnCompleteListener(userInGroupListener);
                                    }
                                });
                    }else {
                        Log.w("CreateAccount: ", "createUserWithEmail:failure", task.getException());
                        Toast.makeText(getContext(), "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    };


    final OnCompleteListener userInGroupListener = new OnCompleteListener() {
        @Override
        public void onComplete(@NonNull Task task) {
            mDatabaseRef.child("useringroup").child(groupID)
                    .child(userID).setValue(true).addOnCompleteListener(groupInUserListener);
        }
    };

    final OnCompleteListener groupInUserListener = new OnCompleteListener() {
        @Override
        public void onComplete(@NonNull Task task) {
            mDatabaseRef.child("groupinuser").child(userID)
                    .child(groupID).setValue(true).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(getContext(), "Create account Successful", Toast.LENGTH_SHORT).show();
                    SaveInfoListener saveInfoListener = (SaveInfoListener) getActivity();
                    saveInfoListener.saveInfo();
                }
            });
        }
    };


    /***********************************************************************************************
     ************************************* Inner class ********************************************
     ***********************************************************************************************/

}
