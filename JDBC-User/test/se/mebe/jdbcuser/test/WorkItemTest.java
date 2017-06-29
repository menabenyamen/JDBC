package se.mebe.jdbcuser.test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import se.mebe.jdbcuser.exception.RepositoryException;
import se.mebe.jdbcuser.exception.ServiceException;
import se.mebe.jdbcuser.model.WorkItem;
import se.mebe.jdbcuser.repository.BackUpRepository;
import se.mebe.jdbcuser.repository.WorkItemRepository;
import se.mebe.jdbcuser.service.WorkItemService;



@RunWith(MockitoJUnitRunner.class)
public final class WorkItemTest {

	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	@Mock
	private WorkItemRepository workItemRepository;

	@Mock
	private BackUpRepository backUpRepository;

	@InjectMocks
	private WorkItemService workItemService;

	@Test
	public void canNotCreateWorkItemWithSameId() throws SQLException, ServiceException {

		WorkItem workItem = new WorkItem(0, "C++", "solv a problem", "Started", 0);

		expectedException.expect(ServiceException.class);
		expectedException.expectMessage("This id is Exist in your Table !!");

		when(workItemRepository.getWorkItemId(workItem.getId())).thenReturn(workItem.getId());

		workItemService.createWorkItem(workItem);
	}

	@Test
	public void canCreateAWorkItem() throws SQLException, ServiceException {

		WorkItem workItem = new WorkItem(0, "C++", "solve a problem", "Started", 0);


		when(workItemRepository.getWorkItemId(workItem.getId())).thenReturn(5L);
		when(workItemRepository.createTask(workItem)).thenReturn(workItem);
		workItemService.createWorkItem(workItem);

		assertThat(workItemRepository.createTask(workItem), is(equalTo(workItem)));
	}

	@Test
	public void canNotUpdateWithMissingId() throws SQLException, ServiceException {

		long itemId = 2L;
		String state = "Unstarted";

		expectedException.expect(ServiceException.class);
		expectedException.expectMessage("This id dose not exist !!");

		when(workItemRepository.getWorkItemId(itemId)).thenReturn(22L);

		workItemService.updateWorkItemState(itemId, state);
	}

	@Test
	public void SouldUpdateWorkItemState() throws SQLException, ServiceException, RepositoryException {

		long itemId = 1L;
		String state = "Started";
		String status = "Unstarted";
		WorkItem workItem = new WorkItem(itemId, "Keson är fel", "Jag vill ha fetaost!", status, 20L);

		when(workItemRepository.createTask(workItem)).thenReturn(workItem);
		when(workItemRepository.getWorkItemStatusByWorkItemId(itemId)).thenReturn("Started");
		when(workItemRepository.updateTaskStatus(status, itemId)).thenReturn(state);
		when(workItemRepository.getWorkItemId(itemId)).thenReturn(itemId);
		when(workItemRepository.updateTaskStatus(state, itemId)).thenReturn("Started");
		workItemService.updateWorkItemState(itemId, state);

		
		assertThat(workItemRepository.getWorkItemStatusByWorkItemId(itemId), is(equalTo(state)));
	}

	@Test
	public void canNotDeleteWithNotExistingId() throws SQLException, ServiceException {

		long itemId = 15L;

		expectedException.expect(ServiceException.class);
		expectedException.expectMessage("You cant delelt an ID that dosnt exist !!");

		when(workItemRepository.getWorkItemId(itemId)).thenReturn(33L);

		workItemService.deleteWorkItem(itemId);

	}

	@Test
	public void willDeleteAWorkItem() throws SQLException, ServiceException {

		long itemId = 10L;
		long workItemId = 10L;

		when(workItemRepository.getWorkItemId(itemId)).thenReturn(workItemId);

		workItemService.deleteWorkItem(itemId);

		assertThat(workItemRepository.getWorkItemId(itemId), is(equalTo(10L)));

	}

	@Test
	public void canNotAsignWorkItemsToInactiveUser() throws SQLException, ServiceException {

		long itemId = 2;
		long userId = 1;
		String state = "Inactive";

		expectedException.expect(ServiceException.class);
		expectedException.expectMessage("User cant get work item, You Are INACTIVE User !!");

		when(backUpRepository.getUserState(userId)).thenReturn(state);

		workItemService.assignTaskToUser(itemId, userId);

	}

	@Test
	public void canNotAsignMoreThan5WorkItemsToUser() throws SQLException, ServiceException {

		long itemId = 5L;
		long userId = 5L;
		String state = "Active";

		List<Long> list = new ArrayList<>();
		list.add(5L);
		list.add(5L);
		list.add(5L);
		list.add(5L);
		list.add(5L);
		list.add(5L);

		expectedException.expect(ServiceException.class);
		expectedException.expectMessage("This user has 5 work item , he get no more !!");

		when(backUpRepository.getUserState(userId)).thenReturn(state);
		when(backUpRepository.getUserIdFomUserWorkItem(userId)).thenReturn(list);

		workItemService.assignTaskToUser(itemId, userId);

	}

	@Test
	public void willTrowExceptionIfUserIdDontExist() throws ServiceException {

		long userId = 10L;
		long itemId = 2L;

		List<Long> userIdList = new ArrayList<>();
		userIdList.add(10L);
		userIdList.add(10L);
		userIdList.add(10L);
		userIdList.add(10L);

		expectedException.expect(ServiceException.class);
		expectedException.expectMessage("This user id is not in your table");

		when(backUpRepository.getUserState(userId)).thenReturn("Active");
		when(backUpRepository.getUserIdFomUserWorkItem(userId)).thenReturn(userIdList);
		when(backUpRepository.getOneUserIdFomUserWorkItem(userId)).thenReturn(66L);

		workItemService.assignTaskToUser(itemId, userId);

	}

	@Test
	public void canAsignWorkItemToUser() throws SQLException, ServiceException {

		long itemId = 2L;
		long userId = 3L;
		
		List<Long> userIdList = new ArrayList<>();
		userIdList.add(3L);
		userIdList.add(10L);
		userIdList.add(20L);
		
		when(backUpRepository.getUserState(userId)).thenReturn("Active");
		when(backUpRepository.getUserIdFomUserWorkItem(userId)).thenReturn(userIdList);
		when(backUpRepository.getOneUserIdFomUserWorkItem(userId)).thenReturn(userId);
		
		workItemService.assignTaskToUser(itemId, userId);
		assertThat(backUpRepository.getUserIdFomUserWorkItem(userId).iterator().next(), is(equalTo(userId)));

	}

	@Test
	public void willNotGetWorkItemWithNotexistingState() throws RepositoryException {

		String state = "Done";
		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("This state dosnt exist");

		when(workItemRepository.getWorkItemsByState(state).isEmpty())
				.thenThrow(new ServiceException("This state dosnt exist"));

		workItemRepository.getStateFromWorkItem(state);
	}

	@Test
	public void willReturnWorkItemByState() throws SQLException, ServiceException, RepositoryException {

		List<String> list = new ArrayList<>();
		String state = "Unstarted";
		list.add(state);

		when(workItemRepository.getStateFromWorkItem(state)).thenReturn(list);
		when(workItemRepository.getWorkItemsByState(state)).thenReturn(list);

		workItemService.getWorkItemForAState(state);

		assertThat(workItemRepository.getWorkItemsByState(state).iterator().next(), is(equalTo("Unstarted")));

	}

	@Test
	public void shouldThrowExceptionWithoutExistingId() throws RepositoryException {
		long teamId = 200;
		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("This team id dosnt exist !!");

		when(workItemRepository.getAllWorkItemForOneTeam(teamId).isEmpty())
				.thenThrow(new ServiceException("This team id dosnt exist !!"));

		workItemRepository.getteamIdFromWorkItem(teamId);

	}

	@Test
	public void willReturnWorkItemForTeam() throws SQLException, ServiceException, RepositoryException {

		long teamId = 100L;
		long workItemId = 100L;
		List<String> list = new ArrayList<>();
		list.add("keso");
		List<Long> workIdforTeamList = new ArrayList<>();
		workIdforTeamList.add(workItemId);

		when(workItemRepository.getteamIdFromWorkItem(teamId)).thenReturn(workIdforTeamList);
		when(workItemRepository.getAllWorkItemForOneTeam(teamId)).thenReturn(list);

		workItemService.getTeamWorkItem(teamId);

		assertThat(workItemRepository.getAllWorkItemForOneTeam(teamId).iterator().next(), is(equalTo("keso")));

	}

	@Test
	public void shouldThrowExceptionWithOutExistingUserId() throws RepositoryException {

		long userId = 20L;

		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("This user id dosnt exist !!");

		when(workItemRepository.getAllWorkItemForUser(userId).isEmpty())
				.thenThrow(new ServiceException("This user id dosnt exist !!"));

		workItemRepository.getAllWorkItemForUser(userId);

	}

	@Test
	public void willReturnWorkItemsForUser() throws SQLException, ServiceException, RepositoryException {

		long userId = 1L;

		List<String> list = new ArrayList<>();
		list.add("booooo");
		list.add("booooo");
		list.add("booooo");
		list.add("booooo");
		list.add("booooo");
		
		List<Long> userIdList = new ArrayList<>();
		userIdList.add(userId);

		when(backUpRepository.getUserIdFomUserWorkItem(userId)).thenReturn(userIdList);
		when(workItemRepository.getAllWorkItemForUser(userId)).thenReturn(list);

		workItemService.getUserWorkItem(userId);

		assertThat(workItemRepository.getAllWorkItemForUser(userId), is(equalTo(list)));

	}

}
