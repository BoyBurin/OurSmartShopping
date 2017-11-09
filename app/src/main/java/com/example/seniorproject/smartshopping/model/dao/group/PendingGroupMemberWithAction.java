package com.example.seniorproject.smartshopping.model.dao.group;

import android.view.View;

/**
 * Created by boyburin on 11/9/2017 AD.
 */

public class PendingGroupMemberWithAction {

    private View.OnClickListener accept;
    private View.OnClickListener decline;
    private PendingGroupMember pendingGroupMember;

    public PendingGroupMemberWithAction(){

    }

    public PendingGroupMemberWithAction(View.OnClickListener accept, View.OnClickListener decline, PendingGroupMember pendingGroupMember){
        this.accept = accept;
        this.decline = decline;
        this.pendingGroupMember = pendingGroupMember;
    }

    public View.OnClickListener getAccept() {
        return accept;
    }

    public void setAccept(View.OnClickListener accept) {
        this.accept = accept;
    }

    public View.OnClickListener getDecline() {
        return decline;
    }

    public void setDecline(View.OnClickListener decline) {
        this.decline = decline;
    }

    public PendingGroupMember getPendingGroupMember() {
        return pendingGroupMember;
    }

    public void setPendingGroupMember(PendingGroupMember pendingGroupMember) {
        this.pendingGroupMember = pendingGroupMember;
    }
}
