package com.example.seniorproject.smartshopping.controller.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.seniorproject.smartshopping.R;
import com.example.seniorproject.smartshopping.controller.fragment.mainfragment.MoreItemInventoryFragment;
import com.example.seniorproject.smartshopping.model.dao.ItemInventoryMap;
import com.example.seniorproject.smartshopping.model.manager.ItemInventoryManager;

public class MoreItemInventoryActivity extends AppCompatActivity {

    /***********************************************************************************************
     ************************************* Variable class ********************************************
     ***********************************************************************************************/

    final String MORE_ITEM_INVENTORY_FRAGMENT = "moreItemInventoryFragment";

    private ItemInventoryMap itemInventoryMap;
    private int position;

        /***********************************************************************************************
         ************************************* Methods ********************************************
         ***********************************************************************************************/

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_more_item_inventory);

            position = getIntent().getIntExtra("position", 0);
            itemInventoryMap = ItemInventoryManager.getInstance().getItemInventory(position);

            initInstances();

            if(savedInstanceState == null){
                MoreItemInventoryFragment moreItemInventoryFragment = MoreItemInventoryFragment.newInstance(position);
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.containerMoreItemInventory, moreItemInventoryFragment,
                                MORE_ITEM_INVENTORY_FRAGMENT)
                        .commit();
            }
        }

        private void initInstances() {
            setTitle(itemInventoryMap.getItemInventory().getName().toString());
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Runtime.getRuntime().gc();
    }

    /***********************************************************************************************
         ************************************* Listener variable ********************************************
         ***********************************************************************************************/

        /***********************************************************************************************
         ************************************* Implement Methods ********************************************
         ***********************************************************************************************/
}
