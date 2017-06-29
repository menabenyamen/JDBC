package se.mebe.jdbcuser.service;

import java.io.IOException;
import java.sql.SQLException;

import se.mebe.jdbcuser.exception.ServiceException;
import se.mebe.jdbcuser.pagingrepository.PagingRepository;
import se.mebe.jdbcuser.repository.BackUpRepository;

public final class PagingService {

	private final PagingRepository<?> pagingRepository;
	private final BackUpRepository backUpRepository;

	public PagingService(PagingRepository<?> pagingRepository, BackUpRepository backUpRepository) {
		this.pagingRepository = pagingRepository;
		this.backUpRepository = backUpRepository;
	}

	public void getAllMembers(int pageSize, long beginIndex) throws SQLException, ServiceException, IOException {

		do {

			if (backUpRepository.getUsersId().contains(beginIndex) || beginIndex == 0) {
				pagingRepository.getAll(pageSize, beginIndex);
				pagingRepository.saveToFile();

			} else if (backUpRepository.getWorkItemId().contains(beginIndex) || beginIndex == 0) {
				pagingRepository.getAll(pageSize, beginIndex);
				pagingRepository.saveToFile();

			} else if (backUpRepository.getIssueId().contains(beginIndex) || beginIndex == 0) {
				pagingRepository.getAll(pageSize, beginIndex);
				pagingRepository.saveToFile();

			} else {
				throw new ServiceException("No such element (BEGIN INDEX) exist in this table !!");
			}

			beginIndex = beginIndex + 10;
		} while (beginIndex < 30);

	}
}