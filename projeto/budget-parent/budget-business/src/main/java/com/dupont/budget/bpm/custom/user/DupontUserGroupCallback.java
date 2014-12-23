package com.dupont.budget.bpm.custom.user;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.inject.Default;
import javax.inject.Inject;

import org.kie.api.task.UserGroupCallback;
import org.slf4j.Logger;

import com.dupont.budget.model.Papel;
import com.dupont.budget.model.PapelUsuario;
import com.dupont.budget.model.Usuario;
import com.dupont.budget.service.DomainService;
@Default
public class DupontUserGroupCallback implements UserGroupCallback {
	@Inject
	private DomainService domainService;
	@Inject
	private UserGroupCallbackCacheManager userGroupCallbackCacheManager;

	@Inject
	private Logger logger;

    public boolean existsUser(String userId) {
    	if(userId.equals("Administrator") )
    		return true;
    	String user= userGroupCallbackCacheManager.getUserFromCache(userId);
    	if(user==null)
    	{
    		Usuario usuario = domainService.getUsuarioByLogin(userId);
    		user = usuario.getLogin();
    		List<String> groups = new ArrayList<String>();
    		for(PapelUsuario papel : usuario.getPapeis())
    		{
    			String grupo = papel.getPapel().getNome();
    			groups.add(grupo);
    			userGroupCallbackCacheManager.setGroupOnCache(grupo);
    		}
    		userGroupCallbackCacheManager.setUserOnCache(userId);
    		userGroupCallbackCacheManager.setGroupsOnCache(userId, groups);
    	}
    	if(user==null)
    	{
    		logger.error("Usuario "+user + " NAO ENCONTRADO");
    		return false;
    	}
        return true;
    }

    public boolean existsGroup(String groupId) {
    	if(groupId.equals("Administrators") )
    		return true;
    	String group= userGroupCallbackCacheManager.getUserFromCache(groupId);
    	if(group == null)
    	{
    		Papel papel =  new Papel();
    		papel.setNome(groupId);
    		List<Papel> papeis = domainService.findByName(papel);
    		if(papeis !=null && papeis.size() >0)
    		{
    			group = papeis.get(0).getNome();
    			userGroupCallbackCacheManager.setGroupOnCache(groupId);
    		}
    		else
    		{
    			logger.error("GRUPO "+groupId + " NAO ENCONTRADO");
    		}
    	}

        return group!=null;
    }

    public List<String> getGroupsForUser(String userId,
                                         List<String> groupIds, List<String> allExistingGroupIds) {
    	List<String> userGroups = userGroupCallbackCacheManager.getGroupsFromCache(userId);
    	if(userGroups == null)
    	{

    		List<String> groups = new ArrayList<String>();
    		Usuario u = domainService.getUsuarioByLogin(userId);
    		for(PapelUsuario papel : u.getPapeis())
    		{
    			groups.add(papel.getPapel().getNome());
    		}

    		userGroupCallbackCacheManager.setGroupsOnCache(userId, groups);
    	}
        return userGroups;
    }
}
