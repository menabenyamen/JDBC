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
import se.mebe.jdbcuser.model.WorkItem;

public final class SQLPagingWorkItems implements PagingRepository<WorkItem> {

	private final MyConnection connection;
	private final List<WorkItem> workItemList;
	private final List<String> readWorkItemList;

	public SQLPagingWorkItems() throws SQLException {
		this.connection = new MyConnection();
		this.workItemList = new ArrayList<>();
		this.readWorkItemList = new ArrayList<>();
	}

	@Override
	public List<WorkItem> getAll(int pageSize, long beginIndex) throws SQLException {

		try (PreparedStatement statement = connection.getConnection(ResultSet.TYPE_SCROLL_SENSITIVE)
				.prepareStatement("SELECT * FROM WorkItem ORDER BY id LIMIT ? OFFSET ?")) {

			statement.setInt(1, pageSize);
			statement.setLong(2, beginIndex);

			ResultSet result = statement.executeQuery();
			while (result.next()) {
				workItemList.add(extractWorkItem(result));

			}

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return workItemList;

	}

	@Override
	public void saveToFile() {

		try (BufferedWriter bw = new BufferedWriter(
				new FileWriter("/Users/menabenyamen/Documents/workspace/JDBC-User/SaveWorkItemList.txt", true))) {

			bw.write(workItemList.toString());

		} catch (IOException e) {

			e.printStackTrace();

		}
		//
		// FileOutputStream fileOutPutStream = null;
		// ObjectOutputStream userOutPutStream = null;
		// try {
		// File file = new
		// File("/Users/menabenyamen/Documents/workspace/JDBC-User/SaveWorkItemList.txt");
		// if (!file.exists()) {
		// file.createNewFile();
		// }
		//
		// fileOutPutStream = new FileOutputStream(file, true);
		// userOutPutStream = new ObjectOutputStream(fileOutPutStream);
		// userOutPutStream.writeObject(workItemList);
		//
		// } catch (Exception ex) {
		//
		// ex.printStackTrace();
		//
		// } finally {
		//
		// if (fileOutPutStream != null) {
		// try {
		// fileOutPutStream.close();
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		// }
		//
		// if (userOutPutStream != null) {
		// try {
		// userOutPutStream.close();
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		// }
		//
		// }
	}

	@Override
	public List<String> readFromFile() throws IOException {
		String readWorkItem;

		BufferedReader br = new BufferedReader(new FileReader("SaveUserList.txt"));
		try {
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();

			while (line != null) {
				sb.append(line);
				sb.append(System.lineSeparator());
				line = br.readLine();
			}
			readWorkItem = sb.toString();
		} finally {
			br.close();
		}
		readWorkItemList.add(readWorkItem);
		return readWorkItemList;
	}

	private WorkItem extractWorkItem(ResultSet result) throws SQLException {

		long id = result.getLong(1);
		String title = result.getString(2);
		String description = result.getString(3);
		String state = result.getString(4);
		long teamId = result.getLong(5);

		return new WorkItem(id, title, description, state, teamId);

	}

}
