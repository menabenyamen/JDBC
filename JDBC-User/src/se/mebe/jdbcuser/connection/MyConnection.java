package se.mebe.jdbcuser.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class MyConnection {

	private final Connection connection;
	private static final String CONNECTION_URL = "jdbc:mysql://localhost/CMDB?useSSL=false";
	private static final String USERNAME = "root";
	private static final String PASSWORD = "benyamen";

	public MyConnection() throws SQLException {
		this.connection = DriverManager.getConnection(CONNECTION_URL, USERNAME, PASSWORD);
	}

	public String getConnectionUrl() {
		return CONNECTION_URL;
	}

	public String getUsername() {
		return USERNAME;
	}

	public String getPassword() {
		return PASSWORD;
	}

	public Connection getConnection() {
		return connection;
	}

	public Connection getConnection(int typeScrollSensitive) {

		return connection;
	}

}
