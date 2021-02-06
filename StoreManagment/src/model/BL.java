package model;

import java.time.LocalDate;

public class BL {
	long id;
	LocalDate date;
	Client client;
	
	public BL(long id, LocalDate date, Client client) {
		super();
		this.id = id;
		this.date = date;
		this.client = client;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	@Override
	public String toString() {
		return "{\n\tid:" + id + ",\n\tdate:" + date + ",\n\tclient:" + client + "\n}";
	}
}
