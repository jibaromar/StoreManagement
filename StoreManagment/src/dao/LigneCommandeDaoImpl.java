package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.BL;
import model.Categorie;
import model.Client;
import model.LigneCommande;
import model.Produit;

public class LigneCommandeDaoImpl extends AbstractDao implements ILigneCommandeDao {

	@Override
	public LigneCommande add(LigneCommande obj) {
		String query = "INSERT INTO LignesCommande (qte, produitId, BLId)"
		        + " VALUES (?, ?, ?)";
		PreparedStatement pst;
		try {
			pst = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			pst.setLong(1, obj.getQuantity());
			pst.setLong(2, obj.getProduit().getId());
			pst.setLong(3, obj.getBL().getId());
			
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
	
	public boolean edit(LigneCommande obj) {
		String query = "UPDATE LignesCommande"
				+ " SET qte=?, produitId=?, BLId=?"
		        + " WHERE id=?";
		PreparedStatement pst;
		try {
			pst = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			pst.setLong(1, obj.getQuantity());
			pst.setLong(2, obj.getProduit().getId());
			pst.setLong(3, obj.getBL().getId());
			pst.setLong(4,  obj.getId());
			
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
		String query = "DELETE FROM LignesCommande WHERE id = ?";
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
	public LigneCommande getOne(long id) {
		LigneCommande ligneCommande = null;
		String sql = "SELECT LignesCommande.id, LignesCommande.qte,\n"
				+ "	   Produits.*,\n"
				+ "    Categories.*,\n"
				+ "    BL.*,\n"
				+ "    Clients.*\n"
				+ "FROM LignesCommande, Produits, Categories, BL, Clients\n"
				+ "WHERE \n"
				+ "	LignesCommande.produitId = Produits.id\n"
				+ "    AND Produits.categorieId = Categories.id\n"
				+ "    AND BL.clientId = Clients.id\n"
				+ "    AND LignesCommande.BLId = BL.id\n"
				+ "	   AND LignesCommande.id=?";
		PreparedStatement pst;
		ResultSet rs;
		try {
			pst = connection.prepareStatement(sql);
			pst.setLong(1, id);
			rs = pst.executeQuery();
			if (rs.next()) {
				Categorie categorie = new Categorie(rs.getLong("Categories.id"), rs.getString("Categories.label"), rs.getString("Categories.description"));
				Produit produit = new Produit(rs.getLong("Produits.id"), rs.getString("Produits.designation"), categorie, rs.getDouble("Produits.prix_achat"), rs.getDouble("Produits.prix_vente"), rs.getInt("Produits.qte"), rs.getDate("Produits.date").toLocalDate());
				Client client = new Client(rs.getLong("Clients.id"), rs.getString("Clients.nom"), rs.getString("Clients.prenom"), rs.getString("Clients.telephone"), rs.getString("Clients.email"), rs.getString("Clients.adresse"));
				BL bl = new BL(rs.getLong("BL.id"), rs.getDate("BL.date").toLocalDate(), client);
				ligneCommande = new LigneCommande(rs.getLong("LignesCommande.id"), rs.getLong("LignesCommande.qte"), produit, bl);				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ligneCommande;
	}
	
	@Override
	public List<LigneCommande> getAll(long BLId) {
		List <LigneCommande> ligneCommandes = new ArrayList <LigneCommande> ();
		String sql = "SELECT LignesCommande.id, LignesCommande.qte,\n"
				+ "	   Produits.*,\n"
				+ "    Categories.*,\n"
				+ "    BL.*,\n"
				+ "    Clients.*\n"
				+ "FROM LignesCommande, Produits, Categories, BL, Clients\n"
				+ "WHERE \n"
				+ "	LignesCommande.produitId = Produits.id\n"
				+ "    AND Produits.categorieId = Categories.id\n"
				+ "    AND BL.clientId = Clients.id\n"
				+ "    AND LignesCommande.BLId = BL.id\n"
				+ "	   AND LignesCommande.BLId=?";
		PreparedStatement pst;
		ResultSet rs;
		try {
			pst = connection.prepareStatement(sql);
			pst.setLong(1, BLId);
			rs = pst.executeQuery();
			while (rs.next()) {
				Categorie categorie = new Categorie(rs.getLong("Categories.id"), rs.getString("Categories.label"), rs.getString("Categories.description"));
				Produit produit = new Produit(rs.getLong("Produits.id"), rs.getString("Produits.designation"), categorie, rs.getDouble("Produits.prix_achat"), rs.getDouble("Produits.prix_vente"), rs.getInt("Produits.qte"), rs.getDate("Produits.date").toLocalDate());
				Client client = new Client(rs.getLong("Clients.id"), rs.getString("Clients.nom"), rs.getString("Clients.prenom"), rs.getString("Clients.telephone"), rs.getString("Clients.email"), rs.getString("Clients.adresse"));
				BL bl = new BL(rs.getLong("BL.id"), rs.getDate("BL.date").toLocalDate(), client);
				ligneCommandes.add(new LigneCommande(rs.getLong("LignesCommande.id"), rs.getLong("LignesCommande.qte"), produit, bl));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ligneCommandes;
	}

	@Override
	public List<LigneCommande> getAll() {
		List <LigneCommande> ligneCommandes = new ArrayList <LigneCommande> ();
		String sql = "SELECT LignesCommande.id, LignesCommande.qte,\n"
				+ "	   Produits.*,\n"
				+ "    Categories.*,\n"
				+ "    BL.*,\n"
				+ "    Clients.*\n"
				+ "FROM LignesCommande, Produits, Categories, BL, Clients\n"
				+ "WHERE \n"
				+ "	LignesCommande.produitId = Produits.id\n"
				+ "    AND Produits.categorieId = Categories.id\n"
				+ "    AND BL.clientId = Clients.id\n"
				+ "    AND LignesCommande.BLId = BL.id;";
		PreparedStatement pst;
		ResultSet rs;
		try {
			pst = connection.prepareStatement(sql);
			rs = pst.executeQuery();
			while (rs.next()) {
				Categorie categorie = new Categorie(rs.getLong("Categories.id"), rs.getString("Categories.label"), rs.getString("Categories.description"));
				Produit produit = new Produit(rs.getLong("Produits.id"), rs.getString("Produits.designation"), categorie, rs.getDouble("Produits.prix_achat"), rs.getDouble("Produits.prix_vente"), rs.getInt("Produits.qte"), rs.getDate("Produits.date").toLocalDate());
				Client client = new Client(rs.getLong("Clients.id"), rs.getString("Clients.nom"), rs.getString("Clients.prenom"), rs.getString("Clients.telephone"), rs.getString("Clients.email"), rs.getString("Clients.adresse"));
				BL bl = new BL(rs.getLong("BL.id"), rs.getDate("BL.date").toLocalDate(), client);
				ligneCommandes.add(new LigneCommande(rs.getLong("LignesCommande.id"), rs.getLong("LignesCommande.qte"), produit, bl));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ligneCommandes;
	}

	@Override
	public boolean deleteAll(long BLId) {
		String query = "DELETE FROM LignesCommande WHERE BLId = ?";
		PreparedStatement pst;
		try {
			pst = connection.prepareStatement(query);
			pst.setLong(1, BLId);
			
			if (pst.executeUpdate() > 0) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
}
