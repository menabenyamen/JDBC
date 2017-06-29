package se.mebe.jdbcuser.service;

import java.sql.SQLException;
import se.mebe.jdbcuser.exception.ServiceException;
import se.mebe.jdbcuser.pagingrepository.HistoryRepository;
import se.mebe.jdbcuser.pagingrepository.SQLHistory;

public final class HistoryService {

	private final HistoryRepository historyRepository;

	public HistoryService(HistoryRepository historyRepository) throws SQLException {
		this.historyRepository = new SQLHistory();

	}

	public void getDescriptionByHistory(String state, String firstDate, String secondDate) throws ServiceException {

		if (!state.equals("Done")) {
			throw new ServiceException("The state must be Done to get by history !!");

		} else if (historyRepository.getFirstDate(firstDate).before(historyRepository.getCreationDateForTable())) {
			throw new ServiceException("The first date is worng, Your table wasnt created an !!");

		} else if (historyRepository.getSecondDate(secondDate).after(historyRepository.getCurrentDate())) {
			throw new ServiceException("The second date is worng, You dont have record at this date!!");

		} else {
			System.out.println(historyRepository.getDescriptionAndHistory(state, firstDate, secondDate));
		}
	}

}
