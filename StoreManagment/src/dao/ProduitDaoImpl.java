package dao;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import model.Categorie;
import model.Produit;

public class ProduitDaoImpl extends AbstractDao implements IProduitDao {

	@Override
	public Produit add(Produit obj) {
		String query = "INSERT INTO Produits (designation, categorieId, prix_achat, prix_vente, qte, date)"
		        + " VALUES (?, ?, ?, ?, ?, ?)";
		PreparedStatement pst;
		try {
			pst = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			pst.setString(1, obj.getDesignation().trim().replaceAll("\\s+", " "));
			pst.setLong(2, obj.getCategorie().getId());
			pst.setDouble(3, obj.getBuyingPrice());
			pst.setDouble(4, obj.getSellingPrice());
			pst.setInt(5, obj.getQuantity());
			pst.setDate(6, Date.valueOf(obj.getDate())); // turn LocalDate into java.sql.date
			
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
	public boolean edit(Produit obj) {
		String query = "UPDATE Produits"
				+ " SET designation=?, categorieId=?, prix_achat=?, prix_vente=?, qte=?, date=?"
				+ " WHERE id=?";
		PreparedStatement pst;
		try {
			pst = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			pst.setString(1, obj.getDesignation().trim().replaceAll("\\s+", " "));
			pst.setLong(2, obj.getCategorie().getId());
			pst.setDouble(3, obj.getBuyingPrice());
			pst.setDouble(4, obj.getSellingPrice());
			pst.setInt(5, obj.getQuantity());
			pst.setDate(6, Date.valueOf(obj.getDate())); // turn LocalDate into java.sql.date
			pst.setLong(7,  obj.getId());
			
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
		String query = "DELETE FROM Produits WHERE id = ?";
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
	public Produit getOne(long id) {
		Produit produit = null;
		String sql = "SELECT Produits.id, Produits.designation, Produits.prix_achat, Produits.prix_vente, Produits.qte, Produits.date,\n"
				+ "	Categories.id, Categories.label, Categories.description\n"
				+ "FROM Produits, Categories\n"
				+ "WHERE Produits.categorieId = Categories.id\n"
				+ "	AND Produits.id = ?;";
		PreparedStatement pst;
		ResultSet rs;
		try {
			pst = connection.prepareStatement(sql);
			pst.setLong(1, id);
			rs = pst.executeQuery();
			if (rs.next()) {
				produit = new Produit(rs.getLong("Produits.id"), rs.getString("Produits.designation"), new Categorie(rs.getLong("Categories.id"), rs.getString("Categories.label"), rs.getString("Categories.description")), rs.getDouble("Produits.prix_achat"), rs.getDouble("Produits.prix_vente"), rs.getInt("Produits.qte"), rs.getDate("Produits.date").toLocalDate());				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return produit;
	}

	@Override
	public List<Produit> getAll() {
		List <Produit> products = new ArrayList <Produit> ();
		String sql = "SELECT Produits.id, Produits.designation, Produits.prix_achat, Produits.prix_vente, Produits.qte, Produits.date,\n"
				+ "	Categories.id, Categories.label, Categories.description\n"
				+ "FROM Produits, Categories\n"
				+ "WHERE Produits.categorieId = Categories.id;";
		PreparedStatement pst;
		ResultSet rs;
		try {
			pst = connection.prepareStatement(sql);
			rs = pst.executeQuery();
			while (rs.next()) {
				products.add(new Produit(rs.getLong("Produits.id"), rs.getString("Produits.designation"), new Categorie(rs.getLong("Categories.id"), rs.getString("Categories.label"), rs.getString("Categories.description")), rs.getDouble("Produits.prix_achat"), rs.getDouble("Produits.prix_vente"), rs.getInt("Produits.qte"), rs.getDate("Produits.date").toLocalDate()));				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return products;
	}

	@Override
	public List<Produit> getAll(String keyword) {
		List <Produit> products = new ArrayList <Produit> ();
		String sql = "SELECT Produits.id, Produits.designation, Produits.prix_achat, Produits.prix_vente, Produits.qte, Produits.date,\n"
				+ "	Categories.id, Categories.label, Categories.description\n"
				+ "FROM Produits, Categories\n"
				+ "WHERE Produits.categorieId = Categories.id\n"
				+ "	AND Produits.label like ?;";
		PreparedStatement pst;
		ResultSet rs;
		try {
			pst = connection.prepareStatement(sql);
			pst.setString(1, "%" + keyword.trim().replaceAll("\\s+", " ") + "%");
			rs = pst.executeQuery();
			while (rs.next()) {
				products.add(new Produit(rs.getLong("Produits.id"), rs.getString("Produits.designation"), new Categorie(rs.getLong("Categories.id"), rs.getString("Categories.label"), rs.getString("Categories.description")), rs.getDouble("Produits.prix_achat"), rs.getDouble("Produits.prix_vente"), rs.getInt("Produits.qte"), rs.getDate("Produits.date").toLocalDate()));				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return products;
	}
}
