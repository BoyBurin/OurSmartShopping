package com.example.seniorproject.smartshopping.model.daorecyclerview.group;

import com.example.seniorproject.smartshopping.model.dao.group.PendingGroupMemberWithAction;

/**
 * Created by boyburin on 11/9/2017 AD.
 */

public class PendingGroupMemberRecyclerView extends BaseGroup {

    PendingGroupMemberWithAction pendingGroupMember;

    public PendingGroupMemberRecyclerView() {
        super(BaseGroup.PENDING_GROUP_MEMBER);
    }

    public PendingGroupMemberWithAction getPendingGroupMember() {
        return pendingGroupMember;
    }

    public void setPendingGroupMember(PendingGroupMemberWithAction pendingGroupMember) {
        this.pendingGroupMember = pendingGroupMember;
    }
}
