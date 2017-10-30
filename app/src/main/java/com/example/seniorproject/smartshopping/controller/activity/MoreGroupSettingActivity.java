package com.example.seniorproject.smartshopping.controller.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.seniorproject.smartshopping.R;
import com.example.seniorproject.smartshopping.controller.fragment.settingfragment.CreatedGroupFragment;
import com.example.seniorproject.smartshopping.controller.fragment.settingfragment.GroupSettingFragment;
import com.example.seniorproject.smartshopping.controller.fragment.settingfragment.SettingFragment;

public class MoreGroupSettingActivity extends AppCompatActivity implements CreatedGroupFragment.GroupSettingCancel,
        CreatedGroupFragment.GroupSettingAdded, CreatedGroupFragment.PickImage, GroupSettingFragment.ChangeGroup {

    /***********************************************************************************************
     ************************************* Variable class ********************************************
     ***********************************************************************************************/

    private final String GROUP_SETTING = "groupSetting";
    private final String CREATED_GROUP= "createdGroup";

    /***********************************************************************************************
     ************************************* Methods ********************************************
     ***********************************************************************************************/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_group_setting);

        initInstances();
    }

    public void initInstances(){

        setTitle("Groups");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        GroupSettingFragment groupSettingFragment = GroupSettingFragment.newInstance();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.containerGroupSetting, groupSettingFragment,
                        GROUP_SETTING)
                .commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
            return true;
        }

        if(item.getItemId() == R.id.action_create_group){
            GroupSettingFragment groupSettingFragment = (GroupSettingFragment)
                    getSupportFragmentManager().findFragmentByTag(GROUP_SETTING);
            CreatedGroupFragment createdGroupFragment = CreatedGroupFragment.newInstance();

            getSupportFragmentManager()
                    .beginTransaction()
                    .hide(groupSettingFragment)
                    .add(R.id.containerGroupSetting, createdGroupFragment,
                            CREATED_GROUP)
                    .commit();
            return true;
        }

        if(item.getItemId() == R.id.action_join_group){

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_group_setting, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CreatedGroupFragment.RC_PHOTO_PICKER){
            if(resultCode == RESULT_OK){
                CreatedGroupFragment createdGroupFragment = (CreatedGroupFragment)
                        getSupportFragmentManager().findFragmentByTag(CREATED_GROUP);

                createdGroupFragment.onActivityResult(requestCode, resultCode, data);
            }
        }
    }


    /***********************************************************************************************
     ************************************* Listener variable ********************************************
     ***********************************************************************************************/

    /***********************************************************************************************
     ************************************* Implement Methods ********************************************
     ***********************************************************************************************/

    @Override
    public void cancel() {
        GroupSettingFragment groupSettingFragment = (GroupSettingFragment)
                getSupportFragmentManager().findFragmentByTag(GROUP_SETTING);
        CreatedGroupFragment createdGroupFragment = (CreatedGroupFragment)
                getSupportFragmentManager().findFragmentByTag(CREATED_GROUP);

        getSupportFragmentManager().beginTransaction()
                .remove(createdGroupFragment)
                .show(groupSettingFragment)
                .commit();
    }

    @Override
    public void add() {
        GroupSettingFragment groupSettingFragment = (GroupSettingFragment)
                getSupportFragmentManager().findFragmentByTag(GROUP_SETTING);
        CreatedGroupFragment createdGroupFragment = (CreatedGroupFragment)
                getSupportFragmentManager().findFragmentByTag(CREATED_GROUP);


        getSupportFragmentManager().beginTransaction()
                .remove(createdGroupFragment)
                .show(groupSettingFragment)
                .commit();
    }

    @Override
    public void pickImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/jpeg");
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        startActivityForResult(Intent.createChooser(intent, "Complete action using"),
                CreatedGroupFragment.RC_PHOTO_PICKER);
    }

    @Override
    public void changeGroup() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
