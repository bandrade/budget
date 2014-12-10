package com.dupont.budget.bpm.producer;

import org.kie.api.task.UserGroupCallback;

import javax.enterprise.inject.Alternative;
import java.util.ArrayList;
import java.util.List;

@Alternative
public class DupontUserGroupCallback implements UserGroupCallback {

    public boolean existsUser(String userId) {
        return userId.equals("veronica") || userId.equals("Administrator") ;
    }

    public boolean existsGroup(String groupId) {
        return groupId.equals("PM") || groupId.equals("HR") || groupId.equals("RESPONSAVEL_WR31601027") || groupId.equals("Administrators") ;
    }

    public List<String> getGroupsForUser(String userId,	
                                         List<String> groupIds, List<String> allExistingGroupIds) {
        List<String> groups = new ArrayList<String>();
        if (userId.equals("veronica"))
            groups.add("RESPONSAVEL_WR31601027");
        else if (userId.equals("mary"))
            groups.add("HR");
        return groups;
    }
}
