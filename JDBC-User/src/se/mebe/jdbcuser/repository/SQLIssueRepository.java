package se.mebe.jdbcuser.repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import se.mebe.jdbcuser.connection.MyConnection;
import se.mebe.jdbcuser.exception.RepositoryException;
import se.mebe.jdbcuser.model.Issue;

public final class SQLIssueRepository implements IssueRepository {

	private final Set<String> getIssue;
	private final MyConnection connection;

	public SQLIssueRepository() throws SQLException {
		this.getIssue = new HashSet<>();
		this.connection = new MyConnection();
	}

	@Override
	public Issue createIssue(long issueId, String comment, long workItemId) {
		try (PreparedStatement statement = connection.getConnection()
				.prepareStatement("INSERT INTO Issue (id, reason, workItemId) VALUES (?,?,?)")) {

			statement.setLong(1, issueId);
			statement.setString(2, comment);
			statement.setLong(3, workItemId);

			statement.executeUpdate();
			statement.close();

		} catch (SQLException e) {

			e.printStackTrace();
		}
		;
		return new Issue(issueId, comment, workItemId);

	}

	@Override
	public void updateWorkIssue(long issueId, long itemId) {
		try (PreparedStatement statement = connection.getConnection()
				.prepareStatement("UPDATE Issue SET workItemId = ? WHERE id = ?")) {
			statement.setLong(1, itemId);
			statement.setLong(2, issueId);
			statement.executeUpdate();

		}

		catch (SQLException e) {

			e.printStackTrace();
		}
	}

	@Override
	public Set<String> getWorkItemsByIssue() throws RepositoryException {
		try (PreparedStatement statement = connection.getConnection()
				.prepareStatement("SELECT description FROM WorkItem JOIN Issue ON WorkItem.id = Issue.workItemId")) {

			ResultSet result = statement.executeQuery();

			while (result.next()) {
				String itemCase = result.getString("description");
				getIssue.add(itemCase);

			}
			return getIssue;
		} catch (SQLException e) {
			throw new RepositoryException(e);
		}

	}

	@Override
	public String getWorkItemStatus(long itemId) {
		String status = null;
		try (PreparedStatement statement = connection.getConnection()
				.prepareStatement("SELECT state FROM WorkItem WHERE id = ?")) {

			statement.setLong(1, itemId);

			ResultSet result = statement.executeQuery();

			while (result.next()) {
				status = extractUserStatusFromWorkItem(result);

			}

			return status;

		} catch (SQLException e) {

			throw new RuntimeException(e);
		}
	}

	@Override
	public long getItemIdFromIssue(long issueId) {
		long getItemId = 0L;
		try (PreparedStatement statement = connection.getConnection()
				.prepareStatement("SELECT workItemId FROM Issue WHERE id = ?")) {

			statement.setLong(1, issueId);

			ResultSet result = statement.executeQuery();

			while (result.next()) {
				getItemId = extractItemId(result);

			}

			return getItemId;

		} catch (SQLException e) {

			throw new RuntimeException(e);
		}
	}

	private long extractItemId(ResultSet result) throws SQLException {
		long itemId = result.getLong(1);
		return itemId;

	}

	private String extractUserStatusFromWorkItem(ResultSet result) throws SQLException {
		String itemStatus = result.getString("state");
		return itemStatus;

	}

}
