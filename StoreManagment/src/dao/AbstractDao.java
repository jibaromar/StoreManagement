package dao;

import java.sql.Connection;

public abstract class AbstractDao {
	protected Connection connection = DBConnection.getConnection();
}
