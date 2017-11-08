package com.example.seniorproject.smartshopping.model.dao.group;

import android.view.View;

/**
 * Created by boyburin on 10/31/2017 AD.
 */

public class GroupWatingListener {
    private GroupWating groupWating;
    private View.OnClickListener deleteListener;

    public GroupWating getGroupWating() {
        return groupWating;
    }

    public void setGroupWating(GroupWating groupWating) {
        this.groupWating = groupWating;
    }

    public View.OnClickListener getDeleteListener() {
        return deleteListener;
    }

    public void setDeleteListener(View.OnClickListener deleteListener) {
        this.deleteListener = deleteListener;
    }
}
