package com.dupont.budget.bpm.producer;

import org.kie.api.task.UserGroupCallback;

import javax.enterprise.inject.Alternative;
import java.util.ArrayList;
import java.util.List;

@Alternative
public class DupontUserGroupCallback implements UserGroupCallback {

    public boolean existsUser(String userId) {
        return userId.equals("veronicag") || userId.equals("Administrator") || userId.equals("guidov")  ;
    }

    public boolean existsGroup(String groupId) {
        return groupId.equals("RESPONSAVEL_WR31601027") || groupId.equals("LIDER_Distribuicao") ;
    }

    public List<String> getGroupsForUser(String userId,	
                                         List<String> groupIds, List<String> allExistingGroupIds) {
        List<String> groups = new ArrayList<String>();
        if (userId.equals("veronicag"))
            groups.add("RESPONSAVEL_WR31601027");
        else if (userId.equals("guidov"))
            groups.add("LIDER_Distribuicao");
      
        return groups;
    }
}
