package model;

import java.time.LocalDate;

public class BL {
	long id;
	LocalDate date;
	Client client;
	
	public BL(BL bl) { // Copy constructor
		if (bl != null) {
			this.id = bl.getId();
			// TODO test if new instance of the date is created
			this.date = bl.getDate();
			this.client = new Client(bl.getClient());			
		} else {
			this.id = -1L;
			this.date = LocalDate.now();
			this.client = new Client(null);
		}
	}
	
	public BL(LocalDate date, Client client) {
		this.id = -1L;
		this.date = date;
		this.client = client;
	}
	
	public BL(long id, LocalDate date, Client client) {
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
