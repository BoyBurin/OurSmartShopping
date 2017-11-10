package com.example.seniorproject.smartshopping.controller.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.seniorproject.smartshopping.R;
import com.example.seniorproject.smartshopping.controller.fragment.loginfragment.CreateAccountFragment;
import com.example.seniorproject.smartshopping.controller.fragment.loginfragment.LoginFragment;
import com.example.seniorproject.smartshopping.controller.fragment.loginfragment.SelectGroupFragment;
import com.example.seniorproject.smartshopping.model.manager.group.GroupManager;
import com.example.seniorproject.smartshopping.model.manager.iteminventory.ItemInventoryManager;
import com.example.seniorproject.smartshopping.model.manager.shoppinglist.ShoppingListManager;
import com.google.android.gms.common.SignInButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity implements LoginFragment.LoginFragmentListener,
        LoginFragment.CraateAccountFragmentListener, CreateAccountFragment.SaveInfoListener,
        LoginFragment.SelectGroupListener, SelectGroupFragment.SaveCurrentGroupListener,
        LoginFragment.VisibleLoginFragmentListener, SelectGroupFragment.VisibleSelectGroupFragmentListener {

    /***********************************************************************************************
     ************************************* Variable class ********************************************
     ***********************************************************************************************/

    private ImageView imageView;
    private SignInButton btnfacebook;
    private ProgressBar progressbarLogin;

    private final String CREATEACCOUNTFRAGMENT = "createAccountFragment";
    private final String LOGINFRAGMENT = "loginFragment";
    private final String SELECTGROUPFRAGMENT = "selectGroupFragment";

    /***********************************************************************************************
     ************************************* Methods ********************************************
     ***********************************************************************************************/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initInstances();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.containerLogin, LoginFragment.newInstance(), LOGINFRAGMENT)
                    .commit();
        }
    }

    private void initInstances(){

        ShoppingListManager.getInstance().reset();
        GroupManager.getInstance().reset();
        progressbarLogin = (ProgressBar) findViewById(R.id.progressbarLogin);

    }

    @Override
    public void goToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void goToCreateAccount() {
        CreateAccountFragment createAccountFragment = CreateAccountFragment.newInstance();
        LoginFragment loginFragment = (LoginFragment) getSupportFragmentManager().findFragmentByTag(LOGINFRAGMENT);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.containerLogin, createAccountFragment, CREATEACCOUNTFRAGMENT)
                .hide(loginFragment)
                .show(createAccountFragment)
                .commit();
    }

    @Override
    public void saveInfo() {
        CreateAccountFragment createAccountFragment =
                (CreateAccountFragment) getSupportFragmentManager().findFragmentByTag(CREATEACCOUNTFRAGMENT);

        LoginFragment loginFragment =
                (LoginFragment) getSupportFragmentManager().findFragmentByTag(LOGINFRAGMENT);

        getSupportFragmentManager().beginTransaction()
                .remove(createAccountFragment)
                .show(loginFragment)
                .commit();
        goToMain();
    }

    @Override
    public void gotToSelectGroupListener() {
        SelectGroupFragment selectGroupFragment = SelectGroupFragment.newInstance();
        LoginFragment loginFragment = (LoginFragment) getSupportFragmentManager().findFragmentByTag(LOGINFRAGMENT);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.containerLogin, selectGroupFragment, SELECTGROUPFRAGMENT)
                .hide(loginFragment)
                .show(selectGroupFragment)
                .commit();
    }

    @Override
    public void saveCurrentGroup() {
        SelectGroupFragment selectGroupFragment =
                (SelectGroupFragment) getSupportFragmentManager().findFragmentByTag(SELECTGROUPFRAGMENT);

        getSupportFragmentManager().beginTransaction()
                .remove(selectGroupFragment)
                .commit();

        goToMain();
    }

    @Override
    public void visible(boolean visible) {
        FrameLayout container = (FrameLayout) findViewById(R.id.containerLogin);
        if(visible) {
            container.setVisibility(View.VISIBLE);
            progressbarLogin.setVisibility(View.INVISIBLE);
        }else {
            container.setVisibility(View.INVISIBLE);
            progressbarLogin.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void visibleSelectGroup(boolean visible) {
        FrameLayout container = (FrameLayout) findViewById(R.id.containerLogin);
        if(visible) {
            container.setVisibility(View.VISIBLE);
            progressbarLogin.setVisibility(View.INVISIBLE);
        }else {
            container.setVisibility(View.INVISIBLE);
            progressbarLogin.setVisibility(View.VISIBLE);
        }
    }


    /***********************************************************************************************
     ************************************* Listener variable ********************************************
     ***********************************************************************************************/



}
