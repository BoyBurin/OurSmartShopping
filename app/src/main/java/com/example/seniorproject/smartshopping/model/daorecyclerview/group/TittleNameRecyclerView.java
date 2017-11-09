package com.example.seniorproject.smartshopping.model.daorecyclerview.group;

/**
 * Created by boyburin on 11/9/2017 AD.
 */

public class TittleNameRecyclerView extends BaseGroup {

    private String tittleName;

    public TittleNameRecyclerView() {
        super(BaseGroup.TITTLE_NAME);
    }

    public String getTittleName() {
        return tittleName;
    }

    public void setTittleName(String tittleName) {
        this.tittleName = tittleName;
    }
}
