package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.Banque;

public class BanqueDaoImpl extends AbstractDao implements IBanqueDao {
	@Override
	public boolean delete(long id) {
		String query = "DELETE FROM Banques WHERE id = ?";
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
	public Banque getOne(long id) {
		Banque banque = null;
		String sql = "SELECT * FROM Banques WHERE id=?";
		PreparedStatement pst;
		ResultSet rs;
		try {
			pst = connection.prepareStatement(sql);
			pst.setLong(1, id);
			rs = pst.executeQuery();
			if (rs.next()) {
				banque = new Banque(rs.getLong("id"), rs.getString("nom"), rs.getString("abreviation"));				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return banque;
	}

	@Override
	public List<Banque> getAll() {
		List <Banque> banques = new ArrayList <Banque> ();
		String sql = "SELECT * FROM Banques";
		PreparedStatement pst;
		ResultSet rs;
		try {
			pst = connection.prepareStatement(sql);
			rs = pst.executeQuery();
			while (rs.next()) {
				banques.add(new Banque(rs.getLong("id"), rs.getString("nom"), rs.getString("abreviation")));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return banques;
	}

	@Override
	public Banque add(Banque obj) {
		String query = "INSERT INTO Banques (nom, abreviation)"
		        + " VALUES (?, ?)";
		PreparedStatement pst;
		try {
			pst = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			pst.setString(1, obj.getNom().trim().replaceAll("\\s+", " "));
			pst.setString(2, obj.getAbreviation().trim().replaceAll("\\s+", " "));
			
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
	public boolean edit(Banque obj) {
		// TODO Auto-generated method stub
		return false;
	}
}
