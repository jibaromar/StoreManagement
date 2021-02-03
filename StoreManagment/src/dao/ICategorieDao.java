package dao;

import java.util.List;

import model.Categorie;

public interface ICategorieDao extends IDao <Categorie> {
	public List <Categorie> getAll(String keyword);
}
