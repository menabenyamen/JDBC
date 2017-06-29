package se.mebe.jdbcuser.service;

import java.sql.SQLException;
import java.util.List;

import se.mebe.jdbcuser.exception.RepositoryException;
import se.mebe.jdbcuser.exception.ServiceException;
import se.mebe.jdbcuser.model.WorkItem;
import se.mebe.jdbcuser.repository.BackUpRepository;
import se.mebe.jdbcuser.repository.WorkItemRepository;


public final class WorkItemService {

	private final WorkItemRepository workRepository;
	private final BackUpRepository userWorkRepository;

	public WorkItemService(WorkItemRepository workRepository, BackUpRepository userWorkRepository)
			throws SQLException, ServiceException {

		this.workRepository = workRepository;
		this.userWorkRepository = userWorkRepository;
	}

	public void createWorkItem(WorkItem workItem) throws ServiceException {

		if (workRepository.getWorkItemId(workItem.getId()) == workItem.getId()) {
			throw new ServiceException("This id is Exist in your Table !!");

		} else {
			workRepository.createTask(workItem);
		}
	}

	public void updateWorkItemState(long itemId, String status) throws ServiceException {

		if (workRepository.getWorkItemId(itemId) != itemId) {
			throw new ServiceException("This id dose not exist !!");

		} else if (workRepository.getWorkItemId(itemId) == itemId) {
			workRepository.updateTaskStatus(status, itemId);
		}

	}

	public void deleteWorkItem(long itemId) throws ServiceException {
		if (workRepository.getWorkItemId(itemId) != itemId) {
			throw new ServiceException("You cant delelt an ID that dosnt exist !!");

		} else if (workRepository.getWorkItemId(itemId) == itemId) {
			workRepository.removeTask(itemId);
		}

	}

	public void assignTaskToUser(long itemId, long userId) throws ServiceException {

		if (userWorkRepository.getUserState(userId).contains("Inactive")) {
			throw new ServiceException("User cant get work item, You Are INACTIVE User !!");

		} else if (userWorkRepository.getUserIdFomUserWorkItem(userId).size() == 6) {
			throw new ServiceException("This user has 5 work item , he get no more !!");

		} else if (userWorkRepository.getOneUserIdFomUserWorkItem(userId) != userId) {
			throw new ServiceException("This user id is not in your table");

		} else {
			workRepository.assignTask(itemId, userId);
		}
	}

	public List<String> getWorkItemForAState(String state) throws RepositoryException, ServiceException {
		if (!workRepository.getStateFromWorkItem(state).contains(state)) {
			throw new ServiceException("This state dosnt exist");
		} else {

			return workRepository.getWorkItemsByState(state);
		}

	}

	public List<String> getTeamWorkItem(long teamId) throws RepositoryException, ServiceException {
		if (!workRepository.getteamIdFromWorkItem(teamId).contains(teamId)) {
			throw new ServiceException("This team id dosnt exist !!");
		} else {

			return workRepository.getAllWorkItemForOneTeam(teamId);
		}
	}

	public List<String> getUserWorkItem(long userId) throws RepositoryException, ServiceException {
		if (!userWorkRepository.getUserIdFomUserWorkItem(userId).contains(userId)) {
			throw new ServiceException("This user id dosnt exist !!");

		} else {

			return workRepository.getAllWorkItemForUser(userId);
		}
	}

}
