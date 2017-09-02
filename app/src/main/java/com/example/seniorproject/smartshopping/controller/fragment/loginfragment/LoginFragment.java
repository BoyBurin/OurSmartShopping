package com.example.seniorproject.smartshopping.controller.fragment.loginfragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.seniorproject.smartshopping.R;
import com.example.seniorproject.smartshopping.view.customviewgroup.CustomViewGroupEditText;

public class LoginFragment extends Fragment {

    /***********************************************************************************************
     ************************************* Variable class ********************************************
     ***********************************************************************************************/

    private CustomViewGroupEditText customGroupUserName;
    private CustomViewGroupEditText customGroupPassword;


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
    private void onRestoreInstanceState(Bundle savedInstanceState) {
        // Restore Instance State here
    }


    /***********************************************************************************************
     ************************************* Listener variables ********************************************
     ***********************************************************************************************/


    /***********************************************************************************************
     ************************************* Inner class ********************************************
     ***********************************************************************************************/

}
