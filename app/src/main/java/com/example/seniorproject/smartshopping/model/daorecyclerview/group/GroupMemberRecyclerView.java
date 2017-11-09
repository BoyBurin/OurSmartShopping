package com.example.seniorproject.smartshopping.model.daorecyclerview.group;

import com.example.seniorproject.smartshopping.model.dao.user.UserInGroupMap;

/**
 * Created by boyburin on 11/9/2017 AD.
 */

public class GroupMemberRecyclerView extends BaseGroup{

    UserInGroupMap member;

    public GroupMemberRecyclerView() {
        super(BaseGroup.GROUP_MEMBER);
    }

    public UserInGroupMap getMember() {
        return member;
    }

    public void setMember(UserInGroupMap member) {
        this.member = member;
    }
}
