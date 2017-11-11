package com.example.seniorproject.smartshopping.controller.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.seniorproject.smartshopping.R;
import com.example.seniorproject.smartshopping.controller.fragment.purchaseitem.PurchaseItemAddFragment;
import com.example.seniorproject.smartshopping.controller.fragment.purchaseitem.PurchaseItemManuallyFragment;
import com.example.seniorproject.smartshopping.model.manager.group.GroupManager;

public class PurchaseItemManuallyActivity extends AppCompatActivity implements PurchaseItemAddFragment.MainFragmentTag{

    /***********************************************************************************************
     ************************************* Variable class ********************************************
     ***********************************************************************************************/

    private static final String PURCHASE_ITEM__FRAGMENT_TAG = "purchaseItemFragmentTag";





    /***********************************************************************************************
     ************************************* Methods ********************************************
     ***********************************************************************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_buy_item);

        initInstances();

        if(savedInstanceState == null){
            PurchaseItemManuallyFragment purchaseItemManuallyFragmentbuy = PurchaseItemManuallyFragment.newInstance();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.containerMoreBuyItem, purchaseItemManuallyFragmentbuy,
                            PURCHASE_ITEM__FRAGMENT_TAG)
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
        return super.onOptionsItemSelected(item);
    }


    /***********************************************************************************************
     ************************************* Implementation ********************************************
     ***********************************************************************************************/

    @Override
    public String getMainFragmentTag() {
        return PURCHASE_ITEM__FRAGMENT_TAG;
    }
}
