package com.dupont.budget.bpm.custom.user;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import javax.ejb.Schedule;
import javax.ejb.Startup;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.slf4j.Logger;

/**
 * A Classe UserGroupCallbackCacheManager realiza cache de usuários e grupos do BD.
 * Assim, evita que consultas desnecessárias sejam realizadas no BD, aumentando a performance das consultas.
 *
 * @author bandrade
 *
 */
@Singleton
@Startup
public class UserGroupCallbackCacheManager implements Serializable {

	private static final long serialVersionUID = 8370614694101779746L;

	private HashMap<String,  List<String>> userGroupCache = new HashMap<String,  List<String>>();
	private HashMap<String,  String> groupCache = new HashMap<String,  String>();


	 @Inject
	 private Logger logger;

	 @Schedule(second = "*/5", minute = "*", hour = "*", persistent = false)
	 public void testeScheduler()
	 {
		 logger.info("teste");
	 }

	/**
	 * @param userId
	 * @param newGroups
	 * Grava no cache grupos para determinado usuário.
	 */
	public void setGroupsOnCache(String userId, List<String> newGroups)
	{
		userId = userId.toLowerCase();

		if (userGroupCache.containsKey(userId))
			userGroupCache.remove(userId);

		userGroupCache.put(userId, newGroups);
	}

	/**
	 * @param userId
	 * @return  grupos para determinado usuário.
	 * Busca no cache grupos para determinado usuário.
	 */
	public List<String> getGroupsFromCache(String userId)
	{
		userId = userId.toLowerCase();

		List<String> groups = userGroupCache.get(userId);

		return groups;
	}
	public void removeGroupsFromCache(String userId)
	{
		userId = userId.toLowerCase();
		userGroupCache.remove(userId);
	}

	private HashMap<String,  String> userCache = new HashMap<String,  String>();

	/**
	 * @param userId
	 * Inclui usuário no cache.
	 */
	public void setUserOnCache(String userId)
	{
		userId = userId.toLowerCase();

		if (userCache.containsKey(userId))
			userCache.remove(userId);

		userCache.put(userId, userId);
	}

	/**
	 * @param userId
	 * @return  usuário existente no cache.
	 * Verifica se usuário existe no cache.
	 */
	public String getUserFromCache(String userId)
	{
		userId = userId.toLowerCase();

		String user = userCache.get(userId);

		return user;
	}

	/**
	 * @param userId
	 * Inclui usuário no cache.
	 */
	public void setGroupOnCache(String groupId)
	{
		groupId = groupId.toLowerCase();

		if (groupCache.containsKey(groupId))
			groupCache.remove(groupId);

		groupCache.put(groupId, groupId);
	}
}
