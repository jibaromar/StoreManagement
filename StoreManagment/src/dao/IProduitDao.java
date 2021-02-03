package dao;

import java.util.List;

import model.Produit;

public interface IProduitDao extends IDao <Produit> {
	public List <Produit> getAll(String keyword);
}
