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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.seniorproject.smartshopping.R;
import com.example.seniorproject.smartshopping.model.dao.group.GroupList;
import com.example.seniorproject.smartshopping.model.manager.Contextor;
import com.example.seniorproject.smartshopping.model.manager.group.GroupManager;
import com.example.seniorproject.smartshopping.view.customviewgroup.CustomViewGroupEditText;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

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

    public interface SelectGroupListener{
        void gotToSelectGroupListener();
    }

    public interface VisibleLoginFragmentListener{
        void visible(boolean visible);
    }

    private CustomViewGroupEditText customGroupUserName;
    private CustomViewGroupEditText customGroupPassword;
    private Button btnLogin;
    private Button btnCreateUser;
    private ProgressBar progressBarLogin;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private CollectionReference cUsers;

    //private String username = "BoyBurin";



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


        db = FirebaseFirestore.getInstance();
        cUsers = db.collection("users");

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


    }

    private void initInstances(View rootView) {
        customGroupUserName = (CustomViewGroupEditText) rootView.findViewById(R.id.customGroupUserName);
        customGroupPassword = (CustomViewGroupEditText) rootView.findViewById(R.id.customGroupPassword);

        customGroupUserName.setImage(getContext().getDrawable(R.drawable.user_login));
        customGroupUserName.setHintEditText("Username");
        customGroupUserName.setEditTextInputTypeToText();

        customGroupPassword.setEditTextInputTypeToPassword();
        customGroupPassword.setImage(getContext().getDrawable(R.drawable.password_login));
        customGroupPassword.setHintEditText("Password");

        btnLogin = (Button)rootView.findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(loginOnClickListener);


        btnCreateUser = (Button)rootView.findViewById(R.id.btnCreateAccount);
        btnCreateUser.setOnClickListener(loginOnClickListener);

        progressBarLogin = (ProgressBar) rootView.findViewById(R.id.progressbarLogin);

    }

    @Override
    public void onStart() {

        super.onStart();
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

                    progressBarLogin.setVisibility(View.VISIBLE);

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

                Toast.makeText(Contextor.getInstance().getContext(), "signInWithEmail:success", Toast.LENGTH_SHORT).show();
            } else {
                // If sign in fails, display a message to the user.
                progressBarLogin.setVisibility(View.GONE);
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

                VisibleLoginFragmentListener visibleLoginFragmentListener =
                        (VisibleLoginFragmentListener) getActivity();
                visibleLoginFragmentListener.visible(false);

                Log.d("TAG", "onAuthStateChanged:signed_in:" + user.getDisplayName());

                String userID = firebaseAuth.getCurrentUser().getUid();

                cUsers.document(userID).collection("groups")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (DocumentSnapshot document : task.getResult()) {
                                        GroupList group = (GroupList) document.toObject(GroupList.class);
                                        GroupManager gm = GroupManager.getInstance();
                                        gm.addGroup(group);
                                    }

                                    SelectGroupListener selectGroupListener = (SelectGroupListener) getActivity();
                                    selectGroupListener.gotToSelectGroupListener();

                                } else {
                                    Log.d("TAG", "Error getting documents: ", task.getException());
                                    Toast.makeText(getContext(), "Error getting groups", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }

        }
    };



    /***********************************************************************************************
     ************************************* Inner class ********************************************
     ***********************************************************************************************/

}
