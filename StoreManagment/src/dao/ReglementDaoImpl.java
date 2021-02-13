package dao;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.Banque;
import model.Reglement;

public class ReglementDaoImpl extends AbstractDao implements IReglementDao {

	@Override
	public Reglement add(Reglement obj) {
		String query = "INSERT INTO Reglements (date, montant, type, numero_cheque, date_echeance, banqueId, nom)"
		        + " VALUES (?, ?, ?, ?, ?, ?, ?)";
		PreparedStatement pst;
		try {
			pst = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			
			pst.setDate(1, Date.valueOf(obj.getDate()));
			pst.setDouble(2, obj.getMontant());
			pst.setString(3, obj.getType());
			
			if (obj.getType() == "CHEQUE") {
				pst.setLong(4, obj.getNumero_cheque());
				pst.setDate(5, Date.valueOf(obj.getDate_echance()));
				pst.setLong(6, obj.getBanque().getId());			
				pst.setString(7, obj.getNom().trim().replaceAll("\\s+", " "));
			} else {
				pst.setString(4, null);
				pst.setDate(5, null);
				pst.setString(6, null);			
				pst.setString(7, null);
			}

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
				+ " SET date=?, montant=?, type=?, numero_cheque=?, date_echeance=?, banqueId=?, nom=?"
				+ " WHERE id=?";
		PreparedStatement pst;
		try {
			pst = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			
			pst.setDate(1, Date.valueOf(obj.getDate()));
			pst.setDouble(2, obj.getMontant());
			pst.setString(3, obj.getType());
			
			if (obj.getType() == "CHEQUE") {
				pst.setLong(4, obj.getNumero_cheque());
				pst.setDate(5, Date.valueOf(obj.getDate_echance()));
				pst.setLong(6, obj.getBanque().getId());			
				pst.setString(7, obj.getNom().trim().replaceAll("\\s+", " "));
			} else {
				pst.setString(4, null);
				pst.setDate(5, null);
				pst.setString(6, null);			
				pst.setString(7, null);
			}
			
			pst.setLong(8,  obj.getId());
			
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
				+ "Banques.*\n"
				+ "FROM Reglements LEFT OUTER JOIN Banques\n"
				+ "ON Reglements.banqueId = Banques.id\n"
				+ "WHERE Reglements.id = ?";
		PreparedStatement pst;
		ResultSet rs;
		try {
			pst = connection.prepareStatement(sql);
			pst.setLong(1, id);
			rs = pst.executeQuery();
			if (rs.next()) {
				reglement = new Reglement(rs.getLong("Reglements.id"), rs.getDate("Reglements.date").toLocalDate(), rs.getDouble("Reglements.montant"));
				if (rs.getString("Reglements.type") == "CHEQUE") {
					reglement.setNumero_cheque(rs.getLong("Reglements.numero_cheque"));
					reglement.setDate_echance(rs.getDate("Reglements.date_echeance").toLocalDate());
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
				+ "Banques.*\n"
				+ "FROM Reglements LEFT OUTER JOIN Banques\n"
				+ "ON Reglements.banqueId = Banques.id";
		PreparedStatement pst;
		ResultSet rs;
		try {
			pst = connection.prepareStatement(sql);
			rs = pst.executeQuery();
			while (rs.next()) {
				Reglement reglement = new Reglement(rs.getLong("Reglements.id"), rs.getDate("Reglements.date").toLocalDate(), rs.getDouble("Reglements.montant"));
				if (rs.getString("Reglements.type") == "CHEQUE") {
					reglement.setNumero_cheque(rs.getLong("Reglements.numero_cheque"));
					reglement.setDate_echance(rs.getDate("Reglements.date_echeance").toLocalDate());
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
}
