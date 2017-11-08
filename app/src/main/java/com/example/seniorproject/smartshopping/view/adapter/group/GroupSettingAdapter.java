package com.example.seniorproject.smartshopping.view.adapter.group;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.seniorproject.smartshopping.R;
import com.example.seniorproject.smartshopping.controller.fragment.settingfragment.SettingFragment;
import com.example.seniorproject.smartshopping.model.dao.group.GroupList;
import com.example.seniorproject.smartshopping.model.dao.group.GroupWatingListener;
import com.example.seniorproject.smartshopping.model.datatype.MutableInteger;
import com.example.seniorproject.smartshopping.view.customviewgroup.CustomViewGroupGroupSetting;
import com.example.seniorproject.smartshopping.view.transformation.CircleTransform;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import static java.security.AccessController.getContext;


/**
 * Created by boyburin on 8/29/2017 AD.
 */

public class GroupSettingAdapter extends BaseAdapter{


    /******************************************************************************************
     * ****************************** Variable *********************************************
     *******************************************************************************************/

    private ArrayList<GroupList> groups;

    private MutableInteger lastPositionInteger;

    /******************************************************************************************
     * ****************************** Methods *********************************************
     *******************************************************************************************/

    public GroupSettingAdapter(MutableInteger lastPositionInteger) {
        this.lastPositionInteger = lastPositionInteger;
        groups = new ArrayList<GroupList>();
    }


    public void setGroups(ArrayList<GroupList> groups){
        this.groups = groups;
    }


    @Override
    public int getCount() {

        return groups.size();
    }

    @Override
    public Object getItem(int i) {

        return groups.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }


    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        //if(getItemViewType(i) == 0) {
        CustomViewGroupGroupSetting item;
        if (view != null)
            item = (CustomViewGroupGroupSetting) view;
        else
            item = new CustomViewGroupGroupSetting(viewGroup.getContext());

        GroupList group = (GroupList) getItem(position);
        item.setNameText(group.getName());
        item.setImageUrl(group.getPhotoUrl());
        lastPositionInteger.setValue(position);

        return item;

    }
}
