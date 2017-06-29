package se.mebe.jdbcuser.repository;

import java.util.List;
import se.mebe.jdbcuser.exception.RepositoryException;
import se.mebe.jdbcuser.model.WorkItem;

public interface WorkItemRepository {

	void removeTask(long id);

	void assignTask(long workitemId, long userId);

	String updateTaskStatus(String status, long id);

	WorkItem createTask(WorkItem workItem);

	String getWorkItemStatusByWorkItemId(long id);

	List<String> getWorkItemsByState(String state) throws RepositoryException;

	List<String> getAllWorkItemForOneTeam(long teamId) throws RepositoryException;

	List<String> getAllWorkItemForUser(long userId) throws RepositoryException;

	List<String> getStateFromWorkItem(String state) throws RepositoryException;

	Long getWorkItemId(long itemId);

	List<Long> getteamIdFromWorkItem(long teamId);

}
