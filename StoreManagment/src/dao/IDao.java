package dao;

import java.util.List;

public interface IDao <T> {
	public T add(T obj); // return the new inserted row or null if error
	public boolean edit(T obj);
	public boolean delete(long id); // return if the rows are deleted or no
	public T getOne(long id);
	public List <T> getAll();
}
