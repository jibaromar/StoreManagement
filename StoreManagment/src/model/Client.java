package model;

public class Client {
	long id;
	String lastName;
	String firstName;
	String phone;
	String email;
	String address;
	
	public Client(Client client) { // no copy constructor no life
		this.id = client.getId();
		this.lastName = client.getLastName();
		this.firstName = client.getFirstName();
		this.phone = client.getPhone();
		this.email = client.getEmail();
		this.address = client.getAddress();
	}
	
	public Client(String lastName, String firstName, String phone, String email, String address) {
		this.id = -1L;
		this.lastName = lastName;
		this.firstName = firstName;
		this.phone = phone;
		this.email = email;
		this.address = address;
	}
	
	public Client(long id, String lastName, String firstName, String phone, String email, String address) {
		this.id = id;
		this.lastName = lastName;
		this.firstName = firstName;
		this.phone = phone;
		this.email = email;
		this.address = address;
	}
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public String getPhone() {
		return phone;
	}
	
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getAddress() {
		return address;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}
	
	public String toString() {
		return "{\n\tid:" + id + ",\n\tlastName:" + lastName + ",\n\tfirstName:" + firstName + ",\n\tphone:" + phone + ",\n\tphone:" + email + ",\n\tphone:" + address + "\n}";
	}
}
