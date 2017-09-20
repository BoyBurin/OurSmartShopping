package com.example.seniorproject.smartshopping.controller.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.seniorproject.smartshopping.R;
import com.example.seniorproject.smartshopping.controller.fragment.loginfragment.CreateAccountFragment;
import com.example.seniorproject.smartshopping.controller.fragment.loginfragment.LoginFragment;
import com.google.android.gms.common.SignInButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity implements LoginFragment.LoginFragmentListener,
        LoginFragment.CraateAccountFragmentListener, CreateAccountFragment.SaveInfoListener {

    /***********************************************************************************************
     ************************************* Variable class ********************************************
     ***********************************************************************************************/

    private ImageView imageView;
    private SignInButton btnfacebook;

    private final String CREATEACCOUNTFRAGMENT = "createAccountFragment";
    private final String LOGINFRAGMENT = "loginFragment";

    /***********************************************************************************************
     ************************************* Methods ********************************************
     ***********************************************************************************************/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initInstances();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            FrameLayout container = (FrameLayout) findViewById(R.id.containerLogin);
            container.setVisibility(View.INVISIBLE);
            ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressbarLogin);
            progressBar.setVisibility(View.VISIBLE);
        }

        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.containerLogin, LoginFragment.newInstance(), LOGINFRAGMENT)
                    .commit();
        }
    }

    private void initInstances(){


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
    }


    /***********************************************************************************************
     ************************************* Listener variable ********************************************
     ***********************************************************************************************/



}
