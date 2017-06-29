 package se.mebe.jdbcuser.pagingrepository;

import java.sql.Date;
import java.util.List;

import se.mebe.jdbcuser.exception.ServiceException;

public interface HistoryRepository {
	
	List<String> getDescriptionAndHistory(String state, String firstDate, String secondDate);

	Date getFirstDate(String firstDateString);

	Date getSecondDate(String secondDateString);

	Date getCreationDateForTable() throws ServiceException;

	Date getCurrentDate();

}
