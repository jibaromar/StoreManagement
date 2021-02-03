package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.Categorie;

public class CategorieDaoImpl extends AbstractDao implements ICategorieDao {

	@Override
	public Categorie add(Categorie obj) {
		String query = " INSERT INTO Categories (label, description)"
		        + " VALUES (?, ?)";
		PreparedStatement pst;
		try {
			pst = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			pst.setString(1, obj.getLabel().trim().replaceAll("\\s+", " "));
			pst.setString(2, obj.getDescription().trim().replaceAll("\\s+", " "));
			
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

	@Override
	public boolean delete(long id) {
		String query = "DELETE FROM Categories WHERE id = ?";
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
	public Categorie getOne(long id) {
		Categorie categorie = null;
		String sql = "SELECT * FROM Categories WHERE id=?";
		PreparedStatement pst;
		ResultSet rs;
		try {
			pst = connection.prepareStatement(sql);
			pst.setLong(1, id);
			rs = pst.executeQuery();
			rs.next();
			categorie = new Categorie(rs.getLong("id"), rs.getString("label"), rs.getString("description"));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return categorie;
	}

	@Override
	public List<Categorie> getAll() {
		List <Categorie> categories = new ArrayList <Categorie> ();
		String sql = "SELECT * FROM Categories";
		PreparedStatement pst;
		ResultSet rs;
		try {
			pst = connection.prepareStatement(sql);
			rs = pst.executeQuery();
			while (rs.next()) {
				categories.add(new Categorie(rs.getLong("id"), rs.getString("label"), rs.getString("description")));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return categories;
	}

	@Override
	public List<Categorie> getAll(String keyword) {
		List <Categorie> categories = new ArrayList <Categorie> ();
		String sql = "SELECT * FROM Categories WHERE label like ?";
		PreparedStatement pst;
		ResultSet rs;
		try {
			pst = connection.prepareStatement(sql);
			pst.setString(1, "%" + keyword.trim().replaceAll("\\s+", " ") + "%");
			rs = pst.executeQuery();
			while (rs.next()) {
				categories.add(new Categorie(rs.getLong("id"), rs.getString("label"), rs.getString("description")));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return categories;
	}
}
