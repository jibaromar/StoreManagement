package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.User;

public class UserDaoImpl extends AbstractDao implements IUserDao {

	@Override
	public User add(User obj) {
		String query = "INSERT INTO Users (name, password)"
		        + " VALUES (?, ?)";
		PreparedStatement pst;
		try {
			pst = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			pst.setString(1, obj.getName().trim().replaceAll("\\s+", ""));
			pst.setString(2, obj.getPassword());
			
			pst.executeUpdate();
			ResultSet rs = pst.getGeneratedKeys();
            if(rs.next())
            {
            	obj.setId(rs.getLong(1));
                return obj;
            }
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public boolean edit(User obj) {
		String query = "UPDATE Users"
				+ " SET name=?, password=?"
		        + " WHERE id=?";
		PreparedStatement pst;
		try {
			pst = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			pst.setString(1, obj.getName().trim().replaceAll("\\s+", ""));
			pst.setString(2, obj.getPassword());
			pst.setLong(3,  obj.getId());
			
			if (pst.executeUpdate() > 0) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean delete(long id) {
		String query = "DELETE FROM Users WHERE id = ?";
		PreparedStatement pst;
		try {
			pst = connection.prepareStatement(query);
			pst.setLong(1, id);
			
			if (pst.executeUpdate() > 0) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public User getOne(long id) {
		User user = null;
		String sql = "SELECT * FROM Users WHERE id=?";
		PreparedStatement pst;
		ResultSet rs;
		try {
			pst = connection.prepareStatement(sql);
			pst.setLong(1, id);
			rs = pst.executeQuery();
			if (rs.next()) {
				user = new User(rs.getLong("id"), rs.getString("name"), rs.getString("password"));				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return user;
	}

	@Override
	public List<User> getAll() {
		List <User> users = new ArrayList <User> ();
		String sql = "SELECT * FROM Users";
		PreparedStatement pst;
		ResultSet rs;
		try {
			pst = connection.prepareStatement(sql);
			rs = pst.executeQuery();
			while (rs.next()) {
				users.add(new User(rs.getLong("id"), rs.getString("name"), rs.getString("password")));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return users;
	}

	@Override
	public boolean isValidUser(User usr) {
		String sql = "SELECT * FROM Users WHERE name = ? AND password = ?";
		PreparedStatement pst;
		ResultSet rs;
		try {
			pst = connection.prepareStatement(sql);
			pst.setString(1, usr.getName());
			pst.setString(2, usr.getPassword());
			rs = pst.executeQuery();
			if (rs.next()) {
				return true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
}
