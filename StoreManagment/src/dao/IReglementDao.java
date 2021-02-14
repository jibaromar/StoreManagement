package dao;

import java.util.List;

import model.Reglement;

public interface IReglementDao extends IDao <Reglement> {
	public List<Reglement> getAll(long id); // BL id
	public boolean deleteAll(long id); // BL id
	public double getTotal(long BLId);
}
