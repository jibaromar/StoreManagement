package dao;

import model.User;

public interface IUserDao extends IDao <User> {
	public boolean isValidUser(User usr); 
}
