package se.mebe.jdbcuser.pagingrepository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import se.mebe.jdbcuser.connection.MyConnection;
import se.mebe.jdbcuser.exception.ServiceException;

public final class SQLHistory implements HistoryRepository {

	private final MyConnection connection;
	private final List<String> getDescription;
	private java.sql.Date getTableDate;
	private Date dateOne;
	private Date dateTwo;
	private java.sql.Date firstDate;
	private java.sql.Date secondDate;

	public SQLHistory() throws SQLException {

		this.getDescription = new ArrayList<>();
		this.connection = new MyConnection();
	}

	@Override
	public List<String> getDescriptionAndHistory(String state, String firstDateString, String secondDateString) {

		getFirstDate(firstDateString);
		getSecondDate(secondDateString);

		try (PreparedStatement statement = connection.getConnection()
				.prepareStatement("SELECT description FROM WorkItem WHERE state = ? AND Date BETWEEN ? AND ?")) {
			statement.setString(1, state);
			statement.setDate(2, firstDate);
			statement.setDate(3, secondDate);

			ResultSet result = statement.executeQuery();

			while (result.next()) {
				getDescription.add(result.getString("description"));

			}

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		return getDescription;

	}

	@Override
	public java.sql.Date getFirstDate(String firstDateString) {

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

		try {
			dateOne = formatter.parse(firstDateString);
			firstDate = new java.sql.Date(dateOne.getTime());

		} catch (ParseException e) {
			e.printStackTrace();
		}
		return firstDate;
	}

	@Override
	public java.sql.Date getSecondDate(String secondDateString) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

		try {
			dateTwo = formatter.parse(secondDateString);
			secondDate = new java.sql.Date(dateTwo.getTime());

		} catch (ParseException e) {
			e.printStackTrace();
		}
		return secondDate;
	}

	@Override
	public java.sql.Date getCreationDateForTable() throws ServiceException {

		try (PreparedStatement statement = connection.getConnection().prepareStatement(
				"SELECT create_time FROM INFORMATION_SCHEMA.TABLES WHERE table_schema = 'CMDB' AND TABLE_NAME = 'WorkItem'")) {

			ResultSet result = statement.executeQuery();

			while (result.next()) {

				getTableDate = result.getDate("create_time");
				return getTableDate;
			}
			throw new ServiceException("Could not find this date:" + getTableDate);

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

	}

	@Override
	public java.sql.Date getCurrentDate() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		dateFormat.format(date);
		java.sql.Date sqlDate = new java.sql.Date(date.getTime());
		return sqlDate;
	}

}
