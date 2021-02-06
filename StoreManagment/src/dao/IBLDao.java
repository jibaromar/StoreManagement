package dao;

import java.util.List;

import model.BL;

public interface IBLDao extends IDao <BL> {
	public List <BL> getAll(long clientId);
	public List <BL> getAll(String clientName);
}
