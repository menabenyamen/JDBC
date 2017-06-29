package se.mebe.jdbcuser.repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import se.mebe.jdbcuser.connection.MyConnection;
import se.mebe.jdbcuser.exception.ServiceException;
import se.mebe.jdbcuser.model.User;

public final class SQLUserRepository implements UserRepository {

	private final MyConnection connection;
	private final Set<User> getUser;

	public SQLUserRepository() throws SQLException, ServiceException {
		this.getUser = new HashSet<>();
		this.connection = new MyConnection();
	}

	@Override
	public User addUser(User user) throws ServiceException {

		try (PreparedStatement statement = connection.getConnection()
				.prepareStatement("INSERT INTO User (id, userName ,firstName, "
						+ "lastName, teamId, userStatus) VALUES (NULL,?,?,?,NULL,?)")) {

			statement.setString(1, user.getUserName());
			statement.setString(2, user.getFirstName());
			statement.setString(3, user.getLastName());
			statement.setString(4, user.getState());
			statement.executeUpdate();
			statement.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return user;
	}

	@Override
	public void updateAsignTeamToUser(long teamId, long userId) {
		try (PreparedStatement statement = connection.getConnection()
				.prepareStatement("UPDATE User SET teamId = ? WHERE id = ?")) {

			statement.setLong(1, teamId);
			statement.setLong(2, userId);

			statement.executeUpdate();
			statement.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void disable(long userId) {
		try (PreparedStatement statement = connection.getConnection()
				.prepareStatement("UPDATE User u SET u.`userStatus` = 'Inactive' WHERE u.`id` = ?")) {

			statement.setLong(1, userId);
			statement.executeUpdate();
			statement.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	@Override
	public Set<User> getUserById(long userId) throws ServiceException {

		try (PreparedStatement statement = connection.getConnection()
				.prepareStatement("SELECT * FROM User WHERE id = ?")) {

			statement.setLong(1, userId);

			ResultSet result = statement.executeQuery();

			if (result.next()) {
				getUser.add(extractUser(result)); 
			}

			return getUser;

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

	}

	@Override
	public Set<User> getAllUsersForOneTeam(long teamId) throws ServiceException {
		try (PreparedStatement statement = connection.getConnection()
				.prepareStatement("SELECT * FROM User WHERE teamId = ?")) {
			statement.setLong(1, teamId);

			ResultSet result = statement.executeQuery();

			while (result.next()) {

				getUser.add(extractUser(result));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return getUser;
	}

	public Set<User> getUserByUserName(String userName) throws ServiceException {

		try (PreparedStatement statement = connection.getConnection()
				.prepareStatement("SELECT * FROM User WHERE userName = ?")) {

			statement.setString(1, userName);

			ResultSet result = statement.executeQuery();

			if (result.next()) {

				getUser.add(extractUser(result));
			}
			return getUser;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

	}

	private User extractUser(ResultSet result) throws SQLException {

		Long id = result.getLong(1);
		String firstName = result.getString(2);
		String lastName = result.getString(3);
		String userName = result.getString(4);
		Long teamId = result.getLong(5);
		String userStates = result.getString(6);

		return new User(id, firstName, lastName, userName, teamId, userStates);

	}

}
