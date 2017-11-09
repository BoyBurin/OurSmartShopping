package com.example.seniorproject.smartshopping.model.daorecyclerview.group;

import com.example.seniorproject.smartshopping.model.dao.group.GroupMap;
import com.example.seniorproject.smartshopping.model.dao.group.PendingGroupMemberWithAction;
import com.example.seniorproject.smartshopping.model.dao.user.UserInGroupMap;

import java.util.ArrayList;

/**
 * Created by boyburin on 11/9/2017 AD.
 */

public class GroupCreator {

    public GroupDetailRecyclerView createGroupDetail(GroupMap group){
        GroupDetailRecyclerView groupDetailRecyclerView = new GroupDetailRecyclerView();

        groupDetailRecyclerView.setGroup(group);

        return groupDetailRecyclerView;
    }

    public TittleNameRecyclerView createTittleName(String tittle){
        TittleNameRecyclerView tittleNameRecyclerView = new TittleNameRecyclerView();

        tittleNameRecyclerView.setTittleName(tittle);

        return tittleNameRecyclerView;
    }

    public ArrayList<GroupMemberRecyclerView> createGroupMember(ArrayList<UserInGroupMap> members){
        ArrayList<GroupMemberRecyclerView> memberRecyclerViews = new ArrayList<>();

        for(UserInGroupMap member : members){
            GroupMemberRecyclerView groupMemberRecyclerView = new GroupMemberRecyclerView();
            groupMemberRecyclerView.setMember(member);

            memberRecyclerViews.add(groupMemberRecyclerView);
        }

        return memberRecyclerViews;
    }

    public ArrayList<PendingGroupMemberRecyclerView> createPendingGroupMember(ArrayList<PendingGroupMemberWithAction> pendingMembers){
        ArrayList<PendingGroupMemberRecyclerView> pendingGroupMemberRecyclerViews = new ArrayList<>();

        for(PendingGroupMemberWithAction pendingMember : pendingMembers){
            PendingGroupMemberRecyclerView pendingGroupMemberRecyclerView = new PendingGroupMemberRecyclerView();
            pendingGroupMemberRecyclerView.setPendingGroupMember(pendingMember);

            pendingGroupMemberRecyclerViews.add(pendingGroupMemberRecyclerView);
        }

        return pendingGroupMemberRecyclerViews;
    }
}
