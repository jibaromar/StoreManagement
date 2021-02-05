package dao;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.BL;

public class BLDaoImpl extends AbstractDao implements IBLDao {

	@Override
	public BL add(BL obj) {
		String query = "INSERT INTO BL (date, clientId)"
		        + " VALUES (?, ?)";
		PreparedStatement pst;
		try {
			pst = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			pst.setDate(1, Date.valueOf(obj.getDate()));
			pst.setLong(2, obj.getClientId());
			
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
	
	public boolean edit(BL obj) {
		String query = "UPDATE BL"
				+ " SET date=?, clientId=?"
		        + " WHERE id=?";
		PreparedStatement pst;
		try {
			pst = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			pst.setDate(1, Date.valueOf(obj.getDate()));
			pst.setLong(2, obj.getClientId());
			pst.setLong(3, obj.getId());
			
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
		String query = "DELETE FROM BL WHERE id = ?";
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
	public BL getOne(long id) {
		BL bl = null;
		String sql = "SELECT * FROM BL WHERE id=?";
		PreparedStatement pst;
		ResultSet rs;
		try {
			pst = connection.prepareStatement(sql);
			pst.setLong(1, id);
			rs = pst.executeQuery();
			if (rs.next()) {
				bl = new BL(rs.getLong("id"), rs.getDate("date").toLocalDate(), rs.getLong("clientId"));				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bl;
	}

	@Override
	public List<BL> getAll() {
		List <BL> bls = new ArrayList <BL> ();
		String sql = "SELECT * FROM BL";
		PreparedStatement pst;
		ResultSet rs;
		try {
			pst = connection.prepareStatement(sql);
			rs = pst.executeQuery();
			while (rs.next()) {
				bls.add(new BL(rs.getLong("id"), rs.getDate("date").toLocalDate(), rs.getLong("clientId")));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bls;
	}

	@Override
	public List<BL> getAll(String clientName) {
		List <BL> bls = new ArrayList <BL> ();
		String sql = "SELECT BL.id, BL.date, BL.clientId, Clients.nom, Clients.prenom, Clients.telephone, Clients.email, Clients.adresse\n"
				+ "FROM BL, Clients\n"
				+ "WHERE BL.clientId = Clients.id AND (Clients.nom like ? OR Clients.prenom like ?);";
		PreparedStatement pst;
		ResultSet rs;
		try {
			pst = connection.prepareStatement(sql);
			pst.setString(1, "%" + clientName.trim().replaceAll("\\s+", " ") + "%");
			pst.setString(2, "%" + clientName.trim().replaceAll("\\s+", " ") + "%");
			rs = pst.executeQuery();
			while (rs.next()) {
				bls.add(new BL(rs.getLong("id"), rs.getDate("date").toLocalDate(), rs.getLong("clientId")));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bls;
	}
}
