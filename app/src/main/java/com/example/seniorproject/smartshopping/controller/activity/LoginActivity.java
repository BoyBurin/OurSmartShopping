package com.example.seniorproject.smartshopping.controller.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.seniorproject.smartshopping.R;
import com.example.seniorproject.smartshopping.controller.fragment.dialogfragment.FragmentDialogAddShoppingList;
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
        LoginFragment.VisibleLoginFragmentListener, SelectGroupFragment.VisibleSelectGroupFragmentListener,
        CreateAccountFragment.CancelInfoListener, CreateAccountFragment.PickImageUser,
        CreateAccountFragment.PickImageGroup {

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

        GroupManager.getInstance().reset();
        progressbarLogin = (ProgressBar) findViewById(R.id.progressbarLogin);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CreateAccountFragment.RC_PHOTO_USER) {
            if (resultCode == RESULT_OK) {
                Fragment createAccountFragment = getSupportFragmentManager()
                        .findFragmentByTag(CREATEACCOUNTFRAGMENT);

                createAccountFragment.onActivityResult(requestCode, resultCode, data);
            }
        }

        if (requestCode == CreateAccountFragment.RC_PHOTO_GROUP) {
            if (resultCode == RESULT_OK) {
                Fragment createAccountFragment = getSupportFragmentManager()
                        .findFragmentByTag(CREATEACCOUNTFRAGMENT);

                createAccountFragment.onActivityResult(requestCode, resultCode, data);
            }
        }
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
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
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
                .replace(R.id.containerLogin, selectGroupFragment, SELECTGROUPFRAGMENT)
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

    @Override
    public void cancel() {
        CreateAccountFragment createAccountFragment =
                (CreateAccountFragment) getSupportFragmentManager().findFragmentByTag(CREATEACCOUNTFRAGMENT);

        LoginFragment loginFragment =
                (LoginFragment) getSupportFragmentManager().findFragmentByTag(LOGINFRAGMENT);

        getSupportFragmentManager().beginTransaction()
                .remove(createAccountFragment)
                .show(loginFragment)
                .commit();
    }

    @Override
    public void pickImageUser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/jpeg");
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        startActivityForResult(Intent.createChooser(intent, "Complete action using"),
                CreateAccountFragment.RC_PHOTO_USER);
    }

    @Override
    public void pickImageGroup() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/jpeg");
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        startActivityForResult(Intent.createChooser(intent, "Complete action using"),
                CreateAccountFragment.RC_PHOTO_GROUP);
    }


    /***********************************************************************************************
     ************************************* Listener variable ********************************************
     ***********************************************************************************************/



}
