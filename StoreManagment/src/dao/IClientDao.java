package dao;

import java.util.List;

import model.Client;

public interface IClientDao extends IDao <Client> {
	public List <Client> getAll(String keyword);
}
