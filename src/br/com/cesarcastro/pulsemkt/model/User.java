package br.com.cesarcastro.pulsemkt.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;

import br.com.cesarcastro.pulsemkt.enums.UserRole;

public class User implements Cloneable {

	@Expose
	private Integer id;
	@Expose
	private String name;
	@Expose
	private String email;
	@JsonInclude(value = Include.NON_NULL)
	private String password;
	@Expose
	private String number;
	@Expose
	private Address address;
	private UserRole role;
	
	public UserRole getUserRole() {
		return this.role;
	}
	public void setUserRole(UserRole role) {
		this.role = role;
	}
	public Address getAddress() {
		return address;
	}

	public User(String email, String password) {
		this.email = email;
		this.password = password;
	}

	public User(Integer id, String name, String email) {
		this.id = id;
		this.name = name;
		this.email = email;
	}

	public User(Integer id, String name, String email, String password, String number, Address address) {
		this.id = id;
		this.name = name;
		this.email = email;
		this.password = password;
		this.number = number;
		this.address = address;
	}

	public User() {
	}

	public User(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String toString() {
		return "User [name=" + name + ", email=" + email + ", password=" + password + "]";
	}

	public static User fromJson(String json) {
		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();
		return gson.fromJson(json, User.class);
	}

	public String toJson() {
		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();
		return gson.toJson(this);
	}

	public boolean isEnrollable() {
		return !this.name.isEmpty() && !this.email.isEmpty() && !this.number.isEmpty() && !(this.address == null)
				&& !this.password.isEmpty();
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public void setAddress(Address address) {
		this.address = address;
	}
	
	public boolean compareUser(User user) {
		return (this.getId().equals(user.getId()) && this.getName().contentEquals(user.getName())
				&& this.getUserRole() == user.getUserRole());
	}
}
