package se.mebe.jdbcuser.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import se.mebe.jdbcuser.connection.MyConnection;
import se.mebe.jdbcuser.exception.RepositoryException;
import se.mebe.jdbcuser.model.WorkItem;

public final class SQLWorkItemRepository implements WorkItemRepository {

	private final MyConnection connection;
	private final List<Long> getId;
	private final List<String> getDescription;
	private final List<String> getTeamItem;
	private final List<String> getUserItem;

	private static final String CREATE_TASK = "INSERT INTO WORKITEM (id, title, description, state, teamid) "
			+ "VALUES (?,?,?,?,?)";
	private static final String UPDATE_STATE = "UPDATE WorkItem SET state = ? WHERE id = ?";
	private static final String REMOVE_TASK = "DELETE FROM WORKITEM WHERE ID = ?";
	private static final String ASSIGN_TASK = "INSERT INTO USERWORKITEM (workitemid,userid) " + "VALUES (?,?)";
	private static final String GETSTATEFROMWORKITEM = "SELECT state FROM WorkItem WHERE state = ?";
	private static final String GET_ALL_WORKITEM_BASED_ON_STATUS = "SELECT description FROM WorkItem WHERE state = ?";
	private static final String GET_ALL_WORKITEM_FOR_USER = "SELECT description FROM WorkItem w JOIN UserWorkItem u ON u.`workItemId` = w.`id` JOIN USER us ON us.`id` = u.`userId` WHERE us.`id` = ?;";
	private static final String GET_ALL_WORKITEM_FOR_TEAM = "SELECT  description FROM WORKITEM WHERE TEAMID = ?";
	private static final String GET_WORKITEM_ID = "SELECT id FROM WorkItem WHERE id = ?";
	private static final String GET_WORKITEM_TEAMID = "SELECT teamId FROM WorkItem WHERE teamId = ?";

	public SQLWorkItemRepository() throws SQLException {
		this.connection = new MyConnection();
		this.getId = new ArrayList<>();
		this.getDescription = new ArrayList<>();
		this.getTeamItem = new ArrayList<>();
		this.getUserItem = new ArrayList<>();
	}

	@Override
	public WorkItem createTask(WorkItem workItem) {
		try (PreparedStatement statement = createStatement(connection.getConnection(), CREATE_TASK)) {

			statement.setLong(1, workItem.getId());
			statement.setString(2, workItem.getTitle());
			statement.setString(3, workItem.getDescription());
			statement.setString(4, workItem.getState());
			statement.setLong(5, workItem.getTeamId());
			statement.executeUpdate();

		} catch (SQLException e) {

			e.printStackTrace();
		}
		return workItem;
	}

	@Override
	public String updateTaskStatus(String status, long workitemId) {
		try (PreparedStatement statement = createStatement(connection.getConnection(), UPDATE_STATE)) {

			statement.setString(1, status);
			statement.setLong(2, workitemId);
			statement.executeUpdate();

		} catch (SQLException e) {

			e.printStackTrace();
		}
		return status;
	}

	@Override
	public void removeTask(long workitemId) {
		try (PreparedStatement statement = createStatement(connection.getConnection(), REMOVE_TASK)) {

			statement.setLong(1, workitemId);
			statement.executeUpdate();

		} catch (SQLException e) {

			e.printStackTrace();
		}
	}

	@Override
	public void assignTask(long workitemId, long userId) {
		try (PreparedStatement statement = createStatement(connection.getConnection(), ASSIGN_TASK)) {

			statement.setLong(1, workitemId);
			statement.setLong(2, userId);
			statement.executeUpdate();

		} catch (SQLException e) {

			e.printStackTrace();
		}

	}

	@Override
	public List<String> getWorkItemsByState(String state) throws RepositoryException {
		try (PreparedStatement statement = createStatement(connection.getConnection(), GET_ALL_WORKITEM_BASED_ON_STATUS,
				state)) {

			statement.setString(1, state);
			ResultSet result = statement.executeQuery();

			while (result.next()) {
				String description = result.getString(1);
				getTeamItem.add(description);
			}
			return getTeamItem;
		} catch (SQLException e) {
			throw new RepositoryException(e);
		}

	}

	@Override
	public List<String> getAllWorkItemForUser(long userId) throws RepositoryException {
		try (PreparedStatement statement = createStatement(connection.getConnection(), GET_ALL_WORKITEM_FOR_USER,
				userId)) {

			statement.setLong(1, userId);
			ResultSet result = statement.executeQuery();

			while (result.next()) {
				String description = result.getString(1);
				getUserItem.add(description);
			}

			return getUserItem;
		} catch (SQLException e) {
			throw new RepositoryException(e);
		}
	}

	@Override
	public List<String> getAllWorkItemForOneTeam(long teamId) throws RepositoryException {
		try (PreparedStatement statement = createStatement(connection.getConnection(), GET_ALL_WORKITEM_FOR_TEAM,
				teamId)) {
			statement.setLong(1, teamId);
			ResultSet result = statement.executeQuery();

			while (result.next()) {
				String description = result.getString(1);
				getDescription.add(description);
			}

			return getDescription;
		} catch (SQLException e) {
			throw new RepositoryException(e);
		}
	}

	@Override
	public List<String> getStateFromWorkItem(String state) throws RepositoryException {
		try (PreparedStatement statement = createStatement(connection.getConnection(), GETSTATEFROMWORKITEM, state)) {

			statement.setString(1, state);
			ResultSet result = statement.executeQuery();

			while (result.next()) {
				String statee = result.getString(1);
				getDescription.add(statee);
			}

		} catch (SQLException e) {
			throw new RepositoryException(e);
		}
		return getDescription;
	}

	@Override
	public Long getWorkItemId(long itemId) {
		long workItemId = 0;
		try (PreparedStatement statement = createStatement(connection.getConnection(), GET_WORKITEM_ID, itemId)) {

			statement.setLong(1, itemId);

			ResultSet result = statement.executeQuery();

			while (result.next()) {
				workItemId = result.getLong(1);

			}

		} catch (SQLException e) {

			throw new RuntimeException(e);
		}
		return workItemId;
	}

	@Override
	public List<Long> getteamIdFromWorkItem(long teamId) {
		try (PreparedStatement statement = createStatement(connection.getConnection(), GET_WORKITEM_TEAMID, teamId)) {

			statement.setLong(1, teamId);

			ResultSet result = statement.executeQuery();

			while (result.next()) {
				Long teamIdd = result.getLong(1);
				getId.add(teamIdd);

			}

			return getId;

		} catch (SQLException e) {

			throw new RuntimeException(e);
		}
	}

	private PreparedStatement createStatement(Connection connection, String query, Object... parameters)
			throws SQLException {

		PreparedStatement statement = connection.prepareStatement(query);

		for (int i = 0; i < parameters.length; i++) {
			statement.setObject(i + 1, parameters[i]);
		}

		return statement;
	}

	@Override
	public String getWorkItemStatusByWorkItemId(long id) {
		String status = null;
		try (PreparedStatement statement = connection.getConnection()
				.prepareStatement("SELECT state FROM WorkItem WHERE id = ?")) {

			statement.setLong(1, id);

			ResultSet result = statement.executeQuery();

			while (result.next()) {
				status = result.getString(1);

			}

			return status;

		} catch (SQLException e) {

			throw new RuntimeException(e);
		}
	}

}
