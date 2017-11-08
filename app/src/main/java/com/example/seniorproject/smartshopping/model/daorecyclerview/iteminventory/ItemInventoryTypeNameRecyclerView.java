package com.example.seniorproject.smartshopping.model.daorecyclerview.iteminventory;

/**
 * Created by boyburin on 11/8/2017 AD.
 */

public class ItemInventoryTypeNameRecyclerView extends BaseItemInventory {

    private String typeName;

    public ItemInventoryTypeNameRecyclerView() {
        super(BaseItemInventory.TYPE_NAME);
    }

    public void setTypeName(String name){
        this.typeName = name;
    }

    public String getTypeName(){
        return typeName;
    }
}
