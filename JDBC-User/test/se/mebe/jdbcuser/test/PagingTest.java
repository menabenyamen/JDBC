package se.mebe.jdbcuser.test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;
import java.io.IOException;
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
import se.mebe.jdbcuser.model.Issue;
import se.mebe.jdbcuser.model.User;
import se.mebe.jdbcuser.model.WorkItem;
import se.mebe.jdbcuser.pagingrepository.PagingRepository;
import se.mebe.jdbcuser.repository.BackUpRepository;
import se.mebe.jdbcuser.service.PagingService;

@RunWith(MockitoJUnitRunner.class)
public final class PagingTest {

	@Mock
	private PagingRepository<User> pagingRepositoryUser;

	@Mock
	private PagingRepository<WorkItem> pagingRepositoryWork;

	@Mock
	private PagingRepository<Issue> pagingRepositoryIssue;

	@Mock
	private BackUpRepository backUpRepository;

	@InjectMocks
	private PagingService pagingService;

	@Rule
	public final ExpectedException expectedException = ExpectedException.none();

	@Test
	public void shouldThrowExceptionIfIdDosntExsit()
			throws SQLException, ServiceException, RepositoryException, IOException {

		final long beginIndex = 33L;
		final int pageSize = 10;

		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("No such element (BEGIN INDEX) exist in this table !!");

		when(pagingRepositoryUser.getAll(pageSize, beginIndex))
				.thenThrow(new ServiceException("No such element (BEGIN INDEX) exist in this table !!"));

		pagingService.getAllMembers(pageSize, beginIndex);
	}

	@Test
	public void shouldRetrunUserId() throws SQLException, ServiceException, IOException {

		final List<Long> list = new ArrayList<>();
		final long value = 10L;
		final long beginIndex = 10L;
		final int pageSize = 10;

		list.add(value);

		when(backUpRepository.getUsersId()).thenReturn(list);

		pagingService.getAllMembers(pageSize, beginIndex);

		assertThat(backUpRepository.getUsersId().iterator().next(), is(10L));
	}

	@Test
	public void shouldGetAllUserByPaging() throws ServiceException, SQLException, IOException {
		final List<Long> list1 = new ArrayList<>();
		final long value = 10L;
		list1.add(value);

		User user = new User(11, "BLackjack00", "Kal", "David", 200, "Inactive");
		final List<User> list = new ArrayList<>();
		list.add(user);

		final long beginIndex = 10L;
		final int pageSize = 10;

		when(backUpRepository.getUsersId()).thenReturn(list1);
		when(pagingRepositoryUser.getAll(pageSize, beginIndex)).thenReturn((list));

		pagingService.getAllMembers(pageSize, beginIndex);

		assertTrue(pagingRepositoryUser.getAll(pageSize, beginIndex).contains(user));

	}

	@Test
	public void shouldSaveAllToFile() throws IOException, SQLException {
		final long beginIndex = 1L;
		final int pageSize = 10;

		// when(pagingRepositoryUser.readFromFile()).thenReturn(pagingRepositoryUser.getAll(beginIndex,
		// pageSize));
		pagingRepositoryUser.saveToFile();
		assertThat(pagingRepositoryUser.readFromFile(), is(equalTo(pagingRepositoryUser.getAll(pageSize, beginIndex))));
	}

	@Test
	public void shouldReturnWorkITemId() throws SQLException, ServiceException, IOException {

		final long beginIndex = 12L;
		final int pageSize = 10;

		final List<Long> workItemIdList = new ArrayList<>();
		final long value = 12L;
		workItemIdList.add(value);

		when(backUpRepository.getWorkItemId()).thenReturn(workItemIdList);
		pagingService.getAllMembers(pageSize, beginIndex);

		assertThat(backUpRepository.getWorkItemId().iterator().next(), is(equalTo(12L)));
	}

	@Test
	public void willGetAllWorkItemFromSQL() throws SQLException, ServiceException, IOException {

		final WorkItem workItem = new WorkItem(12, "Fotball", "buy A player", "Started", 200);
		final List<WorkItem> workItemList = new ArrayList<>();
		workItemList.add(workItem);

		final List<Long> workItemIdList = new ArrayList<>();
		final long value = 12L;
		workItemIdList.add(value);

		final long beginIndex = 12L;
		final int pageSize = 10;

		when(backUpRepository.getWorkItemId()).thenReturn(workItemIdList);
		when(pagingRepositoryWork.getAll(pageSize, beginIndex)).thenReturn(workItemList);

		pagingService.getAllMembers(pageSize, beginIndex);

		assertTrue(pagingRepositoryWork.getAll(pageSize, beginIndex).contains(workItem));
	}

	@Test
	public void shouldSaveWorkITemToFile() throws IOException, SQLException {

		final long beginIndex = 12L;
		final int pageSize = 10;

		pagingRepositoryWork.saveToFile();
		assertThat(pagingRepositoryWork.readFromFile(), is(equalTo(pagingRepositoryWork.getAll(pageSize, beginIndex))));
	}

	@Test
	public void willReturnIssueId() throws SQLException, ServiceException, IOException {

		final long beginIndex = 1000L;
		final int pageSize = 10;

		final List<Long> issueIdList = new ArrayList<>();
		final long value = 1000L;
		issueIdList.add(value);

		when(backUpRepository.getIssueId()).thenReturn(issueIdList);
		pagingService.getAllMembers(pageSize, beginIndex);

		assertThat(backUpRepository.getIssueId().iterator().next(), is(equalTo(1000L)));
	}

	@Test
	public void willReturnIssueFromSQL() throws SQLException, ServiceException, IOException {

		Issue issue = new Issue(1000, "dsadas", 3);
		final long beginIndex = 1000L;
		final int pageSize = 10;

		List<Issue> issueList = new ArrayList<>();
		issueList.add(issue);

		final List<Long> issueIdList = new ArrayList<>();
		final long value = 1000L;
		issueIdList.add(value);

		when(backUpRepository.getIssueId()).thenReturn(issueIdList);
		when(pagingRepositoryIssue.getAll(pageSize, beginIndex)).thenReturn(issueList);
		pagingService.getAllMembers(pageSize, beginIndex);

		assertTrue(pagingRepositoryIssue.getAll(pageSize, beginIndex).contains(issue));
	}

	@Test
	public void shouldSaveAllIssueToFile() throws IOException, SQLException {

		final long beginIndex = 1000L;
		final int pageSize = 10;

		pagingRepositoryIssue.saveToFile();
		assertThat(pagingRepositoryIssue.readFromFile(),
				is(equalTo(pagingRepositoryIssue.getAll(pageSize, beginIndex))));
	}

}
