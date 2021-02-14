package dao;

import java.util.List;

import model.LigneCommande;

public interface ILigneCommandeDao extends IDao <LigneCommande> {
	public boolean deleteAll(long BLId);
	public List<LigneCommande> getAll(long BLId);
	// TODO remove this methode when merging command lignes and payment with BL
	public double getTotal(long BLId);
}
