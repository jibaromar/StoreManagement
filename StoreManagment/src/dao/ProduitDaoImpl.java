package dao;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.Produit;

public class ProduitDaoImpl extends AbstractDao implements IProduitDao {

	@Override
	public Produit add(Produit obj) {
		String query = " INSERT INTO Produits (designation, categorieId, prix_achat, prix_vente, qte, date)"
		        + " VALUES (?, ?, ?, ?, ?, ?)";
		PreparedStatement pst;
		try {
			pst = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			pst.setString(1, obj.getDesignation().trim().replaceAll("\\s+", " "));
			pst.setLong(2, obj.getCategorieId());
			pst.setDouble(3, obj.getBuyingPrice());
			pst.setDouble(3, obj.getSellingPrice());
			pst.setInt(4, obj.getQuantity());
			pst.setDate(5, Date.valueOf(obj.getDate())); // turn LocalDate into java.sql.date
			
			ResultSet rs = pst.getGeneratedKeys();
            if(rs.next())
            {
                return new Produit(rs.getLong("id"), rs.getString("designation"), rs.getLong("categorieId"), rs.getDouble("prix_achat"), rs.getDouble("prix_vente"), rs.getInt("qte"), rs.getDate("date").toLocalDate());
            }
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean delete(long id) {
		String query = "DELETE FROM Produit WHERE id = ?";
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
		String sql = "SELECT * FROM Categories WHERE id=?";
		PreparedStatement pst;
		ResultSet rs;
		try {
			pst = connection.prepareStatement(sql);
			pst.setLong(1, id);
			rs = pst.executeQuery();
			if (rs.next()) {
				produit = new Produit(rs.getLong("id"), rs.getString("designation"), rs.getLong("categorieId"), rs.getDouble("prix_achat"), rs.getDouble("prix_vente"), rs.getInt("qte"), rs.getDate("date").toLocalDate());				
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
		String sql = "SELECT * FROM Produits";
		PreparedStatement pst;
		ResultSet rs;
		try {
			pst = connection.prepareStatement(sql);
			rs = pst.executeQuery();
			while (rs.next()) {
				products.add(new Produit(rs.getLong("id"), rs.getString("designation"), rs.getLong("categorieId"), rs.getDouble("prix_achat"), rs.getDouble("prix_vente"), rs.getInt("qte"), rs.getDate("date").toLocalDate()));
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
		String sql = "SELECT * FROM Produits WHERE label like ?";
		PreparedStatement pst;
		ResultSet rs;
		try {
			pst = connection.prepareStatement(sql);
			pst.setString(1, "%" + keyword.trim().replaceAll("\\s+", " ") + "%");
			rs = pst.executeQuery();
			while (rs.next()) {
				products.add(new Produit(rs.getLong("id"), rs.getString("designation"), rs.getLong("categorieId"), rs.getDouble("prix_achat"), rs.getDouble("prix_vente"), rs.getInt("qte"), rs.getDate("date").toLocalDate()));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return products;
	}
}
