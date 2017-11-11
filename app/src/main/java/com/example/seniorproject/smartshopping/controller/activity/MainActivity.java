package com.example.seniorproject.smartshopping.controller.activity;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;

import com.example.seniorproject.smartshopping.R;
import com.example.seniorproject.smartshopping.controller.fragment.purchaseitem.PurchaseItemFragment;
import com.example.seniorproject.smartshopping.controller.fragment.dialogfragment.DialogAddItemInventoryFragment;
import com.example.seniorproject.smartshopping.controller.fragment.dialogfragment.FragmentDialogAddShoppingList;
import com.example.seniorproject.smartshopping.controller.fragment.groupfragment.GroupFragment;
import com.example.seniorproject.smartshopping.controller.fragment.inventoryfragment.InventoryFragment;
import com.example.seniorproject.smartshopping.controller.fragment.settingfragment.SettingFragment;
import com.example.seniorproject.smartshopping.controller.fragment.shoppinglistfragment.ShoppingListFragment;
import com.example.seniorproject.smartshopping.controller.fragment.shoppinghistoryfragment.ShoppingHistoryFragment;
import com.example.seniorproject.smartshopping.model.dao.iteminventory.ItemInventoryMap;
import com.example.seniorproject.smartshopping.model.dao.shoppinglist.ShoppingListMap;

public class MainActivity extends AppCompatActivity implements ShoppingListFragment.ShoopingListFloatingButton
, FragmentDialogAddShoppingList.DeleteAddShoppingListDialog, FragmentDialogAddShoppingList.PickImageShoppingListDialog,
        InventoryFragment.MoreItemInventoryListener, ShoppingListFragment.MoreShoppingListItemListener,
        DialogAddItemInventoryFragment.BarcodeListener, InventoryFragment.ItemInventoryFloatingButton,
        DialogAddItemInventoryFragment.DeleteItemInventoryDialog, SettingFragment.SignOutListener, SettingFragment.GroupSetting {
    /***********************************************************************************************
     ************************************* Variable class ********************************************
     ***********************************************************************************************/

    private ImageButton btnPromotion;
    private ImageButton btnGroup;
    private ImageButton btnInventory;
    private ImageButton btnShoppingList;
    private ImageButton btnShoppingHistory;
    private ImageButton btnSetting;



    final String SHOPPING_LIST_FRAGMENT = "ShoppingListFragment";
    final String  DIALOG_ADD_SHOPPING_LIST_FRAGMENT = "dialogAddShoppingListFragment";
    final String INVENTORY_FRAGMENT = "inventoryFragment";
    final String GROUP_FRAGMENT = "groupFragment";
    final String SHOPPING_HISTORY_FRAGMENT = "shoppingHistoryFragment";
    final String DIALOG_ADD_ITEM_INVENATORY_FRAGMENT = "dialogAddItemInventoryFragment";
    final String SETTINGFRAGMENT = "settingFragment";
    final String BUYITEMFRAGMENT = "buyItemFragment";

    private Fragment current;
    private ImageButton currentBtn;



    /***********************************************************************************************
     ************************************* Methods ********************************************
     ***********************************************************************************************/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initInstances(savedInstanceState);
    }

    private void initInstances(Bundle savedInstanceState){


        //setTitle(GroupManager.getInstance().getCurrentGroup().getGroup().getName());

        btnPromotion = (ImageButton) findViewById(R.id.btnPromotion);
        btnPromotion.setOnClickListener(topBarOnClickListener);

        btnGroup = (ImageButton) findViewById(R.id.btnGroup);
        btnGroup.setOnClickListener(topBarOnClickListener);

        btnSetting = (ImageButton) findViewById(R.id.btnSetting);
        btnSetting.setOnClickListener(topBarOnClickListener);

        btnShoppingList = (ImageButton) findViewById(R.id.btnShoppingList);
        btnShoppingList.setOnClickListener(topBarOnClickListener);

        btnInventory = (ImageButton) findViewById(R.id.btnInventory);
        btnInventory.setOnClickListener(topBarOnClickListener);

        btnShoppingHistory = (ImageButton) findViewById(R.id.btnShoppingHistory);
        btnShoppingHistory.setOnClickListener(topBarOnClickListener);


        currentBtn = btnGroup;

        if(savedInstanceState == null) {
            ShoppingListFragment shoppingListFragment = ShoppingListFragment.newInstance();
            InventoryFragment inventoryFragment = InventoryFragment.newInstance();
            GroupFragment groupFragment = GroupFragment.newInstance();
            //ShoppingHistoryFragment shoppingHistoryFragment = ShoppingHistoryFragment.newInstance();
            SettingFragment settingFragment = SettingFragment.newInstance();
            PurchaseItemFragment purchaseItemFragment = PurchaseItemFragment.newInstance();


            getSupportFragmentManager().beginTransaction()
                    .add(R.id.containerMain, shoppingListFragment,
                            SHOPPING_LIST_FRAGMENT)
                    .add(R.id.containerMain, inventoryFragment,
                            INVENTORY_FRAGMENT)
                    .add(R.id.containerMain, groupFragment,
                            GROUP_FRAGMENT)
                    //.add(R.id.containerMain, shoppingHistoryFragment,
                    //        SHOPPING_HISTORY_FRAGMENT)
                    .add(R.id.containerMain, settingFragment,
                            SETTINGFRAGMENT)
                    .add(R.id.containerMain, purchaseItemFragment,
                            BUYITEMFRAGMENT)
                    .hide(inventoryFragment)
                    .hide(shoppingListFragment)
                    //.hide(shoppingHistoryFragment)
                    .hide(settingFragment)
                    .hide(purchaseItemFragment)
                    .show(groupFragment)
                    .commit();

            current = groupFragment;
            currentBtn = btnGroup;
            currentBtn.setBackgroundResource(R.drawable.shape_rect_overlay);
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == FragmentDialogAddShoppingList.RC_PHOTO_PICKER){
            if(resultCode == RESULT_OK){
                Fragment addShoppingListDialog = getSupportFragmentManager()
                        .findFragmentByTag(DIALOG_ADD_SHOPPING_LIST_FRAGMENT);

                addShoppingListDialog.onActivityResult(requestCode, resultCode, data);
            }
        }

        if(requestCode == 0){
            if(resultCode == RESULT_OK) {
                String contents = data.getStringExtra("SCAN_RESULT");
                Fragment dialogAddItemInventoryFragment = getSupportFragmentManager()
                        .findFragmentByTag(DIALOG_ADD_ITEM_INVENATORY_FRAGMENT);

                dialogAddItemInventoryFragment.onActivityResult(requestCode, resultCode, data);
            }

        }
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onStart() {

        super.onStart();
        //updateUI(currentUser);
    }

    @Override
    public void onStop() {
        super.onStop();
    }


    /***********************************************************************************************
     ************************************* Listener variable ********************************************
     ***********************************************************************************************/

    final View.OnClickListener topBarOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            ShoppingListFragment shoppingListFragment = (ShoppingListFragment)
                    getSupportFragmentManager().findFragmentByTag(SHOPPING_LIST_FRAGMENT);

            InventoryFragment inventoryFragment = (InventoryFragment)
                    getSupportFragmentManager().findFragmentByTag(INVENTORY_FRAGMENT);

            GroupFragment groupFragment = (GroupFragment)
                    getSupportFragmentManager().findFragmentByTag(GROUP_FRAGMENT);

            ShoppingHistoryFragment shoppingHistoryFragment = (ShoppingHistoryFragment)
                    getSupportFragmentManager().findFragmentByTag(SHOPPING_HISTORY_FRAGMENT);

            SettingFragment settingFragment = (SettingFragment)
                    getSupportFragmentManager().findFragmentByTag(SETTINGFRAGMENT);

            PurchaseItemFragment purchaseItemFragment = (PurchaseItemFragment)
                    getSupportFragmentManager().findFragmentByTag(BUYITEMFRAGMENT);

            if(view == btnPromotion){
                if(current == purchaseItemFragment) return;

                getSupportFragmentManager().beginTransaction()
                        .show(purchaseItemFragment)
                        .hide(current)
                        .commit();
                current = purchaseItemFragment;
                currentBtn.setBackgroundResource(android.R.color.transparent);
                currentBtn = btnPromotion;
                currentBtn.setBackgroundResource(R.drawable.shape_rect_overlay);
            }

            if(view == btnShoppingList){

                if(current == shoppingListFragment) return;

                getSupportFragmentManager().beginTransaction()
                        .show(shoppingListFragment)
                        .hide(current)
                        .commit();
                current = shoppingListFragment;
                currentBtn.setBackgroundResource(android.R.color.transparent);
                currentBtn = btnShoppingList;
                currentBtn.setBackgroundResource(R.drawable.shape_rect_overlay);
            }

            if(view == btnInventory){

                if(current == inventoryFragment) return;

                getSupportFragmentManager().beginTransaction()
                        .show(inventoryFragment)
                        .hide(current)
                        .commit();
                current = inventoryFragment;
                currentBtn.setBackgroundResource(android.R.color.transparent);
                currentBtn = btnInventory;
                currentBtn.setBackgroundResource(R.drawable.shape_rect_overlay);
            }

            if(view == btnGroup){

                if(current == groupFragment) return;

                getSupportFragmentManager().beginTransaction()
                        .show(groupFragment)
                        .hide(current)
                        .commit();
                current = groupFragment;
                currentBtn.setBackgroundResource(android.R.color.transparent);
                currentBtn = btnGroup;
                currentBtn.setBackgroundResource(R.drawable.shape_rect_overlay);
            }

            if(view == btnShoppingHistory){

                if(current == shoppingHistoryFragment) return;

//                getSupportFragmentManager().beginTransaction()
//                        .show(shoppingHistoryFragment)
//                        .hide(current)
//                        .commit();

                //current = shoppingHistoryFragment;
                currentBtn.setBackgroundResource(android.R.color.transparent);
                currentBtn = btnShoppingHistory;
                currentBtn.setBackgroundResource(R.drawable.shape_rect_overlay);

            }

            if(view == btnSetting){

                if(current == settingFragment) return;

                getSupportFragmentManager().beginTransaction()
                        .show(settingFragment)
                        .hide(current)
                        .commit();

                current = settingFragment;
                currentBtn.setBackgroundResource(android.R.color.transparent);
                currentBtn = btnSetting;
                currentBtn.setBackgroundResource(R.drawable.shape_rect_overlay);

            }
        }
    };

    private void hideKeyboard(){
        InputMethodManager imm = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(this);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    /***********************************************************************************************
     ************************************* Implement Methods ********************************************
     ***********************************************************************************************/

    @Override
    public void setShoopingListFloatingButtonFloationgButton() {
        DialogFragment addShoppingListDialog =
                FragmentDialogAddShoppingList.newInstance();
        addShoppingListDialog.show(getSupportFragmentManager(), DIALOG_ADD_SHOPPING_LIST_FRAGMENT);

    }


    @Override
    public void delete() {
        Fragment addShoppingListDialog = getSupportFragmentManager().findFragmentByTag(DIALOG_ADD_SHOPPING_LIST_FRAGMENT);
        getSupportFragmentManager().beginTransaction()
                .remove(addShoppingListDialog)
                .commit();
        hideKeyboard();
    }

    @Override
    public void pickImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/jpeg");
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        startActivityForResult(Intent.createChooser(intent, "Complete action using"),
                FragmentDialogAddShoppingList.RC_PHOTO_PICKER);
    }

    @Override
    public void goToMoreItemInventory(ItemInventoryMap itemInventoryMap) {
        Intent intent = new Intent(this, MoreItemInventoryActivity.class);
        intent.putExtra("itemInventoryMap", itemInventoryMap);
        startActivity(intent);
    }

    @Override
    public void goToMoreShoppingListItem(ShoppingListMap shoppingListMap, int position) {
        Intent intent = new Intent(this, MoreShoppingListItemActivity.class);
        intent.putExtra("position", position);
        intent.putExtra("shoppingListMap", shoppingListMap);
        startActivity(intent);
    }

    @Override
    public void scanBarcode() {
        Intent intent = new Intent();
        intent.setAction("com.google.zxing.client.android.SCAN");
        intent.putExtra("SAVE_HISTORY", false);
        startActivityForResult(intent, 0);
    }

    @Override
    public void setItemInventoryFloationgButton() {
        DialogFragment addItemInventory =
                DialogAddItemInventoryFragment.newInstance();
        addItemInventory.show(getSupportFragmentManager(), DIALOG_ADD_ITEM_INVENATORY_FRAGMENT);
    }

    @Override
    public void deleteAddItemInventoryDialog() {
        Fragment addItemInventory = getSupportFragmentManager().findFragmentByTag(DIALOG_ADD_ITEM_INVENATORY_FRAGMENT);
        getSupportFragmentManager().beginTransaction()
                .remove(addItemInventory)
                .commit();
        hideKeyboard();
    }

    @Override
    public void signOut() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void goToGroupSetting() {
        Intent intent = new Intent(this, MoreGroupSettingActivity.class);
        startActivity(intent);
    }
}
