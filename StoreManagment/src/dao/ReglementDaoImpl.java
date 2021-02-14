package dao;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.BL;
import model.Banque;
import model.Client;
import model.Reglement;

public class ReglementDaoImpl extends AbstractDao implements IReglementDao {

	@Override
	public Reglement add(Reglement obj) {
		String query = "INSERT INTO Reglements (date, montant, type, numero_cheque, date_echeance, banqueId, nom, BLId)"
		        + " VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
		PreparedStatement pst;
		try {
			pst = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			
			pst.setDate(1, Date.valueOf(obj.getDate()));
			pst.setDouble(2, obj.getMontant());
			pst.setString(3, obj.getType());
			
			if (obj.getType().equals("CHEQUE")) {
				pst.setLong(4, obj.getNumero_cheque());
				pst.setDate(5, Date.valueOf(obj.getDate_echeance()));
				pst.setLong(6, obj.getBanque().getId());			
				pst.setString(7, obj.getNom().trim().replaceAll("\\s+", " "));
			} else {
				pst.setString(4, null);
				pst.setDate(5, null);
				pst.setString(6, null);			
				pst.setString(7, null);
			}
			pst.setLong(8, obj.getBL().getId());

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
	public boolean edit(Reglement obj) {
		String query = "UPDATE Reglements"
				+ " SET date=?, montant=?, type=?, numero_cheque=?, date_echeance=?, banqueId=?, nom=?, BLId=?"
				+ " WHERE id=?";
		PreparedStatement pst;
		try {
			pst = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			
			pst.setDate(1, Date.valueOf(obj.getDate()));
			pst.setDouble(2, obj.getMontant());
			pst.setString(3, obj.getType());
			
			if (obj.getType().equals("CHEQUE")) {
				pst.setLong(4, obj.getNumero_cheque());
				pst.setDate(5, Date.valueOf(obj.getDate_echeance()));
				pst.setLong(6, obj.getBanque().getId());			
				pst.setString(7, obj.getNom().trim().replaceAll("\\s+", " "));
			} else {
				pst.setString(4, null);
				pst.setDate(5, null);
				pst.setString(6, null);			
				pst.setString(7, null);
			}
			pst.setLong(8, obj.getBL().getId());
			
			pst.setLong(9,  obj.getId());
			
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
		String query = "DELETE FROM Reglements WHERE id = ?";
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
	public Reglement getOne(long id) {
		Reglement reglement = null;
		String sql = "SELECT Reglements.id, Reglements.date, Reglements.montant, Reglements.type, Reglements.numero_cheque, Reglements.date_echeance, Reglements.nom,\n"
				+ "Banques.*,\n"
				+ "BL.*,\n"
				+ "Clients.*\n"
				+ "FROM (Reglements INNER JOIN (BL INNER JOIN Clients ON BL.clientId = Clients.id) ON Reglements.BLId = BL.id) LEFT OUTER JOIN Banques\n"
				+ "ON Reglements.banqueId = Banques.id\n"
				+ "WHERE Reglements.BLId = 30";
		PreparedStatement pst;
		ResultSet rs;
		try {
			pst = connection.prepareStatement(sql);
			pst.setLong(1, id);
			rs = pst.executeQuery();
			if (rs.next()) {
				reglement = new Reglement(rs.getLong("Reglements.id"),
						rs.getDate("Reglements.date").toLocalDate(),
						rs.getDouble("Reglements.montant"),
						new BL(rs.getLong("BL.id"),
								rs.getDate("BL.date").toLocalDate(),
								new Client(rs.getLong("Clients.id"),
										rs.getString("Clients.nom"),
										rs.getString("Clients.prenom"),
										rs.getString("Clients.telephone"),
										rs.getString("Clients.email"),
										rs.getString("Clients.adresse"))));
				if (rs.getString("Reglements.type").equals("CHEQUE")) {
					reglement.setType(rs.getString("Reglements.type"));
					reglement.setNumero_cheque(rs.getLong("Reglements.numero_cheque"));
					reglement.setDate_echeance(rs.getDate("Reglements.date_echeance").toLocalDate());
					reglement.setBanque(new Banque(rs.getLong("Banques.id"), rs.getString("Banques.nom"), rs.getString("Banques.abreviation")));
					reglement.setNom(rs.getString("Reglements.nom"));					
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return reglement;
	}

	@Override
	public List<Reglement> getAll() {
		List <Reglement> reglements = new ArrayList <Reglement> ();
		String sql = "SELECT Reglements.id, Reglements.date, Reglements.montant, Reglements.type, Reglements.numero_cheque, Reglements.date_echeance, Reglements.nom,\n"
				+ "Banques.*,\n"
				+ "BL.*,\n"
				+ "Clients.*\n"
				+ "FROM (Reglements INNER JOIN (BL INNER JOIN Clients ON BL.clientId = Clients.id) ON Reglements.BLId = BL.id) LEFT OUTER JOIN Banques\n"
				+ "ON Reglements.banqueId = Banques.id";
		PreparedStatement pst;
		ResultSet rs;
		try {
			pst = connection.prepareStatement(sql);
			rs = pst.executeQuery();
			while (rs.next()) {
				Reglement reglement = new Reglement(rs.getLong("Reglements.id"),
						rs.getDate("Reglements.date").toLocalDate(),
						rs.getDouble("Reglements.montant"),
						new BL(rs.getLong("BL.id"),
								rs.getDate("BL.date").toLocalDate(),
								new Client(rs.getLong("Clients.id"),
										rs.getString("Clients.nom"),
										rs.getString("Clients.prenom"),
										rs.getString("Clients.telephone"),
										rs.getString("Clients.email"),
										rs.getString("Clients.adresse"))));
				if (rs.getString("Reglements.type").equals("CHEQUE")) {
					reglement.setType(rs.getString("Reglements.type"));
					reglement.setNumero_cheque(rs.getLong("Reglements.numero_cheque"));
					reglement.setDate_echeance(rs.getDate("Reglements.date_echeance").toLocalDate());
					reglement.setBanque(new Banque(rs.getLong("Banques.id"), rs.getString("Banques.nom"), rs.getString("Banques.abreviation")));
					reglement.setNom(rs.getString("Reglements.nom"));					
				}
				reglements.add(reglement);				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return reglements;
	}

	@Override
	public List<Reglement> getAll(long id) {
		List <Reglement> reglements = new ArrayList <Reglement> ();
		String sql = "SELECT Reglements.id, Reglements.date, Reglements.montant, Reglements.type, Reglements.numero_cheque, Reglements.date_echeance, Reglements.nom,\n"
				+ "Banques.*,\n"
				+ "BL.*,\n"
				+ "Clients.*\n"
				+ "FROM (Reglements INNER JOIN (BL INNER JOIN Clients ON BL.clientId = Clients.id) ON Reglements.BLId = BL.id) LEFT OUTER JOIN Banques\n"
				+ "ON Reglements.banqueId = Banques.id\n"
				+ "WHERE Reglements.BLId = ?";
		PreparedStatement pst;
		ResultSet rs;
		try {
			pst = connection.prepareStatement(sql);
			pst.setLong(1, id);
			rs = pst.executeQuery();
			while (rs.next()) {
				Reglement reglement = new Reglement(rs.getLong("Reglements.id"),
						rs.getDate("Reglements.date").toLocalDate(),
						rs.getDouble("Reglements.montant"),
						new BL(rs.getLong("BL.id"),
								rs.getDate("BL.date").toLocalDate(),
								new Client(rs.getLong("Clients.id"),
										rs.getString("Clients.nom"),
										rs.getString("Clients.prenom"),
										rs.getString("Clients.telephone"),
										rs.getString("Clients.email"),
										rs.getString("Clients.adresse"))));
				if (rs.getString("Reglements.type").equals("CHEQUE")) {
					reglement.setType(rs.getString("Reglements.type"));
					reglement.setNumero_cheque(rs.getLong("Reglements.numero_cheque"));
					reglement.setDate_echeance(rs.getDate("Reglements.date_echeance").toLocalDate());
					reglement.setBanque(new Banque(rs.getLong("Banques.id"), rs.getString("Banques.nom"), rs.getString("Banques.abreviation")));
					reglement.setNom(rs.getString("Reglements.nom"));					
				}
				reglements.add(reglement);				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return reglements;
	}

	@Override
	public boolean deleteAll(long id) {
		String query = "DELETE FROM Reglements WHERE BLId = ?";
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
	public double getTotal(long BLId) {
		String query = "SELECT SUM(Reglements.montant) as Total\n"
				+ "FROM Reglements\n"
				+ "WHERE Reglements.BLId = ?;";
		PreparedStatement pst;
		try {
			pst = connection.prepareStatement(query);
			pst.setLong(1, BLId);
			ResultSet rs = pst.executeQuery();
			if (rs.next()) {
				return rs.getDouble("Total");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1.0;
	}
}
