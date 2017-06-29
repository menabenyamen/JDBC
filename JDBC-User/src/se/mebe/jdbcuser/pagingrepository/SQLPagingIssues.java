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
import se.mebe.jdbcuser.model.Issue;

public final class SQLPagingIssues implements PagingRepository<Issue> {

	private final MyConnection connection;
	private final List<Issue> issueList;
	private final List<String> readIssueList;

	public SQLPagingIssues() throws SQLException {
		this.connection = new MyConnection();
		this.issueList = new ArrayList<>();
		this.readIssueList = new ArrayList<>();
	}

	@Override
	public List<Issue> getAll(int pageSize, long begineIndex) throws SQLException {

		try (PreparedStatement statement = connection.getConnection(ResultSet.TYPE_SCROLL_SENSITIVE)
				.prepareStatement("SELECT * FROM Issue ORDER BY id LIMIT ? OFFSET ?")) {

			statement.setInt(1, pageSize);
			statement.setLong(2, begineIndex);

			ResultSet result = statement.executeQuery();
			while (result.next()) {
				issueList.add(extractIssue(result));

			}

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		return issueList;

	}

	@Override
	public void saveToFile() {

		try (BufferedWriter bw = new BufferedWriter(
				new FileWriter("/Users/menabenyamen/Documents/workspace/JDBC-User/SaveIssueList.txt", true))) {

			bw.write(issueList.toString());

		} catch (IOException e) {

			e.printStackTrace();

		}

	}

	@Override
	public List<String> readFromFile() throws IOException {
		String readIssue;

		BufferedReader br = new BufferedReader(new FileReader("SaveUserList.txt"));
		try {
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();

			while (line != null) {
				sb.append(line);
				sb.append(System.lineSeparator());
				line = br.readLine();
			}
			readIssue = sb.toString();
		} finally {
			br.close();
		}
		readIssueList.add(readIssue);
		return readIssueList;
	}

	private Issue extractIssue(ResultSet result) throws SQLException {

		long id = result.getLong(1);
		String issueReason = result.getString(2);
		Long itemId = result.getLong(3);

		return new Issue(id, issueReason, itemId);

	}

}
