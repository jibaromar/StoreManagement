package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.Client;

public class ClientDaoImpl extends AbstractDao implements IClientDao {

	@Override
	public Client add(Client obj) {
		String query = "INSERT INTO Clients (nom, prenom, telephone, email, adresse)"
		        + " VALUES (?, ?, ?, ?, ?)";
		PreparedStatement pst;
		try {
			pst = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			pst.setString(1, obj.getLastName().trim().replaceAll("\\s+", " "));
			pst.setString(2, obj.getFirstName().trim().replaceAll("\\s+", " "));
			pst.setString(3, obj.getPhone().trim().replaceAll("\\s+", " "));
			pst.setString(4, obj.getEmail().trim().replaceAll("\\s+", " "));
			pst.setString(5, obj.getAddress().trim().replaceAll("\\s+", " "));
			
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
	
	public boolean edit(Client obj) {
		String query = "UPDATE Clients"
				+ " SET nom=?, prenom=?, telephone=?, email=?, adresse=?"
		        + " WHERE id=?";
		PreparedStatement pst;
		try {
			pst = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			pst.setString(1, obj.getLastName().trim().replaceAll("\\s+", " "));
			pst.setString(2, obj.getFirstName().trim().replaceAll("\\s+", " "));
			pst.setString(3, obj.getPhone().trim().replaceAll("\\s+", " "));
			pst.setString(4, obj.getEmail().trim().replaceAll("\\s+", " "));
			pst.setString(5, obj.getAddress().trim().replaceAll("\\s+", " "));
			pst.setLong(6,  obj.getId());
			
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
		String query = "DELETE FROM Clients WHERE id = ?";
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
	public Client getOne(long id) {
		Client client = null;
		String sql = "SELECT * FROM Clients WHERE id=?";
		PreparedStatement pst;
		ResultSet rs;
		try {
			pst = connection.prepareStatement(sql);
			pst.setLong(1, id);
			rs = pst.executeQuery();
			if (rs.next()) {
				client = new Client(rs.getLong("id"), rs.getString("nom"), rs.getString("prenom"), rs.getString("telephone"), rs.getString("email"), rs.getString("adresse"));				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return client;
	}

	@Override
	public List<Client> getAll() {
		List <Client> clients = new ArrayList <Client> ();
		String sql = "SELECT * FROM Clients";
		PreparedStatement pst;
		ResultSet rs;
		try {
			pst = connection.prepareStatement(sql);
			rs = pst.executeQuery();
			while (rs.next()) {
				clients.add(new Client(rs.getLong("id"), rs.getString("nom"), rs.getString("prenom"), rs.getString("telephone"), rs.getString("email"), rs.getString("adresse")));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return clients;
	}

	@Override
	public List<Client> getAll(String name) {
		List <Client> clients = new ArrayList <Client> ();
		String sql = "SELECT * FROM Clients WHERE label like ? or prenom like ?";
		PreparedStatement pst;
		ResultSet rs;
		try {
			pst = connection.prepareStatement(sql);
			pst.setString(1, "%" + name.trim().replaceAll("\\s+", " ") + "%");
			pst.setString(2, "%" + name.trim().replaceAll("\\s+", " ") + "%");
			rs = pst.executeQuery();
			while (rs.next()) {
				clients.add(new Client(rs.getLong("id"), rs.getString("nom"), rs.getString("prenom"), rs.getString("telephone"), rs.getString("email"), rs.getString("adresse")));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return clients;
	}
}
