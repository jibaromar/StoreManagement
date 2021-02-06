package dao;

import java.util.List;

import model.LigneCommande;

public interface ILigneCommandeDao extends IDao <LigneCommande> {
	public List<LigneCommande> getAll(long BLId);
}
