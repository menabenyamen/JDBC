package se.mebe.jdbcuser.repository;

import java.util.List;

public interface BackUpRepository {

	List<Long> getUserIdFomUserWorkItem(long userId);

	void updateWorkItemState(long userId);

	String getUserState(long userId);

	List<String> getUsersName(String userName);

	List<Long> getUserTeamId(long userId);

	List<Long> getUsersId();

	Long getOneUserIdFomUserWorkItem(long userId);

	List<Long> getWorkItemId();

	List<Long> getIssueId();
	
	

}
