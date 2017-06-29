package se.mebe.jdbcuser.repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import se.mebe.jdbcuser.connection.MyConnection;

public final class SQLBackUpRepository implements BackUpRepository {

	private final MyConnection connection;
	private final List<Long> getUserId;
	private final List<Long> getId;
	private final List<String> getUserState;

	public SQLBackUpRepository() throws SQLException {
		this.connection = new MyConnection();
		this.getUserId = new ArrayList<>();
		this.getId = new ArrayList<>();
		this.getUserState = new ArrayList<>();
	}

	@Override
	public List<Long> getUserIdFomUserWorkItem(long userId) {
		try (PreparedStatement statement = connection.getConnection()
				.prepareStatement("SELECT userId FROM UserWorkItem WHERE userId = ?")) {

			statement.setLong(1, userId);

			ResultSet result = statement.executeQuery();

			while (result.next()) {
				Long userIdd = result.getLong(1);
				getUserId.add(userIdd);

			}

			return getUserId;

		} catch (SQLException e) {

			throw new RuntimeException(e);
		}
	}

	@Override
	public List<Long> getUsersId() {

		try (PreparedStatement statement = connection.getConnection().prepareStatement("SELECT u.`id` FROM User u")) {

			ResultSet result = statement.executeQuery();

			while (result.next()) {
				getId.add(extractLongUser(result));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return getId;
	}

	@Override
	public List<Long> getUserTeamId(long userId) {

		try (PreparedStatement statement = connection.getConnection()
				.prepareStatement("SELECT teamId FROM User WHERE id = ?")) {

			statement.setLong(1, userId);
			ResultSet result = statement.executeQuery();

			while (result.next()) {
				long teamId = result.getLong(1);
				getId.add(teamId);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return getId;
	}

	@Override
	public void updateWorkItemState(long userId) {
		try (PreparedStatement statement = connection.getConnection()
				.prepareStatement("UPDATE WorkItem JOIN UserWorkItem ON "
						+ "WorkItem.id = UserWorkItem.workItemId SET state = 'Unstarted' "
						+ "WHERE UserWorkItem.`userId` = ?")) {
			statement.setLong(1, userId);
			statement.executeUpdate();
			statement.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getUserState(long userId) {
		String userState = null;
		try (PreparedStatement statement = connection.getConnection()
				.prepareStatement("SELECT userStatus FROM User WHERE id = ?")) {

			statement.setLong(1, userId);
			ResultSet result = statement.executeQuery();

			while (result.next()) {
				userState = result.getString(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return userState;
	}

	@Override
	public List<String> getUsersName(String userName) {

		try (PreparedStatement statement = connection.getConnection()
				.prepareStatement("SELECT userName FROM User WHERE userName = ?")) {

			statement.setString(1, userName);
			ResultSet result = statement.executeQuery();

			while (result.next()) {
				String userNamee = result.getString(1);
				getUserState.add(userNamee);
			}
		} catch (

		SQLException e) {
			e.printStackTrace();
		}
		return getUserState;
	}

	@Override
	public Long getOneUserIdFomUserWorkItem(long userId) {
		long userIdd = 0;
		try (PreparedStatement statement = connection.getConnection()
				.prepareStatement("SELECT userId FROM UserWorkItem WHERE userId = ?")) {

			statement.setLong(1, userId);

			ResultSet result = statement.executeQuery();

			while (result.next()) {
				userIdd = result.getLong(1);

			}

		} catch (SQLException e) {

			throw new RuntimeException(e);
		}
		return userIdd;
	}
	
	@Override
	public List<Long> getWorkItemId() {

		try (PreparedStatement statement = connection.getConnection().prepareStatement("SELECT WorkItem.`id` FROM WorkItem")) {

			ResultSet result = statement.executeQuery();

			while (result.next()) {
				getId.add(extractLongUser(result));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return getId;
	}
	
	@Override
	public List<Long> getIssueId() {

		try (PreparedStatement statement = connection.getConnection().prepareStatement("SELECT Issue.`id` FROM Issue")) {

			ResultSet result = statement.executeQuery();

			while (result.next()) {
				getId.add(extractLongUser(result));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return getId;
	}
	
	

	private Long extractLongUser(ResultSet result) throws SQLException {
		Long usersId = result.getLong(1);

		return usersId;
	}

}
