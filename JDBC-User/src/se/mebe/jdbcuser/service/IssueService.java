package se.mebe.jdbcuser.service;


import java.sql.SQLException;
import java.util.Set;

import se.mebe.jdbcuser.exception.RepositoryException;
import se.mebe.jdbcuser.exception.ServiceException;
import se.mebe.jdbcuser.repository.IssueRepository;
import se.mebe.jdbcuser.repository.SQLWorkItemRepository;


public final class IssueService {

	private final IssueRepository issueReopsitory;
	private final SQLWorkItemRepository workRepository;

	public IssueService(IssueRepository issueReopsitory) throws SQLException {
		this.issueReopsitory = issueReopsitory;
		this.workRepository = new SQLWorkItemRepository();
	}

	public void addIssue(long issueId, long itemId, String issueReason, String state) throws ServiceException {

		if (!issueReopsitory.getWorkItemStatus(itemId).contains("Done")) {
			throw new ServiceException("Work item is not yet Done !!");

		} else if (issueReopsitory.getItemIdFromIssue(issueId)==issueId) {
			throw new ServiceException("This Id is in the issue !!");

		} else if (issueReopsitory.getWorkItemStatus(itemId).contains("Done")) {
			issueReopsitory.createIssue(issueId, issueReason, itemId);
			workRepository.updateTaskStatus(state, itemId);

		}

	}

	public void updateIssue(long itemId, long issueId, String state) throws ServiceException {

		if (issueReopsitory.getItemIdFromIssue(issueId)==itemId) {
			throw new ServiceException("This Issue have workItem !!");

		} else if (issueReopsitory.getItemIdFromIssue(issueId) != itemId) {

			issueReopsitory.updateWorkIssue(issueId, itemId);
			workRepository.updateTaskStatus(state, itemId);
		}

	}

	public Set<String> getAllWorkItem() throws ServiceException, RepositoryException {

		if (issueReopsitory.getWorkItemsByIssue().isEmpty()) {
			throw new ServiceException("It is empty !!");
		} else {

			return issueReopsitory.getWorkItemsByIssue();
		}
	}
}
