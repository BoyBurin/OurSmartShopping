package com.example.seniorproject.smartshopping.controller.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.seniorproject.smartshopping.R;
import com.example.seniorproject.smartshopping.controller.fragment.loginfragment.LoginFragment;
import com.google.android.gms.common.SignInButton;

public class LoginActivity extends AppCompatActivity implements LoginFragment.LoginFragmentListener {

    /***********************************************************************************************
     ************************************* Variable class ********************************************
     ***********************************************************************************************/

    private ImageView imageView;
    private SignInButton btnfacebook;

    /***********************************************************************************************
     ************************************* Methods ********************************************
     ***********************************************************************************************/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initInstances();

        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.containerLogin, LoginFragment.newInstance())
                    .commit();
        }
    }

    private void initInstances(){


    }

    @Override
    public void goToMain() {
        finish();
    }


    /***********************************************************************************************
     ************************************* Listener variable ********************************************
     ***********************************************************************************************/



}
