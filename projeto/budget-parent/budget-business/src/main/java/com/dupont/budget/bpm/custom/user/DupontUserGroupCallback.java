package com.dupont.budget.bpm.custom.user;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.inject.Default;
import javax.inject.Inject;

import org.kie.api.task.UserGroupCallback;

import com.dupont.budget.model.Usuario;
import com.dupont.budget.service.DomainService;
@Default
public class DupontUserGroupCallback implements UserGroupCallback {
	@Inject
	private DomainService domainService;
	@Inject
	private UserGroupCallbackCacheManager userGroupCallbackCacheManager;
	
    public boolean existsUser(String userId) {
    	
    	String user= userGroupCallbackCacheManager.getUserFromCache(userId);
    	if(user==null)
    	{
    		Usuario usuario = domainService.getUsuarioByLogin(userId);
    		user = usuario.getLogin();
    		userGroupCallbackCacheManager.setUserOnCache(userId);
    	}
    	
        return user!=null || userId.equals("Administrator") ;
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
