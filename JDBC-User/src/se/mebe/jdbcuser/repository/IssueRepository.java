package se.mebe.jdbcuser.repository;

import java.util.Set;
import se.mebe.jdbcuser.exception.RepositoryException;
import se.mebe.jdbcuser.model.Issue;

public interface IssueRepository {

	Issue createIssue(long issueId, String comment, long workItemId);

	void updateWorkIssue(long issueId, long itemId);

	Set<String> getWorkItemsByIssue() throws RepositoryException;

	String getWorkItemStatus(long itemId);

	long getItemIdFromIssue(long issueId);

}
