package com.example.seniorproject.smartshopping.model.daorecyclerview.group;

import com.example.seniorproject.smartshopping.model.dao.group.GroupMap;

/**
 * Created by boyburin on 11/9/2017 AD.
 */

public class GroupDetailRecyclerView extends BaseGroup{

    GroupMap group;

    public GroupDetailRecyclerView() {
        super(BaseGroup.GROUP_DETAIL);
    }

    public GroupMap getGroup() {
        return group;
    }

    public void setGroup(GroupMap group) {
        this.group = group;
    }
}
