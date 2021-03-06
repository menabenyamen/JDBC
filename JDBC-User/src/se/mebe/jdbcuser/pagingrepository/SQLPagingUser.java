package se.mebe.jdbcuser.pagingrepository;

import java.io.BufferedReader;
import java.io.BufferedWriter;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import se.mebe.jdbcuser.connection.MyConnection;
import se.mebe.jdbcuser.model.User;

public final class SQLPagingUser implements PagingRepository<User> {

	private final MyConnection connection;
	private final List<User> userList;
	private final List<String> readUserList;

	public SQLPagingUser() throws SQLException {
		this.connection = new MyConnection();
		this.userList = new ArrayList<>();
		this.readUserList = new ArrayList<>();
	}

	@Override
	public List<User> getAll(int pageSize, long beginIndex) throws SQLException {

		try (PreparedStatement statement = connection.getConnection(ResultSet.TYPE_SCROLL_SENSITIVE)
				.prepareStatement("SELECT * FROM User ORDER BY id LIMIT ? OFFSET ?")) {

			statement.setInt(1, pageSize);
			statement.setLong(2, beginIndex);

			ResultSet result = statement.executeQuery();
			while (result.next()) {
				userList.add(extractUser(result));

			}

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		return userList;

	}

	@Override
	public void saveToFile() {
		try (BufferedWriter bw = new BufferedWriter(
				new FileWriter("/Users/menabenyamen/Documents/workspace/JDBC-User/SaveUserList.txt", true))) {

			bw.write(userList.toString() + "\n");

		} catch (IOException e) {

			e.printStackTrace();

		}
	}

	@Override
	public List<String> readFromFile() throws IOException {
		String readUser;

		BufferedReader br = new BufferedReader(new FileReader("SaveUserList.txt"));
		try {
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();

			while (line != null) {
				sb.append(line);
				sb.append(System.lineSeparator());
				line = br.readLine();
			}
			readUser = sb.toString();
		} finally {
			br.close();
		}

		readUserList.add(readUser);
		return readUserList;

	}

	private User extractUser(ResultSet result) throws SQLException {

		long id = result.getLong(1);
		String userName = result.getString(2);
		String firstName = result.getString(3);
		String lastName = result.getString(4);
		long teamId = result.getLong(5);
		String userStatus = result.getString(6);

		return new User(id, userName, firstName, lastName, teamId, userStatus);

	}

}
