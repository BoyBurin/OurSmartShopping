package com.example.seniorproject.smartshopping.controller.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.seniorproject.smartshopping.R;
import com.example.seniorproject.smartshopping.controller.fragment.mainfragment.InventoryFragment;
import com.example.seniorproject.smartshopping.controller.fragment.loginfragment.LoginFragment;
import com.example.seniorproject.smartshopping.controller.fragment.mainfragment.ShoppingListFragment;
import com.example.seniorproject.smartshopping.model.dao.Group;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    /***********************************************************************************************
     ************************************* Variable class ********************************************
     ***********************************************************************************************/

    private ImageButton btnPromotion;
    private ImageButton btnGroup;
    private ImageButton btnInventory;
    private ImageButton btnShoppingList;
    private ImageButton btnShoppingHistory;
    private ImageButton btnSetting;

    private FirebaseDatabase mRootRef;
    private DatabaseReference mGroupRef;


    /***********************************************************************************************
     ************************************* Methods ********************************************
     ***********************************************************************************************/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init(){
        btnPromotion = (ImageButton) findViewById(R.id.btnPromotion);
        btnPromotion.setOnClickListener(topBarOnClickListener);

        btnSetting = (ImageButton) findViewById(R.id.btnSetting);
        btnSetting.setOnClickListener(topBarOnClickListener);

        btnShoppingList = (ImageButton) findViewById(R.id.btnShoppingList);
        btnShoppingList.setOnClickListener(topBarOnClickListener);

        mRootRef = FirebaseDatabase.getInstance();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null){
            Toast.makeText(this, "No User Login", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
    }


    /***********************************************************************************************
     ************************************* Listener variable ********************************************
     ***********************************************************************************************/

    final View.OnClickListener topBarOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(view == btnPromotion){
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(MainActivity.this, "Logout Success", Toast.LENGTH_SHORT).show();
            }

            if(view == btnSetting){
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                Group myFirstGroup = new Group();
                myFirstGroup.setName("BoyLand");
                myFirstGroup.addMember(user.getUid());
                mGroupRef.push().setValue(myFirstGroup);

                Toast.makeText(MainActivity.this, "Create Group Success; " + myFirstGroup.getName(),
                        Toast.LENGTH_SHORT).show();
            }

            if(view == btnInventory){

                InventoryFragment inventoryFragment = (InventoryFragment)
                        getSupportFragmentManager().findFragmentByTag("InventoryFragment");

                getSupportFragmentManager().beginTransaction()
                        .attach(inventoryFragment)
                        .commit();
            }
        }
    };


}
