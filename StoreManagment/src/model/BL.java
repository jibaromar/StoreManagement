package model;

import java.time.LocalDate;

public class BL {
	long id;
	LocalDate date;
	long clientId;
	
	public BL(long id, LocalDate date, long clientId) {
		super();
		this.id = id;
		this.date = date;
		this.clientId = clientId;
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

	public long getClientId() {
		return clientId;
	}

	public void setClientId(long clientId) {
		this.clientId = clientId;
	}

	@Override
	public String toString() {
		return "{\n\tid:" + id + ",\n\tdate:" + date + ",\n\tclientId:" + clientId + "\n}";
	}
}
