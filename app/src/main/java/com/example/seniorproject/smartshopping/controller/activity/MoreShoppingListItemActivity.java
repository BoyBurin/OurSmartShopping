package com.example.seniorproject.smartshopping.controller.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.seniorproject.smartshopping.R;
import com.example.seniorproject.smartshopping.controller.fragment.shoppinglistfragment.MoreShoppingListItemFragment;
import com.example.seniorproject.smartshopping.model.dao.ShoppingListMap;

public class MoreShoppingListItemActivity extends AppCompatActivity {

    /***********************************************************************************************
     ************************************* Variable class ********************************************
     ***********************************************************************************************/

    final String MORE_SHOOPING_LIST_ITEM = "moreShoppingListItem";

    private ShoppingListMap shoppingListMap;
    private int position;


    /***********************************************************************************************
     ************************************* Methods ********************************************
     ***********************************************************************************************/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_shopping_list_item);

        position = getIntent().getIntExtra("position", 0);
        shoppingListMap = getIntent().getParcelableExtra("shoppingListMap");

        initInstances();

        if(savedInstanceState == null){
            MoreShoppingListItemFragment moreShoppingListItemFragment = MoreShoppingListItemFragment.newInstance(shoppingListMap, position);
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.containerMoreShoppingListItem, moreShoppingListItemFragment,
                            MORE_SHOOPING_LIST_ITEM)
                    .commit();
        }
    }

    private void initInstances() {
        setTitle(shoppingListMap.getShoppingList().getName().toString());
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
