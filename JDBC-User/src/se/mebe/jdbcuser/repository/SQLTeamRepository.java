package se.mebe.jdbcuser.repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import se.mebe.jdbcuser.connection.MyConnection;
import se.mebe.jdbcuser.exception.RepositoryException;
import se.mebe.jdbcuser.model.Team;

public final class SQLTeamRepository implements TeamRepository {

	private final MyConnection connection;

	public SQLTeamRepository() throws SQLException {
		// alla arrayer måste finnas inuti metoderna så att vi inte måste tömma
		// de.
		this.connection = new MyConnection();
	}

	@Override
	public boolean addTeam(Long teamId, String teamName, Long numberOfMembers, String state) {

		try (PreparedStatement statement = connection.getConnection()
				.prepareStatement("INSERT INTO Team (id, teamName, numberOfUsers, teamStatus) VALUES (?,?,?,?)")) {

			statement.setLong(1, teamId);
			statement.setString(2, teamName);
			statement.setLong(3, numberOfMembers);
			statement.setString(4, state);
			statement.executeUpdate();
			statement.close();

		} catch (SQLException e) {

			e.printStackTrace();
		}
		return true;
	}

	@Override
	public boolean updateTeam(String teamName, Long teamId) {
		try (PreparedStatement statement = connection.getConnection()
				.prepareStatement("UPDATE Team SET teamName = ? WHERE id = ?")) {

			statement.setString(1, teamName);
			statement.setLong(2, teamId);
			statement.executeUpdate();
			statement.close();

		} catch (SQLException e) {

			e.printStackTrace();
		}
		return true;
	}

	@Override
	public List<Team> getAllTeams() throws RepositoryException {
		List<Team> getTeam = new ArrayList<>();
		try (PreparedStatement statement = connection.getConnection().prepareStatement("SELECT * FROM Team")) {

			ResultSet result = statement.executeQuery();

			while (result.next()) {

				getTeam.add(extractTeam(result));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return getTeam;

	}

	@Override
	public void disableATeam(long teamId) {
		try (PreparedStatement statement = connection.getConnection()
				.prepareStatement("UPDATE Team SET teamStatus = 'Inactive' WHERE id = ?")) {

			statement.setLong(1, teamId);
			statement.executeUpdate();
			statement.close();

		} catch (SQLException e) {

			e.printStackTrace();
		}

	}

	@Override
	public boolean addUserToTeam(Long userId, Long teamId) {
		try (PreparedStatement statement = connection.getConnection()
				.prepareStatement("UPDATE User SET teamId = ? WHERE id=?")) {

			statement.setLong(1, teamId);
			statement.setLong(2, userId);
			statement.executeUpdate();
			statement.close();
			updateNumberOfMembers();

		} catch (SQLException e) {

			e.printStackTrace();
		}
		return true;
	}

	@Override
	public Map<Long, String> getAllStatusForTeam() {
		try (PreparedStatement statement = connection.getConnection()
				.prepareStatement("SELECT id, teamStatus FROM TEAM")) {

			ResultSet result = statement.executeQuery();
			Map<Long, String> getActiveTeam = new LinkedHashMap<>();

			while (result.next()) {

				getActiveTeam.put(result.getLong(1), result.getString(2));
			}
			return getActiveTeam;

		} catch (SQLException e) {
			e.printStackTrace();

		}
		return null;

	}

	@Override
	public List<Long> getAllTeamId() {
		try (PreparedStatement statement = connection.getConnection().prepareStatement("SELECT id FROM Team")) {

			ResultSet result = statement.executeQuery();
			List<Long> getTeamId = new ArrayList<>();

			while (result.next()) {

				getTeamId.add(result.getLong(1));
			}
			return getTeamId;

		} catch (SQLException e) {
			e.printStackTrace();

		}
		return null;

	}

	@Override
	public Map<Long, String> getAllTeamName() {
		try (PreparedStatement statement = connection.getConnection()
				.prepareStatement("SELECT id ,`teamName` FROM team")) {

			ResultSet result = statement.executeQuery();
			Map<Long, String> getTeamName = new LinkedHashMap<>();

			while (result.next()) {

				getTeamName.put(result.getLong(1), result.getString(2));

			}
			return getTeamName;

		} catch (SQLException e) {
			e.printStackTrace();

		}
		return null;

	}

	@Override
	public List<Long> getAllUserIdFromUser() {
		try (PreparedStatement statement = connection.getConnection().prepareStatement("SELECT id FROM User")) {

			ResultSet result = statement.executeQuery();
			List<Long> getUserId = new ArrayList<>();

			while (result.next()) {

				getUserId.add(result.getLong(1));

			}
			return getUserId;

		} catch (SQLException e) {
			e.printStackTrace();

		}
		return null;

	}

	@Override
	public Map<Long, Long> getAllUserIdAndTeamIdFromUser() {
		try (PreparedStatement statement = connection.getConnection().prepareStatement("SELECT id, teamId FROM User")) {

			ResultSet result = statement.executeQuery();
			Map<Long, Long> getIdFromTeam = new LinkedHashMap<>();

			while (result.next()) {

				getIdFromTeam.put(result.getLong(1), result.getLong(2));
			}
			return getIdFromTeam;

		} catch (SQLException e) {
			e.printStackTrace();

		}
		return null;

	}

	@Override
	public Map<Long, Long> getNumberOfTeamMembers() {

		updateNumberOfMembers();
		try (PreparedStatement statement = connection.getConnection()
				.prepareStatement("SELECT id, `numberOfUsers` FROM Team;")) {

			ResultSet result = statement.executeQuery();
			Map<Long, Long> getNumbersOfTeamMembers = new LinkedHashMap<>();

			while (result.next()) {
				getNumbersOfTeamMembers.put(result.getLong(1), result.getLong(2));

			}
			return getNumbersOfTeamMembers;

		} catch (SQLException e) {
			e.printStackTrace();

		}
		return null;

	}

	private Team extractTeam(ResultSet result) throws SQLException {

		long id = result.getLong(1);
		String teamName = result.getString(2);
		long numberOfMembers = result.getLong(3);
		String state = result.getString(4);

		return new Team(id, teamName, numberOfMembers, state);

	}

	private void updateNumberOfMembers() {
		try (Statement stmt = connection.getConnection().createStatement()) {
			String count = " UPDATE `Team`  SET numberOfUsers = (SELECT COUNT(id) FROM `User`WHERE User.teamId = Team.id)";

			stmt.executeUpdate(count);

		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}

}
