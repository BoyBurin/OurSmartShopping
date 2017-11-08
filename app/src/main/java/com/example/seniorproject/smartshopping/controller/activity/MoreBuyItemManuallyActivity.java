package com.example.seniorproject.smartshopping.controller.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.seniorproject.smartshopping.R;
import com.example.seniorproject.smartshopping.controller.fragment.buyitemfragment.MoreBuyItemManuallyFragment;
import com.example.seniorproject.smartshopping.model.manager.group.GroupManager;

public class MoreBuyItemManuallyActivity extends AppCompatActivity {

    /***********************************************************************************************
     ************************************* Variable class ********************************************
     ***********************************************************************************************/

    final String MORE_BUY_ITEM_MANUALLY_FRAGMENT = "moreBuyItemManuallyFragment";



    /***********************************************************************************************
     ************************************* Methods ********************************************
     ***********************************************************************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_buy_item);

        initInstances();

        if(savedInstanceState == null){
            MoreBuyItemManuallyFragment moreBuyItemManuallyFragmentbuy = MoreBuyItemManuallyFragment.newInstance();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.containerMoreBuyItem, moreBuyItemManuallyFragmentbuy,
                            MORE_BUY_ITEM_MANUALLY_FRAGMENT)
                    .commit();
        }
    }

    public void initInstances() {

        setTitle(GroupManager.getInstance().getCurrentGroup().getGroup().getName());
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }

        if(item.getItemId() == R.id.action_add){

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add, menu);
        return true;
    }
}
