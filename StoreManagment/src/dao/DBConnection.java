package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
	String db = "store";
	String user = "admin";
	String pwd = "admin";
	String url = "jdbc:mysql://localhost:3306/" + db;
	private static Connection connection = null;
	
	private DBConnection() {
		try {
			connection = DriverManager.getConnection(url, user, pwd);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public static Connection getConnection() {
		if (connection == null) {
			new DBConnection();
		}
		return connection;
	}
}
