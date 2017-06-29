package se.mebe.jdbcuser.test;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import static org.mockito.Mockito.*;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import se.mebe.jdbcuser.exception.ServiceException;
import se.mebe.jdbcuser.pagingrepository.HistoryRepository;

import se.mebe.jdbcuser.service.HistoryService;

@RunWith(MockitoJUnitRunner.class)
public final class HistoryTest {

	@Mock
	private HistoryRepository historyRepository;

	@InjectMocks
	private HistoryService historyService;

	@Rule
	public final ExpectedException expectedException = ExpectedException.none();

	@Test
	public void shouldThrowExceptionIfStateIsNotDone() throws ServiceException {
		final String state = "Started";
		final String firstDate = "2017-01-02";
		final String secondDate = "2017-01-04";

		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("The state must be Done to get by history !!");

		when(historyRepository.getDescriptionAndHistory(state, firstDate, secondDate))
				.thenThrow(new ServiceException("The state must be Done to get by history !!"));

		historyService.getDescriptionByHistory(state, firstDate, secondDate);
	}

	@Test
	public void willNotAcceptDateBeforeTableCreation() throws ServiceException {
		final String state = "Done";
		final String firstDate = "2016-01-01";
		final String secondDate = "2017-01-02";

		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("The first date is worng, Your table wasnt created an !!");

		when(historyRepository.getFirstDate(firstDate))
				.thenThrow(new ServiceException("The first date is worng, Your table wasnt created an !!"));

		historyService.getDescriptionByHistory(state, firstDate, secondDate);
	}

	@Test
	public void willNotAcceptDateEfterCurrentDate() throws ServiceException, SQLException {
		final String state = "Done";
		final String firstDate = "2017-01-02";
		final String secondDate = "2017-01-20";

		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("The second date is worng, You dont have record at this date!!");

		when(historyRepository.getSecondDate(secondDate))
				.thenThrow(new ServiceException("The second date is worng, You dont have record at this date!!"));

		historyService.getDescriptionByHistory(state, firstDate, secondDate);

	}

	@Test
	public void shouldReturnDescriptionBetweenTwoDate() throws ServiceException {
		final String state = "Done";
		final String firstDate = "2017-01-02";
		final String secondDate = "2017-01-03";

		final List<String> descriptionList = new ArrayList<>();
		descriptionList.add("Coding in Java");

		when(historyRepository.getDescriptionAndHistory(state, firstDate, secondDate)).thenReturn(descriptionList);

		historyService.getDescriptionByHistory(state, firstDate, secondDate);

		assertThat(historyRepository.getDescriptionAndHistory(state, firstDate, secondDate).iterator().next(),
				is(equalTo("Coding in Java")));
	}
}
