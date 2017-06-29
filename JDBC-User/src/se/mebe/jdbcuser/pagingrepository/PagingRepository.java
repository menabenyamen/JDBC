package se.mebe.jdbcuser.pagingrepository;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public interface PagingRepository<T> {

	List<T> getAll(int pageSize, long beginIndex) throws SQLException;

	void saveToFile();

	List<String> readFromFile() throws IOException;

}
