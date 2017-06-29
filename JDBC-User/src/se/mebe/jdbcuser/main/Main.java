package se.mebe.jdbcuser.main;

import java.io.IOException;
import java.sql.SQLException;
import se.mebe.jdbcuser.exception.ServiceException;
import se.mebe.jdbcuser.pagingrepository.SQLHistory;
//import se.mebe.jdbcuser.pagingrepository.SQLPagingIssues;
//import se.mebe.jdbcuser.pagingrepository.SQLPagingUser;
//import se.mebe.jdbcuser.pagingrepository.SQLPagingWorkItems;
//import se.mebe.jdbcuser.repository.SQLBackUpRepository;
import se.mebe.jdbcuser.service.HistoryService;
//import se.mebe.jdbcuser.service.PagingService;

public class Main {

	public static void main(String[] args) throws SQLException, ServiceException, IOException {

//		PagingService user = new PagingService(new SQLPagingUser(), new SQLBackUpRepository());
//
//		user.getAllMembers(10, 0);
//		user.getAllMembers(10, 10);
//		user.getAllMembers(10, 20);
//
//		PagingService workItem = new PagingService(new SQLPagingWorkItems(), new SQLBackUpRepository());
//		workItem.getAllMembers(10, 0);
//		workItem.getAllMembers(10, 10);
//		workItem.getAllMembers(10, 20);
//
//		PagingService issue = new PagingService(new SQLPagingIssues(), new SQLBackUpRepository());
//		issue.getAllMembers(10, 0);
//		issue.getAllMembers(10, 10);
//		issue.getAllMembers(10, 20);

		HistoryService workItems = new HistoryService(new SQLHistory());
		workItems.getDescriptionByHistory("Done", "2017-01-02", "2017-01-03");

	}

}
