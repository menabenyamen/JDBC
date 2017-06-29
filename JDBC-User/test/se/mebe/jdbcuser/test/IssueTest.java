package se.mebe.jdbcuser.test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import se.mebe.jdbcuser.exception.RepositoryException;
import se.mebe.jdbcuser.exception.ServiceException;
import se.mebe.jdbcuser.repository.IssueRepository;
import se.mebe.jdbcuser.repository.WorkItemRepository;
import se.mebe.jdbcuser.service.IssueService;

import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public final class IssueTest {

	@Mock
	private IssueRepository issueRepository;

	@Mock
	private WorkItemRepository workItemRepository;

	@InjectMocks
	private IssueService isuueSerivce;

	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	@Test
	public void shouldThrowExceptionIfItemStateIsNotDone() throws SQLException, ServiceException {

		long itemId = 1L;
		String issueReason = "dsadas";
		String state = "Started";
		long issueId = 1L;

		expectedException.expect(ServiceException.class);
		expectedException.expectMessage("Work item is not yet Done !!");

		// verify(issueRepository).createIssue(issueId, issueReason, itemId);
		when(issueRepository.getWorkItemStatus(itemId)).thenReturn(state);
		isuueSerivce.addIssue(issueId, itemId, issueReason, state);

	}

	@Test
	public void shouldThrowExceptionIfIssueIdExist() throws SQLException, ServiceException {

		long itemId = 1L;
		String issueReason = "dsadas";
		String state = "Done";
		long issueId = 1000L;

		expectedException.expect(ServiceException.class);
		expectedException.expectMessage("This Id is in the issue !!");

		when(issueRepository.getWorkItemStatus(itemId)).thenReturn(state);
		when(issueRepository.getItemIdFromIssue(issueId)).thenReturn(issueId);
		isuueSerivce.addIssue(issueId, itemId, issueReason, state);
	}

	@Test
	public void canAddAIssue() throws SQLException, ServiceException {

		long itemId = 7L;
		String issueReason = "Dont Work";
		String state = "Done";
		long issueId = 1026L;

		when(issueRepository.getWorkItemStatus(itemId)).thenReturn(state);
		when(issueRepository.getItemIdFromIssue(issueId)).thenReturn(itemId);
		isuueSerivce.addIssue(issueId, itemId, issueReason, state);

		assertThat(issueRepository.getItemIdFromIssue(issueId), is(equalTo(itemId)));

	}

	@Test
	public void ShouldThrowAnExceptionIfIssueHaveNotWorkItem() throws SQLException, ServiceException {

		long issueId = 1000L;
		long itemId = 1L;
		String state = "Done";

		expectedException.expect(ServiceException.class);
		expectedException.expectMessage("This Issue have workItem !!");

		when(issueRepository.getItemIdFromIssue(issueId)).thenReturn(itemId);
		isuueSerivce.updateIssue(itemId, issueId, state);

	}

	@Test
	public void shouldUpdateWorkItemState() throws SQLException, ServiceException {

		long issueId = 1049L;
		long itemId = 17L;
		String state = "Unstarted";

		isuueSerivce.updateIssue(itemId, issueId, state);

		verify(issueRepository).updateWorkIssue(issueId, itemId);

	}

	@Test
	public void shouldReturnWorkItem() throws RepositoryException, SQLException, ServiceException {
		// final IssueService isuueSerivce = new IssueService();

		final Set<String> getIssue = new HashSet<>();
		getIssue.add("keso");

		assertThat(issueRepository.getWorkItemsByIssue(), is(notNullValue()));
		when(issueRepository.getWorkItemsByIssue()).thenReturn(getIssue);
		isuueSerivce.getAllWorkItem();
		// issueRepository.getWorkItemsByIssue();

	}

	@Test
	public void ShouldThrowAnExceptionIfGetItemByIssueIsEmpty()
			throws SQLException, ServiceException, RepositoryException {

		final Set<String> empty = new HashSet<>();

		expectedException.expect(ServiceException.class);
		expectedException.expectMessage("It is empty !!");

		when(issueRepository.getWorkItemsByIssue()).thenReturn(empty);
		isuueSerivce.getAllWorkItem();
	}
}
